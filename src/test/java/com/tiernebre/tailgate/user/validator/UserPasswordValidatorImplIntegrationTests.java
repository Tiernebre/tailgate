package com.tiernebre.tailgate.user.validator;

import com.tiernebre.tailgate.test.SpringIntegrationTestingSuite;
import com.tiernebre.tailgate.user.dto.ResetTokenUpdatePasswordRequest;
import com.tiernebre.tailgate.user.exception.InvalidUpdatePasswordRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.tiernebre.tailgate.user.validator.UserValidationConstants.*;
import static org.junit.Assert.*;

public class UserPasswordValidatorImplIntegrationTests extends SpringIntegrationTestingSuite {
    private static final String VALID_EMAIL_VALIDATION_MESSAGE = "email must be a well-formed email address";

    @Autowired
    private UserPasswordValidatorImpl userPasswordValidator;

    @Nested
    @DisplayName("validateUpdateRequest")
    class ValidateUpdateRequestTests {
        @NullSource
        @EmptySource
        @ValueSource(strings = { " " })
        @ParameterizedTest(name ="validates that the new password is not \"{0}\"")
        void validatesThatThePasswordIsNotBlank(String newPassword) {
            ResetTokenUpdatePasswordRequest updatePasswordRequest = ResetTokenUpdatePasswordRequest.builder()
                    .newPassword(newPassword)
                    .build();
            Set<String> errorsCaught = assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordValidator.validateUpdateRequest(updatePasswordRequest)
            ).getErrors();
            assertTrue(errorsCaught.contains("newPassword must not be blank"));
        }

        @NullSource
        @EmptySource
        @ValueSource(strings = { " " })
        @ParameterizedTest(name = "validates that the email is not \"{0}\"")
        void validatesThatTheEmailIsNotBlank(String email) {
            ResetTokenUpdatePasswordRequest updatePasswordRequest = ResetTokenUpdatePasswordRequest.builder()
                    .email(email)
                    .build();
            Set<String> errorsCaught = assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordValidator.validateUpdateRequest(updatePasswordRequest)
            ).getErrors();
            assertTrue(errorsCaught.contains("email must not be blank"));
        }

        @ParameterizedTest(name = "validates that the email cannot be {0}")
        @ArgumentsSource(InvalidEmailArgumentsProvider.class)
        void validatesThatTheEmailCannotBe(String email) {
            ResetTokenUpdatePasswordRequest updatePasswordRequest = ResetTokenUpdatePasswordRequest.builder()
                    .email(email)
                    .build();
            Set<String> errorsCaught = assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordValidator.validateUpdateRequest(updatePasswordRequest)
            ).getErrors();
            assertTrue(errorsCaught.contains(VALID_EMAIL_VALIDATION_MESSAGE));
        }

        @ParameterizedTest(name = "validates that the email can be {0}")
        @ArgumentsSource(ValidEmailArgumentsProvider.class)
        void validatesThatTheEmailCanBe(String email) {
            ResetTokenUpdatePasswordRequest updatePasswordRequest = ResetTokenUpdatePasswordRequest.builder()
                    .email(email)
                    .build();
            Set<String> errorsCaught = assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordValidator.validateUpdateRequest(updatePasswordRequest)
            ).getErrors();
            assertFalse(errorsCaught.contains(VALID_EMAIL_VALIDATION_MESSAGE));
        }

        @ParameterizedTest(name = "validates that the security question answers cannot have size {0}")
        @ValueSource(longs = {
                1L,
                3L,
                4L,
                5L,
                10L
        })
        void validatesThatTheSecurityQuestionAnswersCannotHaveSize(long size) {
            Map<Long, String> securityQuestionAnswers = new HashMap<>();
            for (long i = 0; i < size; i++) {
                securityQuestionAnswers.put(i, UUID.randomUUID().toString());
            }
            ResetTokenUpdatePasswordRequest updatePasswordRequest = ResetTokenUpdatePasswordRequest.builder()
                    .securityQuestionAnswers(securityQuestionAnswers)
                    .build();
            Set<String> errorsCaught = assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordValidator.validateUpdateRequest(updatePasswordRequest)
            ).getErrors();
            assertTrue(errorsCaught.contains(NUMBER_OF_SECURITY_QUESTION_ANSWERS_VALIDATION_MESSAGE));
        }

        @ParameterizedTest(name = "validates that the security question answers cannot have size {0}")
        @EmptySource
        @NullSource
        void validatesThatTheSecurityQuestionAnswersCannotBeEmpty(Map<Long, String> securityQuestionAnswers) {
            ResetTokenUpdatePasswordRequest updatePasswordRequest = ResetTokenUpdatePasswordRequest.builder()
                    .securityQuestionAnswers(securityQuestionAnswers)
                    .build();
            Set<String> errorsCaught = assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordValidator.validateUpdateRequest(updatePasswordRequest)
            ).getErrors();
            assertTrue(errorsCaught.contains(EMPTY_SECURITY_QUESTION_ANSWERS_VALIDATION_MESSAGE));
        }

        @Test
        @DisplayName("validates that the security question answers cannot contain null entries")
        void validatesThatTheSecurityQuestionAnswersCannotContainNullEntries() {
            Map<Long, String> securityQuestionAnswers = new HashMap<>();
            for (long i = 0; i < NUMBER_OF_ALLOWED_SECURITY_QUESTIONS; i++) {
                securityQuestionAnswers.put(i, null);
            }
            ResetTokenUpdatePasswordRequest updatePasswordRequest = ResetTokenUpdatePasswordRequest.builder()
                    .securityQuestionAnswers(securityQuestionAnswers)
                    .build();
            Set<String> errorsCaught = assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordValidator.validateUpdateRequest(updatePasswordRequest)
            ).getErrors();
            assertTrue(errorsCaught.contains(NULL_SECURITY_QUESTION_ANSWERS_ENTRIES_VALIDATION_MESSAGE));
        }
    }
}
