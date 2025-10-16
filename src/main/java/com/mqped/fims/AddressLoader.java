package com.mqped.fims;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.mqped.fims.model.Address;
import com.mqped.fims.service.AddressService;

@Component
public class AddressLoader implements ApplicationRunner {

    private final AddressService addressService;

    public AddressLoader(AddressService addressService) {
        this.addressService = addressService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/data/15_PA.csv"))) {
            
            reader.readLine(); // skip the header

            String line;
            while ((line = reader.readLine()) != null) {
                String[] campos = line.split(",", -1);

                Address address = new Address();
                address.setId(campos[0]);
                address.setState(campos[1]);
                address.setMunicipality(campos[2]);
                address.setDistrict(campos[3]);
                address.setSubdistrict(campos[4]);
                address.setNeighborhood(campos[5]);
                address.setZipCode(campos[6]);
                address.setStreetType(campos[7]);
                address.setStreet(campos[8]);
                address.setNumber(campos[9]);
                address.setComplement(campos[10]);
                address.setLatitude(campos[11].isEmpty() ? null : Double.valueOf(campos[11]));
                address.setLongitude(campos[12].isEmpty() ? null : Double.valueOf(campos[12]));

                addressService.add(address);
            }

            Collection<Address> addresses = addressService.findAll();
            addresses.forEach(System.out::println);
        } catch (IOException e) {
            // Tratar exceções de I/O
            e.printStackTrace();
        }
    }

}
