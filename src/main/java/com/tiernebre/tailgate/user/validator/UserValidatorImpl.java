package com.tiernebre.tailgate.user.validator;

import com.tiernebre.tailgate.security_questions.SecurityQuestionService;
import com.tiernebre.tailgate.user.dto.CreateUserRequest;
import com.tiernebre.tailgate.user.dto.CreateUserSecurityQuestionRequest;
import com.tiernebre.tailgate.user.exception.InvalidUserException;
import com.tiernebre.tailgate.validator.BaseValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserValidatorImpl extends BaseValidator implements UserValidator {
    static final String NULL_CREATE_USER_REQUEST_ERROR_MESSAGE = "The request to create a user must not be null.";
    static final String NON_EXISTENT_SECURITY_QUESTIONS_ERROR_MESSAGE = "Some of the security question ids provided do not exist.";

    private final UserPasswordValidator passwordValidator;
    private final SecurityQuestionService securityQuestionService;

    @Autowired
    public UserValidatorImpl(
            Validator validator,
            UserPasswordValidator passwordValidator,
            SecurityQuestionService securityQuestionService
    ) {
        super(validator);
        this.passwordValidator = passwordValidator;
        this.securityQuestionService = securityQuestionService;
    }

    @Override
    public void validate(CreateUserRequest createUserRequest) throws InvalidUserException {
        Objects.requireNonNull(createUserRequest, NULL_CREATE_USER_REQUEST_ERROR_MESSAGE);
        Set<String> errorsFound = validateCommon(createUserRequest);
        Set<String> passwordErrors = passwordValidator.validate(createUserRequest.getPassword(), createUserRequest.getConfirmationPassword());
        errorsFound.addAll(passwordErrors);
        errorsFound.addAll(validateSecurityQuestions(createUserRequest));
        if (CollectionUtils.isNotEmpty(errorsFound)) {
            throw new InvalidUserException(errorsFound);
        }
    }

    private Set<String> validateSecurityQuestions(CreateUserRequest createUserRequest) {
        Collection<CreateUserSecurityQuestionRequest> securityQuestionsToValidate = createUserRequest.getSecurityQuestions();
        if (CollectionUtils.isEmpty(securityQuestionsToValidate)) return Collections.emptySet();

        Set<Long> securityQuestionIds = securityQuestionsToValidate
                .stream()
                .filter(Objects::nonNull)
                .map(CreateUserSecurityQuestionRequest::getId)
                .collect(Collectors.toSet());
        Set<String> foundErrors = new HashSet<>();
        if (securityQuestionService.someDoNotExistWithIds(securityQuestionIds)) {
            foundErrors.add(NON_EXISTENT_SECURITY_QUESTIONS_ERROR_MESSAGE);
        }

        Set<String> errorsWithEntries = securityQuestionsToValidate.stream()
                .map(this::validateCommon)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        foundErrors.addAll(errorsWithEntries);
        return foundErrors;
    }
}
