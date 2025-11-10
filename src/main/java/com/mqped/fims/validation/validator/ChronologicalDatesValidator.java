package com.mqped.fims.validation.validator;

import com.mqped.fims.validation.annotation.ChronologicalDates;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * Validates that two date-time fields in a class are in chronological order,
 * based on the {@link ChronologicalDates} annotation.
 *
 * <p>
 * Example:
 * </p>
 * 
 * <pre>{@code
 * @ChronologicalDates(start = "createdAt", end = "updatedAt", allowEqual = true)
 * public class EntityDTO {
 *     private LocalDateTime createdAt;
 *     private LocalDateTime updatedAt;
 * }
 * }</pre>
 *
 * <p>
 * The validation passes if:
 * <ul>
 * <li>Both dates are null (other constraints handle nullability)</li>
 * <li>End date is equal to or after start date (depending on
 * {@code allowEqual})</li>
 * <li>The fields cannot be found (treated as misconfiguration → validation
 * skipped)</li>
 * </ul>
 * </p>
 */
public class ChronologicalDatesValidator implements ConstraintValidator<ChronologicalDates, Object> {

    private String startField;
    private String endField;
    private boolean allowEqual;

    @Override
    public void initialize(ChronologicalDates constraintAnnotation) {
        this.startField = constraintAnnotation.start();
        this.endField = constraintAnnotation.end();
        this.allowEqual = constraintAnnotation.allowEqual();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null)
            return true; // null object → skip validation

        try {
            Field start = getField(value, startField);
            Field end = getField(value, endField);

            LocalDateTime startDate = getLocalDateTime(start, value);
            LocalDateTime endDate = getLocalDateTime(end, value);

            // Null fields should be handled elsewhere (e.g., @NotNull)
            if (startDate == null || endDate == null)
                return true;

            // Chronological check
            return allowEqual ? !endDate.isBefore(startDate) : endDate.isAfter(startDate);

        } catch (ReflectiveOperationException e) {
            // Misconfiguration (field not found or inaccessible)
            // Log a warning in real projects (via SLF4J)
            return true;
        }
    }

    /**
     * Fetches a declared field and ensures it’s accessible.
     */
    private Field getField(Object target, String name) throws NoSuchFieldException {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    /**
     * Safely retrieves a LocalDateTime value from a field.
     */
    private LocalDateTime getLocalDateTime(Field field, Object target) throws IllegalAccessException {
        Object value = field.get(target);
        return (value instanceof LocalDateTime) ? (LocalDateTime) value : null;
    }
}
