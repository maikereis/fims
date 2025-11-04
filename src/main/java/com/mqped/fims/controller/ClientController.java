package com.mqped.fims.controller;

import com.mqped.fims.model.dto.ClientDTO;
import com.mqped.fims.model.entity.Client;
import com.mqped.fims.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    
    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody Client client) {
        Client savedClient = service.add(client);
        return new ResponseEntity<>(ClientDTO.fromEntity(savedClient), HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        List<ClientDTO> dtos = service.findAll().stream()
                .map(ClientDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Integer id) {
        Optional<Client> client = service.findById(id);
        return client.map(c -> ResponseEntity.ok(ClientDTO.fromEntity(c)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Integer id, @RequestBody Client client) {
        Optional<Client> updated = service.update(id, client);
        return updated.map(c -> ResponseEntity.ok(ClientDTO.fromEntity(c)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Integer id) {
        if (service.existsById(id)) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // CHECK
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Client API is up and running!");
    }
}