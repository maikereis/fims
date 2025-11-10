package com.mqped.fims.validation.annotation;

import com.mqped.fims.validation.validator.CpfOrCnpjRequiredValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Class-level validation annotation ensuring that at least one of
 * the {@code cpf} or {@code cnpj} fields in a class is provided (non-null and
 * non-empty).
 *
 * <p>
 * Typically used on client or person entities where either a CPF or
 * a CNPJ must exist, but not necessarily both.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * 
 * <pre>{@code
 * @CpfOrCnpjRequired
 * public class ClientDTO {
 *     private String cpf;
 *     private String cnpj;
 * }
 * }</pre>
 *
 * <p>
 * The validation logic is implemented in
 * {@link com.mqped.fims.validation.validator.CpfOrCnpjRequiredValidator},
 * which inspects the "cpf" and "cnpj" fields reflectively.
 * </p>
 *
 * <p>
 * To override the validation message, define a key in
 * <code>ValidationMessages.properties</code>:
 * 
 * <pre>{@code
 * validation.cpf.or.cnpj.required = Either CPF or CNPJ must be provided.
 * }</pre>
 * </p>
 *
 * @see CpfOrCnpjRequiredValidator
 * @since 1.0
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CpfOrCnpjRequiredValidator.class)
@Documented
public @interface CpfOrCnpjRequired {

    /** Message shown when neither CPF nor CNPJ is provided. */
    String message() default "{validation.cpf.or.cnpj.required}";

    /** Validation groups for Jakarta Bean Validation. */
    Class<?>[] groups() default {};

    /** Payload for clients of the Jakarta Bean Validation API. */
    Class<? extends Payload>[] payload() default {};
}
