package com.mqped.fims;

import com.mqped.fims.model.entity.Address;
import com.mqped.fims.model.entity.Client;
import com.mqped.fims.model.entity.ContractAccount;
import com.mqped.fims.model.entity.Installation;
import com.mqped.fims.model.enums.StatusType;
import com.mqped.fims.service.AddressService;
import com.mqped.fims.service.ClientService;
import com.mqped.fims.service.ContractAccountService;
import com.mqped.fims.service.InstallationService;
import com.mqped.fims.util.StringNormalizer;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Order(4)
@Profile("!test")
public class ContractAccountLoader implements ApplicationRunner {

    @Value("${contract.csv.path}")
    private Resource csvResource;

    private final ContractAccountService contractAccountService;
    private final ClientService clientService;
    private final InstallationService installationService;
    private final AddressService addressService;

    private static final DateTimeFormatter CSV_DATETIME_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    public ContractAccountLoader(ContractAccountService contractAccountService,
                                  ClientService clientService,
                                  InstallationService installationService,
                                  AddressService addressService) {
        this.contractAccountService = contractAccountService;
        this.clientService = clientService;
        this.installationService = installationService;
        this.addressService = addressService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, List<Installation>> installationCache = new HashMap<>();
        Map<Integer, Client> clientCache = new HashMap<>();
        Map<String, Address> addressCache = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(csvResource.getInputStream(), StandardCharsets.UTF_8))) {

            reader.readLine(); // skip header

            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1);

                String accountNumber = fields[0];
                int clientId = Integer.parseInt(fields[1]);
                String clientName = StringNormalizer.normalize(fields[2]);
                String addressId = fields[3];

                LocalDateTime contractCreatedAt = LocalDateTime.parse(fields[4], CSV_DATETIME_FORMATTER);
                LocalDateTime contractDeletedAt = fields[5].isEmpty() ? null
                        : LocalDateTime.parse(fields[5], CSV_DATETIME_FORMATTER);
                LocalDateTime installationCreatedAt = fields[6].isEmpty() ? LocalDateTime.now()
                        : LocalDateTime.parse(fields[6], CSV_DATETIME_FORMATTER);
                LocalDateTime installationDeletedAt = fields[7].isEmpty() ? null
                        : LocalDateTime.parse(fields[7], CSV_DATETIME_FORMATTER);

                StatusType status = fields[8].isEmpty() ? null : StatusType.valueOf(fields[8]);
                LocalDateTime statusStart = fields[9].isEmpty() ? null
                        : LocalDateTime.parse(fields[9], CSV_DATETIME_FORMATTER);
                LocalDateTime statusEnd = fields[10].isEmpty() ? null
                        : LocalDateTime.parse(fields[10], CSV_DATETIME_FORMATTER);

                // --- Client (cached) ---
                Client client;
                if (clientId == 0) {
                    client = new Client();
                    client.setName(clientName);
                    client = clientService.add(client);
                    clientCache.put(client.getId(), client);
                } else {
                    client = clientCache.computeIfAbsent(clientId,
                            id -> clientService.findById(id)
                                    .orElseThrow(() -> new IllegalArgumentException(
                                            "Client with id " + id + " not found")));
                }

                // --- Address (cached) ---
                Address address = addressCache.computeIfAbsent(addressId,
                        id -> addressService.findByAddressId(id)
                                .orElseGet(() -> {
                                    Address newAddress = new Address();
                                    newAddress.setAddressId(id);
                                    newAddress.setState("Unknown");
                                    newAddress.setMunicipality("Unknown");
                                    newAddress.setStreet("Unknown");
                                    return addressService.add(newAddress);
                                }));

                // --- Installation (cached) ---
                List<Installation> installations = installationCache.computeIfAbsent(addressId,
                        id -> installationService.findByAddressIdWithContracts(id));

                Installation installation = installations.stream()
                        .findFirst()
                        .orElseGet(() -> {
                            Installation newInst = new Installation();
                            newInst.setAddress(address);
                            newInst.setCreatedAt(installationCreatedAt);
                            newInst.setDeletedAt(installationDeletedAt);
                            Installation saved = installationService.add(newInst);
                            installations.add(saved);
                            return saved;
                        });

                // --- Contract Account ---
                ContractAccount contractAccount = new ContractAccount();
                contractAccount.setAccountNumber(accountNumber);
                contractAccount.setClient(client);
                contractAccount.setInstallation(installation);
                contractAccount.setCreatedAt(contractCreatedAt);
                contractAccount.setDeletedAt(contractDeletedAt);
                contractAccount.setStatus(status);
                contractAccount.setStatusStart(statusStart);
                contractAccount.setStatusEnd(statusEnd);

                ContractAccount saved = contractAccountService.add(contractAccount);
                
                if (installation.getContractAccounts() == null) {
                    installation.setContractAccounts(new ArrayList<>());
                }
                installation.getContractAccounts().add(saved);
            }
        }
    }
}