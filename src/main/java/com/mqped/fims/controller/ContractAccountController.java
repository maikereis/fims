package com.mqped.fims.controller;

import com.mqped.fims.model.dto.ContractAccountDTO;
import com.mqped.fims.model.entity.ContractAccount;
import com.mqped.fims.service.ContractAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for managing {@link ContractAccount} entities.
 * <p>
 * Provides endpoints for creating, reading, updating, and deleting contract
 * account records.
 * These records represent customer billing or service accounts that can be
 * linked to installations,
 * clients, and targets within the FIMS (Field Inspection Management System)
 * ecosystem.
 * </p>
 *
 * <h2>Available Endpoints</h2>
 * <ul>
 * <li><b>POST /api/contract-accounts</b> — Create a new contract account.</li>
 * <li><b>GET /api/contract-accounts</b> — Retrieve all contract accounts with
 * full details.</li>
 * <li><b>GET /api/contract-accounts/minimal</b> — Retrieve all contract
 * accounts without installation details.</li>
 * <li><b>GET /api/contract-accounts/{id}</b> — Retrieve a contract account by
 * ID.</li>
 * <li><b>PUT /api/contract-accounts/{id}</b> — Update an existing contract
 * account.</li>
 * <li><b>DELETE /api/contract-accounts/{id}</b> — Delete a contract account by
 * ID.</li>
 * <li><b>GET /api/contract-accounts/check</b> — Health check for the contract
 * account API.</li>
 * </ul>
 *
 * <p>
 * This controller converts entities to {@link ContractAccountDTO} before
 * returning them in responses,
 * ensuring proper encapsulation and separation between the persistence and
 * presentation layers.
 * </p>
 *
 * @author Rodrigo
 * @since 1.0
 */
@RestController
@RequestMapping("/api/contract-accounts")
public class ContractAccountController {

    private final ContractAccountService service;

    /**
     * Constructs a new {@code ContractAccountController} with the required service
     * dependency.
     *
     * @param service the {@link ContractAccountService} used to manage contract
     *                account data.
     */
    public ContractAccountController(ContractAccountService service) {
        this.service = service;
    }

    /**
     * Creates a new contract account.
     *
     * @param contractAccount the {@link ContractAccount} entity containing account
     *                        details to persist.
     * @return a {@link ResponseEntity} containing the created
     *         {@link ContractAccountDTO}
     *         and HTTP status {@code 201 (Created)}.
     */
    @PostMapping
    public ResponseEntity<ContractAccountDTO> createContractAccount(@RequestBody ContractAccount contractAccount) {
        ContractAccount saved = service.add(contractAccount);
        return new ResponseEntity<>(ContractAccountDTO.fromEntity(saved), HttpStatus.CREATED);
    }

    /**
     * Retrieves all contract accounts with full related data (e.g., installation
     * details).
     *
     * @return a {@link ResponseEntity} containing a list of
     *         {@link ContractAccountDTO} objects
     *         and HTTP status {@code 200 (OK)}.
     */
    @GetMapping
    public ResponseEntity<List<ContractAccountDTO>> getAllContractAccounts() {
        List<ContractAccountDTO> dtos = service.findAll().stream()
                .map(ContractAccountDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves all contract accounts, excluding installation details.
     * <p>
     * This lightweight version is useful for listing or summary views where full
     * relational data is unnecessary.
     * </p>
     *
     * @return a {@link ResponseEntity} containing a simplified list of
     *         {@link ContractAccountDTO}
     *         objects and HTTP status {@code 200 (OK)}.
     */
    @GetMapping("/minimal")
    public ResponseEntity<List<ContractAccountDTO>> getAllContractAccountsMinimal() {
        List<ContractAccountDTO> dtos = service.findAll().stream()
                .map(ContractAccountDTO::fromEntityWithoutInstallation)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves a specific contract account by its unique ID.
     *
     * @param id the unique identifier of the contract account.
     * @return a {@link ResponseEntity} containing the requested
     *         {@link ContractAccountDTO}
     *         and HTTP status {@code 200 (OK)}.
     * @throws org.springframework.web.server.ResponseStatusException if the
     *                                                                contract
     *                                                                account is not
     *                                                                found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContractAccountDTO> getContractAccountById(@PathVariable Integer id) {
        ContractAccount account = service.findById(id);
        return ResponseEntity.ok(ContractAccountDTO.fromEntity(account));
    }

    /**
     * Updates an existing contract account by ID.
     *
     * @param id              the unique identifier of the contract account to
     *                        update.
     * @param contractAccount the {@link ContractAccount} object containing updated
     *                        information.
     * @return a {@link ResponseEntity} containing the updated
     *         {@link ContractAccountDTO}
     *         and HTTP status {@code 200 (OK)}.
     * @throws org.springframework.web.server.ResponseStatusException if the account
     *                                                                is not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContractAccountDTO> updateContractAccount(@PathVariable Integer id,
            @RequestBody ContractAccount contractAccount) {
        ContractAccount updated = service.update(id, contractAccount);
        return ResponseEntity.ok(ContractAccountDTO.fromEntity(updated));
    }

    /**
     * Deletes a contract account by its ID.
     *
     * @param id the unique identifier of the contract account to delete.
     * @return a {@link ResponseEntity} with no content and HTTP status
     *         {@code 204 (No Content)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContractAccount(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Simple health check endpoint for the ContractAccount API.
     * <p>
     * Can be used for monitoring or verifying that the endpoint is active and
     * responding.
     * </p>
     *
     * @return a {@link ResponseEntity} containing a simple status message
     *         and HTTP status {@code 200 (OK)}.
     */
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("ContractAccount API is up and running!");
    }
}
