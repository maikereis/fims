package com.mqped.fims.controller;

import com.mqped.fims.model.dto.ContractAccountDTO;
import com.mqped.fims.model.entity.ContractAccount;
import com.mqped.fims.service.ContractAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contract-accounts")
public class ContractAccountController {

    private final ContractAccountService service;

    public ContractAccountController(ContractAccountService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<ContractAccountDTO> createContractAccount(@RequestBody ContractAccount contractAccount) {
        ContractAccount saved = service.add(contractAccount);
        return new ResponseEntity<>(ContractAccountDTO.fromEntity(saved), HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<ContractAccountDTO>> getAllContractAccounts() {
        List<ContractAccountDTO> dtos = service.findAll().stream()
                .map(ContractAccountDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // READ ALL (WITHOUT INSTALLATION DETAILS)
    @GetMapping("/minimal")
    public ResponseEntity<List<ContractAccountDTO>> getAllContractAccountsMinimal() {
        List<ContractAccountDTO> dtos = service.findAll().stream()
                .map(ContractAccountDTO::fromEntityWithoutInstallation)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<ContractAccountDTO> getContractAccountById(@PathVariable Integer id) {
        Optional<ContractAccount> account = service.findById(id);
        return account.map(a -> ResponseEntity.ok(ContractAccountDTO.fromEntity(a)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ContractAccountDTO> updateContractAccount(@PathVariable Integer id,
                                                                     @RequestBody ContractAccount contractAccount) {
        Optional<ContractAccount> updated = service.update(id, contractAccount);
        return updated.map(a -> ResponseEntity.ok(ContractAccountDTO.fromEntity(a)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContractAccount(@PathVariable Integer id) {
        if (service.existsById(id)) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // HEALTH CHECK
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("ContractAccount API is up and running!");
    }
}