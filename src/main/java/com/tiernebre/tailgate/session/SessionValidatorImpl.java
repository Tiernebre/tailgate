package com.tiernebre.tailgate.session;

import com.tiernebre.tailgate.validator.BaseValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.util.Set;

@Component
public class SessionValidatorImpl extends BaseValidator implements SessionValidator {
    final static String INVALID_REFRESH_TOKEN_REQUEST_ERROR = "The request to refresh a session had a null or blank refresh token. Please use a non-null and non-blank refresh token.";

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

    @Override
    public void validateRefreshToken(String refreshToken) throws InvalidRefreshSessionRequestException {
        if (StringUtils.isBlank(refreshToken)) {
            throw new InvalidRefreshSessionRequestException(INVALID_REFRESH_TOKEN_REQUEST_ERROR);
        }
    }
}
