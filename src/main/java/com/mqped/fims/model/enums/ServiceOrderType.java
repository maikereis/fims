package com.mqped.fims.model.enums;

/**
 * Enumeration representing the possible types of service orders
 * handled by the Field Inspection Management System (FIMS).
 * <p>
 * Each {@code ServiceOrderType} defines the operational purpose or action
 * associated with a service order, indicating the type of task that must
 * be performed in the field.
 * </p>
 *
 * <h2>Defined Types</h2>
 * <ul>
 * <li>{@link #IS_ON} — Service order to activate or reconnect service.</li>
 * <li>{@link #IS_CUT} — Service order to interrupt or disconnect service.</li>
 * <li>{@link #IS_OFF} — Service order to verify or confirm service is off.</li>
 * </ul>
 *
 * <h2>Example</h2>
 * 
 * <pre>{@code
 * if (order.getType() == ServiceOrderType.IS_CUT) {
 *     serviceExecutor.scheduleDisconnection(order);
 * }
 * }</pre>
 */
public enum ServiceOrderType {

    /**
     * Indicates a service order to activate or reconnect the customer's service.
     */
    IS_ON,

    /** Indicates a service order to cut or disconnect the customer's service. */
    IS_CUT,

    /**
     * Indicates a service order to verify that the service is inactive or turned
     * off.
     */
    IS_OFF
}
