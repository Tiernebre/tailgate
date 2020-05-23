package com.tiernebre.tailgate.session;

import com.tiernebre.tailgate.validator.Validator;

public interface SessionValidator extends Validator<CreateSessionRequest, InvalidCreateSessionRequestException> {
    /**
     * Validates that a given refresh token is legitimate.
     *
     * Does nothing if the refresh token is valid. Otherwise, throws an exception.
     * @param refreshToken The refresh token to validate.
     */
    void validateRefreshToken(String refreshToken);
}
