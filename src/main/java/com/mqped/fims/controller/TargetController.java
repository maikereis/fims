package com.mqped.fims.controller;

import com.mqped.fims.model.dto.TargetDTO;
import com.mqped.fims.model.entity.Target;
import com.mqped.fims.model.enums.TargetType;
import com.mqped.fims.service.TargetService;
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
 * REST controller responsible for managing {@link Target} entities.
 * <p>
 * Provides CRUD operations and query endpoints related to target data,
 * including
 * filtering by score, distance, signature, client, contract account, and type.
 * </p>
 *
 * <h2>Available Endpoints</h2>
 * <ul>
 * <li><b>POST /api/targets</b> — Create a new target.</li>
 * <li><b>GET /api/targets</b> — Retrieve all targets.</li>
 * <li><b>GET /api/targets/{id}</b> — Retrieve a target by ID.</li>
 * <li><b>PUT /api/targets/{id}</b> — Update a target by ID.</li>
 * <li><b>DELETE /api/targets/{id}</b> — Delete a target by ID.</li>
 * <li><b>GET /api/targets/contract/{contractAccountId}</b> — Retrieve targets
 * by contract account.</li>
 * <li><b>GET /api/targets/client/{clientId}</b> — Retrieve targets by
 * client.</li>
 * <li><b>GET /api/targets/type/{type}</b> — Retrieve targets by type.</li>
 * <li><b>GET /api/targets/signature/{signature}</b> — Retrieve targets by exact
 * signature.</li>
 * <li><b>GET /api/targets/signature/contains/{partial}</b> — Retrieve targets
 * containing partial signature match.</li>
 * <li><b>GET /api/targets/score/greater/{value}</b> — Retrieve targets with
 * score greater than value.</li>
 * <li><b>GET /api/targets/score/less/{value}</b> — Retrieve targets with score
 * less than value.</li>
 * <li><b>GET /api/targets/score/between</b> — Retrieve targets with score
 * between min and max.</li>
 * <li><b>GET /api/targets/distance/greater/{min}</b> — Retrieve targets with
 * distance greater than min.</li>
 * <li><b>GET /api/targets/distance/less/{max}</b> — Retrieve targets with
 * distance less than max.</li>
 * <li><b>GET /api/targets/distance/between</b> — Retrieve targets with distance
 * between min and max.</li>
 * <li><b>GET /api/targets/check</b> — API health check.</li>
 * </ul>
 *
 * <p>
 * This controller uses {@link TargetDTO} for API responses to ensure a clean
 * separation
 * between persistence and API layers.
 * </p>
 *
 * @author Rodrigo
 * @since 1.0
 */
@Tag(name = "Target API", description = "Endpoints for managing Target entities, including CRUD and advanced filters")
@RestController
@RequestMapping("/api/targets")
public class TargetController {

    private final TargetService service;

    public TargetController(TargetService service) {
        this.service = service;
    }

