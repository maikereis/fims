package com.mqped.fims.validation.validator;
import com.mqped.fims.model.entity.Client;
import com.mqped.fims.validation.annotation.CpfOrCnpjRequired;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfOrCnpjRequiredValidator implements ConstraintValidator<CpfOrCnpjRequired, Client> {

    @Override
    public boolean isValid(Client client, ConstraintValidatorContext context) {
        if (client == null) return true; // let @NotNull handle this elsewhere

        boolean hasCpf = client.getCpf() != null && !client.getCpf().isBlank();
        boolean hasCnpj = client.getCnpj() != null && !client.getCnpj().isBlank();

        return hasCpf || hasCnpj;
    }
}
