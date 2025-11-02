package com.mqped.fims.service;

import com.mqped.fims.model.Address;
import com.mqped.fims.repository.InstallationRepository;
import com.mqped.fims.model.Installation;
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

    @BeforeEach
    void setUp() {
        service = new InstallationService(repository);
        repository.deleteAll();
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
        Installation i1 = new Installation();
        Installation i2 = new Installation();

        i1.setAddress(new Address());
        i2.setAddress(new Address());
        i1.setCreatedAt(LocalDateTime.now());
        i2.setCreatedAt(LocalDateTime.now());

        Installation result1 = service.add(i1);
        Installation result2 = service.add(i2);

        assertNotNull(result1.getId());
        assertNotNull(result2.getId());
        assertTrue(result2.getId() > result1.getId());
    }

    @Test
    void testFindById_ExistingInstallation() {
        Installation installation = new Installation();
        Address address = new Address();
        address.setStreet("Avenida Paulista");
        installation.setAddress(address);
        installation.setCreatedAt(LocalDateTime.now());

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
        Installation i2 = createValidInstallation();

        Address a1 = new Address();
        a1.setStreet("Rua 1");
        Address a2 = new Address();
        a2.setStreet("Rua 2");

        i1.setAddress(a1);
        i2.setAddress(a2);

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
        Installation installation = new Installation();
        Address address = new Address();
        address.setStreet("Rua das Flores");
        installation.setAddress(address);
        installation.setCreatedAt(LocalDateTime.now());
        return installation;
    }
}