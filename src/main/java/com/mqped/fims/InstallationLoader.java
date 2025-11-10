package com.mqped.fims;

import com.mqped.fims.exceptions.ResourceNotFoundException;
import com.mqped.fims.model.entity.Address;
import com.mqped.fims.model.entity.Installation;
import com.mqped.fims.service.AddressService;
import com.mqped.fims.service.InstallationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Component
@Order(3)
@Profile("dev")
public class InstallationLoader implements ApplicationRunner {

    @Value("${installation.csv.path}")
    private Resource csvResource;

    private final InstallationService installationService;
    private final AddressService addressService;

    public InstallationLoader(InstallationService installationService, AddressService addressService) {
        this.installationService = installationService;
        this.addressService = addressService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        int successCount = 0;
        int errorCount = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(csvResource.getInputStream(), StandardCharsets.UTF_8))) {

            reader.readLine(); // skip header

            String line;
            int lineNumber = 1; // track line number for better error messages

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                try {
                    String[] fields = line.split(",", -1);
                    if (fields.length < 15) {
                        System.err.println("Line " + lineNumber + ": Skipping malformed line (expected 15 fields, got "
                                + fields.length + "): " + line);
                        errorCount++;
                        continue;
                    }

                    // --- Fetch existing Address ---
                    String addressId = fields[0];
                    Address address;

                    try {
                        address = addressService.findByAddressId(addressId);
                    } catch (ResourceNotFoundException e) {
                        System.err.println("Line " + lineNumber + ": Address not found for addressId: " + addressId);
                        errorCount++;
                        continue;
                    }

                    // --- Build Installation ---
                    Installation installation = new Installation();
                    installation.setAddress(address);

                    try {
                        installation.setCreatedAt(parseDate(fields[13]));
                        installation.setDeletedAt(parseDate(fields[14]));
                    } catch (DateTimeParseException e) {
                        System.err.println("Line " + lineNumber + ": Invalid date format - " + e.getMessage());
                        errorCount++;
                        continue;
                    }

                    installationService.add(installation);
                    successCount++;

                } catch (Exception e) {
                    System.err.println("Line " + lineNumber + ": Unexpected error - " + e.getMessage());
                    errorCount++;
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            throw e; // rethrow to indicate critical failure
        }

        // Summary log
        System.out.println("Installation loading completed:");
        System.out.println("  - Successfully loaded: " + successCount);
        System.out.println("  - Errors encountered: " + errorCount);
    }

    private static LocalDateTime parseDate(String value) throws DateTimeParseException {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(value.trim());
    }
}