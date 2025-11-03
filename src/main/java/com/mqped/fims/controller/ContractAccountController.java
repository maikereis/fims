package com.mqped.fims.controller;

import com.mqped.fims.model.ContractAccount;
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
    public ResponseEntity<ContractAccount> createContractAccount(@RequestBody ContractAccount contractAccount) {
        ContractAccount saved = service.add(contractAccount);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<ContractAccount>> getAllContractAccounts() {
        List<ContractAccount> accounts = service.findAll();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<ContractAccount> getContractAccountById(@PathVariable Integer id) {
        Optional<ContractAccount> account = service.findById(id);
        return account.map(a -> new ResponseEntity<>(a, HttpStatus.OK))
                      .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ContractAccount> updateContractAccount(@PathVariable Integer id,
                                                                 @RequestBody ContractAccount contractAccount) {
        Optional<ContractAccount> updated = service.update(id, contractAccount);
        return updated.map(a -> new ResponseEntity<>(a, HttpStatus.OK))
                      .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContractAccount(@PathVariable Integer id) {
        if (service.existsById(id)) {
            service.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // HEALTH CHECK
    @GetMapping("/check")
    public ResponseEntity<String> check() {
        return new ResponseEntity<>("ContractAccount API is up and running!", HttpStatus.OK);
    }
}
