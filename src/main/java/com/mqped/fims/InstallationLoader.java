package com.mqped.fims;

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

@Component
@Order(3)
@Profile("!test")
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
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(csvResource.getInputStream(), StandardCharsets.UTF_8))) {

            reader.readLine(); // skip header

            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1);
                if (fields.length < 15) {
                    System.err.println("Skipping malformed line: " + line);
                    continue;
                }

                // --- Fetch existing Address ---
                String addressId = fields[0];
                Address address = addressService.findByAddressId(addressId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Address not found for addressId: " + addressId));

                // --- Build Installation ---
                Installation installation = new Installation();
                installation.setAddress(address);
                installation.setCreatedAt(parseDate(fields[13]));
                installation.setDeletedAt(parseDate(fields[14]));

                installationService.add(installation);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static LocalDateTime parseDate(String value) {
        return (value == null || value.isEmpty()) ? null : LocalDateTime.parse(value);
    }
}
