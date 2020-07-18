package com.tiernebre.zone_blitz.user.validator;

import com.tiernebre.zone_blitz.security_questions.SecurityQuestionService;
import com.tiernebre.zone_blitz.test.AbstractIntegrationTestingSuite;
import com.tiernebre.zone_blitz.user.dto.CreateUserRequest;
import com.tiernebre.zone_blitz.user.dto.CreateUserSecurityQuestionRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;

import java.util.*;

import static com.tiernebre.zone_blitz.user.validator.UserValidationConstants.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MockBeans({
        @MockBean(SecurityQuestionService.class)
})
public class UserSecurityQuestionValidatorImplIntegrationTests extends AbstractIntegrationTestingSuite {
    @Autowired
    private UserSecurityQuestionValidatorImpl userSecurityQuestionValidator;

    @Nested
    @DisplayName("validate")
    public class ValidateTests {
        @Test
        @DisplayName("does not allow security questions with null ids")
        void doesNotAllowSecurityQuestionsWithNullIds() {
            List<CreateUserSecurityQuestionRequest> securityQuestionRequests = ImmutableList.of(
                    CreateUserSecurityQuestionRequest.builder().id(null).build()
            );
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .email(UUID.randomUUID().toString())
                    .password(UUID.randomUUID().toString())
                    .securityQuestions(securityQuestionRequests)
                    .build();
            Set<String> foundErrors = userSecurityQuestionValidator.validate(createUserRequest);
            assertTrue(foundErrors.contains(NULL_SECURITY_QUESTION_ID_VALIDATION_MESSAGE));
        }

        @ParameterizedTest(name = "does not allow security questions with answer = \"{0}\"")
        @NullSource
        @EmptySource
        @ValueSource(strings = {" "})
        void doesNotAllowSecurityQuestionsWithBlankAnswer(String answer) {
            List<CreateUserSecurityQuestionRequest> securityQuestionRequests = ImmutableList.of(
                    CreateUserSecurityQuestionRequest.builder()
                            .id(null)
                            .answer(answer)
                            .build()
            );
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .email(UUID.randomUUID().toString())
                    .password(UUID.randomUUID().toString())
                    .securityQuestions(securityQuestionRequests)
                    .build();
            Set<String> foundErrors = userSecurityQuestionValidator.validate(createUserRequest);
            assertTrue(foundErrors.contains(NULL_SECURITY_QUESTION_ANSWER_VALIDATION_MESSAGE));
        }
    }
}
