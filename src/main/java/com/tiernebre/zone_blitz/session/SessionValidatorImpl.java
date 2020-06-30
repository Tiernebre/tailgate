package com.tiernebre.zone_blitz.session;

import com.tiernebre.zone_blitz.validator.BaseValidator;
import com.tiernebre.zone_blitz.validator.StringValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.util.Objects;
import java.util.Set;

@Component
public class SessionValidatorImpl extends BaseValidator implements SessionValidator {
    final static String INVALID_REFRESH_TOKEN_REQUEST_ERROR = "The request to refresh a session had a null or blank refresh token. Please use a non-null and non-blank refresh token.";
    final static String NULL_CREATE_SESSION_REQUEST_ERROR_MESSAGE = "The request to create a session must not be null.";

    @Autowired
    public SessionValidatorImpl(
            Validator validator
    ) {
        super(validator);
    }

    @Override
    public void validate(CreateSessionRequest valueToValidate) throws InvalidCreateSessionRequestException {
        Objects.requireNonNull(valueToValidate, NULL_CREATE_SESSION_REQUEST_ERROR_MESSAGE);
        Set<String> errorsFound = validateCommon(valueToValidate);
        if (CollectionUtils.isNotEmpty(errorsFound)) {
            throw new InvalidCreateSessionRequestException(errorsFound);
        }
    }

    @Override
    public void validateRefreshToken(String refreshToken) {
        StringValidator.requireNonBlank(refreshToken, INVALID_REFRESH_TOKEN_REQUEST_ERROR);
    }
}
