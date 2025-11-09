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
        Client client = new Client();
        client.setName("Test Client");
        client.setCpf("111.111.111-11");
        client.setBirthDate(LocalDateTime.now().minusYears(18));
        client.setCreatedAt(LocalDateTime.now());
        client = clientRepository.save(client);

        Address address = new Address();
        address.setAddressId("ADDR-001");
        address.setState("PA");
        address.setMunicipality("Belém");
        address.setNeighborhood("Icoaraci");
        address.setStreet("Rua Teste");
        address.setZipCode("66810-000");
        address.setNumber("0");
        address = addressRepository.save(address);

        Installation installation = new Installation();
        installation.setAddress(address);
        installation.setCreatedAt(LocalDateTime.now());
        installation = installationRepository.save(installation);

        ContractAccount account = new ContractAccount();
        account.setAccountNumber("DEFAULT-ACC");
        account.setInstallation(installation);
        account.setClient(client);
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

        ContractAccount result = service.findById(saved.getId());

        assertNotNull(result);
        assertEquals("ACC-123", result.getAccountNumber());
    }

    @Test
    void testFindById_NonExistingAccount_ThrowsException() {
        assertThrows(ResourceNotFoundException.class, () -> service.findById(999));
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
        original.setAccountNumber("ACC-010");
        ContractAccount saved = service.add(original);

        ContractAccount updated = createValidContractAccount();

        updated.setDeletedAt(LocalDateTime.now().plusDays(1));

        ContractAccount result = service.update(saved.getId(), updated);

        assertNotNull(result);
        assertEquals(saved.getId(), result.getId());
        assertEquals("ACC-010", result.getAccountNumber());
        assertNotNull(result.getDeletedAt());
    }

    @Test
    void testUpdate_NonExistingAccount_ThrowsException() {
        ContractAccount account = createValidContractAccount();
        account.setAccountNumber("GHOST");

        assertThrows(ResourceNotFoundException.class, () -> service.update(999, account));
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
    void testDeleteById_NonExistingAccount_ThrowsException() {
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