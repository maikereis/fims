package com.mqped.fims.controller;

import com.mqped.fims.exceptions.ResourceNotFoundException;
import com.mqped.fims.model.dto.ContractAccountDTO;
import com.mqped.fims.model.entity.Client;
import com.mqped.fims.model.entity.ContractAccount;
import com.mqped.fims.model.entity.Installation;
import com.mqped.fims.service.ContractAccountService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContractAccountControllerTest {

    @Mock
    private ContractAccountService service;

    @InjectMocks
    private ContractAccountController controller;

    private ContractAccount account1;
    private ContractAccount account2;
    private Client client1;
    private Installation installation1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        client1 = new Client();
        client1.setId(1);
        client1.setName("John Doe");

        installation1 = new Installation();
        installation1.setId(1);

        account1 = new ContractAccount();
        account1.setId(1);
        account1.setAccountNumber("ACC123");
        account1.setClient(client1);
        account1.setInstallation(installation1);
        account1.setCreatedAt(LocalDateTime.now());

        account2 = new ContractAccount();
        account2.setId(2);
        account2.setAccountNumber("ACC456");
        account2.setClient(client1);
        account2.setInstallation(installation1);
        account2.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testCheck_returnsOk() {
        ResponseEntity<String> response = controller.check();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ContractAccount API is up and running!", response.getBody());
    }

    @Test
    void testCreateContractAccount_returnsCreatedAccountDTO() {
        when(service.add(account1)).thenReturn(account1);

        ResponseEntity<ContractAccountDTO> response = controller.createContractAccount(account1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(account1.getId(), response.getBody().getId());
        assertEquals(account1.getAccountNumber(), response.getBody().getAccountNumber());
        assertEquals("John Doe", response.getBody().getClientName());
        verify(service, times(1)).add(account1);
    }

    @Test
    void testGetAllContractAccounts_returnsAllAccountDTOs() {
        List<ContractAccount> accounts = Arrays.asList(account1, account2);
        when(service.findAll()).thenReturn(accounts);

        ResponseEntity<List<ContractAccountDTO>> response = controller.getAllContractAccounts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("ACC123", response.getBody().get(0).getAccountNumber());
        assertEquals("ACC456", response.getBody().get(1).getAccountNumber());
        verify(service, times(1)).findAll();
    }

    @Test
    void testGetAllContractAccountsMinimal_returnsMinimalDTOs() {
        List<ContractAccount> accounts = Arrays.asList(account1, account2);
        when(service.findAll()).thenReturn(accounts);

        ResponseEntity<List<ContractAccountDTO>> response = controller.getAllContractAccountsMinimal();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        // Minimal version should have installationId but not full installation object
        assertEquals(1, response.getBody().get(0).getInstallationId());
        assertNull(response.getBody().get(0).getInstallation());
        verify(service, times(1)).findAll();
    }

    @Test
    void testGetContractAccountById_returnsAccountDTOWhenFound() {
        when(service.findById(1)).thenReturn(account1);

        ResponseEntity<ContractAccountDTO> response = controller.getContractAccountById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(account1.getId(), response.getBody().getId());
        assertEquals("John Doe", response.getBody().getClientName());
        verify(service, times(1)).findById(1);
    }

    @Test
    void testGetContractAccountById_throwsExceptionWhenNotFound() {
        when(service.findById(3)).thenThrow(new ResourceNotFoundException("ContractAccount with id 3 not found"));

        assertThrows(ResourceNotFoundException.class, () -> controller.getContractAccountById(3));
        verify(service, times(1)).findById(3);
    }

    @Test
    void testUpdateContractAccount_returnsUpdatedAccountDTOWhenFound() {
        ContractAccount updated = new ContractAccount();
        updated.setId(1);
        updated.setAccountNumber("ACC123-UPDATED");
        updated.setClient(client1);
        updated.setInstallation(installation1);
        updated.setCreatedAt(account1.getCreatedAt());
        
        when(service.update(1, account1)).thenReturn(updated);

        ResponseEntity<ContractAccountDTO> response = controller.updateContractAccount(1, account1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ACC123-UPDATED", response.getBody().getAccountNumber());
        verify(service, times(1)).update(1, account1);
    }

    @Test
    void testUpdateContractAccount_throwsExceptionWhenNotFound() {
        when(service.update(3, account1)).thenThrow(new ResourceNotFoundException("ContractAccount with id 3 not found"));

        assertThrows(ResourceNotFoundException.class, () -> controller.updateContractAccount(3, account1));
        verify(service, times(1)).update(3, account1);
    }

    @Test
    void testDeleteContractAccount_returnsNoContentWhenFound() {
        doNothing().when(service).deleteById(1);

        ResponseEntity<Void> response = controller.deleteContractAccount(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).deleteById(1);
    }

    @Test
    void testDeleteContractAccount_throwsExceptionWhenNotFound() {
        doThrow(new ResourceNotFoundException("ContractAccount with id 3 not found"))
            .when(service).deleteById(3);

        assertThrows(ResourceNotFoundException.class, () -> controller.deleteContractAccount(3));
        verify(service, times(1)).deleteById(3);
    }
}