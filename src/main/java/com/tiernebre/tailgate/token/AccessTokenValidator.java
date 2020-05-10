package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.validator.Validator;

public interface AccessTokenValidator extends Validator<CreateSessionRequest, InvalidCreateAccessTokenRequestException> {
}
