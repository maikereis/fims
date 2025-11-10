package com.mqped.fims.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Annotation for validating that two date fields within the same object
 * are in correct chronological order — e.g., {@code startDate <= endDate}.
 *
 * <p>
 * Common use cases include validating start and end timestamps
 * in entities like contracts, events, or service orders.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * 
 * <pre>{@code
 * @ChronologicalDates(start = "startDate", end = "endDate", allowEqual = false)
 * public class ServicePeriod {
 *     private LocalDate startDate;
 *     private LocalDate endDate;
 * }
 * }</pre>
 *
 * <h3>Validator Contract:</h3>
 * The validation logic is implemented by
 * {@link com.mqped.fims.validation.validator.ChronologicalDatesValidator}.
 * It uses reflection to access the two fields and compares their values
 * if both are non-null.
 *
 * <h3>Default Behavior:</h3>
 * <ul>
 * <li>Equality is allowed ({@code allowEqual = true})</li>
 * <li>Constraint applies at the class level ({@code @Target(TYPE)})</li>
 * <li>Supports multiple constraints on the same class via
 * {@code @ChronologicalDates.List}</li>
 * </ul>
 *
 * <p>
 * To override the default validation message, add a key to your
 * <code>ValidationMessages.properties</code>:
 * 
 * <pre>{@code
 * validation.dates.order.invalid = Start date must be before end date
 * }</pre>
 * </p>
 *
 * @see com.mqped.fims.validation.validator.ChronologicalDatesValidator
 * @since 1.0
 */
@Documented
@Constraint(validatedBy = com.mqped.fims.validation.validator.ChronologicalDatesValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ChronologicalDates.List.class)
public @interface ChronologicalDates {

    /** Message key or literal used when validation fails. */
    String message() default "{validation.dates.order.invalid}";

    /** Validation groups (part of the Jakarta Bean Validation API). */
    Class<?>[] groups() default {};

    /** Payload type for clients of the Bean Validation API. */
    Class<? extends Payload>[] payload() default {};

    /** The name of the field representing the start date. */
    String start();

    /** The name of the field representing the end date. */
    String end();

    /** Whether the start and end date can be equal (default: true). */
    boolean allowEqual() default true;

    /**
     * Container annotation that allows multiple {@link ChronologicalDates}
     * constraints on the same type.
     */
    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        ChronologicalDates[] value();
    }
}
