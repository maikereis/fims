package com.mqped.fims.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mqped.fims.model.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    boolean existsByCpf(String cpf);

    boolean existsByCnpj(String cnpj);

    Optional<Client> findByCpf(String cpf);

    Optional<Client> findByCnpj(String cnpj);
}
