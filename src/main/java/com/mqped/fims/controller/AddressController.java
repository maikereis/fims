package com.mqped.fims.controller;

import com.mqped.fims.model.dto.AddressDTO;
import com.mqped.fims.model.entity.Address;
import com.mqped.fims.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@RequestBody Address address) {
        Address savedAddress = service.add(address);
        return new ResponseEntity<>(AddressDTO.fromEntity(savedAddress), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        List<AddressDTO> dtos = service.findAll().stream()
                .map(AddressDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Integer id) {
        Address address = service.findById(id);
        return ResponseEntity.ok(AddressDTO.fromEntity(address));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Integer id, @RequestBody Address address) {
        Address updated = service.update(id, address);
        return ResponseEntity.ok(AddressDTO.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Address API is up and running!");
    }
}