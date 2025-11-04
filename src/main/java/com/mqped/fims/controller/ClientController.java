package com.mqped.fims.controller;

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
    public ResponseEntity<Client> createClient(@RequestBody Client address) {
        Client savedClient = service.add(address);
        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = service.findAll();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Integer id) {
        Optional<Client> client = service.findById(id);
        return client.map(a -> new ResponseEntity<>(a, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Integer id) {
        Optional<Client> existingClient = service.findById(id);
        if (existingClient.isPresent()) {
            service.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // CHECK
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return new ResponseEntity<>("Client API is up and running!", HttpStatus.OK);
    }
}
