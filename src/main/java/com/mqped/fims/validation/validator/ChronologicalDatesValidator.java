package com.mqped.fims.validation.validator;

import com.mqped.fims.validation.annotation.ChronologicalDates;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

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
        if (value == null) return true;

        try {
            Field start = value.getClass().getDeclaredField(startField);
            Field end = value.getClass().getDeclaredField(endField);
            start.setAccessible(true);
            end.setAccessible(true);

            Object startValue = start.get(value);
            Object endValue = end.get(value);

            if (!(startValue instanceof LocalDateTime) || !(endValue instanceof LocalDateTime)) {
                return true; // skip if not LocalDateTime
            }

            LocalDateTime startDate = (LocalDateTime) startValue;
            LocalDateTime endDate = (LocalDateTime) endValue;

            if (startDate == null || endDate == null) {
                return true; // handled by @NotNull elsewhere
            }

            return allowEqual ? !endDate.isBefore(startDate) : endDate.isAfter(startDate);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            // misconfiguration, skip validation
            return true;
        }
    }
}
