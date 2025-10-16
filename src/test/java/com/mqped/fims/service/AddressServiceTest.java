package com.mqped.fims.service;

import com.mqped.fims.model.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AddressServiceTest {

    private AddressService service;

    @BeforeEach
    void setUp() {
        service = new AddressService();
    }

    @Test
    void testAdd_AssignsIdAndStoresAddress() {
        Address address = new Address();
        address.setState("SP");
        address.setMunicipality("São Paulo");

        Address result = service.add(address);

        assertNotNull(result.getId(), "ID should be auto-assigned");
        assertEquals(1, result.getId(), "First address should have ID 1");
        assertEquals("SP", result.getState());
        assertEquals("São Paulo", result.getMunicipality());
    }

    @Test
    void testAdd_IncrementingIds() {
        Address address1 = new Address();
        Address address2 = new Address();

        Address result1 = service.add(address1);
        Address result2 = service.add(address2);

        assertEquals(1, result1.getId());
        assertEquals(2, result2.getId());
    }

    @Test
    void testFindById_ExistingAddress() {
        Address address = new Address();
        address.setStreet("Rua das Flores");

        Address saved = service.add(address);
        Optional<Address> result = service.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("Rua das Flores", result.get().getStreet());
    }

    @Test
    void testFindById_NonExistingAddress() {
        Optional<Address> result = service.findById(999);

        assertFalse(result.isPresent());
    }

    @Test
    void testFindAll_EmptyList() {
        List<Address> result = service.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAll_MultipleAddresses() {
        Address address1 = new Address();
        address1.setStreet("Avenida Paulista");

        Address address2 = new Address();
        address2.setStreet("Rua Augusta");

        service.add(address1);
        service.add(address2);

        List<Address> result = service.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void testUpdate_ExistingAddress() {
        Address original = new Address();
        original.setStreet("Old Street");
        Address saved = service.add(original);

        Address updated = new Address();
        updated.setStreet("New Street");
        updated.setNumber("123");

        Optional<Address> result = service.update(saved.getId(), updated);

        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId(), "ID should remain the same");
        assertEquals("New Street", result.get().getStreet());
        assertEquals("123", result.get().getNumber());
    }

    @Test
    void testUpdate_NonExistingAddress() {
        Address address = new Address();
        address.setStreet("Some Street");

        Optional<Address> result = service.update(999, address);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteById_ExistingAddress() {
        Address address = new Address();
        Address saved = service.add(address);

        boolean result = service.deleteById(saved.getId());

        assertTrue(result);
        assertFalse(service.existsById(saved.getId()));
    }

    @Test
    void testDeleteById_NonExistingAddress() {
        boolean result = service.deleteById(999);

        assertFalse(result);
    }

    @Test
    void testExistsById_ExistingAddress() {
        Address address = new Address();
        Address saved = service.add(address);

        boolean result = service.existsById(saved.getId());

        assertTrue(result);
    }

    @Test
    void testExistsById_NonExistingAddress() {
        boolean result = service.existsById(999);

        assertFalse(result);
    }

    @Test
    void testCount_EmptyService() {
        long count = service.count();

        assertEquals(0, count);
    }

    @Test
    void testCount_WithAddresses() {
        service.add(new Address());
        service.add(new Address());
        service.add(new Address());

        long count = service.count();

        assertEquals(3, count);
    }

    @Test
    void testCount_AfterDeletion() {
        Address address1 = service.add(new Address());
        service.add(new Address());

        service.deleteById(address1.getId());

        long count = service.count();

        assertEquals(1, count);
    }
}