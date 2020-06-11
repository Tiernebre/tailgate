package com.tiernebre.tailgate.user.validator;

import com.tiernebre.tailgate.user.dto.CreateUserRequest;
import com.tiernebre.tailgate.user.exception.InvalidUserException;
import com.tiernebre.tailgate.validator.BaseValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.util.Objects;
import java.util.Set;

@Component
public class UserValidatorImpl extends BaseValidator implements UserValidator {
    static final String NULL_CREATE_USER_REQUEST_ERROR_MESSAGE = "The request to create a user must not be null.";
    static final String NON_EXISTENT_SECURITY_QUESTIONS_ERROR_MESSAGE = "Some of the security question ids provided do not exist.";

    private final UserPasswordValidator passwordValidator;

    @Autowired
    public UserValidatorImpl(
            Validator validator,
            UserPasswordValidator passwordValidator
    ) {
        super(validator);
        this.passwordValidator = passwordValidator;
    }

    @Override
    public void validate(CreateUserRequest createUserRequest) throws InvalidUserException {
        Objects.requireNonNull(createUserRequest, NULL_CREATE_USER_REQUEST_ERROR_MESSAGE);
        Set<String> errorsFound = validateCommon(createUserRequest);
        Set<String> passwordErrors = passwordValidator.validate(createUserRequest.getPassword(), createUserRequest.getConfirmationPassword());
        errorsFound.addAll(passwordErrors);
        if (CollectionUtils.isNotEmpty(errorsFound)) {
            throw new InvalidUserException(errorsFound);
        }
    }
}
