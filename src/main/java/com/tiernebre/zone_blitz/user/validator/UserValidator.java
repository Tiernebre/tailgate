package com.tiernebre.zone_blitz.user.validator;

import com.tiernebre.zone_blitz.user.dto.CreateUserRequest;
import com.tiernebre.zone_blitz.user.exception.InvalidUserException;
import com.tiernebre.zone_blitz.validator.Validator;

public interface UserValidator extends Validator<CreateUserRequest, InvalidUserException> {
}
