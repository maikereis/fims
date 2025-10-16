package com.mqped.fims.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    void testGettersAndSetters() {
        Address address = new Address();

        address.setId("204583458");
        address.setStreet("Rua dos Timbiras");
        address.setStreetType("Rua");
        address.setNumber("456");
        address.setComplement("Casa 2");
        address.setNeighborhood("Marco");
        address.setSubdistrict("Belém Centro");
        address.setDistrict("Distrito Administrativo de Belém");
        address.setMunicipality("Belém");
        address.setState("PA");
        address.setZipCode("66093-000");
        address.setLatitude(-1.4567);
        address.setLongitude(-48.5014);

        assertEquals("204583458", address.getId());
        assertEquals("Rua dos Timbiras", address.getStreet());
        assertEquals("Rua", address.getStreetType());
        assertEquals("456", address.getNumber());
        assertEquals("Casa 2", address.getComplement());
        assertEquals("Marco", address.getNeighborhood());
        assertEquals("Belém Centro", address.getSubdistrict());
        assertEquals("Distrito Administrativo de Belém", address.getDistrict());
        assertEquals("Belém", address.getMunicipality());
        assertEquals("PA", address.getState());
        assertEquals("66093-000", address.getZipCode());
        assertEquals(-1.4567, address.getLatitude());
        assertEquals(-48.5014, address.getLongitude());
    }

    @Test
    void testEmptyConstructorDefaults() {
        Address address = new Address();
        assertNull(address.getId());
        assertNull(address.getStreet());
        assertNull(address.getNumber());
        assertNull(address.getLatitude());
        assertNull(address.getLongitude());
    }

    @Test
    void testToStringContainsKeyFields() {
        Address address = new Address();
        address.setStreet("Travessa Humaitá");
        address.setMunicipality("Ananindeua");

        String str = address.toString();
        assertTrue(str.contains("Travessa Humaitá"));
        assertTrue(str.contains("Ananindeua"));
    }
}