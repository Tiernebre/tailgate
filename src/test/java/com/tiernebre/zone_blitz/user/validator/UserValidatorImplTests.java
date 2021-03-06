package com.tiernebre.zone_blitz.user.validator;

import com.tiernebre.zone_blitz.user.UserFactory;
import com.tiernebre.zone_blitz.user.dto.CreateUserRequest;
import com.tiernebre.zone_blitz.user.exception.InvalidUserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.com.google.common.collect.ImmutableSet;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserValidatorImplTests {
    @InjectMocks
    private UserValidatorImpl userValidator;

    @Mock
    private UserPasswordValidator userPasswordValidator;

    @Mock
    private Validator validator;

    @Mock
    private UserSecurityQuestionValidator securityQuestionValidator;

    @DisplayName("validate")
    @Nested
    public class ValidatorTests {
        @DisplayName("throws UserInvalidException if one error was found")
        @Test
        void testValidateThrowsInvalidExceptionIfASingleErrorOccurs() {
            when(userPasswordValidator.validate(anyString(), anyString())).thenReturn(Collections.emptySet());
            when(securityQuestionValidator.validate(any())).thenReturn(Collections.emptySet());
            CreateUserRequest entityToValidate = UserFactory.generateOneCreateUserRequest();
            String property = "foo";
            Path propertyPath = mock(Path.class);
            when(propertyPath.toString()).thenReturn(property);
            String errorReason = "must not be null";
            String expectedMessage = property + " " + errorReason;
            ConstraintViolation constraintViolation = mock(ConstraintViolation.class);
            when(constraintViolation.getPropertyPath()).thenReturn(propertyPath);
            when(constraintViolation.getMessage()).thenReturn(errorReason);
            when(validator.validate(eq(entityToValidate))).thenReturn(Collections.singleton(constraintViolation));
            InvalidUserException exceptionThrown = assertThrows(InvalidUserException.class, () -> {
                userValidator.validate(entityToValidate);
            });
            assertEquals(expectedMessage, exceptionThrown.getErrors().stream().findFirst().orElse(null));
        }

        @DisplayName("throws UserInvalidException if multiple errors were found")
        @Test
        void testValidateThrowsInvalidExceptionIfMultipleErrorsOccurred() {
            when(userPasswordValidator.validate(anyString(), anyString())).thenReturn(Collections.emptySet());
            when(securityQuestionValidator.validate(any())).thenReturn(Collections.emptySet());
            List<String> propertiesWithErrors = List.of(
                    "bar",
                    "baz"
            );
            List<Path> propertyPaths = List.of(
                    mock(Path.class),
                    mock(Path.class)
            );
            List<String> expectedErrorReasons = List.of(
                    "must not be null",
                    "must not be blank"
            );
            List<String> expectedErrors = new ArrayList<>();
            for (int i = 0; i < propertiesWithErrors.size(); i++) {
                String property = propertiesWithErrors.get(i);
                when(propertyPaths.get(i).toString()).thenReturn(property);
                String reason = expectedErrorReasons.get(i);
                expectedErrors.add(property + " " + reason);
            }
            CreateUserRequest entityToValidate = UserFactory.generateOneCreateUserRequest();
            ConstraintViolation firstConstraintViolation = mock(ConstraintViolation.class);
            when(firstConstraintViolation.getMessage()).thenReturn(expectedErrorReasons.get(0));
            when(firstConstraintViolation.getPropertyPath()).thenReturn(propertyPaths.get(0));
            ConstraintViolation secondConstraintViolation = mock(ConstraintViolation.class);
            when(secondConstraintViolation.getMessage()).thenReturn(expectedErrorReasons.get(1));
            when(secondConstraintViolation.getPropertyPath()).thenReturn(propertyPaths.get(1));
            when(validator.validate(eq(entityToValidate))).thenReturn(Set.of(firstConstraintViolation, secondConstraintViolation));
            InvalidUserException thrownException = assertThrows(InvalidUserException.class, () -> {
                userValidator.validate(entityToValidate);
            });
            assertEquals(expectedErrors, new ArrayList<>(thrownException.getErrors()));
        }

        @DisplayName("does not throw UserInvalidException if no errors were found")
        @Test
        void testValidateDoesNothingIfNoErrorsExist() {
            when(userPasswordValidator.validate(anyString(), anyString())).thenReturn(Collections.emptySet());
            when(securityQuestionValidator.validate(any())).thenReturn(Collections.emptySet());
            CreateUserRequest entityToValidate = UserFactory.generateOneCreateUserRequest();
            when(validator.validate(eq(entityToValidate))).thenReturn(Collections.emptySet());
            assertDoesNotThrow(() -> {
                userValidator.validate(entityToValidate);
            });
        }

        @DisplayName("throws UserInvalidException if errors were found from the password validator")
        @Test
        void testValidateAccountsForPasswordErrors() {
            CreateUserRequest entityToValidate = UserFactory.generateOneCreateUserRequest();
            when(securityQuestionValidator.validate(any())).thenReturn(Collections.emptySet());
            when(validator.validate(eq(entityToValidate))).thenReturn(Collections.emptySet());
            String expectedPasswordError = "Expected Test Failure for Password.";
            when(userPasswordValidator.validate(eq(entityToValidate.getPassword()), eq(entityToValidate.getConfirmationPassword()))).thenReturn(Set.of(expectedPasswordError));
            InvalidUserException thrownException = assertThrows(InvalidUserException.class, () -> {
                userValidator.validate(entityToValidate);
            });
            assertTrue(thrownException.getErrors().contains(expectedPasswordError));
        }

        @DisplayName("throws UserInvalidException if errors were found from the user security questionvalidator")
        @Test
        void testValidateAccountsForSecurityQuestionErrors() {
            when(userPasswordValidator.validate(anyString(), anyString())).thenReturn(Collections.emptySet());
            CreateUserRequest entityToValidate = UserFactory.generateOneCreateUserRequest();
            when(validator.validate(eq(entityToValidate))).thenReturn(Collections.emptySet());
            String expectedSecurityQuestionError = "Expected Test Failure for Security Questions.";
            when(securityQuestionValidator.validate(entityToValidate)).thenReturn(ImmutableSet.of(expectedSecurityQuestionError));
            InvalidUserException thrownException = assertThrows(InvalidUserException.class, () -> {
                userValidator.validate(entityToValidate);
            });
            assertTrue(thrownException.getErrors().contains(expectedSecurityQuestionError));
        }
    }
}
