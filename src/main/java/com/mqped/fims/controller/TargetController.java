package com.mqped.fims.controller;

import com.mqped.fims.model.dto.TargetDTO;
import com.mqped.fims.model.entity.Target;
import com.mqped.fims.model.enums.TargetType;
import com.mqped.fims.service.TargetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/targets")
public class TargetController {

    private final TargetService service;

    public TargetController(TargetService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<TargetDTO> createTarget(@RequestBody Target target) {
        Target savedTarget = service.add(target);
        return new ResponseEntity<>(TargetDTO.fromEntity(savedTarget), HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<TargetDTO>> getAllTargets() {
        List<TargetDTO> dtos = service.findAll().stream()
                .map(TargetDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<TargetDTO> getTargetById(@PathVariable Integer id) {
        Optional<Target> target = service.findById(id);
        return target.map(t -> ResponseEntity.ok(TargetDTO.fromEntity(t)))
                     .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<TargetDTO> updateTarget(@PathVariable Integer id, @RequestBody Target target) {
        Optional<Target> updated = service.update(id, target);
        return updated.map(t -> ResponseEntity.ok(TargetDTO.fromEntity(t)))
                      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarget(@PathVariable Integer id) {
        if (service.existsById(id)) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // READ BY CONTRACT ACCOUNT ID
    @GetMapping("/contract/{contractAccountId}")
    public ResponseEntity<List<TargetDTO>> getTargetsByContractAccount(@PathVariable Integer contractAccountId) {
        List<TargetDTO> dtos = service.findByContractAccountId(contractAccountId)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    // READ BY CLIENT ID
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<TargetDTO>> getTargetsByClient(@PathVariable Integer clientId) {
        List<TargetDTO> dtos = service.findByClientId(clientId)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    // READ BY TYPE
    @GetMapping("/type/{type}")
    public ResponseEntity<List<TargetDTO>> getTargetsByType(@PathVariable TargetType type) {
        List<TargetDTO> dtos = service.findByType(type)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    // SEARCH BY SIGNATURE
    @GetMapping("/signature/{signature}")
    public ResponseEntity<List<TargetDTO>> getTargetsBySignature(@PathVariable String signature) {
        List<TargetDTO> dtos = service.findBySignature(signature)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    // SEARCH BY PARTIAL SIGNATURE
    @GetMapping("/signature/contains/{partial}")
    public ResponseEntity<List<TargetDTO>> getTargetsBySignatureContaining(@PathVariable String partial) {
        List<TargetDTO> dtos = service.findBySignatureContaining(partial)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    // SEARCH BY SCORE GREATER THAN
    @GetMapping("/score/greater/{value}")
    public ResponseEntity<List<TargetDTO>> getTargetsByScoreGreater(@PathVariable Double value) {
        List<TargetDTO> dtos = service.findByScoreGreater(value)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    // SEARCH BY SCORE LESS THAN
    @GetMapping("/score/less/{value}")
    public ResponseEntity<List<TargetDTO>> getTargetsByScoreLess(@PathVariable Double value) {
        List<TargetDTO> dtos = service.findByScoreLess(value)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    // SEARCH BY SCORE BETWEEN
    @GetMapping("/score/between")
    public ResponseEntity<List<TargetDTO>> getTargetsByScoreBetween(
            @RequestParam Double min,
            @RequestParam Double max) {
        List<TargetDTO> dtos = service.findByScoreBetween(min, max)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    // SEARCH BY DISTANCE GREATER THAN
    @GetMapping("/distance/greater/{min}")
    public ResponseEntity<List<TargetDTO>> getByDistanceGreater(@PathVariable Double min) {
        List<TargetDTO> dtos = service.findByDistanceGreater(min)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    // SEARCH BY DISTANCE LESS THAN
    @GetMapping("/distance/less/{max}")
    public ResponseEntity<List<TargetDTO>> getByDistanceLess(@PathVariable Double max) {
        List<TargetDTO> dtos = service.findByDistanceLess(max)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    // SEARCH BY DISTANCE BETWEEN
    @GetMapping("/distance/between")
    public ResponseEntity<List<TargetDTO>> getByDistanceBetween(
            @RequestParam Double min,
            @RequestParam Double max) {
        List<TargetDTO> dtos = service.findByDistanceBetween(min, max)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    // HEALTH CHECK
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Target API is up and running!");
    }
}
