package com.mqped.fims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mqped.fims.model.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    // JpaRepository already provides findAll, findById, save, deleteById, etc.
}
