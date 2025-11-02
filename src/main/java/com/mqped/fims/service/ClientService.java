package com.mqped.fims.service;

import com.mqped.fims.model.Client;
import com.mqped.fims.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService implements CrudService<Client, Integer> {

    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public Client add(Client client) {
        validate(client);
        return repository.save(client);
    }

    @Override
    public List<Client> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Client> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Client> update(Integer id, Client client) {
        validate(client);

        return repository.findById(id).map(existing -> {
            existing.setName(client.getName());
            existing.setCpf(client.getCpf());
            existing.setBirthDate(client.getBirthDate());
            existing.setMotherName(client.getMotherName());
            existing.setCnpj(client.getCnpj());
            existing.setGenre(client.getGenre());
            existing.setCreatedAt(client.getCreatedAt()); // optional, depends if you want to allow updating creation
                                                          // timestamp

            return repository.save(existing);
        });
    }

    @Override
    public void deleteById(Integer id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Client with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }

    private void validate(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        if (client.getName() == null || client.getName().isBlank()) {
            throw new IllegalArgumentException("Client name is required");
        }

        String cpf = client.getCpf();
        if (cpf != null && !cpf.isBlank() && !isValidCpf(cpf)) {
            throw new IllegalArgumentException("Invalid CPF format. Expected XXX.XXX.XXX-XX");
        }
    }

    private boolean isValidCpf(String cpf) {
        if (cpf == null)
            return false;
        return cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    }
}
