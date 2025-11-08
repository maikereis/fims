package com.mqped.fims.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = com.mqped.fims.validation.validator.ChronologicalDatesValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ChronologicalDates.List.class)
public @interface ChronologicalDates {

    String message() default "{validation.dates.order.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    /** Start date field name */
    String start();

    /** End date field name */
    String end();

    /** Whether equality is allowed (default true) */
    boolean allowEqual() default true;

    /**
     * Container annotation to support multiple ChronologicalDates on the same class
     */
    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        ChronologicalDates[] value();
    }
}
