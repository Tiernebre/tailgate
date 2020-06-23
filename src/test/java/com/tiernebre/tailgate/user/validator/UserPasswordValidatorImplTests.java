package com.tiernebre.tailgate.user.validator;

import com.tiernebre.tailgate.user.dto.ResetTokenUpdatePasswordRequest;
import com.tiernebre.tailgate.user.dto.UserUpdatePasswordRequest;
import com.tiernebre.tailgate.user.exception.InvalidUpdatePasswordRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.Validator;
import java.util.Collections;
import java.util.Set;

import static com.tiernebre.tailgate.user.validator.UserValidationConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserPasswordValidatorImplTests {
    @InjectMocks
    private UserPasswordValidatorImpl userPasswordValidator;

    @Mock
    private Validator validator;

    @Nested
    @DisplayName("validate")
    public class ValidateTests {
        @DisplayName("adds an error if the password and confirmation password do not match")
        @Test
        void testValidateEnsurePasswordAndConfirmationPasswordAreEqual() {
            String password = "testPassword12345!";
            Set<String> errors = userPasswordValidator.validate(password, password + "!");
            assertTrue(errors.contains(PASSWORD_MATCHES_ERROR));
        }

        @DisplayName("adds an error if the password does not have numerical digit characters")
        @Test
        void testValidateEnsurePasswordMustHaveNumericalDigitCharacters() {
            String password = "testPassword!";
            Set<String> errors = userPasswordValidator.validate(password, password);
            assertTrue(errors.contains(PASSWORD_CONTAIN_DIGITS_ERROR));
        }

        @DisplayName("adds an error if the password does not have uppercase alphabetical characters")
        @Test
        void testValidateEnsurePasswordMustHaveUppercaseAlphabeticalCharacters() {
            String password = "testpassword12345!";
            Set<String> errors = userPasswordValidator.validate(password, password);
            assertTrue(errors.contains(PASSWORD_MIXED_CHARACTERS_ERROR));
        }

        @DisplayName("adds an error if the password does not have lowercase alphabetical characters")
        @Test
        void testValidateEnsurePasswordMustHaveLowercaseAlphabeticalCharacters() {
            String password = "TESTPASSWORD12345!";
            Set<String> errors = userPasswordValidator.validate(password, password);
            assertTrue(errors.contains(PASSWORD_MIXED_CHARACTERS_ERROR));
        }

        @DisplayName("adds an error if the password does not have special characters")
        @Test
        void testValidateEnsurePasswordMustHaveSpecialCharacters() {
            String password = "TestPassword12345";
            Set<String> errors = userPasswordValidator.validate(password, password);
            assertTrue(errors.contains(PASSWORD_SPECIAL_CHARACTERS_ERROR));
        }
    }

    @Nested
    @DisplayName("validateResetTokenUpdateRequest")
    public class ValidateResetTokenUpdateRequestTests {
        @DisplayName("adds an error if the new password and confirmation new password do not match")
        @Test
        void testValidateEnsurePasswordAndConfirmationPasswordAreEqual() {
            when(validator.validate(any())).thenReturn(Collections.emptySet());
            String password = "testPassword12345!";
            ResetTokenUpdatePasswordRequest updatePasswordRequest = ResetTokenUpdatePasswordRequest.builder()
                    .newPassword(password)
                    .confirmationNewPassword(password + "!")
                    .build();
            Set<String> passwordErrors = assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordValidator.validateResetTokenUpdateRequest(updatePasswordRequest)
            ).getErrors();
            assertTrue(passwordErrors.contains(PASSWORD_MATCHES_ERROR));
        }

        @DisplayName("adds an error if the password does not have numerical digit characters")
        @Test
        void testValidateEnsurePasswordMustHaveNumericalDigitCharacters() {
            when(validator.validate(any())).thenReturn(Collections.emptySet());
            String password = "testPassword!";
            ResetTokenUpdatePasswordRequest updatePasswordRequest = ResetTokenUpdatePasswordRequest.builder()
                    .newPassword(password)
                    .confirmationNewPassword(password)
                    .build();
            Set<String> passwordErrors = assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordValidator.validateResetTokenUpdateRequest(updatePasswordRequest)
            ).getErrors();
            assertTrue(passwordErrors.contains(PASSWORD_CONTAIN_DIGITS_ERROR));
        }

        @DisplayName("adds an error if the password does not have uppercase alphabetical characters")
        @Test
        void testValidateEnsurePasswordMustHaveUppercaseAlphabeticalCharacters() {
            when(validator.validate(any())).thenReturn(Collections.emptySet());
            String password = "testpassword12345!";
            ResetTokenUpdatePasswordRequest updatePasswordRequest = ResetTokenUpdatePasswordRequest.builder()
                    .newPassword(password)
                    .confirmationNewPassword(password)
                    .build();
            Set<String> passwordErrors = assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordValidator.validateResetTokenUpdateRequest(updatePasswordRequest)
            ).getErrors();
            assertTrue(passwordErrors.contains(PASSWORD_MIXED_CHARACTERS_ERROR));
        }

        @DisplayName("adds an error if the password does not have lowercase alphabetical characters")
        @Test
        void testValidateEnsurePasswordMustHaveLowercaseAlphabeticalCharacters() {
            when(validator.validate(any())).thenReturn(Collections.emptySet());
            String password = "TESTPASSWORD12345!";
            ResetTokenUpdatePasswordRequest updatePasswordRequest = ResetTokenUpdatePasswordRequest.builder()
                    .newPassword(password)
                    .confirmationNewPassword(password)
                    .build();
            Set<String> passwordErrors = assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordValidator.validateResetTokenUpdateRequest(updatePasswordRequest)
            ).getErrors();
            assertTrue(passwordErrors.contains(PASSWORD_MIXED_CHARACTERS_ERROR));
        }

        @DisplayName("adds an error if the password does not have special characters")
        @Test
        void testValidateEnsurePasswordMustHaveSpecialCharacters() {
            String password = "TestPassword12345";
            ResetTokenUpdatePasswordRequest updatePasswordRequest = ResetTokenUpdatePasswordRequest.builder()
                    .newPassword(password)
                    .confirmationNewPassword(password)
                    .build();
            Set<String> passwordErrors = assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordValidator.validateResetTokenUpdateRequest(updatePasswordRequest)
            ).getErrors();
            assertTrue(passwordErrors.contains(PASSWORD_SPECIAL_CHARACTERS_ERROR));
        }

        @DisplayName("does not allow a null update request")
        @Test
        void doesNotAllowANullUpdateRequest() {
            String message = assertThrows(
                    NullPointerException.class,
                    () -> userPasswordValidator.validateResetTokenUpdateRequest(null)
            ).getMessage();
            assertEquals(NULL_PASSWORD_UPDATE_REQUEST_ERROR, message);
        }
    }

    @Nested
    @DisplayName("validateUpdateRequest")
    public class ValidateUpdateRequestTests {
        @DisplayName("adds an error if the new password and confirmation new password do not match")
        @Test
        void testValidateEnsurePasswordAndConfirmationPasswordAreEqual() {
            when(validator.validate(any())).thenReturn(Collections.emptySet());
            String password = "testPassword12345!";
            UserUpdatePasswordRequest updatePasswordRequest = UserUpdatePasswordRequest.builder()
                    .newPassword(password)
                    .confirmationNewPassword(password + "!")
                    .build();
            Set<String> passwordErrors = assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordValidator.validateUpdateRequest(updatePasswordRequest)
            ).getErrors();
            assertTrue(passwordErrors.contains(PASSWORD_MATCHES_ERROR));
        }

        @DisplayName("adds an error if the password does not have numerical digit characters")
        @Test
        void testValidateEnsurePasswordMustHaveNumericalDigitCharacters() {
            when(validator.validate(any())).thenReturn(Collections.emptySet());
            String password = "testPassword!";
            UserUpdatePasswordRequest updatePasswordRequest = UserUpdatePasswordRequest.builder()
                    .newPassword(password)
                    .confirmationNewPassword(password)
                    .build();
            Set<String> passwordErrors = assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordValidator.validateUpdateRequest(updatePasswordRequest)
            ).getErrors();
            assertTrue(passwordErrors.contains(PASSWORD_CONTAIN_DIGITS_ERROR));
        }

        @DisplayName("adds an error if the password does not have uppercase alphabetical characters")
        @Test
        void testValidateEnsurePasswordMustHaveUppercaseAlphabeticalCharacters() {
            when(validator.validate(any())).thenReturn(Collections.emptySet());
            String password = "testpassword12345!";
            UserUpdatePasswordRequest updatePasswordRequest = UserUpdatePasswordRequest.builder()
                    .newPassword(password)
                    .confirmationNewPassword(password)
                    .build();
            Set<String> passwordErrors = assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordValidator.validateUpdateRequest(updatePasswordRequest)
            ).getErrors();
            assertTrue(passwordErrors.contains(PASSWORD_MIXED_CHARACTERS_ERROR));
        }

        @DisplayName("adds an error if the password does not have lowercase alphabetical characters")
        @Test
        void testValidateEnsurePasswordMustHaveLowercaseAlphabeticalCharacters() {
            when(validator.validate(any())).thenReturn(Collections.emptySet());
            String password = "TESTPASSWORD12345!";
            UserUpdatePasswordRequest updatePasswordRequest = UserUpdatePasswordRequest.builder()
                    .newPassword(password)
                    .confirmationNewPassword(password)
                    .build();
            Set<String> passwordErrors = assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordValidator.validateUpdateRequest(updatePasswordRequest)
            ).getErrors();
            assertTrue(passwordErrors.contains(PASSWORD_MIXED_CHARACTERS_ERROR));
        }

        @DisplayName("adds an error if the password does not have special characters")
        @Test
        void testValidateEnsurePasswordMustHaveSpecialCharacters() {
            String password = "TestPassword12345";
            UserUpdatePasswordRequest updatePasswordRequest = UserUpdatePasswordRequest.builder()
                    .newPassword(password)
                    .confirmationNewPassword(password)
                    .build();
            Set<String> passwordErrors = assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordValidator.validateUpdateRequest(updatePasswordRequest)
            ).getErrors();
            assertTrue(passwordErrors.contains(PASSWORD_SPECIAL_CHARACTERS_ERROR));
        }

        @DisplayName("does not allow a null update request")
        @Test
        void doesNotAllowANullUpdateRequest() {
            String message = assertThrows(
                    NullPointerException.class,
                    () -> userPasswordValidator.validateUpdateRequest(null)
            ).getMessage();
            assertEquals(NULL_PASSWORD_UPDATE_REQUEST_ERROR, message);
        }
    }
}
