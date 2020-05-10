package com.tiernebre.tailgate.session;

import com.tiernebre.tailgate.validator.BaseValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.util.Set;

@Component
public class SessionValidatorImpl extends BaseValidator implements SessionValidator {
    @Autowired
    public SessionValidatorImpl(
            Validator validator
    ) {
        super(validator);
    }

    @Override
    public void validate(CreateSessionRequest valueToValidate) throws InvalidCreateSessionRequestException {
        Set<String> errorsFound = validateCommon(valueToValidate);
        if (CollectionUtils.isNotEmpty(errorsFound)) {
            throw new InvalidCreateSessionRequestException(errorsFound);
        }
    }
}
