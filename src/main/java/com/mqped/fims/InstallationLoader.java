package com.mqped.fims;

import com.mqped.fims.model.Address;
import com.mqped.fims.model.Installation;
import com.mqped.fims.service.InstallationService;
import com.mqped.fims.util.StringNormalizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;

@Component
public class InstallationLoader implements ApplicationRunner {

    @Value("${installation.csv.path}")
    private Resource csvResource;

    private final InstallationService installationService;

    public InstallationLoader(InstallationService installationService) {
        this.installationService = installationService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(csvResource.getInputStream(), StandardCharsets.UTF_8))) {

            // Skip header
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                // Split CSV, preserving empty fields
                String[] fields = line.split(",", -1);
                if (fields.length < 15) {
                    System.err.println("Skipping malformed line: " + line);
                    continue;
                }

                // --- Build Address ---
                Address address = new Address();
                address.setAddressId(fields[0]);
                address.setState(StringNormalizer.normalize(fields[1]));
                address.setMunicipality(StringNormalizer.normalize(fields[2]));
                address.setDistrict(StringNormalizer.normalize(fields[3]));
                address.setSubdistrict(StringNormalizer.normalize(fields[4]));
                address.setNeighborhood(StringNormalizer.normalize(fields[5]));
                address.setZipCode(fields[6]);
                address.setStreetType(StringNormalizer.normalize(fields[7]));
                address.setStreet(StringNormalizer.normalize(fields[8]));
                address.setNumber(fields[9]);
                address.setComplement(StringNormalizer.normalize(fields[10]));
                address.setLatitude(parseDouble(fields[11]));
                address.setLongitude(parseDouble(fields[12]));

                // --- Build Installation ---
                Installation installation = new Installation();
                installation.setAddress(address);
                installation.setCreateAt(parseDate(fields[13]));
                installation.setDeletedAt(parseDate(fields[14]));

                installationService.add(installation);
            }

            // Print loaded installations
            Collection<Installation> installations = installationService.findAll();
            installations.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static LocalDateTime parseDate(String value) {
        return value == null || value.isEmpty() ? null : LocalDateTime.parse(value);
    }

    private static Double parseDouble(String value) {
        try {
            return value == null || value.isEmpty() ? null : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
