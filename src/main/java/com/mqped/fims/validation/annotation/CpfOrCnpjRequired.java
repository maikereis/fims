package com.mqped.fims.validation.annotation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

import com.mqped.fims.validation.validator.CpfOrCnpjRequiredValidator;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CpfOrCnpjRequiredValidator.class)
@Documented
public @interface CpfOrCnpjRequired {
    String message() default "Either CPF or CNPJ must be provided.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
