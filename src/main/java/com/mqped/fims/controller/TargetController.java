package com.mqped.fims.controller;

import com.mqped.fims.model.dto.TargetDTO;
import com.mqped.fims.model.entity.Target;
import com.mqped.fims.model.enums.TargetType;
import com.mqped.fims.service.TargetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing {@link Target} entities.
 * Provides CRUD operations and custom queries related to targets.
 */
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
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Target API is up and running!");
    }
}
