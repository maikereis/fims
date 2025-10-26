package com.mqped.fims.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InstallationTest {

    @Test
    @DisplayName("Should set and get all fields correctly")
    void testGettersAndSetters() {
        Installation installation = new Installation();
        Address address = new Address();
        address.setStreet("Avenida Nazaré");
        address.setMunicipality("Belém");
        address.setState("PA");

        LocalDateTime created = LocalDateTime.of(2025, 1, 1, 12, 0);
        LocalDateTime deleted = LocalDateTime.of(2025, 5, 1, 12, 0);

        installation.setId(123);
        installation.setAddress(address);
        installation.setCreateAt(created);
        installation.setDeletedAt(deleted);

        assertEquals(123, installation.getId());
        assertEquals(address, installation.getAddress());
        assertEquals(created, installation.getCreateAt());
        assertEquals(deleted, installation.getDeletedAt());
    }

    @Test
    @DisplayName("Should initialize all fields as null by default")
    void testEmptyConstructorDefaults() {
        Installation installation = new Installation();

        assertNull(installation.getId());
        assertNull(installation.getAddress());
        assertNull(installation.getCreateAt());
        assertNull(installation.getDeletedAt());
    }

    @Test
    @DisplayName("Should include all fields in toString output")
    void testToStringContainsAllFields() {
        Installation installation = new Installation();

        Address address = new Address();
        address.setStreet("Rua dos Caripunas");
        address.setMunicipality("Belém");
        address.setState("PA");

        installation.setId(123);
        installation.setAddress(address);
        installation.setCreateAt(LocalDateTime.of(2025, 10, 22, 18, 0));
        installation.setDeletedAt(LocalDateTime.of(2025, 10, 23, 18, 0));

        String str = installation.toString();

        assertTrue(str.contains("id=123"), "Should contain id");
        assertTrue(str.contains("Rua dos Caripunas"), "Should contain address street");
        assertTrue(str.contains("PA"), "Should contain state");
        assertTrue(str.contains("createdAt"), "Should contain createdAt field");
        assertTrue(str.contains("deletedAt"), "Should contain deletedAt field");
    }

    @Test
    @DisplayName("Should handle null values gracefully in toString")
    void testToStringWithNullValues() {
        Installation installation = new Installation();

        String str = installation.toString();

        assertNotNull(str);
        assertTrue(str.startsWith("Installation{"));
        assertTrue(str.contains("null"), "Should print 'null' for missing fields");
    }

    @Test
    @DisplayName("Should allow updating fields multiple times")
    void testFieldMutability() {
        Installation installation = new Installation();

        installation.setId(123);
        assertEquals(123, installation.getId());

        installation.setId(456);
        assertEquals(456, installation.getId());

        LocalDateTime firstCreate = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime secondCreate = LocalDateTime.of(2025, 2, 1, 10, 0);

        installation.setCreateAt(firstCreate);
        assertEquals(firstCreate, installation.getCreateAt());

        installation.setCreateAt(secondCreate);
        assertEquals(secondCreate, installation.getCreateAt());
    }

    @Test
    @DisplayName("Should correctly represent Installation with nested Address in toString")
    void testToStringWithNestedAddress() {
        Installation installation = new Installation();
        Address address = new Address();
        address.setStreet("Travessa Humaitá");
        address.setState("PA");

        installation.setId(123);
        installation.setAddress(address);

        String str = installation.toString();

        assertTrue(str.contains("Travessa Humaitá"));
        assertTrue(str.contains("PA"));
        assertTrue(str.contains("123"));
    }
}
