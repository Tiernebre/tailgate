package com.tiernebre.tailgate.session;

import com.tiernebre.tailgate.token.InvalidCreateSessionRequestException;
import com.tiernebre.tailgate.validator.Validator;

public interface SessionValidator extends Validator<CreateSessionRequest, InvalidCreateSessionRequestException> {
}
