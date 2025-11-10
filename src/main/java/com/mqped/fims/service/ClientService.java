package com.mqped.fims.service;

import com.mqped.fims.exceptions.InvalidDataException;
import com.mqped.fims.exceptions.ResourceNotFoundException;
import com.mqped.fims.model.entity.Client;
import com.mqped.fims.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for managing {@link Client} entities.
 * <p>
 * Implements standard CRUD operations as defined in {@link CrudService}
 * and includes domain-specific validation logic, such as verifying CPF format
 * and ensuring required fields are provided.
 * </p>
 *
 * <p>
 * Exceptions thrown:
 * <ul>
 * <li>{@link InvalidDataException} — when provided data is null or fails
 * validation</li>
 * <li>{@link ResourceNotFoundException} — when a requested {@link Client}
 * cannot be found</li>
 * </ul>
 * </p>
 *
 * <p>
 * This service acts as a domain/business logic layer between controllers and
 * the persistence layer represented by {@link ClientRepository}.
 * </p>
 *
 * @author
 * @since 1.0
 */
@Service
public class ClientService implements CrudService<Client, Integer> {

    private final ClientRepository repository;

    /**
     * Constructs a new {@code ClientService} with the provided repository.
     *
     * @param repository the {@link ClientRepository} used for data persistence
     */
    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    /**
     * Persists a new {@link Client} after validating required fields and format.
     *
     * @param client the client entity to persist
     * @return the saved {@link Client}
     * @throws InvalidDataException if the client is null or has invalid fields
     */
    @Override
    public Client add(Client client) {
        validate(client);
        return repository.save(client);
    }

    /**
     * Retrieves all clients from the database.
     *
     * @return list of all {@link Client} entities
     */
    @Override
    public List<Client> findAll() {
        return repository.findAll();
    }

    /**
     * Finds a client by its unique integer ID.
     *
     * @param id the client ID
     * @return the found {@link Client}
     * @throws ResourceNotFoundException if no client with the given ID exists
     */
    @Override
    public Client findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " not found"));
    }

    /**
     * Updates an existing {@link Client} with new field values.
     * <p>
     * This method first validates the provided data, retrieves the existing client,
     * applies field updates, and saves it back to the repository.
     * </p>
     *
     * @param id     the ID of the client to update
     * @param client the new client data
     * @return the updated {@link Client}
     * @throws InvalidDataException      if validation fails
     * @throws ResourceNotFoundException if the client does not exist
     */
    @Override
    public Client update(Integer id, Client client) {
        validate(client);

        Client existing = findById(id); // throws if not found

        existing.setName(client.getName());
        existing.setCpf(client.getCpf());
        existing.setBirthDate(client.getBirthDate());
        existing.setMotherName(client.getMotherName());
        existing.setCnpj(client.getCnpj());
        existing.setGenre(client.getGenre());
        // existing.setCreatedAt(client.getCreatedAt()); // intentionally left out to
        // preserve creation date

        return repository.save(existing);
    }

    /**
     * Deletes a {@link Client} by its ID.
     *
     * @param id the ID of the client to delete
     * @throws ResourceNotFoundException if the client does not exist
     */
    @Override
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Client with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    /**
     * Checks whether a {@link Client} exists by its ID.
     *
     * @param id the client ID to check
     * @return {@code true} if the client exists, {@code false} otherwise
     */
    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    /**
     * Counts all {@link Client} records in the repository.
     *
     * @return the total number of clients
     */
    @Override
    public long count() {
        return repository.count();
    }

    /**
     * Validates the provided {@link Client} object.
     * <ul>
     * <li>Ensures the client object is not null</li>
     * <li>Ensures the client name is not blank</li>
     * <li>If provided, validates the CPF format</li>
     * </ul>
     *
     * @param client the client to validate
     * @throws InvalidDataException if validation fails
     */
    private void validate(Client client) {
        if (client == null) {
            throw new InvalidDataException("Client cannot be null");
        }
        if (client.getName() == null || client.getName().isBlank()) {
            throw new InvalidDataException("Client name is required");
        }

        String cpf = client.getCpf();
        if (cpf != null && !cpf.isBlank() && !isValidCpf(cpf)) {
            throw new InvalidDataException("Invalid CPF format. Expected XXX.XXX.XXX-XX");
        }
    }

    /**
     * Validates the CPF format.
     * <p>
     * This method checks only the syntactic format using a regex pattern;
     * it does not perform checksum or authenticity validation.
     * </p>
     *
     * @param cpf the CPF string to validate
     * @return {@code true} if valid format, {@code false} otherwise
     */
    private boolean isValidCpf(String cpf) {
        if (cpf == null)
            return false;
        return cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    }
}
