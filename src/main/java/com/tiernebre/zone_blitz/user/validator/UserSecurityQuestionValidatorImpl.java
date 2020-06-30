package com.tiernebre.zone_blitz.user.validator;

import com.tiernebre.zone_blitz.security_questions.SecurityQuestionService;
import com.tiernebre.zone_blitz.user.dto.CreateUserRequest;
import com.tiernebre.zone_blitz.user.dto.CreateUserSecurityQuestionRequest;
import com.tiernebre.zone_blitz.validator.BaseValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

import static com.tiernebre.zone_blitz.user.validator.UserValidationConstants.*;
import static com.tiernebre.zone_blitz.user.validator.UserValidatorImpl.NON_EXISTENT_SECURITY_QUESTIONS_ERROR_MESSAGE;

@Component
public class UserSecurityQuestionValidatorImpl extends BaseValidator implements UserSecurityQuestionValidator {
    private final SecurityQuestionService securityQuestionService;

    @Autowired
    public UserSecurityQuestionValidatorImpl(
            Validator validator,
            SecurityQuestionService securityQuestionService
    ) {
        super(validator);
        this.securityQuestionService = securityQuestionService;
    }

    @Override
    public Set<String> validate(CreateUserRequest createUserRequest) {
        Collection<CreateUserSecurityQuestionRequest> securityQuestionsToValidate = createUserRequest.getSecurityQuestions();
        if (CollectionUtils.isEmpty(securityQuestionsToValidate)) return Collections.emptySet();

        Set<String> foundErrors = validateSecurityQuestionsExist(securityQuestionsToValidate);
        foundErrors.addAll(validateSecurityQuestionDtosAreValid(securityQuestionsToValidate));
        foundErrors.addAll(validateSecurityQuestionsDoNotHaveDuplicateInformation(createUserRequest));
        return foundErrors;
    }

    private Set<String> validateSecurityQuestionsExist(Collection<CreateUserSecurityQuestionRequest> securityQuestionRequests) {
        Set<Long> securityQuestionIds = securityQuestionRequests
                .stream()
                .filter(Objects::nonNull)
                .map(CreateUserSecurityQuestionRequest::getId)
                .collect(Collectors.toSet());
        Set<String> foundErrors = new HashSet<>();
        if (securityQuestionService.someDoNotExistWithIds(securityQuestionIds)) {
            foundErrors.add(NON_EXISTENT_SECURITY_QUESTIONS_ERROR_MESSAGE);
        }
        return foundErrors;
    }

    private Set<String> validateSecurityQuestionDtosAreValid(Collection<CreateUserSecurityQuestionRequest> securityQuestionRequests) {
        return securityQuestionRequests.stream()
                .filter(Objects::nonNull)
                .map(this::validateCommon)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<String> validateSecurityQuestionsDoNotHaveDuplicateInformation(
            CreateUserRequest createUserRequest
    ) {
        Set<String> foundErrors = new HashSet<>();
        Set<String> uniqueSecurityQuestionAnswers = createUserRequest.getSecurityQuestions().stream()
                .map(CreateUserSecurityQuestionRequest::getAnswer)
                .filter(StringUtils::isNotBlank)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        Set<Long> uniqueSecurityQuestionIds = createUserRequest.getSecurityQuestions().stream()
                .map(CreateUserSecurityQuestionRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (uniqueSecurityQuestionAnswers.size() < NUMBER_OF_ALLOWED_SECURITY_QUESTIONS) {
            foundErrors.add(SAME_SECURITY_QUESTION_ANSWERS_VALIDATION_MESSAGE);
        }
        Set<String> userInformation = Set.of(
                createUserRequest.getEmail().toLowerCase(),
                createUserRequest.getPassword().toLowerCase()
        );
        if (!Collections.disjoint(uniqueSecurityQuestionAnswers, userInformation)) {
            foundErrors.add(SECURITY_QUESTION_ANSWERS_CANNOT_DUPLICATE_SENSITIVE_INFORMATION);
        }
        if (uniqueSecurityQuestionIds.size() != createUserRequest.getSecurityQuestions().size()) {
            foundErrors.add(SAME_SECURITY_QUESTIONS_VALIDATION_MESSAGE);
        }
        return foundErrors;
    }
}
