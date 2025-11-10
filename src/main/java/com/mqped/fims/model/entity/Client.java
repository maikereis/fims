package com.mqped.fims.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;
import java.util.List;

import com.mqped.fims.validation.annotation.ChronologicalDates;
import com.mqped.fims.validation.annotation.CpfOrCnpjRequired;

/**
 * Represents a client or customer within the system.
 * <p>
 * The {@code Client} entity stores personal and identification data such as CPF
 * or CNPJ,
 * birth date, and other demographic information. Each client can be associated
 * with
 * multiple {@link ContractAccount} records.
 * </p>
 *
 * <p>
 * <strong>Database table:</strong> {@code clients}
 * </p>
 *
 * <p>
 * This entity uses the following custom validation annotations:
 * </p>
 * <ul>
 * <li>{@link CpfOrCnpjRequired} — ensures that at least one of CPF or CNPJ is
 * provided.</li>
 * <li>{@link ChronologicalDates} — ensures that {@code createdAt} occurs after
 * {@code birthDate}.</li>
 * </ul>
 *
 * @author MQPED
 * @see ContractAccount
 */
@Entity
@CpfOrCnpjRequired
@Table(name = "clients")
@ChronologicalDates(start = "birthDate", end = "createdAt", allowEqual = false, message = "Creation date must be after birth date.")
public class Client {

    /**
     * Unique identifier for the client record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Full legal name of the client.
     * <p>
     * Cannot be blank.
     * </p>
     */
    @NotBlank(message = "Name is required.")
    private String name;

    /**
     * CPF number of the client (individual taxpayer registry ID).
     * <p>
     * Optional if CNPJ is provided.
     * </p>
     */
    private String cpf;

    /**
     * Birth date of the client.
     * <p>
     * Cannot be null and must occur in the past or present.
     * </p>
     */
    @NotNull(message = "Birth date is required.")
    private LocalDateTime birthDate;

    /**
     * Mother's full name — optional demographic information.
     */
    private String motherName;

    /**
     * CNPJ number of the client (corporate taxpayer registry ID).
     * <p>
     * Optional if CPF is provided.
     * </p>
     */
    private String cnpj;

    /**
     * Gender of the client (e.g., Male, Female, Non-binary).
     * <p>
     * Optional field — format not restricted.
     * </p>
     */
    private String genre;

    /**
     * Date and time when the client record was created.
     * <p>
     * Cannot be in the future.
     * </p>
     */
    @PastOrPresent(message = "Creation date cannot be in the future.")
    private LocalDateTime createdAt;

    /**
     * List of contract accounts associated with this client.
     * <p>
     * Cascade and orphan removal are enabled — removing a client will
     * also remove all their related contract accounts.
     * </p>
     */
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractAccount> contractAccounts;

    // ---------------------------------------------------
    // Getters and Setters
    // ---------------------------------------------------

    /** @return the client ID */
    public Integer getId() {
        return id;
    }

    /** @param id sets the client ID */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @return the full name of the client */
    public String getName() {
        return name;
    }

    /** @param name sets the full name of the client */
    public void setName(String name) {
        this.name = name;
    }

    /** @return the CPF of the client */
    public String getCpf() {
        return cpf;
    }

    /** @param cpf sets the CPF of the client */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /** @return the client's birth date */
    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    /** @param birthDate sets the client's birth date */
    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    /** @return the mother's name */
    public String getMotherName() {
        return motherName;
    }

    /** @param motherName sets the mother's name */
    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    /** @return the CNPJ of the client */
    public String getCnpj() {
        return cnpj;
    }

    /** @param cnpj sets the CNPJ of the client */
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    /** @return the client's gender */
    public String getGenre() {
        return genre;
    }

    /** @param genre sets the client's gender */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /** @return the record creation date */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** @param createdAt sets the record creation date */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /** @return the list of contract accounts linked to this client */
    public List<ContractAccount> getContractAccounts() {
        return contractAccounts;
    }

    /** @param contractAccounts sets the list of associated contract accounts */
    public void setContractAccounts(List<ContractAccount> contractAccounts) {
        this.contractAccounts = contractAccounts;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", genre='" + genre + '\'' +
                '}';
    }
}
