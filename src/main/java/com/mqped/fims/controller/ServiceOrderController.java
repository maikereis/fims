package com.mqped.fims.controller;

import com.mqped.fims.model.dto.ServiceOrderDTO;
import com.mqped.fims.model.entity.ServiceOrder;
import com.mqped.fims.model.enums.ServiceOrderStatus;
import com.mqped.fims.service.ServiceOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/service-orders")
public class ServiceOrderController {

    private final ServiceOrderService service;

    public ServiceOrderController(ServiceOrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ServiceOrderDTO> createServiceOrder(@RequestBody ServiceOrder order) {
        ServiceOrder savedOrder = service.add(order);
        return new ResponseEntity<>(ServiceOrderDTO.fromEntity(savedOrder), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ServiceOrderDTO>> getAll() {
        List<ServiceOrderDTO> dtos = service.findAll().stream()
                .map(ServiceOrderDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOrderDTO> getById(@PathVariable Integer id) {
        ServiceOrder order = service.findById(id);
        return ResponseEntity.ok(ServiceOrderDTO.fromEntity(order));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceOrderDTO> update(@PathVariable Integer id, @RequestBody ServiceOrder order) {
        ServiceOrder updated = service.update(id, order);
        return ResponseEntity.ok(ServiceOrderDTO.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ServiceOrderDTO>> getByStatus(@PathVariable ServiceOrderStatus status) {
        List<ServiceOrderDTO> dtos = service.findByStatus(status).stream()
                .map(ServiceOrderDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/target/{targetId}")
    public ResponseEntity<List<ServiceOrderDTO>> getByTargetId(@PathVariable Integer targetId) {
        List<ServiceOrderDTO> dtos = service.findByTargetId(targetId).stream()
                .map(ServiceOrderDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/target/distance")
    public ResponseEntity<List<ServiceOrderDTO>> getByTargetDistance(
            @RequestParam Double min,
            @RequestParam Double max) {
        List<ServiceOrderDTO> dtos = service.findByTargetDistanceFromBaseBetween(min, max).stream()
                .map(ServiceOrderDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/target/signature/{signature}")
    public ResponseEntity<List<ServiceOrderDTO>> getByTargetSignature(@PathVariable String signature) {
        List<ServiceOrderDTO> dtos = service.findByTargetSignature(signature).stream()
                .map(ServiceOrderDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/target/signature/contains/{partial}")
    public ResponseEntity<List<ServiceOrderDTO>> getByTargetSignatureContaining(@PathVariable String partial) {
        List<ServiceOrderDTO> dtos = service.findByTargetSignatureContaining(partial).stream()
                .map(ServiceOrderDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/older-than/{days}")
    public ResponseEntity<List<ServiceOrderDTO>> getOlderThanDays(@PathVariable long days) {
        List<ServiceOrderDTO> dtos = service.findOlderThanDays(days).stream()
                .map(ServiceOrderDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/created-between")
    public ResponseEntity<List<ServiceOrderDTO>> getByCreatedAtBetween(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        List<ServiceOrderDTO> dtos = service.findByCreatedAtBetween(start, end).stream()
                .map(ServiceOrderDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("ServiceOrder API is up and running!");
    }
}