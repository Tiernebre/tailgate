package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.validator.Validator;

public interface TokenValidator extends Validator<CreateTokenRequest, InvalidCreateTokenRequestException> {
}
