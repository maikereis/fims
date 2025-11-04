package com.mqped.fims.service;

import com.mqped.fims.exceptions.ResourceNotFoundException;
import com.mqped.fims.model.entity.Client;
import com.mqped.fims.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClientServiceTest {

    private ClientService service;

    @Autowired
    private ClientRepository repository;

    @BeforeEach
    void setUp() {
        service = new ClientService(repository);
        repository.deleteAll();
    }

    @Test
    void testAdd_AssignsIdAndStoresClient() {
        Client client = new Client();
        client.setName("Ana Clara");
        client.setCpf("123.456.789-00");
        client.setGenre("F");
        client.setCreatedAt(LocalDateTime.now());

        Client result = service.add(client);

        assertNotNull(result.getId(), "ID should be auto-assigned");
        assertEquals("Ana Clara", result.getName());
        assertEquals("123.456.789-00", result.getCpf());
    }

    @Test
    void testFindById_ExistingClient() {
        Client c = new Client();
        c.setName("Carlos Eduardo");
        c.setCpf("987.654.321-00");
        Client saved = service.add(c);

        Optional<Client> result = service.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("Carlos Eduardo", result.get().getName());
        assertEquals("987.654.321-00", result.get().getCpf());
    }

    @Test
    void testFindById_NonExistingClient() {
        Optional<Client> result = service.findById(999);
        assertFalse(result.isPresent());
    }

    @Test
    void testFindAll_MultipleClients() {
        Client c1 = new Client();
        c1.setName("Ana Clara");
        Client c2 = new Client();
        c2.setName("Carlos Eduardo");

        service.add(c1);
        service.add(c2);

        List<Client> result = service.findAll();
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> c.getName().equals("Ana Clara")));
        assertTrue(result.stream().anyMatch(c -> c.getName().equals("Carlos Eduardo")));
    }

    @Test
    void testUpdate_ExistingClient() {
        Client original = new Client();
        original.setName("João Silva");
        original.setCpf("111.111.111-11");
        Client saved = service.add(original);

        Client updated = new Client();
        updated.setName("Mariana Souza");
        updated.setCpf("222.222.222-22");
        updated.setGenre("F");
        updated.setMotherName("Clara Souza");

        Optional<Client> result = service.update(saved.getId(), updated);

        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
        assertEquals("Mariana Souza", result.get().getName());
        assertEquals("222.222.222-22", result.get().getCpf());
        assertEquals("F", result.get().getGenre());
        assertEquals("Clara Souza", result.get().getMotherName());
    }

    @Test
    void testUpdate_NonExistingClient() {
        Client client = new Client();
        client.setName("Pedro Alves");

        Optional<Client> result = service.update(999, client);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteById_ExistingClient() {
        Client client = new Client();
        client.setName("Lucas Ferreira");
        Client saved = service.add(client);

        service.deleteById(saved.getId());
        assertFalse(service.existsById(saved.getId()));
    }

    @Test
    void testDeleteById_NonExistingClient() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> service.deleteById(999));
        assertEquals("Client with id 999 not found", exception.getMessage());
    }

    @Test
    void testExistsById() {
        Client client = new Client();
        client.setName("Gabriela Martins");
        Client saved = service.add(client);

        assertTrue(service.existsById(saved.getId()));
        assertFalse(service.existsById(999));
    }

    @Test
    void testCount() {
        assertEquals(0, service.count());

        Client c1 = new Client();
        c1.setName("Rafael Lima");
        Client c2 = new Client();
        c2.setName("Fernanda Rocha");

        service.add(c1);
        service.add(c2);
        assertEquals(2, service.count());
    }
}
