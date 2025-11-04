package com.mqped.fims.controller;

import com.mqped.fims.model.entity.ContractAccount;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContractAccountControllerTest {

    @Mock
    private ContractAccountService service;

    @InjectMocks
    private ContractAccountController controller;

    private ContractAccount account1;
    private ContractAccount account2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        account1 = new ContractAccount();
        account1.setId(1);
        account1.setAccountNumber("ACC123");
        account1.setCreatedAt(LocalDateTime.now());

        account2 = new ContractAccount();
        account2.setId(2);
        account2.setAccountNumber("ACC456");
        account2.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testCheck_returnsOk() {
        ResponseEntity<String> response = controller.check();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ContractAccount API is up and running!", response.getBody());
    }

    @Test
    void testCreateContractAccount_returnsCreatedAccount() {
        when(service.add(account1)).thenReturn(account1);

        ResponseEntity<ContractAccount> response = controller.createContractAccount(account1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(account1, response.getBody());
        verify(service, times(1)).add(account1);
    }

    @Test
    void testGetAllContractAccounts_returnsAllAccounts() {
        List<ContractAccount> accounts = Arrays.asList(account1, account2);
        when(service.findAll()).thenReturn(accounts);

        ResponseEntity<List<ContractAccount>> response = controller.getAllContractAccounts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(service, times(1)).findAll();
    }

    @Test
    void testGetContractAccountById_returnsAccountWhenFound() {
        when(service.findById(1)).thenReturn(Optional.of(account1));

        ResponseEntity<ContractAccount> response = controller.getContractAccountById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account1, response.getBody());
    }

    @Test
    void testGetContractAccountById_returnsNotFoundWhenMissing() {
        when(service.findById(3)).thenReturn(Optional.empty());

        ResponseEntity<ContractAccount> response = controller.getContractAccountById(3);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateContractAccount_returnsUpdatedAccountWhenFound() {
        when(service.update(1, account1)).thenReturn(Optional.of(account1));

        ResponseEntity<ContractAccount> response = controller.updateContractAccount(1, account1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account1, response.getBody());
        verify(service, times(1)).update(1, account1);
    }

    @Test
    void testUpdateContractAccount_returnsNotFoundWhenMissing() {
        when(service.update(3, account1)).thenReturn(Optional.empty());

        ResponseEntity<ContractAccount> response = controller.updateContractAccount(3, account1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteContractAccount_returnsNoContentWhenFound() {
        when(service.existsById(1)).thenReturn(true);
        doNothing().when(service).deleteById(1);

        ResponseEntity<Void> response = controller.deleteContractAccount(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).deleteById(1);
    }

    @Test
    void testDeleteContractAccount_returnsNotFoundWhenMissing() {
        when(service.existsById(3)).thenReturn(false);

        ResponseEntity<Void> response = controller.deleteContractAccount(3);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(service, never()).deleteById(anyInt());
    }
}
