package com.mqped.fims.model.enums;

/**
 * Enumeration representing the operational status of a contract account
 * or installation within the Field Inspection Management System (FIMS).
 * <p>
 * Each {@code StatusType} indicates the current service state, typically
 * reflecting whether the service is active, temporarily interrupted, or
 * fully deactivated.
 * </p>
 *
 * <h2>Defined Statuses</h2>
 * <ul>
 * <li>{@link #ON} — Service is currently active and operational.</li>
 * <li>{@link #CUT} — Service is temporarily interrupted or disconnected.</li>
 * <li>{@link #OFF} — Service is fully deactivated or permanently
 * disconnected.</li>
 * </ul>
 *
 * <h2>Example</h2>
 * 
 * <pre>{@code
 * if (contractAccount.getStatus() == StatusType.CUT) {
 *     notificationService.alertCustomer(contractAccount);
 * }
 * }</pre>
 */
public enum StatusType {

    /** Indicates that the service is active and operational. */
    ON,

    /**
     * Indicates that the service has been temporarily interrupted or disconnected.
     */
    CUT,

    /** Indicates that the service is fully deactivated or turned off. */
    OFF
}
