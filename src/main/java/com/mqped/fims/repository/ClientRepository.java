package com.mqped.fims.repository;

import com.mqped.fims.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    // JpaRepository already provides findAll, findById, save, deleteById, etc.
}
