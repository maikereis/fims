package com.mqped.fims.controller;

import com.mqped.fims.model.dto.InstallationDTO;
import com.mqped.fims.model.entity.Installation;
import com.mqped.fims.service.InstallationService;
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
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Installation API is up and running!");
    }
}
