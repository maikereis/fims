package com.mqped.fims.model.dto;

import com.mqped.fims.model.entity.Client;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing a {@link Client} entity.
 * <p>
 * The {@code ClientDTO} class is used to transfer client information between
 * application layers (e.g., controllers, services, and clients) without
 * exposing
 * the JPA entity directly. It contains personal and registration details about
 * a client,
 * such as name, CPF/CNPJ, birth date, and creation timestamp.
 * </p>
 *
 * <h2>Usage Example</h2>
 * 
 * <pre>{@code
 * Client client = clientRepository.findById(1).orElseThrow();
 * ClientDTO dto = ClientDTO.fromEntity(client);
 * System.out.println("Client name: " + dto.getName());
 * }</pre>
 *
 * @see com.mqped.fims.model.entity.Client
 */
public class ClientDTO {

    /** Unique identifier of the client. */
    private Integer id;

    /** Full name of the client. */
    private String name;

    /** CPF (Cadastro de Pessoas Físicas) number for individuals. */
    private String cpf;

    /** Client’s date of birth. */
    private LocalDateTime birthDate;

    /** Full name of the client’s mother. */
    private String motherName;

    /** CNPJ (Cadastro Nacional da Pessoa Jurídica) number for companies. */
    private String cnpj;

    /** Gender of the client (e.g., Male, Female, Other). */
    private String genre;

    /** Date and time when the client record was created. */
    private LocalDateTime createdAt;

    // --- Getters and Setters ---

    /** @return the unique identifier of the client. */
    public Integer getId() {
        return id;
    }

    /** @param id sets the unique identifier of the client. */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @return the full name of the client. */
    public String getName() {
        return name;
    }

    /** @param name sets the full name of the client. */
    public void setName(String name) {
        this.name = name;
    }

    /** @return the CPF number of the client (for individuals). */
    public String getCpf() {
        return cpf;
    }

    /** @param cpf sets the CPF number of the client. */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /** @return the client’s date of birth. */
    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    /** @param birthDate sets the client’s date of birth. */
    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    /** @return the name of the client’s mother. */
    public String getMotherName() {
        return motherName;
    }

    /** @param motherName sets the name of the client’s mother. */
    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    /** @return the CNPJ number of the client (for companies). */
    public String getCnpj() {
        return cnpj;
    }

    /** @param cnpj sets the CNPJ number of the client. */
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    /** @return the gender of the client. */
    public String getGenre() {
        return genre;
    }

    /** @param genre sets the gender of the client. */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /** @return the timestamp when the client record was created. */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** @param createdAt sets the timestamp when the client record was created. */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Converts a {@link Client} entity into a {@link ClientDTO}.
     *
     * @param client the {@link Client} entity to convert
     * @return a {@link ClientDTO} populated with data from the entity
     */
    public static ClientDTO fromEntity(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setCpf(client.getCpf());
        dto.setBirthDate(client.getBirthDate());
        dto.setMotherName(client.getMotherName());
        dto.setCnpj(client.getCnpj());
        dto.setGenre(client.getGenre());
        dto.setCreatedAt(client.getCreatedAt());
        return dto;
    }
}
