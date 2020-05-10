package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.validator.BaseValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.util.Set;

@Component
public class AccessAccessTokenValidatorImpl extends BaseValidator implements AccessTokenValidator {
    @Autowired
    public AccessAccessTokenValidatorImpl(
            Validator validator
    ) {
        super(validator);
    }

    @Override
    public void validate(CreateAccessTokenRequest valueToValidate) throws InvalidCreateAccessTokenRequestException {
        Set<String> errorsFound = validateCommon(valueToValidate);
        if (CollectionUtils.isNotEmpty(errorsFound)) {
            throw new InvalidCreateAccessTokenRequestException(errorsFound);
        }
    }
}
