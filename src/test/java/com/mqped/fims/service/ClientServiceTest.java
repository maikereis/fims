package com.mqped.fims.service;

import com.mqped.fims.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClientServiceTest {

    private ClientService service;

    @BeforeEach
    void setUp() {
        service = new ClientService();
    }

    @Test
    void testAdd_AssignsIdAndStoresClient() {
        Client client = new Client();
        client.setName("Alice");
        client.setCpf("12345678900");
        client.setGenre("F");
        client.setCreatedAt(LocalDateTime.now());

        Client result = service.add(client);

        assertNotNull(result.getId(), "ID should be auto-assigned");
        assertEquals(1, result.getId(), "First client should have ID 1");
        assertEquals("Alice", result.getName());
        assertEquals("12345678900", result.getCpf());
    }

    @Test
    void testAdd_IncrementingIds() {
        Client c1 = new Client();
        Client c2 = new Client();

        Client result1 = service.add(c1);
        Client result2 = service.add(c2);

        assertEquals(1, result1.getId());
        assertEquals(2, result2.getId());
    }

    @Test
    void testFindById_ExistingClient() {
        Client c = new Client();
        c.setName("Bob");
        c.setCpf("98765432100");
        Client saved = service.add(c);

        Optional<Client> result = service.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("Bob", result.get().getName());
        assertEquals("98765432100", result.get().getCpf());
    }

    @Test
    void testFindById_NonExistingClient() {
        Optional<Client> result = service.findById(999);

        assertFalse(result.isPresent());
    }

    @Test
    void testFindAll_EmptyList() {
        List<Client> result = service.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAll_MultipleClients() {
        Client c1 = new Client();
        c1.setName("Alice");

        Client c2 = new Client();
        c2.setName("Bob");

        service.add(c1);
        service.add(c2);

        List<Client> result = service.findAll();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
        assertEquals("Bob", result.get(1).getName());
    }

    @Test
    void testUpdate_ExistingClient() {
        Client original = new Client();
        original.setName("Old Name");
        original.setCpf("11111111111");
        Client saved = service.add(original);

        Client updated = new Client();
        updated.setName("New Name");
        updated.setCpf("22222222222");
        updated.setGenre("M");
        updated.setMotherName("Jane Doe");

        Optional<Client> result = service.update(saved.getId(), updated);

        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId(), "ID should remain unchanged");
        assertEquals("New Name", result.get().getName());
        assertEquals("22222222222", result.get().getCpf());
        assertEquals("M", result.get().getGenre());
        assertEquals("Jane Doe", result.get().getMotherName());
    }

    @Test
    void testUpdate_NonExistingClient() {
        Client client = new Client();
        client.setName("Ghost");

        Optional<Client> result = service.update(999, client);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteById_ExistingClient() {
        Client client = new Client();
        client.setName("To Delete");
        Client saved = service.add(client);

        boolean deleted = service.deleteById(saved.getId());

        assertTrue(deleted);
        assertFalse(service.existsById(saved.getId()));
    }

    @Test
    void testDeleteById_NonExistingClient() {
        boolean deleted = service.deleteById(999);

        assertFalse(deleted);
    }

    @Test
    void testExistsById_ExistingClient() {
        Client client = new Client();
        client.setName("Tester");
        Client saved = service.add(client);

        assertTrue(service.existsById(saved.getId()));
    }

    @Test
    void testExistsById_NonExistingClient() {
        assertFalse(service.existsById(999));
    }

    @Test
    void testCount_EmptyService() {
        assertEquals(0, service.count());
    }

    @Test
    void testCount_WithClients() {
        service.add(new Client());
        service.add(new Client());
        service.add(new Client());

        assertEquals(3, service.count());
    }

    @Test
    void testCount_AfterDeletion() {
        Client c1 = service.add(new Client());
        service.add(new Client());

        service.deleteById(c1.getId());

        assertEquals(1, service.count());
    }

    @Test
    void testToString_ContainsKeyFields() {
        Client client = new Client();
        client.setId(1);
        client.setName("Alice");
        client.setCpf("123");
        String s = client.toString();

        assertTrue(s.contains("Alice"));
        assertFalse(s.contains("123"));
        assertTrue(s.contains("Client{"));
    }
}
