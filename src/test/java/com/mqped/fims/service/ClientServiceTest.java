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

    private Client createValidClient(String name, String cpf) {
        Client client = new Client();
        client.setName(name);
        client.setCpf(cpf);
        client.setGenre("F");
        client.setBirthDate(LocalDateTime.now().minusYears(25));
        client.setCreatedAt(LocalDateTime.now());
        return client;
    }

    @Test
    void testAdd_AssignsIdAndStoresClient() {
        Client client = createValidClient("Ana Clara", "123.456.789-00");

        Client result = service.add(client);

        assertNotNull(result.getId(), "ID should be auto-assigned");
        assertEquals("Ana Clara", result.getName());
        assertEquals("123.456.789-00", result.getCpf());
    }

    @Test
    void testFindById_ExistingClient() {
        Client saved = service.add(createValidClient("Carlos Eduardo", "987.654.321-00"));

        Client result = service.findById(saved.getId());

        assertNotNull(result);
        assertEquals("Carlos Eduardo", result.getName());
        assertEquals("987.654.321-00", result.getCpf());
    }

    @Test
    void testFindById_NonExistingClient_ThrowsException() {
        assertThrows(ResourceNotFoundException.class, () -> service.findById(999));
    }

    @Test
    void testFindAll_MultipleClients() {
        service.add(createValidClient("Ana Clara", "111.111.111-11"));
        service.add(createValidClient("Carlos Eduardo", "222.222.222-22"));

        List<Client> result = service.findAll();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> c.getName().equals("Ana Clara")));
        assertTrue(result.stream().anyMatch(c -> c.getName().equals("Carlos Eduardo")));
    }

    @Test
    void testUpdate_ExistingClient() {
        Client saved = service.add(createValidClient("João Silva", "111.111.111-11"));

        Client updated = createValidClient("Mariana Souza", "222.222.222-22");
        updated.setMotherName("Clara Souza");

        Client result = service.update(saved.getId(), updated);

        assertNotNull(result);
        assertEquals(saved.getId(), result.getId());
        assertEquals("Mariana Souza", result.getName());
        assertEquals("222.222.222-22", result.getCpf());
        assertEquals("F", result.getGenre());
        assertEquals("Clara Souza", result.getMotherName());
    }

    @Test
    void testUpdate_NonExistingClient_ThrowsException() {
        Client client = createValidClient("Pedro Alves", "333.333.333-33");
        
        assertThrows(ResourceNotFoundException.class, () -> service.update(999, client));
    }

    @Test
    void testDeleteById_ExistingClient() {
        Client saved = service.add(createValidClient("Lucas Ferreira", "444.444.444-44"));

        service.deleteById(saved.getId());
        assertFalse(service.existsById(saved.getId()));
    }

    @Test
    void testDeleteById_NonExistingClient_ThrowsException() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> service.deleteById(999));
        assertEquals("Client with id 999 not found", exception.getMessage());
    }

    @Test
    void testExistsById() {
        Client saved = service.add(createValidClient("Gabriela Martins", "555.555.555-55"));

        assertTrue(service.existsById(saved.getId()));
        assertFalse(service.existsById(999));
    }

    @Test
    void testCount() {
        assertEquals(0, service.count());

        service.add(createValidClient("Rafael Lima", "666.666.666-66"));
        service.add(createValidClient("Fernanda Rocha", "777.777.777-77"));

        assertEquals(2, service.count());
    }
}