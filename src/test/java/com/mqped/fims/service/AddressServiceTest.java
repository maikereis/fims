package com.mqped.fims.service;

import com.mqped.fims.exceptions.ResourceNotFoundException;
import com.mqped.fims.model.entity.Address;
import com.mqped.fims.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

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
        address.setAddressId("ADDR-" + System.nanoTime());
        address.setState(state);
        address.setMunicipality(municipality);
        address.setNeighborhood("Centro");
        address.setStreet(street);
        address.setNumber("123");
        address.setZipCode("66000-000");
        address.setLatitude(-1.455833);
        address.setLongitude(-48.504444);
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

        Address result = service.findById(saved.getId());
        
        assertNotNull(result);
        assertEquals("Rua do Comércio", result.getStreet());
    }

    @Test
    void testFindById_NonExistingAddress_ThrowsException() {
        assertThrows(ResourceNotFoundException.class, () -> service.findById(999));
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
        updated.setNumber("456");

        Address result = service.update(saved.getId(), updated);

        assertNotNull(result);
        assertEquals(saved.getId(), result.getId());
        assertEquals("Rua Princesa Isabel", result.getStreet());
        assertEquals("456", result.getNumber());
    }

    @Test
    void testUpdate_NonExistingAddress_ThrowsException() {
        Address address = createValidAddress("PA", "Belém", "Rua Quinze de Novembro");

        assertThrows(ResourceNotFoundException.class, () -> service.update(999, address));
    }

    @Test
    void testDeleteById_ExistingAddress() {
        Address address = createValidAddress("PA", "Belém", "Rua do Estado");
        Address saved = service.add(address);

        service.deleteById(saved.getId());
        assertFalse(service.existsById(saved.getId()));
    }

    @Test
    void testDeleteById_NonExistingAddress_ThrowsException() {
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

    @Test
    void testFindByAddressId_ExistingAddress() {
        Address address = createValidAddress("PA", "Belém", "Rua Teste");
        String addressId = "TEST-ADDR-123";
        address.setAddressId(addressId);
        service.add(address);

        Address result = service.findByAddressId(addressId);
        
        assertNotNull(result);
        assertEquals(addressId, result.getAddressId());
        assertEquals("Rua Teste", result.getStreet());
    }

    @Test
    void testFindByAddressId_NonExistingAddress_ThrowsException() {
        assertThrows(ResourceNotFoundException.class, () -> service.findByAddressId("NON-EXISTENT"));
    }
}