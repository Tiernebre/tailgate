package com.tiernebre.tailgate.session;

import com.tiernebre.tailgate.session.CreateSessionRequest;
import com.tiernebre.tailgate.token.InvalidCreateAccessTokenRequestException;
import com.tiernebre.tailgate.validator.Validator;

public interface SessionValidator extends Validator<CreateSessionRequest, InvalidCreateAccessTokenRequestException> {
}
