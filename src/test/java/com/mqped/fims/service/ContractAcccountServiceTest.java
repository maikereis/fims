package com.mqped.fims.service;

import com.mqped.fims.model.Client;
import com.mqped.fims.model.ContractAccount;
import com.mqped.fims.model.Installation;
import com.mqped.fims.model.StatusType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ContractAccountServiceTest {

    private ContractAccountService service;

    @BeforeEach
    void setUp() {
        service = new ContractAccountService();
    }

    @Test
    void testAdd_AssignsIdAndStoresAccount() {
        ContractAccount ca = new ContractAccount();
        ca.setAccountNumber("ACC001");
        ca.setCreatedAt(LocalDateTime.now());
        ca.setStatus(StatusType.ON);

        ContractAccount result = service.add(ca);

        assertNotNull(result.getId(), "ID should be auto-assigned");
        assertEquals(1, result.getId());
        assertEquals("ACC001", result.getAccountNumber());
        assertEquals(StatusType.ON, result.getStatus());
    }

    @Test
    void testAdd_IncrementingIds() {
        ContractAccount a1 = new ContractAccount();
        ContractAccount a2 = new ContractAccount();

        ContractAccount r1 = service.add(a1);
        ContractAccount r2 = service.add(a2);

        assertEquals(1, r1.getId());
        assertEquals(2, r2.getId());
    }

    @Test
    void testFindById_ExistingAccount() {
        ContractAccount ca = new ContractAccount();
        ca.setAccountNumber("ACC002");
        ContractAccount saved = service.add(ca);

        Optional<ContractAccount> result = service.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("ACC002", result.get().getAccountNumber());
    }

    @Test
    void testFindById_NonExistingAccount() {
        Optional<ContractAccount> result = service.findById(999);

        assertFalse(result.isPresent());
    }

    @Test
    void testFindAll_EmptyList() {
        List<ContractAccount> result = service.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAll_MultipleAccounts() {
        ContractAccount a1 = new ContractAccount();
        a1.setAccountNumber("A001");

        ContractAccount a2 = new ContractAccount();
        a2.setAccountNumber("A002");

        service.add(a1);
        service.add(a2);

        List<ContractAccount> result = service.findAll();

        assertEquals(2, result.size());
        assertEquals("A001", result.get(0).getAccountNumber());
        assertEquals("A002", result.get(1).getAccountNumber());
    }

    @Test
    void testUpdate_ExistingAccount() {
        ContractAccount original = new ContractAccount();
        original.setAccountNumber("OLD_ACC");
        original.setStatus(StatusType.ON);
        ContractAccount saved = service.add(original);

        ContractAccount updated = new ContractAccount();
        updated.setAccountNumber("NEW_ACC");
        updated.setStatus(StatusType.OFF);
        updated.setStatusStart(LocalDateTime.now().minusDays(5));
        updated.setStatusEnd(LocalDateTime.now());

        Optional<ContractAccount> result = service.update(saved.getId(), updated);

        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId(), "ID should remain unchanged");
        assertEquals("NEW_ACC", result.get().getAccountNumber());
        assertEquals(StatusType.OFF, result.get().getStatus());
    }

    @Test
    void testUpdate_NonExistingAccount() {
        ContractAccount ca = new ContractAccount();
        ca.setAccountNumber("GhostACC");

        Optional<ContractAccount> result = service.update(999, ca);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteById_ExistingAccount() {
        ContractAccount ca = new ContractAccount();
        ca.setAccountNumber("DEL_ACC");
        ContractAccount saved = service.add(ca);

        boolean deleted = service.deleteById(saved.getId());

        assertTrue(deleted);
        assertFalse(service.existsById(saved.getId()));
    }

    @Test
    void testDeleteById_NonExistingAccount() {
        boolean deleted = service.deleteById(999);

        assertFalse(deleted);
    }

    @Test
    void testExistsById_ExistingAccount() {
        ContractAccount ca = new ContractAccount();
        ContractAccount saved = service.add(ca);

        assertTrue(service.existsById(saved.getId()));
    }

    @Test
    void testExistsById_NonExistingAccount() {
        assertFalse(service.existsById(999));
    }

    @Test
    void testCount_EmptyService() {
        assertEquals(0, service.count());
    }

    @Test
    void testCount_WithAccounts() {
        service.add(new ContractAccount());
        service.add(new ContractAccount());

        assertEquals(2, service.count());
    }

    @Test
    void testCount_AfterDeletion() {
        ContractAccount c1 = service.add(new ContractAccount());
        service.add(new ContractAccount());

        service.deleteById(c1.getId());

        assertEquals(1, service.count());
    }

    @Test
    void testToString_ContainsKeyFields() {
        Client client = new Client();
        client.setName("Alice");

        Installation installation = new Installation();
        installation.setId(42);

        ContractAccount ca = new ContractAccount();
        ca.setId(1);
        ca.setAccountNumber("ACC999");
        ca.setClient(client);
        ca.setInstallation(installation);
        ca.setStatus(StatusType.ON);
        String s = ca.toString();

        assertTrue(s.contains("ContractAccount{"));
        assertTrue(s.contains("ACC999"));
        assertTrue(s.contains("Alice"));
        assertTrue(s.contains("42"));
        assertTrue(s.contains("ON"));
    }
}