    /**
     * Creates a new target.
     *
     * @param target the target entity to be created.
     * @return the created {@link TargetDTO} with HTTP 201 (Created).
     */
    @Operation(summary = "Create target", description = "Creates a new target entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Target created successfully", content = @Content(schema = @Schema(implementation = TargetDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    public ResponseEntity<TargetDTO> createTarget(@RequestBody Target target) {
        Target savedTarget = service.add(target);
        return new ResponseEntity<>(TargetDTO.fromEntity(savedTarget), HttpStatus.CREATED);
    }

    /**
     * Retrieves all targets.
     *
     * @return list of all {@link TargetDTO}.
     */
    @Operation(summary = "Get all targets", description = "Retrieves all targets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Targets retrieved successfully", content = @Content(schema = @Schema(implementation = TargetDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<TargetDTO>> getAllTargets() {
        List<TargetDTO> dtos = service.findAll().stream()
                .map(TargetDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves a target by its ID.
     *
     * @param id target ID.
     * @return the corresponding {@link TargetDTO}.
     */
    @Operation(summary = "Get target by ID", description = "Retrieves a target by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Target retrieved successfully", content = @Content(schema = @Schema(implementation = TargetDTO.class))),
            @ApiResponse(responseCode = "404", description = "Target not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TargetDTO> getTargetById(@PathVariable Integer id) {
        Target target = service.findById(id);
        return ResponseEntity.ok(TargetDTO.fromEntity(target));
    }

    /**
     * Updates a target by its ID.
     *
     * @param id     target ID.
     * @param target updated target data.
     * @return the updated {@link TargetDTO}.
     */
    @Operation(summary = "Update target", description = "Updates an existing target by ID")
    @PutMapping("/{id}")
    public ResponseEntity<TargetDTO> updateTarget(@PathVariable Integer id, @RequestBody Target target) {
        Target updated = service.update(id, target);
        return ResponseEntity.ok(TargetDTO.fromEntity(updated));
    }

    /**
     * Deletes a target by its ID.
     *
     * @param id target ID.
     * @return HTTP 204 (No Content).
     */
    @Operation(summary = "Delete target", description = "Deletes a target by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarget(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves targets by contract account ID.
     *
     * @param contractAccountId the contract account ID.
     * @return list of {@link TargetDTO}.
     */
    @Operation(summary = "Get targets by contract account", description = "Retrieves targets filtered by contract account ID")
    @GetMapping("/contract/{contractAccountId}")
    public ResponseEntity<List<TargetDTO>> getTargetsByContractAccount(@PathVariable Integer contractAccountId) {
        List<TargetDTO> dtos = service.findByContractAccountId(contractAccountId)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves targets by client ID.
     *
     * @param clientId client ID.
     * @return list of {@link TargetDTO}.
     */
    @Operation(summary = "Get targets by client", description = "Retrieves targets filtered by client ID")
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<TargetDTO>> getTargetsByClient(@PathVariable Integer clientId) {
        List<TargetDTO> dtos = service.findByClientId(clientId)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves targets by type.
     *
     * @param type {@link TargetType}.
     * @return list of {@link TargetDTO}.
     */
    @Operation(summary = "Get targets by type", description = "Retrieves targets filtered by target type")
    @GetMapping("/type/{type}")
    public ResponseEntity<List<TargetDTO>> getTargetsByType(@PathVariable TargetType type) {
        List<TargetDTO> dtos = service.findByType(type)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves targets by exact signature.
     *
     * @param signature target signature.
     * @return list of {@link TargetDTO}.
     */
    @Operation(summary = "Get targets by signature", description = "Retrieves targets with exact signature match")
    @GetMapping("/signature/{signature}")
    public ResponseEntity<List<TargetDTO>> getTargetsBySignature(@PathVariable String signature) {
        List<TargetDTO> dtos = service.findBySignature(signature)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves targets where signature contains a partial match.
     *
     * @param partial part of the signature string.
     * @return list of {@link TargetDTO}.
     */
    @Operation(summary = "Get targets by partial signature", description = "Retrieves targets where signature contains a given substring")
    @GetMapping("/signature/contains/{partial}")
    public ResponseEntity<List<TargetDTO>> getTargetsBySignatureContaining(@PathVariable String partial) {
        List<TargetDTO> dtos = service.findBySignatureContaining(partial)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves targets with score greater than a given value.
     *
     * @param value minimum score value.
     * @return list of {@link TargetDTO}.
     */
    @Operation(summary = "Get targets by score greater than", description = "Retrieves targets with score greater than the specified value")
    @GetMapping("/score/greater/{value}")
    public ResponseEntity<List<TargetDTO>> getTargetsByScoreGreater(@PathVariable Double value) {
        List<TargetDTO> dtos = service.findByScoreGreater(value)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves targets with score less than a given value.
     *
     * @param value maximum score value.
     * @return list of {@link TargetDTO}.
     */
    @Operation(summary = "Get targets by score less than", description = "Retrieves targets with score less than the specified value")
    @GetMapping("/score/less/{value}")
    public ResponseEntity<List<TargetDTO>> getTargetsByScoreLess(@PathVariable Double value) {
        List<TargetDTO> dtos = service.findByScoreLess(value)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves targets with score between two values.
     *
     * @param min minimum score value.
     * @param max maximum score value.
     * @return list of {@link TargetDTO}.
     */
    @Operation(summary = "Get targets by score between", description = "Retrieves targets with score between two values")
    @GetMapping("/score/between")
    public ResponseEntity<List<TargetDTO>> getTargetsByScoreBetween(
            @RequestParam Double min,
            @RequestParam Double max) {
        List<TargetDTO> dtos = service.findByScoreBetween(min, max)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves targets with distance greater than a given value.
     *
     * @param min minimum distance.
     * @return list of {@link TargetDTO}.
     */
    @Operation(summary = "Get targets by distance greater than", description = "Retrieves targets with distance greater than the specified value")
    @GetMapping("/distance/greater/{min}")
    public ResponseEntity<List<TargetDTO>> getByDistanceGreater(@PathVariable Double min) {
        List<TargetDTO> dtos = service.findByDistanceGreater(min)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves targets with distance less than a given value.
     *
     * @param max maximum distance.
     * @return list of {@link TargetDTO}.
     */
    @Operation(summary = "Get targets by distance less than", description = "Retrieves targets with distance less than the specified value")
    @GetMapping("/distance/less/{max}")
    public ResponseEntity<List<TargetDTO>> getByDistanceLess(@PathVariable Double max) {
        List<TargetDTO> dtos = service.findByDistanceLess(max)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves targets with distance between two values.
     *
     * @param min minimum distance.
     * @param max maximum distance.
     * @return list of {@link TargetDTO}.
     */
    @Operation(summary = "Get targets by distance between", description = "Retrieves targets with distance between two values")
    @GetMapping("/distance/between")
    public ResponseEntity<List<TargetDTO>> getByDistanceBetween(
            @RequestParam Double min,
            @RequestParam Double max) {
        List<TargetDTO> dtos = service.findByDistanceBetween(min, max)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Simple health check endpoint.
     *
     * @return confirmation message if API is available.
     */
    @Operation(summary = "Health check", description = "Simple endpoint to verify that the Target API is running")
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Target API is up and running!");
    }
}
