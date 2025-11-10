package com.mqped.fims.model.enums;

/**
 * Enumeration representing the source or method by which a {@code Target}
 * was identified or generated within the Field Inspection Management System
 * (FIMS).
 * <p>
 * Each {@code TargetType} indicates the origin of a service target, helping
 * differentiate whether it was produced automatically, reported by a user,
 * or derived from predefined rules or customer complaints.
 * </p>
 *
 * <h2>Defined Target Types</h2>
 * <ul>
 * <li>{@link #MACHINE_LEARNING} — Target identified automatically using
 * predictive or analytical models.</li>
 * <li>{@link #METER_READER_INDICATION} — Target reported manually by a meter
 * reader or field agent.</li>
 * <li>{@link #RULES} — Target generated based on business or operational
 * rules.</li>
 * <li>{@link #COMPLAINTS} — Target created in response to a customer or
 * internal complaint.</li>
 * </ul>
 *
 * <h2>Example</h2>
 * 
 * <pre>{@code
 * if (target.getType() == TargetType.MACHINE_LEARNING) {
 *     aiService.logDetection(target);
 * }
 * }</pre>
 */
public enum TargetType {

    /** Target identified automatically using predictive or analytical models. */
    MACHINE_LEARNING,

    /** Target reported manually by a meter reader or field agent. */
    METER_READER_INDICATION,

    /** Target generated based on business or operational rules. */
    RULES,

    /** Target created in response to a customer or internal complaint. */
    COMPLAINTS
}
