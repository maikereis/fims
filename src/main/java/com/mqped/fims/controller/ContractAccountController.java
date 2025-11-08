package com.mqped.fims.controller;

import com.mqped.fims.model.dto.ContractAccountDTO;
import com.mqped.fims.model.entity.ContractAccount;
import com.mqped.fims.service.ContractAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contract-accounts")
public class ContractAccountController {

    private final ContractAccountService service;

    public ContractAccountController(ContractAccountService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ContractAccountDTO> createContractAccount(@RequestBody ContractAccount contractAccount) {
        ContractAccount saved = service.add(contractAccount);
        return new ResponseEntity<>(ContractAccountDTO.fromEntity(saved), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ContractAccountDTO>> getAllContractAccounts() {
        List<ContractAccountDTO> dtos = service.findAll().stream()
                .map(ContractAccountDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/minimal")
    public ResponseEntity<List<ContractAccountDTO>> getAllContractAccountsMinimal() {
        List<ContractAccountDTO> dtos = service.findAll().stream()
                .map(ContractAccountDTO::fromEntityWithoutInstallation)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractAccountDTO> getContractAccountById(@PathVariable Integer id) {
        ContractAccount account = service.findById(id);
        return ResponseEntity.ok(ContractAccountDTO.fromEntity(account));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContractAccountDTO> updateContractAccount(@PathVariable Integer id,
                                                                     @RequestBody ContractAccount contractAccount) {
        ContractAccount updated = service.update(id, contractAccount);
        return ResponseEntity.ok(ContractAccountDTO.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContractAccount(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("ContractAccount API is up and running!");
    }
}