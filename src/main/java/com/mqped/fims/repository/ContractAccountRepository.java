package com.mqped.fims.repository;

import com.mqped.fims.model.ContractAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractAccountRepository extends JpaRepository<ContractAccount, Integer> {
    // JpaRepository already provides findAll, findById, save, deleteById, etc.
}
