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

/**
 * REST controller responsible for managing {@link ServiceOrder} entities.
 * <p>
 * Provides endpoints to create, read, update, delete, and query service orders,
 * including advanced filters based on status, target attributes, and creation
 * dates.
 * </p>
 *
 * <h2>Available Endpoints</h2>
 * <ul>
 * <li><b>POST /api/service-orders</b> — Create a new service order.</li>
 * <li><b>GET /api/service-orders</b> — Retrieve all service orders.</li>
 * <li><b>GET /api/service-orders/{id}</b> — Retrieve a service order by
 * ID.</li>
 * <li><b>PUT /api/service-orders/{id}</b> — Update a service order.</li>
 * <li><b>DELETE /api/service-orders/{id}</b> — Delete a service order.</li>
 * <li><b>GET /api/service-orders/status/{status}</b> — Filter by status.</li>
 * <li><b>GET /api/service-orders/target/{targetId}</b> — Filter by target
 * ID.</li>
 * <li><b>GET /api/service-orders/target/distance</b> — Filter by target
 * distance range.</li>
 * <li><b>GET /api/service-orders/target/signature/{signature}</b> — Find by
 * exact target signature.</li>
 * <li><b>GET /api/service-orders/target/signature/contains/{partial}</b> — Find
 * by partial target signature match.</li>
 * <li><b>GET /api/service-orders/older-than/{days}</b> — Find orders older than
 * a number of days.</li>
 * <li><b>GET /api/service-orders/created-between</b> — Find orders created
 * between two dates.</li>
 * <li><b>GET /api/service-orders/check</b> — API health check.</li>
 * </ul>
 *
 * <p>
 * This controller uses {@link ServiceOrderDTO} for all responses to separate
 * the persistence layer from the
 * API layer and prevent direct exposure of JPA entities.
 * </p>
 *
 * @author Rodrigo
 * @since 1.0
 */
@RestController
@RequestMapping("/api/service-orders")
public class ServiceOrderController {

    private final ServiceOrderService service;

    /**
     * Constructs a new {@code ServiceOrderController}.
     *
     * @param service the {@link ServiceOrderService} used to manage service order
     *                data.
     */
    public ServiceOrderController(ServiceOrderService service) {
        this.service = service;
    }

    /**
     * Creates a new service order.
     *
     * @param order the {@link ServiceOrder} entity to be created.
     * @return a {@link ResponseEntity} containing the created
     *         {@link ServiceOrderDTO}
     *         and HTTP status {@code 201 (Created)}.
     */
    @PostMapping
    public ResponseEntity<ServiceOrderDTO> createServiceOrder(@RequestBody ServiceOrder order) {
        ServiceOrder savedOrder = service.add(order);
        return new ResponseEntity<>(ServiceOrderDTO.fromEntity(savedOrder), HttpStatus.CREATED);
    }

