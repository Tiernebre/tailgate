package com.tiernebre.zone_blitz.user.validator;

import com.tiernebre.zone_blitz.test.AbstractIntegrationTestingSuite;
import com.tiernebre.zone_blitz.user.dto.ResetTokenUpdatePasswordRequest;
import com.tiernebre.zone_blitz.user.dto.UserUpdatePasswordRequest;
import com.tiernebre.zone_blitz.user.exception.InvalidUpdatePasswordRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

import static com.tiernebre.zone_blitz.user.validator.UserValidationConstants.*;
import static org.junit.Assert.*;

public class UserPasswordValidatorImplIntegrationTests extends AbstractIntegrationTestingSuite {
    private static final String VALID_EMAIL_VALIDATION_MESSAGE = "email must be a well-formed email address";

    @Autowired
    private UserPasswordValidatorImpl userPasswordValidator;

    @Nested
    @DisplayName("validateUpdateRequest")
    class ValidateUpdateRequest {
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

        @NullSource
        @EmptySource
        @ValueSource(strings = { " " })
        @ParameterizedTest(name = "validates that the old password for user update requests is not \"{0}\"")
        void validatesThatTheOldPasswordIsNotBlank(String oldPassword) {
            UserUpdatePasswordRequest userUpdatePasswordRequest = UserUpdatePasswordRequest.builder()
                    .oldPassword(oldPassword)
                    .build();
            Set<String> errorsCaught = assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordValidator.validateUpdateRequest(userUpdatePasswordRequest)
            ).getErrors();
            assertTrue(errorsCaught.contains(REQUIRED_OLD_PASSWORD_VALIDATION_MESSAGE));
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

        @ParameterizedTest(name = "validates that the security question answers cannot contain \"{0}\" entries")
        @NullSource
        @EmptySource
        @ValueSource(strings = " ")
        void validatesThatTheSecurityQuestionAnswersCannotContainBlankEntries(String entry) {
            Map<Long, String> securityQuestionAnswers = new HashMap<>();
            for (long i = 0; i < NUMBER_OF_ALLOWED_SECURITY_QUESTIONS; i++) {
                securityQuestionAnswers.put(i, entry);
            }
            ResetTokenUpdatePasswordRequest updatePasswordRequest = ResetTokenUpdatePasswordRequest.builder()
                    .securityQuestionAnswers(securityQuestionAnswers)
                    .build();
            Set<String> errorsCaught = assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordValidator.validateUpdateRequest(updatePasswordRequest)
            ).getErrors();
            assertTrue(errorsCaught.contains(BLANK_SECURITY_QUESTION_ANSWERS_ENTRIES_VALIDATION_MESSAGE));
        }
    }
}
