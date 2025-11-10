package com.mqped.fims.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mqped.fims.model.entity.Client;

/**
 * Repository interface for managing {@link Client} entities.
 * <p>
 * Extends {@link JpaRepository} to provide CRUD operations and defines
 * additional query methods for domain-specific identifiers such as CPF and CNPJ.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Perform CRUD operations on {@link Client} entities</li>
 *   <li>Check existence of clients by CPF or CNPJ</li>
 *   <li>Retrieve clients by their unique CPF or CNPJ identifiers</li>
 * </ul>
 *
 * <h3>Notes:</h3>
 * <ul>
 *   <li>CPF and CNPJ are expected to be unique across all clients</li>
 *   <li>These identifiers are typically formatted (e.g. {@code 000.000.000-00}, {@code 00.000.000/0000-00})</li>
 * </ul>
 *
 * <h3>Example Usage:</h3>
 * <pre>{@code
 * Optional<Client> client = clientRepository.findByCpf("123.456.789-00");
 * if (client.isPresent()) {
 *     System.out.println(client.get().getName());
 * }
 * }</pre>
 *
 * @see com.mqped.fims.model.entity.Client
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @since 1.0
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    /**
     * Checks if a client with the given CPF exists.
     *
     * @param cpf the CPF to check (formatted as XXX.XXX.XXX-XX)
     * @return {@code true} if a client with the given CPF exists, otherwise {@code false}
     */
    boolean existsByCpf(String cpf);

    /**
     * Checks if a client with the given CNPJ exists.
     *
     * @param cnpj the CNPJ to check (formatted as XX.XXX.XXX/XXXX-XX)
     * @return {@code true} if a client with the given CNPJ exists, otherwise {@code false}
     */
    boolean existsByCnpj(String cnpj);

    /**
     * Retrieves a client by its unique CPF.
     *
     * @param cpf the CPF of the client (formatted as XXX.XXX.XXX-XX)
     * @return an {@link Optional} containing the client, or empty if none found
     */
    Optional<Client> findByCpf(String cpf);

    /**
     * Retrieves a client by its unique CNPJ.
     *
     * @param cnpj the CNPJ of the client (formatted as XX.XXX.XXX/XXXX-XX)
     * @return an {@link Optional} containing the client, or empty if none found
     */
    Optional<Client> findByCnpj(String cnpj);
}
