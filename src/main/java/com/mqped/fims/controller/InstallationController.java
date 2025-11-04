package com.mqped.fims.controller;

import com.mqped.fims.model.dto.InstallationDTO;
import com.mqped.fims.model.entity.Installation;
import com.mqped.fims.service.InstallationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/installations")
public class InstallationController {

    private final InstallationService service;

    public InstallationController(InstallationService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<InstallationDTO> createInstallation(@RequestBody Installation installation) {
        Installation savedInstallation = service.add(installation);
        return new ResponseEntity<>(InstallationDTO.fromEntity(savedInstallation), HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<InstallationDTO>> getAllInstallations() {
        List<InstallationDTO> dtos = service.findAll().stream()
                .map(InstallationDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // READ ALL (WITHOUT ADDRESS DETAILS)
    @GetMapping("/minimal")
    public ResponseEntity<List<InstallationDTO>> getAllInstallationsMinimal() {
        List<InstallationDTO> dtos = service.findAll().stream()
                .map(InstallationDTO::fromEntityWithoutAddress)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<InstallationDTO> getInstallationById(@PathVariable Integer id) {
        Optional<Installation> installation = service.findById(id);
        return installation.map(i -> ResponseEntity.ok(InstallationDTO.fromEntity(i)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<InstallationDTO> updateInstallation(@PathVariable Integer id, 
                                                               @RequestBody Installation installation) {
        Optional<Installation> updated = service.update(id, installation);
        return updated.map(i -> ResponseEntity.ok(InstallationDTO.fromEntity(i)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstallation(@PathVariable Integer id) {
        if (service.existsById(id)) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // CHECK
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Installation API is up and running!");
    }
}