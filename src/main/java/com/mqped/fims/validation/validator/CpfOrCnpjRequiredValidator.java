package com.mqped.fims.validation.validator;

import com.mqped.fims.model.entity.Client;
import com.mqped.fims.validation.annotation.CpfOrCnpjRequired;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Ensures that a {@link Client} entity has at least one of CPF or CNPJ
 * provided.
 * <p>
 * If both are missing or blank, validation fails.
 * This validator does not validate CPF/CNPJ format — only their presence.
 * Use {@code @CPF} or {@code @CNPJ} annotations for format validation.
 */
public class CpfOrCnpjRequiredValidator implements ConstraintValidator<CpfOrCnpjRequired, Client> {

    @Override
    public boolean isValid(Client client, ConstraintValidatorContext context) {
        if (client == null) return true; // let @NotNull handle this elsewhere

        boolean hasCpf = client.getCpf() != null && !client.getCpf().isBlank();
        boolean hasCnpj = client.getCnpj() != null && !client.getCnpj().isBlank();

        return hasCpf || hasCnpj;
    }
}
