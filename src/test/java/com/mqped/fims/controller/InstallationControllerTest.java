package com.mqped.fims.controller;

import com.mqped.fims.exceptions.ResourceNotFoundException;
import com.mqped.fims.model.dto.InstallationDTO;
import com.mqped.fims.model.entity.Address;
import com.mqped.fims.model.entity.Installation;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstallationControllerTest {

    @Mock
    private InstallationService service;

    @InjectMocks
    private InstallationController controller;

    private Installation installation1;
    private Installation installation2;
    private Address address1;
    private Address address2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        address1 = new Address();
        address1.setId(1);
        address1.setAddressId("5642728");
        address1.setState("Pará");
        address1.setMunicipality("Marituba");
        address1.setNeighborhood("Centro");
        address1.setStreet("Rua Principal");
        address1.setNumber("100");

        address2 = new Address();
        address2.setId(2);
        address2.setAddressId("4559429");
        address2.setState("Pará");
        address2.setMunicipality("Belém");
        address2.setNeighborhood("Nazaré");
        address2.setStreet("Avenida Central");
        address2.setNumber("200");

        installation1 = new Installation();
        installation1.setId(1);
        installation1.setAddress(address1);
        installation1.setCreatedAt(LocalDateTime.now().minusDays(100));
        installation1.setDeletedAt(null);

        installation2 = new Installation();
        installation2.setId(2);
        installation2.setAddress(address2);
        installation2.setCreatedAt(LocalDateTime.now().minusDays(50));
        installation2.setDeletedAt(null);
    }

    @Test
    void testCheck_returnsOk() {
        ResponseEntity<String> response = controller.check();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Installation API is up and running!", response.getBody());
    }

    @Test
    void testCreateInstallation_returnsCreatedInstallationDTO() {
        when(service.add(installation1)).thenReturn(installation1);

        ResponseEntity<InstallationDTO> response = controller.createInstallation(installation1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(installation1.getId(), response.getBody().getId());
        assertNotNull(response.getBody().getAddress());
        assertEquals("Marituba", response.getBody().getAddress().getMunicipality());
        verify(service, times(1)).add(installation1);
    }

    @Test
    void testGetAllInstallations_returnsAllInstallationDTOs() {
        List<Installation> installations = Arrays.asList(installation1, installation2);
        when(service.findAll()).thenReturn(installations);

        ResponseEntity<List<InstallationDTO>> response = controller.getAllInstallations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Marituba", response.getBody().get(0).getAddress().getMunicipality());
        assertEquals("Belém", response.getBody().get(1).getAddress().getMunicipality());
        verify(service, times(1)).findAll();
    }

    @Test
    void testGetAllInstallationsMinimal_returnsMinimalDTOs() {
        List<Installation> installations = Arrays.asList(installation1, installation2);
        when(service.findAll()).thenReturn(installations);

        ResponseEntity<List<InstallationDTO>> response = controller.getAllInstallationsMinimal();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        // Minimal version should have addressId but not full address object
        assertEquals(1, response.getBody().get(0).getAddressId());
        assertNull(response.getBody().get(0).getAddress());
        verify(service, times(1)).findAll();
    }

    @Test
    void testGetInstallationById_returnsInstallationDTOWhenFound() {
        when(service.findById(1)).thenReturn(installation1);

        ResponseEntity<InstallationDTO> response = controller.getInstallationById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(installation1.getId(), response.getBody().getId());
        assertNotNull(response.getBody().getAddress());
        verify(service, times(1)).findById(1);
    }

    @Test
    void testGetInstallationById_throwsExceptionWhenNotFound() {
        when(service.findById(3)).thenThrow(new ResourceNotFoundException("Installation with id 3 not found"));

        assertThrows(ResourceNotFoundException.class, () -> controller.getInstallationById(3));
        verify(service, times(1)).findById(3);
    }

    @Test
    void testUpdateInstallation_returnsUpdatedInstallationDTOWhenFound() {
        Installation updated = new Installation();
        updated.setId(1);
        updated.setAddress(address1);
        updated.setCreatedAt(installation1.getCreatedAt());

        when(service.update(1, installation1)).thenReturn(updated);

        ResponseEntity<InstallationDTO> response = controller.updateInstallation(1, installation1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
        verify(service, times(1)).update(1, installation1);
    }

    @Test
    void testUpdateInstallation_throwsExceptionWhenNotFound() {
        when(service.update(3, installation1)).thenThrow(new ResourceNotFoundException("Installation with id 3 not found"));

        assertThrows(ResourceNotFoundException.class, () -> controller.updateInstallation(3, installation1));
        verify(service, times(1)).update(3, installation1);
    }

    @Test
    void testDeleteInstallation_returnsNoContentWhenFound() {
        doNothing().when(service).deleteById(1);

        ResponseEntity<Void> response = controller.deleteInstallation(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).deleteById(1);
    }

    @Test
    void testDeleteInstallation_throwsExceptionWhenNotFound() {
        doThrow(new ResourceNotFoundException("Installation with id 3 not found"))
            .when(service).deleteById(3);

        assertThrows(ResourceNotFoundException.class, () -> controller.deleteInstallation(3));
        verify(service, times(1)).deleteById(3);
    }
}