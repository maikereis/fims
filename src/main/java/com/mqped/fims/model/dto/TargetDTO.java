package com.mqped.fims.model.dto;

import java.time.LocalDateTime;

import com.mqped.fims.model.entity.Target;
import com.mqped.fims.model.enums.TargetType;

/**
 * Data Transfer Object (DTO) representing a {@link Target} entity.
 * <p>
 * The {@code TargetDTO} encapsulates data related to operational or analytical
 * targets linked to a {@code ContractAccount}, typically representing service,
 * inspection, or analysis objectives in the FIMS domain model.
 * </p>
 *
 * <h2>Fields Overview</h2>
 * <ul>
 * <li><b>id</b> — unique identifier of the target</li>
 * <li><b>contractAccountId</b> — associated contract account ID</li>
 * <li><b>contractAccountNumber</b> — external account number for reference</li>
 * <li><b>type</b> — category or nature of the target ({@link TargetType})</li>
 * <li><b>signature</b> — identifying signature or hash for uniqueness</li>
 * <li><b>score</b> — calculated score or priority metric</li>
 * <li><b>expectedCNR</b> — expected consumption-to-revenue ratio</li>
 * <li><b>expectedTicket</b> — expected ticket or invoice amount</li>
 * <li><b>distanceFromBase</b> — physical distance from a base location (in
 * km)</li>
 * <li><b>createdAt</b> — timestamp when the target was created</li>
 * <li><b>active</b> — indicates whether the target is currently active</li>
 * </ul>
 *
 * <h2>Example Usage</h2>
 * 
 * <pre>{@code
 * Target target = targetService.findById(1);
 * TargetDTO dto = TargetDTO.fromEntity(target);
 * System.out.println(dto.getType());
 * }</pre>
 */
public class TargetDTO {

    /** Unique identifier for the target. */
    private Integer id;

    /** ID of the associated contract account. */
    private Integer contractAccountId;

    /** Contract account number for display or reference. */
    private String contractAccountNumber;

    /** Type of target (e.g., inspection, analysis, intervention). */
    private TargetType type;

    /** Unique signature or identifier string for the target. */
    private String signature;

    /** Calculated score or weight assigned to the target. */
    private Double score;

    /** Expected consumption-to-revenue ratio (CNR). */
    private Double expectedCNR;

    /** Expected financial ticket or invoice value. */
    private Double expectedTicket;

    /** Distance from operational base, typically in kilometers. */
    private Double distanceFromBase;

    /** Timestamp when the target record was created. */
    private LocalDateTime createdAt;

    /** Indicates if the target is active or deactivated. */
    private Boolean active;

    // Getters and setters

    /** @return unique identifier of the target */
    public Integer getId() {
        return id;
    }

    /** @param id unique identifier to set */
    public void setId(Integer id) {
        this.id = id;
    }

    /** @return associated contract account ID */
    public Integer getContractAccountId() {
        return contractAccountId;
    }

    /** @param contractAccountId contract account ID to set */
    public void setContractAccountId(Integer contractAccountId) {
        this.contractAccountId = contractAccountId;
    }

    /** @return contract account number */
    public String getContractAccountNumber() {
        return contractAccountNumber;
    }

    /** @param contractAccountNumber contract account number to set */
    public void setContractAccountNumber(String contractAccountNumber) {
        this.contractAccountNumber = contractAccountNumber;
    }

    /** @return target type */
    public TargetType getType() {
        return type;
    }

    /** @param type target type to set */
    public void setType(TargetType type) {
        this.type = type;
    }

    /** @return target signature */
    public String getSignature() {
        return signature;
    }

    /** @param signature unique signature to set */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /** @return target score */
    public Double getScore() {
        return score;
    }

    /** @param score score value to set */
    public void setScore(Double score) {
        this.score = score;
    }

    /** @return expected CNR value */
    public Double getExpectedCNR() {
        return expectedCNR;
    }

    /** @param expectedCNR expected CNR value to set */
    public void setExpectedCNR(Double expectedCNR) {
        this.expectedCNR = expectedCNR;
    }

    /** @return expected ticket or invoice amount */
    public Double getExpectedTicket() {
        return expectedTicket;
    }

    /** @param expectedTicket expected ticket value to set */
    public void setExpectedTicket(Double expectedTicket) {
        this.expectedTicket = expectedTicket;
    }

    /** @return distance from base (in km) */
    public Double getDistanceFromBase() {
        return distanceFromBase;
    }

    /** @param distanceFromBase distance value to set (in km) */
    public void setDistanceFromBase(Double distanceFromBase) {
        this.distanceFromBase = distanceFromBase;
    }

    /** @return timestamp when target was created */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** @param createdAt creation timestamp to set */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /** @return {@code true} if the target is active, otherwise {@code false} */
    public Boolean getActive() {
        return active;
    }

    /** @param active whether the target is active */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * Maps a {@link Target} entity to its corresponding {@link TargetDTO}.
     *
     * @param target the {@link Target} entity to convert (must not be {@code null})
     * @return a populated {@link TargetDTO} instance
     */
    public static TargetDTO fromEntity(Target target) {
        TargetDTO dto = new TargetDTO();
        dto.setId(target.getId());
        dto.setType(target.getType());
        dto.setSignature(target.getSignature());
        dto.setScore(target.getScore());
        dto.setExpectedCNR(target.getExpectedCNR());
        dto.setExpectedTicket(target.getExpectedTicket());
        dto.setDistanceFromBase(target.getDistanceFromBase());
        dto.setCreatedAt(target.getCreatedAt());
        dto.setActive(target.getActive());

        if (target.getContractAccount() != null) {
            dto.setContractAccountId(target.getContractAccount().getId());
            dto.setContractAccountNumber(target.getContractAccount().getAccountNumber());
        }

        return dto;
    }
}
