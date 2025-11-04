package com.mqped.fims.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mqped.fims.model.entity.Client;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    @DisplayName("Should set and get all client fields correctly")
    void testGettersAndSetters() {
        Client client = new Client();

        LocalDateTime birthDate = LocalDateTime.of(1990, 5, 10, 0, 0);
        LocalDateTime createdAt = LocalDateTime.now();

        client.setId(123);
        client.setName("Maria Silva");
        client.setCpf("123.456.789-00");
        client.setBirthDate(birthDate);
        client.setMotherName("Joana Silva");
        client.setCnpj("12.345.678/0001-90");
        client.setGenre("F");
        client.setCreatedAt(createdAt);

        assertEquals(123, client.getId());
        assertEquals("Maria Silva", client.getName());
        assertEquals("123.456.789-00", client.getCpf());
        assertEquals(birthDate, client.getBirthDate());
        assertEquals("Joana Silva", client.getMotherName());
        assertEquals("12.345.678/0001-90", client.getCnpj());
        assertEquals("F", client.getGenre());
        assertEquals(createdAt, client.getCreatedAt());
    }

    @Test
    @DisplayName("Should initialize all fields as null by default")
    void testEmptyConstructorDefaults() {
        Client client = new Client();

        assertNull(client.getId());
        assertNull(client.getName());
        assertNull(client.getCpf());
        assertNull(client.getBirthDate());
        assertNull(client.getMotherName());
        assertNull(client.getCnpj());
        assertNull(client.getGenre());
        assertNull(client.getCreatedAt());
    }

    @Test
    @DisplayName("Should include main fields in toString output")
    void testToStringContainsKeyFields() {
        Client client = new Client();
        client.setId(123);
        client.setName("Carlos Pereira");
        client.setBirthDate(LocalDateTime.of(1985, 8, 20, 0, 0));
        client.setGenre("M");

        String str = client.toString();

        assertTrue(str.contains("Client{"), "toString should start with class name");
        assertTrue(str.contains("id=123"), "Should contain id");
        assertTrue(str.contains("Carlos Pereira"), "toString should contain name");
        assertTrue(str.contains("1985"), "toString should contain birth year");
        assertTrue(str.contains("M"), "toString should contain genre");
    }

    @Test
    @DisplayName("Should handle null values gracefully in toString")
    void testToStringWithNullValues() {
        Client client = new Client();

        String str = client.toString();

        assertNotNull(str);
        assertTrue(str.startsWith("Client{"));
        assertTrue(str.contains("null"), "Should represent null fields as 'null'");
    }

    @Test
    @DisplayName("Should handle CPF and CNPJ formats correctly")
    void testCpfAndCnpjFormats() {
        Client client = new Client();

        client.setCpf("987.654.321-00");
        client.setCnpj("45.678.901/0001-22");

        assertEquals("987.654.321-00", client.getCpf());
        assertEquals("45.678.901/0001-22", client.getCnpj());
    }

    @Test
    @DisplayName("Should allow updating mutable fields multiple times")
    void testFieldMutability() {
        Client client = new Client();

        client.setName("Old Name");
        assertEquals("Old Name", client.getName());

        client.setName("New Name");
        assertEquals("New Name", client.getName());

        client.setGenre("M");
        assertEquals("M", client.getGenre());

        client.setGenre("F");
        assertEquals("F", client.getGenre());
    }

    @Test
    @DisplayName("Should store correct date and time values")
    void testDateFields() {
        Client client = new Client();

        LocalDateTime birth = LocalDateTime.of(2000, 1, 15, 10, 30);
        LocalDateTime created = LocalDateTime.of(2025, 10, 22, 18, 0);

        client.setBirthDate(birth);
        client.setCreatedAt(created);

        assertEquals(birth, client.getBirthDate());
        assertEquals(created, client.getCreatedAt());
        assertTrue(client.getCreatedAt().isAfter(client.getBirthDate()));
    }

    @Test
    @DisplayName("Should handle minimal client information (only required fields)")
    void testMinimalClientData() {
        Client client = new Client();
        client.setId(123);
        client.setName("João Souza");
        client.setCpf("123.456.789-10");

        assertEquals(123, client.getId());
        assertEquals("João Souza", client.getName());
        assertEquals("123.456.789-10", client.getCpf());

        assertNull(client.getCnpj());
        assertNull(client.getBirthDate());
        assertNull(client.getMotherName());
        assertNull(client.getCreatedAt());
    }
}
