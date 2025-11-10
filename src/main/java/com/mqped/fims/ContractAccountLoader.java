package com.mqped.fims;

import com.mqped.fims.exceptions.ResourceNotFoundException;
import com.mqped.fims.model.entity.Client;
import com.mqped.fims.model.entity.ContractAccount;
import com.mqped.fims.model.entity.Installation;
import com.mqped.fims.model.enums.StatusType;
import com.mqped.fims.service.ClientService;
import com.mqped.fims.service.ContractAccountService;
import com.mqped.fims.service.InstallationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads ContractAccount data from CSV.
 * 
 * This loader ONLY searches for existing entities and does NOT create new ones.
 * It assumes that Address, Installation, and Client entities have already been
 * loaded by their respective loaders (Order 1, 2, 3).
 * 
 * If a referenced entity doesn't exist, the contract line is skipped and
 * logged.
 */
@Component
@Order(4)
@Profile("dev")
public class ContractAccountLoader implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ContractAccountLoader.class);
    private static final DateTimeFormatter CSV_DATETIME_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    @Value("${contract.csv.path}")
    private Resource csvResource;

    private final ContractAccountService contractAccountService;
    private final ClientService clientService;
    private final InstallationService installationService;

    // Statistics for reporting
    private int totalLines = 0;
    private int successfulLoads = 0;
    private int skippedLines = 0;

    public ContractAccountLoader(ContractAccountService contractAccountService,
            ClientService clientService,
            InstallationService installationService) {
        this.contractAccountService = contractAccountService;
        this.clientService = clientService;
        this.installationService = installationService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Starting ContractAccount loading from CSV...");

        // Caches to minimize database queries
        Map<Integer, Client> clientCache = new HashMap<>();
        Map<String, List<Installation>> installationCache = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(csvResource.getInputStream(), StandardCharsets.UTF_8))) {

            reader.readLine(); // skip header

            String line;
            int lineNumber = 1;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                totalLines++;

                try {
                    processContractLine(line, clientCache, installationCache);
                    successfulLoads++;
                } catch (Exception e) {
                    skippedLines++;
                    logger.warn("Line {}: Skipping contract - {}", lineNumber, e.getMessage());
                }
            }
        }

        logLoadingSummary();
    }

    /**
     * Process a single contract line from the CSV.
     * Throws exceptions if required entities don't exist.
     */
    private void processContractLine(String line,
            Map<Integer, Client> clientCache,
            Map<String, List<Installation>> installationCache) {
        String[] fields = line.split(",", -1);

        if (fields.length < 11) {
            throw new IllegalArgumentException("Malformed CSV line - insufficient fields");
        }

        // Parse CSV fields
        String accountNumber = fields[0];
        int clientId = parseClientId(fields[1]);
        String clientName = fields[2]; // Only used for logging/validation
        String addressId = fields[3];

        LocalDateTime contractCreatedAt = parseDateTime(fields[4], "contract creation date");
        LocalDateTime contractDeletedAt = parseOptionalDateTime(fields[5]);
        LocalDateTime installationCreatedAt = parseOptionalDateTime(fields[6]);
        LocalDateTime installationDeletedAt = parseOptionalDateTime(fields[7]);

        StatusType status = parseStatus(fields[8]);
        LocalDateTime statusStart = parseOptionalDateTime(fields[9]);
        LocalDateTime statusEnd = parseOptionalDateTime(fields[10]);

        // --- Fetch Client (MUST exist) ---
        Client client = findOrCacheClient(clientId, clientName, clientCache);

        // --- Fetch Installation (MUST exist) ---
        Installation installation = findOrCacheInstallation(
                addressId,
                installationCreatedAt,
                installationCache);

        // --- Create ContractAccount ---
        ContractAccount contractAccount = buildContractAccount(
                accountNumber, client, installation,
                contractCreatedAt, contractDeletedAt,
                status, statusStart, statusEnd);

        contractAccountService.add(contractAccount);
        logger.debug("Loaded contract: {} for client: {} at address: {}",
                accountNumber, client.getName(), addressId);
    }

    /**
     * Find or cache a Client entity. Throws exception if not found.
     */
    private Client findOrCacheClient(int clientId, String clientName, Map<Integer, Client> cache) {
        // Check if clientId is valid
        if (clientId <= 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid clientId: %d for client '%s'. Client must exist before loading contracts.",
                            clientId, clientName));
        }

        // Return from cache or fetch from database
        return cache.computeIfAbsent(clientId, id -> {
            try {
                return clientService.findById(id);
            } catch (ResourceNotFoundException e) {
                throw new IllegalArgumentException(
                        String.format("Client with id %d (name: '%s') not found. Ensure clients are loaded first.",
                                id, clientName));
            }
        });
    }

    /**
     * Find or cache an Installation for the given address.
     * Throws exception if no installation exists for this address.
     */
    private Installation findOrCacheInstallation(String addressId,
            LocalDateTime createdAt,
            Map<String, List<Installation>> cache) {
        List<Installation> installations = cache.computeIfAbsent(addressId,
                id -> installationService.findByAddressIdWithContracts(id));

        if (installations.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("No installation found for addressId: %s. Ensure installations are loaded first.",
                            addressId));
        }

        // Find installation that matches the creation timestamp (if available)
        // or return the first one as fallback
        if (createdAt != null) {
            return installations.stream()
                    .filter(inst -> inst.getCreatedAt() != null &&
                            inst.getCreatedAt().equals(createdAt))
                    .findFirst()
                    .orElse(installations.get(0));
        }

        return installations.get(0);
    }

    /**
     * Build a ContractAccount entity from parsed data.
     */
    private ContractAccount buildContractAccount(String accountNumber,
            Client client,
            Installation installation,
            LocalDateTime createdAt,
            LocalDateTime deletedAt,
            StatusType status,
            LocalDateTime statusStart,
            LocalDateTime statusEnd) {
        ContractAccount contractAccount = new ContractAccount();
        contractAccount.setAccountNumber(accountNumber);
        contractAccount.setClient(client);
        contractAccount.setInstallation(installation);
        contractAccount.setCreatedAt(createdAt);
        contractAccount.setDeletedAt(deletedAt);
        contractAccount.setStatus(status);
        contractAccount.setStatusStart(statusStart);
        contractAccount.setStatusEnd(statusEnd);

        return contractAccount;
    }

    // === Parsing Helper Methods ===

    private int parseClientId(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid clientId: " + value);
        }
    }

    private LocalDateTime parseDateTime(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required but was empty");
        }
        try {
            return LocalDateTime.parse(value.trim(), CSV_DATETIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid " + fieldName + ": " + value + " - " + e.getMessage());
        }
    }

    private LocalDateTime parseOptionalDateTime(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(value.trim(), CSV_DATETIME_FORMATTER);
        } catch (DateTimeParseException e) {
            logger.warn("Invalid datetime format: {}, returning null", value);
            return null;
        }
    }

    private StatusType parseStatus(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return StatusType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid status type: {}, returning null", value);
            return null;
        }
    }

    private void logLoadingSummary() {
        logger.info("=== ContractAccount Loading Summary ===");
        logger.info("Total lines processed: {}", totalLines);
        logger.info("Successfully loaded: {}", successfulLoads);
        logger.info("Skipped (errors): {}", skippedLines);
        logger.info("Success rate: {}%",
                totalLines > 0 ? String.format("%.2f", successfulLoads * 100.0 / totalLines) : "0.00");
    }
}