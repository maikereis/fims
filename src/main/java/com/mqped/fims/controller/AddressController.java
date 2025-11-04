package com.mqped.fims.controller;

import com.mqped.fims.model.dto.AddressDTO;
import com.mqped.fims.model.entity.Address;
import com.mqped.fims.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@RequestBody Address address) {
        Address savedAddress = service.add(address);
        return new ResponseEntity<>(AddressDTO.fromEntity(savedAddress), HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        List<AddressDTO> dtos = service.findAll().stream()
                .map(AddressDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Integer id) {
        Optional<Address> address = service.findById(id);
        return address.map(a -> ResponseEntity.ok(AddressDTO.fromEntity(a)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Integer id, @RequestBody Address address) {
        Optional<Address> updated = service.update(id, address);
        return updated.map(a -> ResponseEntity.ok(AddressDTO.fromEntity(a)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Integer id) {
        if (service.existsById(id)) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // CHECK
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Address API is up and running!");
    }
}