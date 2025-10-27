package com.mqped.fims.controller;

import com.mqped.fims.model.Address;
import com.mqped.fims.model.Installation;
import com.mqped.fims.service.InstallationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstallationControllerTest {

    @Mock
    private InstallationService service;

    @InjectMocks
    private InstallationController controller;

    private Installation installation1;
    private Installation installation2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Address address1 = new Address();
        address1.setAddressId("5642728");
        address1.setState("Pará");
        address1.setMunicipality("Marituba");

        Address address2 = new Address();
        address2.setAddressId("4559429");
        address2.setState("Pará");
        address2.setMunicipality("Belém");

        installation1 = new Installation();
        installation1.setId(1);
        installation1.setAddress(address1);
        installation1.setCreateAt(LocalDateTime.now().minusDays(100));
        installation1.setDeletedAt(null);

        installation2 = new Installation();
        installation2.setId(2);
        installation2.setAddress(address2);
        installation2.setCreateAt(LocalDateTime.now().minusDays(50));
        installation2.setDeletedAt(null);
    }

    @Test
    void testCheck_returnsOk() {
        ResponseEntity<String> response = controller.check();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Installation API is up and running!", response.getBody());
    }

    @Test
    void testCreateInstallation_returnsCreatedInstallation() {
        when(service.add(installation1)).thenReturn(installation1);

        ResponseEntity<Installation> response = controller.createInstallation(installation1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(installation1, response.getBody());
        verify(service, times(1)).add(installation1);
    }

    @Test
    void testGetAllInstallations_returnsAllInstallations() {
        List<Installation> installations = Arrays.asList(installation1, installation2);
        when(service.findAll()).thenReturn(installations);

        ResponseEntity<List<Installation>> response = controller.getAllInstallations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(service, times(1)).findAll();
    }

    @Test
    void testGetInstallationById_returnsInstallationWhenFound() {
        when(service.findById(1)).thenReturn(Optional.of(installation1));

        ResponseEntity<Installation> response = controller.getInstallationById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(installation1, response.getBody());
        verify(service, times(1)).findById(1);
    }

    @Test
    void testGetInstallationById_returnsNotFoundWhenMissing() {
        when(service.findById(3)).thenReturn(Optional.empty());

        ResponseEntity<Installation> response = controller.getInstallationById(3);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(service, times(1)).findById(3);
    }

    @Test
    void testUpdateInstallation_returnsUpdatedInstallationWhenFound() {
        Installation updated = new Installation();
        updated.setAddress(installation1.getAddress());
        updated.setCreateAt(installation1.getCreateAt());

        when(service.update(1, updated)).thenReturn(Optional.of(updated));

        ResponseEntity<Installation> response = controller.updateInstallation(1, updated);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
        verify(service, times(1)).update(1, updated);
    }

    @Test
    void testUpdateInstallation_returnsNotFoundWhenMissing() {
        Installation updated = new Installation();
        when(service.update(3, updated)).thenReturn(Optional.empty());

        ResponseEntity<Installation> response = controller.updateInstallation(3, updated);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(service, times(1)).update(3, updated);
    }

    @Test
    void testDeleteInstallation_returnsNoContentWhenFound() {
        when(service.existsById(1)).thenReturn(true);
        when(service.deleteById(1)).thenReturn(true);

        ResponseEntity<Void> response = controller.deleteInstallation(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).deleteById(1);
    }

    @Test
    void testDeleteInstallation_returnsNotFoundWhenMissing() {
        when(service.existsById(3)).thenReturn(false);

        ResponseEntity<Void> response = controller.deleteInstallation(3);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(service, never()).deleteById(anyInt());
    }
}
