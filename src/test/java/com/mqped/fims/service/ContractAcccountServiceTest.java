package com.mqped.fims.service;

import com.mqped.fims.exceptions.ResourceNotFoundException;
import com.mqped.fims.model.entity.Address;
import com.mqped.fims.model.entity.Client;
import com.mqped.fims.model.entity.ContractAccount;
import com.mqped.fims.model.entity.Installation;
import com.mqped.fims.repository.ClientRepository;
import com.mqped.fims.repository.ContractAccountRepository;
import com.mqped.fims.repository.InstallationRepository;
import com.mqped.fims.repository.AddressRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ContractAccountServiceTest {

    @Autowired
    private ContractAccountRepository contractAccountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private InstallationRepository installationRepository;

    @Autowired
    private AddressRepository addressRepository;

    private ContractAccountService service;

    @BeforeEach
    void setUp() {
        service = new ContractAccountService(
                contractAccountRepository,
                clientRepository,
                installationRepository);

        // Clear all repositories to start fresh
        contractAccountRepository.deleteAll();
        installationRepository.deleteAll();
        addressRepository.deleteAll();
        clientRepository.deleteAll();
    }

    private ContractAccount createValidContractAccount() {
        ContractAccount account = new ContractAccount();
        account.setAccountNumber("DEFAULT-ACC");

        Client client = new Client();
        client.setName("Test Client");
        client = clientRepository.save(client);
        account.setClient(client);

        Address address = new Address();
        address.setAddressId("ADDR-001");
        address.setState("PA");
        address.setMunicipality("Belém");
        address.setStreet("Rua Teste");
        address = addressRepository.save(address);

        Installation installation = new Installation();
        installation.setAddress(address);
        installation.setCreatedAt(LocalDateTime.now());
        installation = installationRepository.save(installation);
        account.setInstallation(installation);

        account.setCreatedAt(LocalDateTime.now());
        return account;
    }

    @Test
    void testAdd_AssignsIdAndStoresAccount() {
        ContractAccount account = createValidContractAccount();
        account.setAccountNumber("ACC-001");

        ContractAccount result = service.add(account);

        assertNotNull(result.getId(), "ID should be auto-assigned");
        assertEquals("ACC-001", result.getAccountNumber());
    }

    @Test
    void testAdd_IncrementingIds() {
        ContractAccount ca1 = createValidContractAccount();
        ca1.setAccountNumber("ACC-001");
        ContractAccount ca2 = createValidContractAccount();
        ca2.setAccountNumber("ACC-002");

        ContractAccount result1 = service.add(ca1);
        ContractAccount result2 = service.add(ca2);

        assertNotNull(result1.getId());
        assertNotNull(result2.getId());
        assertTrue(result2.getId() > result1.getId());
    }

    @Test
    void testFindById_ExistingAccount() {
        ContractAccount account = createValidContractAccount();
        account.setAccountNumber("ACC-123");
        ContractAccount saved = service.add(account);

        Optional<ContractAccount> result = service.findById(saved.getId());
        assertTrue(result.isPresent());
        assertEquals("ACC-123", result.get().getAccountNumber());
    }

    @Test
    void testFindById_NonExistingAccount() {
        Optional<ContractAccount> result = service.findById(999);
        assertFalse(result.isPresent());
    }

    @Test
    void testFindAll_MultipleAccounts() {
        ContractAccount ca1 = createValidContractAccount();
        ca1.setAccountNumber("ACC-001");
        ContractAccount ca2 = createValidContractAccount();
        ca2.setAccountNumber("ACC-002");

        service.add(ca1);
        service.add(ca2);

        List<ContractAccount> result = service.findAll();
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(a -> "ACC-001".equals(a.getAccountNumber())));
        assertTrue(result.stream().anyMatch(a -> "ACC-002".equals(a.getAccountNumber())));
    }

    @Test
    void testUpdate_ExistingAccount() {
        ContractAccount original = createValidContractAccount();
        original.setAccountNumber("OLD-ACC");
        ContractAccount saved = service.add(original);

        ContractAccount updated = createValidContractAccount();
        updated.setAccountNumber("NEW-ACC");
        updated.setDeletedAt(LocalDateTime.now().plusDays(1));

        Optional<ContractAccount> result = service.update(saved.getId(), updated);

        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
        assertEquals("NEW-ACC", result.get().getAccountNumber());
        assertNotNull(result.get().getDeletedAt());
    }

    @Test
    void testUpdate_NonExistingAccount() {
        ContractAccount account = createValidContractAccount();
        account.setAccountNumber("GHOST");

        Optional<ContractAccount> result = service.update(999, account);
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteById_ExistingAccount() {
        ContractAccount account = createValidContractAccount();
        account.setAccountNumber("TO-DELETE");
        ContractAccount saved = service.add(account);

        service.deleteById(saved.getId());
        assertFalse(service.existsById(saved.getId()));
    }

    @Test
    void testDeleteById_NonExistingAccount() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> service.deleteById(999));
        assertEquals("ContractAccount with id 999 not found", exception.getMessage());
    }

    @Test
    void testExistsById() {
        ContractAccount account = createValidContractAccount();
        account.setAccountNumber("TEST-ACC");
        ContractAccount saved = service.add(account);

        assertTrue(service.existsById(saved.getId()));
        assertFalse(service.existsById(999));
    }

    @Test
    void testCount() {
        assertEquals(0, service.count());

        ContractAccount ca1 = createValidContractAccount();
        ca1.setAccountNumber("ACC-001");
        ContractAccount ca2 = createValidContractAccount();
        ca2.setAccountNumber("ACC-002");

        service.add(ca1);
        service.add(ca2);
        assertEquals(2, service.count());
    }
}