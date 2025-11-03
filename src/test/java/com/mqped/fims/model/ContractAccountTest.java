package com.mqped.fims.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ContractAccountTest {

    @Test
    @DisplayName("Should set and get all contract account fields correctly")
    void testGettersAndSetters() {
        ContractAccount account = new ContractAccount();

        Client client = new Client();
        client.setId(1);
        client.setName("Maria Silva");

        Installation installation = new Installation();
        installation.setId(100);

        LocalDateTime now = LocalDateTime.now();

        account.setId(10);
        account.setAccountNumber("ACC123");
        account.setClient(client);
        account.setInstallation(installation);
        account.setCreatedAt(now);
        account.setDeletedAt(now.plusDays(1));
        account.setStatus(StatusType.ON);
        account.setStatusStart(now.minusHours(1));
        account.setStatusEnd(now.plusHours(5));

        assertEquals(10, account.getId());
        assertEquals("ACC123", account.getAccountNumber());
        assertEquals(client, account.getClient());
        assertEquals(installation, account.getInstallation());
        assertEquals(now, account.getCreatedAt());
        assertEquals(now.plusDays(1), account.getDeletedAt());
        assertEquals(StatusType.ON, account.getStatus());
        assertEquals(now.minusHours(1), account.getStatusStart());
        assertEquals(now.plusHours(5), account.getStatusEnd());
    }

    @Test
    @DisplayName("Should initialize all fields as null by default")
    void testEmptyConstructorDefaults() {
        ContractAccount account = new ContractAccount();

        assertNull(account.getId());
        assertNull(account.getAccountNumber());
        assertNull(account.getClient());
        assertNull(account.getInstallation());
        assertNull(account.getCreatedAt());
        assertNull(account.getDeletedAt());
        assertNull(account.getStatus());
        assertNull(account.getStatusStart());
        assertNull(account.getStatusEnd());
    }

    @Test
    @DisplayName("Should include all fields in toString output")
    void testToStringContainsAllFields() {
        ContractAccount account = new ContractAccount();

        Client client = new Client();
        client.setName("João Souza");

        Installation installation = new Installation();
        installation.setId(200);

        LocalDateTime now = LocalDateTime.now();

        account.setId(5);
        account.setAccountNumber("ACC999");
        account.setClient(client);
        account.setInstallation(installation);
        account.setCreatedAt(now);
        account.setDeletedAt(now.plusDays(2));
        account.setStatus(StatusType.CUT);
        account.setStatusStart(now.minusHours(2));
        account.setStatusEnd(now.plusHours(6));

        String str = account.toString();

        assertTrue(str.contains("id=5"));
        assertTrue(str.contains("ACC999"));
        assertTrue(str.contains("João Souza"));
        assertTrue(str.contains("200"));
        assertTrue(str.contains("status=CUT"));
        assertTrue(str.contains("statusStart="));
        assertTrue(str.contains("statusEnd="));
    }

    @Test
    @DisplayName("Should handle null values gracefully in toString")
    void testToStringWithNullValues() {
        ContractAccount account = new ContractAccount();
        String str = account.toString();

        assertNotNull(str);
        assertTrue(str.startsWith("ContractAccount{"));
        assertTrue(str.contains("null"));
    }

    @Test
    @DisplayName("Should allow updating fields multiple times")
    void testFieldMutability() {
        ContractAccount account = new ContractAccount();

        account.setAccountNumber("ACC001");
        assertEquals("ACC001", account.getAccountNumber());

        account.setAccountNumber("ACC002");
        assertEquals("ACC002", account.getAccountNumber());

        account.setStatus(StatusType.ON);
        assertEquals(StatusType.ON, account.getStatus());

        account.setStatus(StatusType.OFF);
        assertEquals(StatusType.OFF, account.getStatus());
    }

    @Test
    @DisplayName("Should handle date fields correctly")
    void testDateFields() {
        ContractAccount account = new ContractAccount();

        LocalDateTime created = LocalDateTime.of(2025, 10, 22, 18, 0);
        LocalDateTime deleted = LocalDateTime.of(2025, 10, 23, 18, 0);
        LocalDateTime statusStart = LocalDateTime.of(2025, 10, 22, 19, 0);
        LocalDateTime statusEnd = LocalDateTime.of(2025, 10, 22, 20, 0);

        account.setCreatedAt(created);
        account.setDeletedAt(deleted);
        account.setStatusStart(statusStart);
        account.setStatusEnd(statusEnd);

        assertEquals(created, account.getCreatedAt());
        assertEquals(deleted, account.getDeletedAt());
        assertEquals(statusStart, account.getStatusStart());
        assertEquals(statusEnd, account.getStatusEnd());
        assertTrue(account.getDeletedAt().isAfter(account.getCreatedAt()));
        assertTrue(account.getStatusEnd().isAfter(account.getStatusStart()));
    }
}
