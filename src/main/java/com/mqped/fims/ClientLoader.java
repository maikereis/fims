package com.mqped.fims;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.mqped.fims.model.entity.Client;
import com.mqped.fims.service.ClientService;
import com.mqped.fims.util.StringNormalizer;

@Component
@Order(2)
@Profile("dev")
public class ClientLoader implements ApplicationRunner {

    @Value("${client.csv.path}")
    private Resource csvResource;

    private final ClientService clientService;

    public ClientLoader(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(csvResource.getInputStream(), StandardCharsets.UTF_8))) {

            reader.readLine(); // skip the header

            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1);

                Client client = new Client();
                client.setName(StringNormalizer.normalize(fields[0]));
                client.setCpf(fields[1]);
                client.setBirthDate(fields[2].isEmpty()
                        ? LocalDateTime.of(1900, 1, 1, 0, 0)
                        : LocalDateTime.parse(fields[2]));
                client.setMotherName(StringNormalizer.normalize(fields[3]));
                client.setCnpj(fields[4]);
                client.setGenre(fields[5].isEmpty() ? "Desconhecido" : StringNormalizer.normalize(fields[5]));
                client.setCreatedAt(fields[6].isEmpty() ? LocalDateTime.now() : LocalDateTime.parse(fields[6]));

                clientService.add(client);
            }

            // Collection<Client> clients = clientService.findAll();
            // clients.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
