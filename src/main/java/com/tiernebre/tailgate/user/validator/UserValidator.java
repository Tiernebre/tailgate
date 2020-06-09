package com.tiernebre.tailgate.user.validator;

import com.tiernebre.tailgate.user.dto.CreateUserRequest;
import com.tiernebre.tailgate.user.exception.InvalidUserException;
import com.tiernebre.tailgate.validator.Validator;

public interface UserValidator extends Validator<CreateUserRequest, InvalidUserException> {
}
