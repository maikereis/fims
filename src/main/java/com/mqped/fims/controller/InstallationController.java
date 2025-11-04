package com.mqped.fims.controller;

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
    public ResponseEntity<Installation> createInstallation(@RequestBody Installation installation) {
        Installation savedInstallation = service.add(installation);
        return new ResponseEntity<>(savedInstallation, HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Installation>> getAllInstallations() {
        List<Installation> installations = service.findAll();
        return new ResponseEntity<>(installations, HttpStatus.OK);
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Installation> getInstallationById(@PathVariable Integer id) {
        Optional<Installation> installation = service.findById(id);
        return installation.map(i -> new ResponseEntity<>(i, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Installation> updateInstallation(@PathVariable Integer id, @RequestBody Installation installation) {
        Optional<Installation> updated = service.update(id, installation);
        return updated.map(i -> new ResponseEntity<>(i, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstallation(@PathVariable Integer id) {
        if (service.existsById(id)) {
            service.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // CHECK
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return new ResponseEntity<>("Installation API is up and running!", HttpStatus.OK);
    }
}
