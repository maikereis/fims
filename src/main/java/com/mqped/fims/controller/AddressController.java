package com.mqped.fims.controller;

import com.mqped.fims.model.dto.AddressDTO;
import com.mqped.fims.model.entity.Address;
import com.mqped.fims.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that manages {@link Address} resources in the system.
 * <p>
 * Provides CRUD operations and health check endpoints for managing address
 * records used throughout the FIMS (Field Inspection Management System)
 * application.
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
@Tag(name = "Address API", description = "Endpoints for managing address records within FIMS.")
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
    @Operation(summary = "Create a new address", description = "Registers a new address record in the system.", responses = {
            @ApiResponse(responseCode = "201", description = "Address created successfully", content = @Content(schema = @Schema(implementation = AddressDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
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
    @Operation(summary = "Retrieve all addresses", description = "Fetches all address records from the system.", responses = {
            @ApiResponse(responseCode = "200", description = "List of addresses retrieved successfully", content = @Content(schema = @Schema(implementation = AddressDTO.class)))
    })
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
    @Operation(summary = "Retrieve an address by ID", description = "Fetches a specific address by its unique identifier.", responses = {
            @ApiResponse(responseCode = "200", description = "Address found successfully", content = @Content(schema = @Schema(implementation = AddressDTO.class))),
            @ApiResponse(responseCode = "404", description = "Address not found", content = @Content)
    })
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
    @Operation(summary = "Update an existing address", description = "Updates a stored address record with new information.", responses = {
            @ApiResponse(responseCode = "200", description = "Address updated successfully", content = @Content(schema = @Schema(implementation = AddressDTO.class))),
            @ApiResponse(responseCode = "404", description = "Address not found", content = @Content)
    })
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
    @Operation(summary = "Delete an address", description = "Deletes an existing address record by its ID.", responses = {
            @ApiResponse(responseCode = "204", description = "Address deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found", content = @Content)
    })
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
    @Operation(summary = "Health check", description = "Verifies that the Address API is running and responsive.", responses = {
            @ApiResponse(responseCode = "200", description = "API is operational")
    })
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Address API is up and running!");
    }
}
