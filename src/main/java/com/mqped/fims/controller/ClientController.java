package com.mqped.fims.controller;

import com.mqped.fims.model.dto.ClientDTO;
import com.mqped.fims.model.entity.Client;
import com.mqped.fims.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for managing {@link Client} entities.
 * <p>
 * Provides endpoints for creating, reading, updating, and deleting client data.
 * This controller communicates with the {@link ClientService} layer to handle
 * business logic and returns data using the {@link ClientDTO} data transfer
 * object.
 * </p>
 *
 * <h2>Available Endpoints</h2>
 * <ul>
 * <li><b>POST /api/clients</b> — Create a new client.</li>
 * <li><b>GET /api/clients</b> — Retrieve all clients.</li>
 * <li><b>GET /api/clients/{id}</b> — Retrieve a client by its unique ID.</li>
 * <li><b>PUT /api/clients/{id}</b> — Update an existing client.</li>
 * <li><b>DELETE /api/clients/{id}</b> — Delete a client by ID.</li>
 * <li><b>GET /api/clients/check</b> — Health check for the client API.</li>
 * </ul>
 *
 * <p>
 * Typical usage includes managing customer records within the FIMS (Field
 * Inspection Management System)
 * and integrating with other service modules such as contracts and service
 * orders.
 * </p>
 *
 * @author Rodrigo
 * @since 1.0
 */
@Tag(name = "Client API", description = "Endpoints for managing client entities, including CRUD operations and health check.")
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService service;

    /**
     * Constructs a new {@code ClientController} with the required service
     * dependency.
     *
     * @param service the {@link ClientService} used to manage client persistence
     *                and business logic.
     */
    public ClientController(ClientService service) {
        this.service = service;
    }

    /**
     * Creates a new {@link Client} record.
     *
     * @param client the {@link Client} entity containing client details to be
     *               saved.
     * @return a {@link ResponseEntity} containing the created {@link ClientDTO} and
     *         HTTP status {@code 201 (Created)}.
     */
    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody Client client) {
        Client savedClient = service.add(client);
        return new ResponseEntity<>(ClientDTO.fromEntity(savedClient), HttpStatus.CREATED);
    }

    /**
     * Retrieves all clients from the database.
     *
     * @return a {@link ResponseEntity} containing a list of {@link ClientDTO}
     *         objects and HTTP status {@code 200 (OK)}.
     */
    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        List<ClientDTO> dtos = service.findAll().stream()
                .map(ClientDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves a single client by its ID.
     *
     * @param id the unique identifier of the client.
     * @return a {@link ResponseEntity} containing the requested {@link ClientDTO}
     *         and HTTP status {@code 200 (OK)}.
     * @throws org.springframework.web.server.ResponseStatusException if the client
     *                                                                is not found.
     */
    @Operation(summary = "Get client by ID", description = "Retrieves a client by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client retrieved successfully", content = @Content(schema = @Schema(implementation = ClientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Integer id) {
        Client client = service.findById(id);
        return ResponseEntity.ok(ClientDTO.fromEntity(client));
    }

    /**
     * Updates an existing client's information.
     *
     * @param id     the unique identifier of the client to update.
     * @param client the {@link Client} object containing updated data.
     * @return a {@link ResponseEntity} containing the updated {@link ClientDTO} and
     *         HTTP status {@code 200 (OK)}.
     * @throws org.springframework.web.server.ResponseStatusException if the client
     *                                                                is not found.
     */
    @Operation(summary = "Update client", description = "Updates an existing client's information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client updated successfully", content = @Content(schema = @Schema(implementation = ClientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Integer id, @RequestBody Client client) {
        Client updated = service.update(id, client);
        return ResponseEntity.ok(ClientDTO.fromEntity(updated));
    }

    /**
     * Deletes a client by its ID.
     *
     * @param id the unique identifier of the client to delete.
     * @return a {@link ResponseEntity} with no content and HTTP status
     *         {@code 204 (No Content)}.
     */
    @Operation(summary = "Delete client", description = "Deletes a client by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Simple health check endpoint for the client API.
     * <p>
     * Can be used for monitoring or testing if the endpoint is active.
     * </p>
     *
     * @return a {@link ResponseEntity} containing a confirmation message and HTTP
     *         status {@code 200 (OK)}.
     */
    @Operation(summary = "Health check", description = "Simple endpoint to verify that the Client API is running.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API is up and running", content = @Content)
    })
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Client API is up and running!");
    }
}
