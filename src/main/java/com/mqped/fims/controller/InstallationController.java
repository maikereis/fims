package com.mqped.fims.controller;

import com.mqped.fims.model.dto.InstallationDTO;
import com.mqped.fims.model.entity.Installation;
import com.mqped.fims.service.InstallationService;
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
 * REST controller responsible for managing {@link Installation} entities.
 * <p>
 * Provides CRUD operations for installations, which represent the physical
 * or logical points of service (such as energy meters or connection points)
 * associated with contract accounts and clients in the FIMS system.
 * </p>
 *
 * <h2>Available Endpoints</h2>
 * <ul>
 * <li><b>POST /api/installations</b> — Create a new installation.</li>
 * <li><b>GET /api/installations</b> — Retrieve all installations with full
 * details.</li>
 * <li><b>GET /api/installations/minimal</b> — Retrieve all installations
 * without address details.</li>
 * <li><b>GET /api/installations/{id}</b> — Retrieve an installation by its
 * ID.</li>
 * <li><b>PUT /api/installations/{id}</b> — Update an existing
 * installation.</li>
 * <li><b>DELETE /api/installations/{id}</b> — Delete an installation by
 * ID.</li>
 * <li><b>GET /api/installations/check</b> — Health check for the Installation
 * API.</li>
 * </ul>
 *
 * <p>
 * This controller uses {@link InstallationDTO} for responses to ensure
 * separation between
 * persistence and presentation layers and to avoid direct exposure of JPA
 * entities.
 * </p>
 *
 * @author Rodrigo
 * @since 1.0
 */
@Tag(name = "Installation API", description = "Endpoints for managing Installation entities, including CRUD operations and health check.")
@RestController
@RequestMapping("/api/installations")
public class InstallationController {

    private final InstallationService service;

    /**
     * Constructs a new {@code InstallationController} with the specified service
     * dependency.
     *
     * @param service the {@link InstallationService} used to manage installation
     *                data.
     */
    public InstallationController(InstallationService service) {
        this.service = service;
    }

    /**
     * Creates a new installation record.
     *
     * @param installation the {@link Installation} entity containing installation
     *                     details to be persisted.
     * @return a {@link ResponseEntity} containing the created
     *         {@link InstallationDTO}
     *         and HTTP status {@code 201 (Created)}.
     */
    @Operation(summary = "Create installation", description = "Creates a new installation record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Installation created successfully", content = @Content(schema = @Schema(implementation = InstallationDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid installation data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<InstallationDTO> createInstallation(@RequestBody Installation installation) {
        Installation savedInstallation = service.add(installation);
        return new ResponseEntity<>(InstallationDTO.fromEntity(savedInstallation), HttpStatus.CREATED);
    }

    /**
     * Retrieves all installations with full details (including address data).
     *
     * @return a {@link ResponseEntity} containing a list of {@link InstallationDTO}
     *         objects
     *         and HTTP status {@code 200 (OK)}.
     */
    @Operation(summary = "Get all installations", description = "Retrieves all installations with full details, including address data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Installations retrieved successfully", content = @Content(schema = @Schema(implementation = InstallationDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<InstallationDTO>> getAllInstallations() {
        List<InstallationDTO> dtos = service.findAll().stream()
                .map(InstallationDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves all installations in minimal format, excluding address details.
     * <p>
     * This lightweight endpoint is useful for tables, summaries, or dropdowns
     * where complete address information is not required.
     * </p>
     *
     * @return a {@link ResponseEntity} containing a simplified list of
     *         {@link InstallationDTO}
     *         and HTTP status {@code 200 (OK)}.
     */
    @Operation(summary = "Get all installations (minimal)", description = "Retrieves all installations excluding address details, useful for tables or summary views.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Minimal installations retrieved successfully", content = @Content(schema = @Schema(implementation = InstallationDTO.class)))
    })
    @GetMapping("/minimal")
    public ResponseEntity<List<InstallationDTO>> getAllInstallationsMinimal() {
        List<InstallationDTO> dtos = service.findAll().stream()
                .map(InstallationDTO::fromEntityWithoutAddress)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves a specific installation by its unique ID.
     *
     * @param id the unique identifier of the installation.
     * @return a {@link ResponseEntity} containing the requested
     *         {@link InstallationDTO}
     *         and HTTP status {@code 200 (OK)}.
     * @throws org.springframework.web.server.ResponseStatusException if no
     *                                                                installation
     *                                                                is found with
     *                                                                the given ID.
     */
    @Operation(summary = "Get installation by ID", description = "Retrieves a specific installation by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Installation retrieved successfully", content = @Content(schema = @Schema(implementation = InstallationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Installation not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<InstallationDTO> getInstallationById(@PathVariable Integer id) {
        Installation installation = service.findById(id);
        return ResponseEntity.ok(InstallationDTO.fromEntity(installation));
    }

    /**
     * Updates an existing installation by its ID.
     *
     * @param id           the unique identifier of the installation to update.
     * @param installation the {@link Installation} object containing updated data.
     * @return a {@link ResponseEntity} containing the updated
     *         {@link InstallationDTO}
     *         and HTTP status {@code 200 (OK)}.
     * @throws org.springframework.web.server.ResponseStatusException if the
     *                                                                installation
     *                                                                does not
     *                                                                exist.
     */
    @Operation(summary = "Update installation", description = "Updates an existing installation by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Installation updated successfully", content = @Content(schema = @Schema(implementation = InstallationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Installation not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<InstallationDTO> updateInstallation(@PathVariable Integer id,
            @RequestBody Installation installation) {
        Installation updated = service.update(id, installation);
        return ResponseEntity.ok(InstallationDTO.fromEntity(updated));
    }

    /**
     * Deletes an installation by its ID.
     *
     * @param id the unique identifier of the installation to delete.
     * @return a {@link ResponseEntity} with HTTP status {@code 204 (No Content)} if
     *         deletion is successful.
     */
    @Operation(summary = "Delete installation", description = "Deletes an installation by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Installation deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Installation not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstallation(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Simple health check endpoint for the Installation API.
     * <p>
     * Used for verifying that the endpoint is operational.
     * </p>
     *
     * @return a {@link ResponseEntity} containing a status message
     *         and HTTP status {@code 200 (OK)}.
     */
    @Operation(summary = "Health check", description = "Simple endpoint to verify that the Installation API is running.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API is up and running", content = @Content)
    })
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Installation API is up and running!");
    }
}
