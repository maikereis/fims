package com.mqped.fims.controller;

import com.mqped.fims.model.Address;
import com.mqped.fims.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressControllerTest {

    @Mock
    private AddressService service;

    @InjectMocks
    private AddressController controller;

    private Address address1;
    private Address address2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        address1 = new Address();
        address1.setId(1);
        address1.setStreet("Street 1");

        address2 = new Address();
        address2.setId(2);
        address2.setStreet("Street 2");
    }

    @Test
    void testCheck_returnsOk() {
        ResponseEntity<String> response = controller.check();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Address API is up and running!", response.getBody());
    }

    @Test
    void testCreateAddress_returnsCreatedAddress() {
        when(service.add(address1)).thenReturn(address1);

        ResponseEntity<Address> response = controller.createAddress(address1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(address1, response.getBody());
        verify(service, times(1)).add(address1);
    }

    @Test
    void testGetAllAddresses_returnsAllAddresses() {
        List<Address> addresses = Arrays.asList(address1, address2);
        when(service.findAll()).thenReturn(addresses);

        ResponseEntity<List<Address>> response = controller.getAllAddresses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(service, times(1)).findAll();
    }

    @Test
    void testGetAddressById_returnsAddressWhenFound() {
        when(service.findById(1)).thenReturn(Optional.of(address1));

        ResponseEntity<Address> response = controller.getAddressById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(address1, response.getBody());
    }

    @Test
    void testGetAddressById_returnsNotFoundWhenMissing() {
        when(service.findById(3)).thenReturn(Optional.empty());

        ResponseEntity<Address> response = controller.getAddressById(3);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteAddress_returnsNoContentWhenFound() {
        when(service.findById(1)).thenReturn(Optional.of(address1));
        doNothing().when(service).deleteById(1);

        ResponseEntity<Void> response = controller.deleteAddress(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).deleteById(1);
    }

    @Test
    void testDeleteAddress_returnsNotFoundWhenMissing() {
        when(service.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = controller.deleteAddress(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(service, never()).deleteById(anyInt());
    }
}
