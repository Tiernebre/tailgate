package com.tiernebre.tailgate.user.validator;

import com.tiernebre.tailgate.user.validator.UserPasswordValidatorImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class UserPasswordValidatorImplTests {
    @InjectMocks
    private UserPasswordValidatorImpl userPasswordValidator;

    @Nested
    @DisplayName("validate")
    public class ValidateTests {
        @DisplayName("adds an error if the password and confirmation password do not match")
        @Test
        void testValidateEnsurePasswordAndConfirmationPasswordAreEqual() {
            String password = "testPassword12345!";
            Set<String> errors = userPasswordValidator.validate(password, password + "!");
            assertTrue(errors.contains("password and confirmationPassword must equal each other"));
        }

        @DisplayName("adds an error if the password does not have numerical digit characters")
        @Test
        void testValidateEnsurePasswordMustHaveNumericalDigitCharacters() {
            String password = "testPassword!";
            Set<String> errors = userPasswordValidator.validate(password, password);
            assertTrue(errors.contains("password must contain numerical digits (0-9)"));
        }

        @DisplayName("adds an error if the password does not have uppercase alphabetical characters")
        @Test
        void testValidateEnsurePasswordMustHaveUppercaseAlphabeticalCharacters() {
            String password = "testpassword12345!";
            Set<String> errors = userPasswordValidator.validate(password, password);
            assertTrue(errors.contains("password must contain mixed uppercase and lowercase alphabetical characters (A-Z, a-z)"));
        }

        @DisplayName("adds an error if the password does not have lowercase alphabetical characters")
        @Test
        void testValidateEnsurePasswordMustHaveLowercaseAlphabeticalCharacters() {
            String password = "TESTPASSWORD12345!";
            Set<String> errors = userPasswordValidator.validate(password, password);
            assertTrue(errors.contains("password must contain mixed uppercase and lowercase alphabetical characters (A-Z, a-z)"));
        }

        @DisplayName("adds an error if the password does not have special characters")
        @Test
        void testValidateEnsurePasswordMustHaveSpecialCharacters() {
            String password = "TestPassword12345";
            Set<String> errors = userPasswordValidator.validate(password, password);
            assertTrue(errors.contains("password must contain non-digit and non-alphanumeric characters"));
        }
    }
}
