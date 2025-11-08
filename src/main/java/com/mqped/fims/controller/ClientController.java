package com.mqped.fims.controller;

import com.mqped.fims.model.dto.ClientDTO;
import com.mqped.fims.model.entity.Client;
import com.mqped.fims.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    
    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody Client client) {
        Client savedClient = service.add(client);
        return new ResponseEntity<>(ClientDTO.fromEntity(savedClient), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        List<ClientDTO> dtos = service.findAll().stream()
                .map(ClientDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Integer id) {
        Client client = service.findById(id);
        return ResponseEntity.ok(ClientDTO.fromEntity(client));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Integer id, @RequestBody Client client) {
        Client updated = service.update(id, client);
        return ResponseEntity.ok(ClientDTO.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Client API is up and running!");
    }
}