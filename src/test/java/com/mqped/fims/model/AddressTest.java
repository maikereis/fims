package com.mqped.fims.model;

import org.junit.jupiter.api.Test;

import com.mqped.fims.model.entity.Address;

import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    @DisplayName("Should set and get all address fields correctly")
    void testGettersAndSetters() {
        Address address = new Address();

        address.setId(1);
        address.setAddressId("204583458");
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

        assertEquals(1, address.getId());
        assertEquals("204583458", address.getAddressId());
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
    @DisplayName("Should initialize with null values")
    void testEmptyConstructorDefaults() {
        Address address = new Address();
        
        assertNull(address.getId());
        assertNull(address.getAddressId());
        assertNull(address.getStreet());
        assertNull(address.getStreetType());
        assertNull(address.getNumber());
        assertNull(address.getComplement());
        assertNull(address.getNeighborhood());
        assertNull(address.getSubdistrict());
        assertNull(address.getDistrict());
        assertNull(address.getMunicipality());
        assertNull(address.getState());
        assertNull(address.getZipCode());
        assertNull(address.getLatitude());
        assertNull(address.getLongitude());
    }

    @Test
    @DisplayName("Should include all fields in toString output")
    void testToStringContainsAllFields() {
        Address address = new Address();
        address.setId(42);
        address.setAddressId("123456");
        address.setStreet("Travessa Humaitá");
        address.setStreetType("Travessa");
        address.setNumber("100");
        address.setComplement("Apt 201");
        address.setNeighborhood("Centro");
        address.setSubdistrict("Distrito Central");
        address.setDistrict("Distrito Administrativo");
        address.setMunicipality("Ananindeua");
        address.setState("PA");
        address.setZipCode("67000-000");
        address.setLatitude(-1.3650);
        address.setLongitude(-48.3800);

        String str = address.toString();
        
        assertTrue(str.contains("id=42"), "Should contain id");
        assertTrue(str.contains("Travessa Humaitá"), "Should contain street");
        assertTrue(str.contains("Ananindeua"), "Should contain municipality");
        assertTrue(str.contains("PA"), "Should contain state");
        assertTrue(str.contains("-1.365"), "Should contain latitude");
        assertTrue(str.contains("-48.38"), "Should contain longitude");
    }

    @Test
    @DisplayName("Should handle null values gracefully")
    void testToStringWithNullValues() {
        Address address = new Address();
        
        String str = address.toString();
        
        assertNotNull(str);
        assertTrue(str.startsWith("Address{"));
        assertTrue(str.contains("null"));
    }

    @Test
    @DisplayName("Should accept valid Brazilian ZIP code format")
    void testZipCodeFormat() {
        Address address = new Address();
        
        address.setZipCode("66093-000");
        assertEquals("66093-000", address.getZipCode());
        
        address.setZipCode("01310-100");
        assertEquals("01310-100", address.getZipCode());
    }

    @Test
    @DisplayName("Should accept valid Brazilian state codes")
    void testStateCode() {
        Address address = new Address();
        
        address.setState("PA");
        assertEquals("PA", address.getState());
        
        address.setState("SP");
        assertEquals("SP", address.getState());
    }

    @Test
    @DisplayName("Should store negative and positive coordinates")
    void testCoordinates() {
        Address address = new Address();
        
        // Belém coordinates (southern hemisphere, western)
        address.setLatitude(-1.4558);
        address.setLongitude(-48.4902);
        
        assertEquals(-1.4558, address.getLatitude());
        assertEquals(-48.4902, address.getLongitude());
        
        // Test with positive values (e.g., northern Brazil)
        address.setLatitude(2.8235);  // Roraima
        address.setLongitude(-60.6758);
        
        assertEquals(2.8235, address.getLatitude());
        assertEquals(-60.6758, address.getLongitude());
    }

    @Test
    @DisplayName("Should handle zero coordinates")
    void testZeroCoordinates() {
        Address address = new Address();
        
        address.setLatitude(0.0);
        address.setLongitude(0.0);
        
        assertEquals(0.0, address.getLatitude());
        assertEquals(0.0, address.getLongitude());
    }

    @Test
    @DisplayName("Should allow updating fields multiple times")
    void testFieldMutability() {
        Address address = new Address();
        
        address.setStreet("Old Street");
        assertEquals("Old Street", address.getStreet());
        
        address.setStreet("New Street");
        assertEquals("New Street", address.getStreet());
        
        address.setNumber("10");
        assertEquals("10", address.getNumber());
        
        address.setNumber("20");
        assertEquals("20", address.getNumber());
    }

    @Test
    @DisplayName("Should handle complete address for Pará state")
    void testCompleteParáAddress() {
        Address address = new Address();
        
        address.setId(1);
        address.setAddressId("PA-001");
        address.setStreetType("Avenida");
        address.setStreet("Almirante Barroso");
        address.setNumber("3775");
        address.setComplement("Torre A");
        address.setNeighborhood("Souza");
        address.setSubdistrict("Belém Centro");
        address.setDistrict("DABEL");
        address.setMunicipality("Belém");
        address.setState("PA");
        address.setZipCode("66613-710");
        address.setLatitude(-1.4332);
        address.setLongitude(-48.4478);
        
        assertNotNull(address.getId());
        assertNotNull(address.getStreet());
        assertNotNull(address.getMunicipality());
        assertNotNull(address.getState());
        assertEquals("PA", address.getState());
        assertTrue(address.getLatitude() < 0, "Latitude should be negative (southern hemisphere)");
        assertTrue(address.getLongitude() < 0, "Longitude should be negative (western hemisphere)");
    }

    @Test
    @DisplayName("Should handle address without optional fields")
    void testMinimalAddress() {
        Address address = new Address();
        
        address.setId(1);
        address.setStreet("Rua Principal");
        address.setMunicipality("Ananindeua");
        address.setState("PA");
        
        assertEquals(1, address.getId());
        assertEquals("Rua Principal", address.getStreet());
        assertEquals("Ananindeua", address.getMunicipality());
        assertEquals("PA", address.getState());
        
        // Optional fields should remain null
        assertNull(address.getComplement());
        assertNull(address.getSubdistrict());
        assertNull(address.getLatitude());
        assertNull(address.getLongitude());
    }
}