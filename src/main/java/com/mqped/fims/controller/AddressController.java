package com.mqped.fims.controller;

import com.mqped.fims.model.dto.AddressDTO;
import com.mqped.fims.model.entity.Address;
import com.mqped.fims.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that manages {@link Address} resources in the system.
 * <p>
 * Provides CRUD operations and health check endpoints for managing address
 * records
 * used throughout the FIMS (Field Inspection Management System) application.
 * </p>
 *
 * <h2>Available Endpoints</h2>
 * <ul>
 * <li><b>POST /api/addresses</b> — Create a new address.</li>
 * <li><b>GET /api/addresses</b> — Retrieve all addresses.</li>
 * <li><b>GET /api/addresses/{id}</b> — Retrieve a specific address by ID.</li>
 * <li><b>PUT /api/addresses/{id}</b> — Update an existing address.</li>
 * <li><b>DELETE /api/addresses/{id}</b> — Delete an address by ID.</li>
 * <li><b>GET /api/addresses/check</b> — Health check endpoint.</li>
 * </ul>
 *
 * @author Rodrigo
 * @since 1.0
 */
@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService service;

    /**
     * Constructs a new {@code AddressController} with the specified
     * {@link AddressService}.
     *
     * @param service the service layer used to handle address operations.
     */
    public AddressController(AddressService service) {
        this.service = service;
    }

    /**
     * Creates a new {@link Address} record.
     *
     * @param address the {@link Address} entity containing the address details to
     *                be created.
     * @return a {@link ResponseEntity} containing the created {@link AddressDTO}
     *         and a {@code 201 Created} status.
     */
    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@RequestBody Address address) {
        Address savedAddress = service.add(address);
        return new ResponseEntity<>(AddressDTO.fromEntity(savedAddress), HttpStatus.CREATED);
    }

    /**
     * Retrieves all stored {@link Address} records.
     *
     * @return a {@link ResponseEntity} containing a list of {@link AddressDTO}
     *         objects and a {@code 200 OK} status.
     */
    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        List<AddressDTO> dtos = service.findAll().stream()
                .map(AddressDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves an {@link Address} by its unique identifier.
     *
     * @param id the unique ID of the address to retrieve.
     * @return a {@link ResponseEntity} containing the requested {@link AddressDTO}
     *         and a {@code 200 OK} status.
     * @throws org.springframework.web.server.ResponseStatusException if the address
     *                                                                is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Integer id) {
        Address address = service.findById(id);
        return ResponseEntity.ok(AddressDTO.fromEntity(address));
    }

    /**
     * Updates an existing {@link Address}.
     *
     * @param id      the unique ID of the address to update.
     * @param address the updated {@link Address} entity data.
     * @return a {@link ResponseEntity} containing the updated {@link AddressDTO}
     *         and a {@code 200 OK} status.
     * @throws org.springframework.web.server.ResponseStatusException if the address
     *                                                                does not
     *                                                                exist.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Integer id, @RequestBody Address address) {
        Address updated = service.update(id, address);
        return ResponseEntity.ok(AddressDTO.fromEntity(updated));
    }

    /**
     * Deletes an {@link Address} by its ID.
     *
     * @param id the unique identifier of the address to delete.
     * @return a {@link ResponseEntity} with a {@code 204 No Content} status if
     *         deletion succeeds.
     * @throws org.springframework.web.server.ResponseStatusException if the address
     *                                                                is not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Simple endpoint to check whether the Address API is running.
     *
     * @return a {@link ResponseEntity} with a status message and a {@code 200 OK}
     *         response.
     */
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Address API is up and running!");
    }
}
