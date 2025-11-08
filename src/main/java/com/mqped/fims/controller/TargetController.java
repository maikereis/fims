package com.mqped.fims.controller;

import com.mqped.fims.model.dto.TargetDTO;
import com.mqped.fims.model.entity.Target;
import com.mqped.fims.model.enums.TargetType;
import com.mqped.fims.service.TargetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/targets")
public class TargetController {

    private final TargetService service;

    public TargetController(TargetService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TargetDTO> createTarget(@RequestBody Target target) {
        Target savedTarget = service.add(target);
        return new ResponseEntity<>(TargetDTO.fromEntity(savedTarget), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TargetDTO>> getAllTargets() {
        List<TargetDTO> dtos = service.findAll().stream()
                .map(TargetDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TargetDTO> getTargetById(@PathVariable Integer id) {
        Target target = service.findById(id);
        return ResponseEntity.ok(TargetDTO.fromEntity(target));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TargetDTO> updateTarget(@PathVariable Integer id, @RequestBody Target target) {
        Target updated = service.update(id, target);
        return ResponseEntity.ok(TargetDTO.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarget(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/contract/{contractAccountId}")
    public ResponseEntity<List<TargetDTO>> getTargetsByContractAccount(@PathVariable Integer contractAccountId) {
        List<TargetDTO> dtos = service.findByContractAccountId(contractAccountId)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<TargetDTO>> getTargetsByClient(@PathVariable Integer clientId) {
        List<TargetDTO> dtos = service.findByClientId(clientId)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<TargetDTO>> getTargetsByType(@PathVariable TargetType type) {
        List<TargetDTO> dtos = service.findByType(type)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/signature/{signature}")
    public ResponseEntity<List<TargetDTO>> getTargetsBySignature(@PathVariable String signature) {
        List<TargetDTO> dtos = service.findBySignature(signature)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/signature/contains/{partial}")
    public ResponseEntity<List<TargetDTO>> getTargetsBySignatureContaining(@PathVariable String partial) {
        List<TargetDTO> dtos = service.findBySignatureContaining(partial)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/score/greater/{value}")
    public ResponseEntity<List<TargetDTO>> getTargetsByScoreGreater(@PathVariable Double value) {
        List<TargetDTO> dtos = service.findByScoreGreater(value)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/score/less/{value}")
    public ResponseEntity<List<TargetDTO>> getTargetsByScoreLess(@PathVariable Double value) {
        List<TargetDTO> dtos = service.findByScoreLess(value)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/score/between")
    public ResponseEntity<List<TargetDTO>> getTargetsByScoreBetween(
            @RequestParam Double min,
            @RequestParam Double max) {
        List<TargetDTO> dtos = service.findByScoreBetween(min, max)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/distance/greater/{min}")
    public ResponseEntity<List<TargetDTO>> getByDistanceGreater(@PathVariable Double min) {
        List<TargetDTO> dtos = service.findByDistanceGreater(min)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/distance/less/{max}")
    public ResponseEntity<List<TargetDTO>> getByDistanceLess(@PathVariable Double max) {
        List<TargetDTO> dtos = service.findByDistanceLess(max)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/distance/between")
    public ResponseEntity<List<TargetDTO>> getByDistanceBetween(
            @RequestParam Double min,
            @RequestParam Double max) {
        List<TargetDTO> dtos = service.findByDistanceBetween(min, max)
                .stream().map(TargetDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Target API is up and running!");
    }
}