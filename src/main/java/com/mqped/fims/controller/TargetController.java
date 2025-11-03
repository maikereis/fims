package com.mqped.fims.controller;

import com.mqped.fims.model.Target;
import com.mqped.fims.model.TargetType;
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
    public ResponseEntity<Target> createTarget(@RequestBody Target target) {
        Target savedTarget = service.add(target);
        return new ResponseEntity<>(savedTarget, HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Target>> getAllTargets() {
        List<Target> targets = service.findAll();
        return new ResponseEntity<>(targets, HttpStatus.OK);
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Target> getTargetById(@PathVariable Integer id) {
        Optional<Target> target = service.findById(id);
        return target.map(t -> new ResponseEntity<>(t, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Target> updateTarget(@PathVariable Integer id, @RequestBody Target target) {
        Optional<Target> updated = service.update(id, target);
        return updated.map(t -> new ResponseEntity<>(t, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarget(@PathVariable Integer id) {
        if (service.existsById(id)) {
            service.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // READ BY CONTRACT ACCOUNT ID
    @GetMapping("/contract/{contractAccountId}")
    public ResponseEntity<List<Target>> getTargetsByContractAccount(@PathVariable Integer contractAccountId) {
        List<Target> targets = service.findByContractAccountId(contractAccountId);
        return new ResponseEntity<>(targets, HttpStatus.OK);
    }

    // READ BY CLIENT ID
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Target>> getTargetsByClient(@PathVariable Integer clientId) {
        List<Target> targets = service.findByClientId(clientId);
        return new ResponseEntity<>(targets, HttpStatus.OK);
    }

    // READ BY TYPE
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Target>> getTargetsByType(@PathVariable TargetType type) {
        List<Target> targets = service.findByType(type);
        return new ResponseEntity<>(targets, HttpStatus.OK);
    }

    // SEARCH BY SIGNATURE
    @GetMapping("/signature/{signature}")
    public ResponseEntity<List<Target>> getTargetsBySignature(@PathVariable String signature) {
        List<Target> targets = service.findBySignature(signature);
        return new ResponseEntity<>(targets, HttpStatus.OK);
    }

    // SEARCH BY PARTIAL SIGNATURE
    @GetMapping("/signature/contains/{partial}")
    public ResponseEntity<List<Target>> getTargetsBySignatureContaining(@PathVariable String partial) {
        List<Target> targets = service.findBySignatureContaining(partial);
        return new ResponseEntity<>(targets, HttpStatus.OK);
    }

    // SEARCH BY SCORE GREATER THAN
    @GetMapping("/score/greater/{value}")
    public ResponseEntity<List<Target>> getTargetsByScoreGreater(@PathVariable Double value) {
        List<Target> targets = service.findByScoreGreater(value);
        return new ResponseEntity<>(targets, HttpStatus.OK);
    }

    // SEARCH BY SCORE LESS THAN
    @GetMapping("/score/less/{value}")
    public ResponseEntity<List<Target>> getTargetsByScoreLess(@PathVariable Double value) {
        List<Target> targets = service.findByScoreLess(value);
        return new ResponseEntity<>(targets, HttpStatus.OK);
    }

    // SEARCH BY SCORE BETWEEN
    @GetMapping("/score/between")
    public ResponseEntity<List<Target>> getTargetsByScoreBetween(
            @RequestParam Double min,
            @RequestParam Double max) {
        List<Target> targets = service.findByScoreBetween(min, max);
        return new ResponseEntity<>(targets, HttpStatus.OK);
    }

    // SEARCH BY DISTANCE GREATER THAN
    @GetMapping("/distance/greater/{min}")
    public ResponseEntity<List<Target>> getByDistanceGreater(@PathVariable Double min) {
        return ResponseEntity.ok(service.findByDistanceGreater(min));
    }

    // SEARCH BY DISTANCE LESS THAN
    @GetMapping("/distance/less/{max}")
    public ResponseEntity<List<Target>> getByDistanceLess(@PathVariable Double max) {
        return ResponseEntity.ok(service.findByDistanceLess(max));
    }

    // SEARCH BY DISTANCE BETWEEN
    @GetMapping("/distance/between")
    public ResponseEntity<List<Target>> getByDistanceBetween(
            @RequestParam Double min,
            @RequestParam Double max) {
        return ResponseEntity.ok(service.findByDistanceBetween(min, max));
    }

    // HEALTH CHECK
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return new ResponseEntity<>("Target API is up and running!", HttpStatus.OK);
    }

}
