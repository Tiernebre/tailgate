package com.tiernebre.tailgate.user.validator;

import com.tiernebre.tailgate.user.CreateUserRequest;
import com.tiernebre.tailgate.user.InvalidUserException;
import com.tiernebre.tailgate.validator.Validator;

public interface UserValidator extends Validator<CreateUserRequest, InvalidUserException> {
}
