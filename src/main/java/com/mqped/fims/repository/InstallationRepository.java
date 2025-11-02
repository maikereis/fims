package com.mqped.fims.repository;

import com.mqped.fims.model.Installation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstallationRepository extends JpaRepository<Installation, Integer> {
    // JpaRepository already provides findAll, findById, save, deleteById, etc.
}
