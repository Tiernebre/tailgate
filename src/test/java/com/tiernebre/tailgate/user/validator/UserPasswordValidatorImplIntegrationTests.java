package com.tiernebre.tailgate.user.validator;

import com.tiernebre.tailgate.test.SpringIntegrationTestingSuite;
import com.tiernebre.tailgate.user.dto.ResetTokenUpdatePasswordRequest;
import com.tiernebre.tailgate.user.exception.InvalidUpdatePasswordRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class UserPasswordValidatorImplIntegrationTests extends SpringIntegrationTestingSuite {
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
        @ParameterizedTest(name ="validates that the email is not \"{0}\"")
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
    }
}
