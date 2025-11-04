package com.mqped.fims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mqped.fims.model.entity.ContractAccount;

@Repository
public interface ContractAccountRepository extends JpaRepository<ContractAccount, Integer> {
    // JpaRepository already provides findAll, findById, save, deleteById, etc.
}
