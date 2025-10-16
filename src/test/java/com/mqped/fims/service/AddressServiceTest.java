package com.mqped.fims.service;

import com.mqped.fims.model.Address;
import com.mqped.fims.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressServiceTest {

    private AddressRepository repository;
    private AddressService service;

    @BeforeEach
    void setUp() {
        // Mock the repository
        repository = mock(AddressRepository.class);
        service = new AddressService(repository);
    }

    @Test
    void testAdd() {
        Address address = new Address();
        address.setId("2384025");

        when(repository.save(address)).thenReturn(address);

        Address result = service.add(address);

        assertEquals("2384025", result.getId());
        verify(repository, times(1)).save(address);
    }

    @Test
    void testDeleteById() {
        service.deleteById("23552366");
        verify(repository, times(1)).deleteById("23552366");
    }

    @Test
    void testFindAll() {
        Address address1 = new Address();
        address1.setId("456435345");
        Address address2 = new Address();
        address2.setId("86747564");

        when(repository.findAll()).thenReturn(List.of(address1, address2));

        List<Address> result = service.findAll();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Address address = new Address();
        address.setId("2342353");

        when(repository.findById("2342353")).thenReturn(Optional.of(address));

        Optional<Address> result = service.findById("2342353");

        assertTrue(result.isPresent());
        assertEquals("2342353", result.get().getId());
    }

    @Test
    void testSave() {
        Address address = new Address();
        address.setId("5675464");

        when(repository.save(address)).thenReturn(address);

        Address result = service.save(address);

        assertEquals("5675464", result.getId());
        verify(repository, times(1)).save(address);
    }
}
