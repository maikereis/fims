package com.mqped.fims.controller;

import com.mqped.fims.model.dto.ClientDTO;
import com.mqped.fims.model.entity.Client;
import com.mqped.fims.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class ClientControllerTest {

    @Mock
    private ClientService service;

    @InjectMocks
    private ClientController controller;

    private Client client1;
    private Client client2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        client1 = new Client();
        client1.setId(1);
        client1.setName("Alice");
        client1.setCpf("111.111.111-11");
        client1.setBirthDate(LocalDateTime.now().minusYears(30));

        client2 = new Client();
        client2.setId(2);
        client2.setName("Bob");
        client2.setCpf("222.222.222-22");
        client2.setBirthDate(LocalDateTime.now().minusYears(25));
    }

    @Test
    void testCheck_returnsOk() {
        ResponseEntity<String> response = controller.check();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Client API is up and running!", response.getBody());
    }

    @Test
    void testCreateClient_returnsCreatedClientDTO() {
        when(service.add(client1)).thenReturn(client1);

        ResponseEntity<ClientDTO> response = controller.createClient(client1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(client1.getId(), response.getBody().getId());
        assertEquals(client1.getName(), response.getBody().getName());
        assertEquals(client1.getCpf(), response.getBody().getCpf());
        verify(service, times(1)).add(client1);
    }

    @Test
    void testGetAllClients_returnsAllClientDTOs() {
        List<Client> clients = Arrays.asList(client1, client2);
        when(service.findAll()).thenReturn(clients);

        ResponseEntity<List<ClientDTO>> response = controller.getAllClients();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Alice", response.getBody().get(0).getName());
        assertEquals("Bob", response.getBody().get(1).getName());
        verify(service, times(1)).findAll();
    }

    @Test
    void testGetClientById_returnsClientDTOWhenFound() {
        when(service.findById(1)).thenReturn(Optional.of(client1));

        ResponseEntity<ClientDTO> response = controller.getClientById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(client1.getId(), response.getBody().getId());
        assertEquals(client1.getName(), response.getBody().getName());
        verify(service, times(1)).findById(1);
    }

    @Test
    void testGetClientById_returnsNotFoundWhenMissing() {
        when(service.findById(3)).thenReturn(Optional.empty());

        ResponseEntity<ClientDTO> response = controller.getClientById(3);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service, times(1)).findById(3);
    }

    @Test
    void testUpdateClient_returnsUpdatedClientDTOWhenFound() {
        Client updatedClient = new Client();
        updatedClient.setId(1);
        updatedClient.setName("Alice Updated");
        updatedClient.setCpf(client1.getCpf());
        
        when(service.update(1, client1)).thenReturn(Optional.of(updatedClient));

        ResponseEntity<ClientDTO> response = controller.updateClient(1, client1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Alice Updated", response.getBody().getName());
        verify(service, times(1)).update(1, client1);
    }

    @Test
    void testUpdateClient_returnsNotFoundWhenMissing() {
        when(service.update(3, client1)).thenReturn(Optional.empty());

        ResponseEntity<ClientDTO> response = controller.updateClient(3, client1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteClient_returnsNoContentWhenFound() {
        when(service.existsById(1)).thenReturn(true);
        doNothing().when(service).deleteById(1);

        ResponseEntity<Void> response = controller.deleteClient(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).deleteById(1);
    }

    @Test
    void testDeleteClient_returnsNotFoundWhenMissing() {
        when(service.existsById(3)).thenReturn(false);

        ResponseEntity<Void> response = controller.deleteClient(3);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(service, never()).deleteById(anyInt());
    }
}