package com.mqped.fims.service;

import com.mqped.fims.model.Address;
import com.mqped.fims.model.Installation;
import com.mqped.fims.repository.AddressRepository;
import com.mqped.fims.repository.InstallationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class InstallationServiceTest {

    private InstallationService service;

    @Autowired
    private InstallationRepository repository;

    @Autowired
    private AddressRepository addressRepository;

    @BeforeEach
    void setUp() {
        service = new InstallationService(repository);
        repository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void testAdd_AssignsIdAndStoresInstallation() {
        Installation installation = createValidInstallation();

        Installation result = service.add(installation);

        assertNotNull(result.getId(), "ID should be auto-assigned");
        assertEquals("Rua das Flores", result.getAddress().getStreet());
    }

    @Test
    void testAdd_IncrementingIds() {
        Installation i1 = createValidInstallation();
        Installation i2 = createValidInstallation();

        Installation result1 = service.add(i1);
        Installation result2 = service.add(i2);

        assertNotNull(result1.getId());
        assertNotNull(result2.getId());
        assertTrue(result2.getId() > result1.getId());
    }

    @Test
    void testFindById_ExistingInstallation() {
        Installation installation = createValidInstallation();
        installation.getAddress().setStreet("Avenida Paulista");

        Installation saved = service.add(installation);
        Optional<Installation> result = service.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("Avenida Paulista", result.get().getAddress().getStreet());
    }

    @Test
    void testFindById_NonExistingInstallation() {
        Optional<Installation> result = service.findById(999);
        assertFalse(result.isPresent());
    }

    @Test
    void testFindAll_MultipleInstallations() {
        Installation i1 = createValidInstallation();
        i1.getAddress().setStreet("Rua 1");
        Installation i2 = createValidInstallation();
        i2.getAddress().setStreet("Rua 2");

        service.add(i1);
        service.add(i2);

        List<Installation> result = service.findAll();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(inst -> "Rua 1".equals(inst.getAddress().getStreet())));
        assertTrue(result.stream().anyMatch(inst -> "Rua 2".equals(inst.getAddress().getStreet())));
    }

    @Test
    void testUpdate_ExistingInstallation() {
        Installation original = createValidInstallation();
        original.getAddress().setStreet("Old Street");

        Installation saved = service.add(original);

        Installation updated = createValidInstallation();
        updated.getAddress().setStreet("New Street");
        updated.setDeletedAt(LocalDateTime.now().plusDays(1));

        Optional<Installation> result = service.update(saved.getId(), updated);

        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
        assertEquals("New Street", result.get().getAddress().getStreet());
        assertNotNull(result.get().getDeletedAt());
    }

    @Test
    void testUpdate_NonExistingInstallation() {
        Installation installation = createValidInstallation();
        Optional<Installation> result = service.update(999, installation);
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteById_ExistingInstallation() {
        Installation installation = createValidInstallation();
        Installation saved = service.add(installation);

        service.deleteById(saved.getId());
        assertFalse(service.existsById(saved.getId()));
    }

    @Test
    void testDeleteById_NonExistingInstallation() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.deleteById(999));
        assertEquals("Installation with id 999 not found", exception.getMessage());
    }

    @Test
    void testExistsById() {
        Installation installation = createValidInstallation();
        Installation saved = service.add(installation);

        assertTrue(service.existsById(saved.getId()));
        assertFalse(service.existsById(999));
    }

    @Test
    void testCount() {
        assertEquals(0, service.count());
        service.add(createValidInstallation());
        service.add(createValidInstallation());
        assertEquals(2, service.count());
    }

    private Installation createValidInstallation() {
        Address address = new Address();
        address.setAddressId("ADDR-TEST-" + System.nanoTime());
        address.setState("PA");
        address.setMunicipality("Belém");
        address.setStreet("Rua das Flores");
        address = addressRepository.save(address);

        Installation installation = new Installation();
        installation.setAddress(address);
        installation.setCreatedAt(LocalDateTime.now());
        return installation;
    }
}