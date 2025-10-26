package com.mqped.fims.service;

import com.mqped.fims.model.Address;
import com.mqped.fims.model.Installation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InstallationServiceTest {

    private InstallationService service;

    @BeforeEach
    void setUp() {
        service = new InstallationService();
    }

    @Test
    void testAdd_AssignsIdAndStoresInstallation() {
        Installation installation = new Installation();
        installation.setCreateAt(LocalDateTime.now());

        Address address = new Address();
        address.setStreet("Rua das Flores");
        installation.setAddress(address);

        Installation result = service.add(installation);

        assertNotNull(result.getId(), "ID should be auto-assigned");
        assertEquals(1, result.getId());
        assertEquals("Rua das Flores", result.getAddress().getStreet());
    }

    @Test
    void testAdd_IncrementingIds() {
        Installation i1 = new Installation();
        Installation i2 = new Installation();

        Installation result1 = service.add(i1);
        Installation result2 = service.add(i2);

        assertEquals(1, result1.getId());
        assertEquals(2, result2.getId());
    }

    @Test
    void testFindById_ExistingInstallation() {
        Installation installation = new Installation();
        Address address = new Address();
        address.setStreet("Avenida Paulista");
        installation.setAddress(address);

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
    void testFindAll_EmptyList() {
        List<Installation> result = service.findAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAll_MultipleInstallations() {
        Installation i1 = new Installation();
        Installation i2 = new Installation();

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
        assertEquals("Rua 1", result.get(0).getAddress().getStreet());
        assertEquals("Rua 2", result.get(1).getAddress().getStreet());
    }

    @Test
    void testUpdate_ExistingInstallation() {
        Installation original = new Installation();
        Address oldAddress = new Address();
        oldAddress.setStreet("Old Street");
        original.setAddress(oldAddress);
        original.setCreateAt(LocalDateTime.now().minusDays(5));
        Installation saved = service.add(original);

        Installation updated = new Installation();
        Address newAddress = new Address();
        newAddress.setStreet("New Street");
        updated.setAddress(newAddress);
        updated.setCreateAt(LocalDateTime.now());
        updated.setDeletedAt(LocalDateTime.now().plusDays(1));

        Optional<Installation> result = service.update(saved.getId(), updated);

        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
        assertEquals("New Street", result.get().getAddress().getStreet());
        assertNotNull(result.get().getDeletedAt());
    }

    @Test
    void testUpdate_NonExistingInstallation() {
        Installation installation = new Installation();
        Optional<Installation> result = service.update(999, installation);
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteById_ExistingInstallation() {
        Installation installation = new Installation();
        Installation saved = service.add(installation);

        boolean deleted = service.deleteById(saved.getId());

        assertTrue(deleted);
        assertFalse(service.existsById(saved.getId()));
    }

    @Test
    void testDeleteById_NonExistingInstallation() {
        boolean deleted = service.deleteById(999);
        assertFalse(deleted);
    }

    @Test
    void testExistsById_ExistingInstallation() {
        Installation installation = new Installation();
        Installation saved = service.add(installation);
        assertTrue(service.existsById(saved.getId()));
    }

    @Test
    void testExistsById_NonExistingInstallation() {
        assertFalse(service.existsById(999));
    }

    @Test
    void testCount_EmptyService() {
        assertEquals(0, service.count());
    }

    @Test
    void testCount_WithInstallations() {
        service.add(new Installation());
        service.add(new Installation());
        service.add(new Installation());
        assertEquals(3, service.count());
    }

    @Test
    void testCount_AfterDeletion() {
        Installation i1 = service.add(new Installation());
        service.add(new Installation());
        service.deleteById(i1.getId());
        assertEquals(1, service.count());
    }

    @Test
    void testToString_ContainsFields() {
        Installation installation = new Installation();
        Address address = new Address();
        address.setStreet("Rua Teste");
        installation.setAddress(address);
        installation.setCreateAt(LocalDateTime.now());
        installation.setDeletedAt(LocalDateTime.now().plusDays(1));
        installation.setId(42);

        String s = installation.toString();

        assertTrue(s.contains("Installation"));
        assertTrue(s.contains("Rua Teste"));
        assertTrue(s.contains("42"));
    }
}
