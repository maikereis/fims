package com.mqped.fims.controller;

import com.mqped.fims.model.dto.InstallationDTO;
import com.mqped.fims.model.entity.Installation;
import com.mqped.fims.service.InstallationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/installations")
public class InstallationController {

    private final InstallationService service;

    public InstallationController(InstallationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InstallationDTO> createInstallation(@RequestBody Installation installation) {
        Installation savedInstallation = service.add(installation);
        return new ResponseEntity<>(InstallationDTO.fromEntity(savedInstallation), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InstallationDTO>> getAllInstallations() {
        List<InstallationDTO> dtos = service.findAll().stream()
                .map(InstallationDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/minimal")
    public ResponseEntity<List<InstallationDTO>> getAllInstallationsMinimal() {
        List<InstallationDTO> dtos = service.findAll().stream()
                .map(InstallationDTO::fromEntityWithoutAddress)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstallationDTO> getInstallationById(@PathVariable Integer id) {
        Installation installation = service.findById(id);
        return ResponseEntity.ok(InstallationDTO.fromEntity(installation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstallationDTO> updateInstallation(@PathVariable Integer id, 
                                                               @RequestBody Installation installation) {
        Installation updated = service.update(id, installation);
        return ResponseEntity.ok(InstallationDTO.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstallation(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Installation API is up and running!");
    }
}