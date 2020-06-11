package com.tiernebre.tailgate.user.validator;

import com.tiernebre.tailgate.security_questions.SecurityQuestionService;
import com.tiernebre.tailgate.user.dto.CreateUserRequest;
import com.tiernebre.tailgate.user.dto.CreateUserSecurityQuestionRequest;
import com.tiernebre.tailgate.validator.BaseValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

import static com.tiernebre.tailgate.user.validator.UserValidatorImpl.NON_EXISTENT_SECURITY_QUESTIONS_ERROR_MESSAGE;

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
                .filter(Objects::nonNull)
                .map(this::validateCommon)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        foundErrors.addAll(errorsWithEntries);
        return foundErrors;
    }
}
