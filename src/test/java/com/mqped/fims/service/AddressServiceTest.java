package com.mqped.fims.service;

import com.mqped.fims.exceptions.ResourceNotFoundException;
import com.mqped.fims.model.entity.Address;
import com.mqped.fims.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AddressServiceTest {

    private AddressService service;

    @Autowired
    private AddressRepository repository;

    @BeforeEach
    void setUp() {
        service = new AddressService(repository);
        repository.deleteAll(); // Ensure clean DB before each test
    }

    private Address createValidAddress(String state, String municipality, String street) {
        Address address = new Address();
        address.setState(state);
        address.setMunicipality(municipality);
        address.setStreet(street);
        return address;
    }

    @Test
    void testAdd_AssignsIdAndStoresAddress() {
        Address address = createValidAddress("PA", "Belém", "Rua dos Mundurucus");

        Address result = service.add(address);

        assertNotNull(result.getId(), "ID should be auto-assigned");
        assertEquals("PA", result.getState());
        assertEquals("Belém", result.getMunicipality());
        assertEquals("Rua dos Mundurucus", result.getStreet());
    }

    @Test
    void testFindById_ExistingAddress() {
        Address address = createValidAddress("PA", "Belém", "Rua do Comércio");
        Address saved = service.add(address);

        Optional<Address> result = service.findById(saved.getId());
        assertTrue(result.isPresent());
        assertEquals("Rua do Comércio", result.get().getStreet());
    }

    @Test
    void testFindById_NonExistingAddress() {
        Optional<Address> result = service.findById(999);
        assertFalse(result.isPresent());
    }

    @Test
    void testFindAll_MultipleAddresses() {
        Address address1 = createValidAddress("PA", "Belém", "Avenida Nazaré");
        Address address2 = createValidAddress("PA", "Belém", "Rua Siqueira Mendes");

        service.add(address1);
        service.add(address2);

        List<Address> result = service.findAll();
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(a -> a.getStreet().equals("Avenida Nazaré")));
        assertTrue(result.stream().anyMatch(a -> a.getStreet().equals("Rua Siqueira Mendes")));
    }

    @Test
    void testUpdate_ExistingAddress() {
        Address original = createValidAddress("PA", "Belém", "Travessa da Paz");
        Address saved = service.add(original);

        Address updated = createValidAddress("PA", "Belém", "Rua Princesa Isabel");
        updated.setNumber("123");

        Optional<Address> result = service.update(saved.getId(), updated);

        assertTrue(result.isPresent());
        assertEquals(saved.getId(), result.get().getId());
        assertEquals("Rua Princesa Isabel", result.get().getStreet());
        assertEquals("123", result.get().getNumber());
    }

    @Test
    void testUpdate_NonExistingAddress() {
        Address address = createValidAddress("PA", "Belém", "Rua Quinze de Novembro");

        Optional<Address> result = service.update(999, address);
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteById_ExistingAddress() {
        Address address = createValidAddress("PA", "Belém", "Rua do Estado");
        Address saved = service.add(address);

        service.deleteById(saved.getId());
        assertFalse(service.existsById(saved.getId()));
    }

    @Test
    void testDeleteById_NonExistingAddress() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> service.deleteById(999));
        assertEquals("Address with id 999 not found", exception.getMessage());
    }

    @Test
    void testExistsById() {
        Address address = createValidAddress("PA", "Belém", "Rua Nova");
        Address saved = service.add(address);

        assertTrue(service.existsById(saved.getId()));
        assertFalse(service.existsById(999));
    }

    @Test
    void testCount() {
        assertEquals(0, service.count());
        service.add(createValidAddress("PA", "Belém", "Rua A"));
        service.add(createValidAddress("PA", "Belém", "Rua B"));
        assertEquals(2, service.count());
    }
}
