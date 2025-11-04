package com.mqped.fims.controller;

import com.mqped.fims.model.entity.ServiceOrder;
import com.mqped.fims.model.enums.ServiceOrderStatus;
import com.mqped.fims.service.ServiceOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/service-orders")
public class ServiceOrderController {

    private final ServiceOrderService service;

    public ServiceOrderController(ServiceOrderService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<ServiceOrder> createServiceOrder(@RequestBody ServiceOrder order) {
        ServiceOrder savedOrder = service.add(order);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<ServiceOrder>> getAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<ServiceOrder> getById(@PathVariable Integer id) {
        Optional<ServiceOrder> order = service.findById(id);
        return order.map(o -> new ResponseEntity<>(o, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ServiceOrder> update(@PathVariable Integer id, @RequestBody ServiceOrder order) {
        Optional<ServiceOrder> updated = service.update(id, order);
        return updated.map(o -> new ResponseEntity<>(o, HttpStatus.OK))
                      .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (service.existsById(id)) {
            service.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // FILTERS
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ServiceOrder>> getByStatus(@PathVariable ServiceOrderStatus status) {
        return new ResponseEntity<>(service.findByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/target/{targetId}")
    public ResponseEntity<List<ServiceOrder>> getByTargetId(@PathVariable Integer targetId) {
        return new ResponseEntity<>(service.findByTargetId(targetId), HttpStatus.OK);
    }

    @GetMapping("/target/distance")
    public ResponseEntity<List<ServiceOrder>> getByTargetDistance(
            @RequestParam Double min,
            @RequestParam Double max) {
        return new ResponseEntity<>(service.findByTargetDistanceFromBaseBetween(min, max), HttpStatus.OK);
    }

    @GetMapping("/target/signature/{signature}")
    public ResponseEntity<List<ServiceOrder>> getByTargetSignature(@PathVariable String signature) {
        return new ResponseEntity<>(service.findByTargetSignature(signature), HttpStatus.OK);
    }

    @GetMapping("/target/signature/contains/{partial}")
    public ResponseEntity<List<ServiceOrder>> getByTargetSignatureContaining(@PathVariable String partial) {
        return new ResponseEntity<>(service.findByTargetSignatureContaining(partial), HttpStatus.OK);
    }

    @GetMapping("/older-than/{days}")
    public ResponseEntity<List<ServiceOrder>> getOlderThanDays(@PathVariable long days) {
        return new ResponseEntity<>(service.findOlderThanDays(days), HttpStatus.OK);
    }

    @GetMapping("/created-between")
    public ResponseEntity<List<ServiceOrder>> getByCreatedAtBetween(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return new ResponseEntity<>(service.findByCreatedAtBetween(start, end), HttpStatus.OK);
    }

    // HEALTH CHECK
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return new ResponseEntity<>("ServiceOrder API is up and running!", HttpStatus.OK);
    }
}
