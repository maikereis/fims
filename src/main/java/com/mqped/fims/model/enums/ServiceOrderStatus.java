package com.mqped.fims.model.enums;

/**
 * Enumeration representing the possible statuses of a service order.
 * <p>
 * A {@code ServiceOrderStatus} defines the current lifecycle stage of a
 * {@link com.mqped.fims.model.entity.ServiceOrder ServiceOrder}, from creation
 * to completion.
 * </p>
 *
 * <h2>Defined Statuses</h2>
 * <ul>
 * <li>{@link #CREATED} — The service order has been created but not yet
 * started.</li>
 * <li>{@link #IN_EXECUTION} — The service order is currently being
 * executed.</li>
 * <li>{@link #EXECUTED} — The service order has been completed
 * successfully.</li>
 * <li>{@link #CANCELED} — The service order was canceled and will not
 * proceed.</li>
 * </ul>
 *
 * <h2>Utility Methods</h2>
 * <ul>
 * <li>{@link #isFinal()} — Returns {@code true} if the service order is in a
 * final state
 * (either {@link #EXECUTED} or {@link #CANCELED}).</li>
 * <li>{@link #isActive()} — Returns {@code true} if the service order is in an
 * active state
 * (either {@link #CREATED} or {@link #IN_EXECUTION}).</li>
 * </ul>
 *
 * <h2>Example</h2>
 * 
 * <pre>{@code
 * if (order.getStatus().isFinal()) {
 *     archive(order);
 * }
 * }</pre>
 */
public enum ServiceOrderStatus {

    /** Service order has been created but execution has not started. */
    CREATED,

    /** Service order is currently in progress. */
    IN_EXECUTION,

    /** Service order execution completed successfully. */
    EXECUTED,

    /** Service order has been canceled and will not continue. */
    CANCELED;

    /**
     * Determines whether this status represents a final state.
     * <p>
     * Final states are:
     * <ul>
     * <li>{@link #EXECUTED}</li>
     * <li>{@link #CANCELED}</li>
     * </ul>
     *
     * @return {@code true} if this status is final; {@code false} otherwise.
     */
    public boolean isFinal() {
        return this == EXECUTED || this == CANCELED;
    }

    /**
     * Determines whether this status represents an active state.
     * <p>
     * Active states are:
     * <ul>
     * <li>{@link #CREATED}</li>
     * <li>{@link #IN_EXECUTION}</li>
     * </ul>
     *
     * @return {@code true} if this status is active; {@code false} otherwise.
     */
    public boolean isActive() {
        return this == CREATED || this == IN_EXECUTION;
    }
}