    /**
     * Retrieves all existing service orders.
     *
     * @return a {@link ResponseEntity} containing a list of {@link ServiceOrderDTO}
     *         and HTTP status {@code 200 (OK)}.
     */
    @GetMapping
    public ResponseEntity<List<ServiceOrderDTO>> getAll() {
        List<ServiceOrderDTO> dtos = service.findAll().stream()
                .map(ServiceOrderDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves a specific service order by its unique identifier.
     *
     * @param id the ID of the service order.
     * @return a {@link ResponseEntity} containing the {@link ServiceOrderDTO}
     *         and HTTP status {@code 200 (OK)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceOrderDTO> getById(@PathVariable Integer id) {
        ServiceOrder order = service.findById(id);
        return ResponseEntity.ok(ServiceOrderDTO.fromEntity(order));
    }

    /**
     * Updates an existing service order.
     *
     * @param id    the ID of the service order to update.
     * @param order the {@link ServiceOrder} entity containing updated data.
     * @return a {@link ResponseEntity} containing the updated
     *         {@link ServiceOrderDTO}
     *         and HTTP status {@code 200 (OK)}.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceOrderDTO> update(@PathVariable Integer id, @RequestBody ServiceOrder order) {
        ServiceOrder updated = service.update(id, order);
        return ResponseEntity.ok(ServiceOrderDTO.fromEntity(updated));
    }

    /**
     * Deletes a service order by its ID.
     *
     * @param id the ID of the service order to delete.
     * @return a {@link ResponseEntity} with HTTP status {@code 204 (No Content)}
     *         upon successful deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all service orders with a specific status.
     *
     * @param status the {@link ServiceOrderStatus} to filter by (e.g. CREATED,
     *               EXECUTED, CANCELED).
     * @return a {@link ResponseEntity} containing a filtered list of
     *         {@link ServiceOrderDTO}.
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ServiceOrderDTO>> getByStatus(@PathVariable ServiceOrderStatus status) {
        List<ServiceOrderDTO> dtos = service.findByStatus(status).stream()
                .map(ServiceOrderDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves service orders associated with a specific target ID.
     *
     * @param targetId the ID of the target to filter by.
     * @return a {@link ResponseEntity} containing the list of matching
     *         {@link ServiceOrderDTO}.
     */
    @GetMapping("/target/{targetId}")
    public ResponseEntity<List<ServiceOrderDTO>> getByTargetId(@PathVariable Integer targetId) {
        List<ServiceOrderDTO> dtos = service.findByTargetId(targetId).stream()
                .map(ServiceOrderDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves service orders whose target distance from the base falls within a
     * specific range.
     *
     * @param min the minimum distance value.
     * @param max the maximum distance value.
     * @return a {@link ResponseEntity} containing the list of matching
     *         {@link ServiceOrderDTO}.
     */
    @GetMapping("/target/distance")
    public ResponseEntity<List<ServiceOrderDTO>> getByTargetDistance(
            @RequestParam Double min,
            @RequestParam Double max) {
        List<ServiceOrderDTO> dtos = service.findByTargetDistanceFromBaseBetween(min, max).stream()
                .map(ServiceOrderDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves service orders with an exact target signature match.
     *
     * @param signature the target signature string to match exactly.
     * @return a {@link ResponseEntity} containing the list of matching
     *         {@link ServiceOrderDTO}.
     */
    @GetMapping("/target/signature/{signature}")
    public ResponseEntity<List<ServiceOrderDTO>> getByTargetSignature(@PathVariable String signature) {
        List<ServiceOrderDTO> dtos = service.findByTargetSignature(signature).stream()
                .map(ServiceOrderDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves service orders whose target signature contains a given substring.
     *
     * @param partial the substring to search for within target signatures.
     * @return a {@link ResponseEntity} containing the list of matching
     *         {@link ServiceOrderDTO}.
     */
    @GetMapping("/target/signature/contains/{partial}")
    public ResponseEntity<List<ServiceOrderDTO>> getByTargetSignatureContaining(@PathVariable String partial) {
        List<ServiceOrderDTO> dtos = service.findByTargetSignatureContaining(partial).stream()
                .map(ServiceOrderDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves service orders older than the specified number of days.
     *
     * @param days the minimum age of the service orders in days.
     * @return a {@link ResponseEntity} containing the list of matching
     *         {@link ServiceOrderDTO}.
     */
    @GetMapping("/older-than/{days}")
    public ResponseEntity<List<ServiceOrderDTO>> getOlderThanDays(@PathVariable long days) {
        List<ServiceOrderDTO> dtos = service.findOlderThanDays(days).stream()
                .map(ServiceOrderDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves service orders created between two date-time values.
     *
     * @param start the start of the date range (inclusive).
     * @param end   the end of the date range (inclusive).
     * @return a {@link ResponseEntity} containing the list of matching
     *         {@link ServiceOrderDTO}.
     */
    @GetMapping("/created-between")
    public ResponseEntity<List<ServiceOrderDTO>> getByCreatedAtBetween(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        List<ServiceOrderDTO> dtos = service.findByCreatedAtBetween(start, end).stream()
                .map(ServiceOrderDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Simple health check endpoint for the Service Order API.
     *
     * @return a {@link ResponseEntity} containing a status message and HTTP status
     *         {@code 200 (OK)}.
     */
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("ServiceOrder API is up and running!");
    }
}
