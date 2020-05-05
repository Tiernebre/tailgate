package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.validator.BaseValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.util.Set;

@Component
public class TokenValidatorImpl extends BaseValidator implements TokenValidator {
    @Autowired
    public TokenValidatorImpl(
            Validator validator
    ) {
        super(validator);
    }

    @Override
    public void validate(CreateTokenRequest valueToValidate) throws InvalidCreateTokenRequestException {
        Set<String> errorsFound = validateCommon(valueToValidate);
        if (CollectionUtils.isNotEmpty(errorsFound)) {
            throw new InvalidCreateTokenRequestException(errorsFound);
        }
    }
}
