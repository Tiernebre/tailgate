package com.tiernebre.tailgate.user.validator;

import com.tiernebre.tailgate.test.SpringIntegrationTestingSuite;
import com.tiernebre.tailgate.user.CreateUserRequest;
import com.tiernebre.tailgate.user.InvalidUserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static com.tiernebre.tailgate.test.ValidatorTestUtils.assertThatValidationInvalidatedCorrectly;
import static org.junit.jupiter.api.Assertions.*;

public class UserValidatorImplIntegrationTests extends SpringIntegrationTestingSuite {
    private static final String STRONG_PASSWORD = "Strong_Password_12345!";

    @Autowired
    private UserValidator userValidator;

    @Nested
    @DisplayName("validate")
    public class ValidateTests {
        @ParameterizedTest(name = "validates that the password must not equal \"{0}\"")
        @ValueSource(strings = { "", " " })
        @NullSource
        void testThatBlankPasswordFails(String password) throws InvalidUserException {
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .email("test@" + UUID.randomUUID().toString())
                    .password(password)
                    .confirmationPassword(password)
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    userValidator,
                    createUserRequest,
                    InvalidUserException.class,
                    "password must not be blank"
            );
        }

        @ParameterizedTest(name = "validates that the email must not equal \"{0}\"")
        @ValueSource(strings = {
                "",
                " ",
                "      "
        })
        @NullSource
        void testThatBlankEmailFails(String email) throws InvalidUserException {
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .email(email)
                    .password(STRONG_PASSWORD)
                    .confirmationPassword(STRONG_PASSWORD)
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    userValidator,
                    createUserRequest,
                    InvalidUserException.class,
                    "email must not be blank"
            );
        }

        @ParameterizedTest(name = "validates that the email must not equal \"{0}\"")
        @ValueSource(strings = {
                "plainaddress",
                "#@%^%#$@#$@#.com",
                "@example.com",
                "Joe Smith <email@example.com>",
                "email.example.com",
                "email@example@example.com",
                ".email@example.com",
                "email.@example.com",
                "email..email@example.com",
                "email@example.com (Joe Smith)",
                "email@-example.com",
                "email@example..com",
                "Abc..123@example.com",
        })
        void testThatInvalidEmailFormatFails(String email) throws InvalidUserException {
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .email(email)
                    .password(STRONG_PASSWORD)
                    .confirmationPassword(STRONG_PASSWORD)
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    userValidator,
                    createUserRequest,
                    InvalidUserException.class,
                    "email must be a well-formed email address"
            );
        }

        @ParameterizedTest(name = "validates that the email can equal \"{0}\"")
        @ValueSource(strings = {
                "email@example.com",
                "firstname.lastname@example.com",
                "email@subdomain.example.com",
                "firstname+lastname@example.com",
                "email@123.123.123.123",
                "email@[123.123.123.123]",
                "\"email\"@example.com",
                "1234567890@example.com",
                "email@example-one.com",
                "_______@example.com",
                "email@example.name",
                "email@example.museum",
                "email@example.co.jp",
                "firstname-lastname@example.com"
        })
        void testThatValidEmailSucceeds(String email) throws InvalidUserException {
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .email(email)
                    .password(STRONG_PASSWORD)
                    .confirmationPassword(STRONG_PASSWORD)
                    .build();
            assertDoesNotThrow(() -> {
                userValidator.validate(createUserRequest);
            });
        }

        @Test
        @DisplayName("validates that a password with 7 characters fails")
        void testThatPasswordWithLengthOfSevenCharactersFails() throws InvalidUserException {
            String password = "aBcD1!2";
            assertEquals(7, password.length());
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .email(null)
                    .password(password)
                    .confirmationPassword(password)
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    userValidator,
                    createUserRequest,
                    InvalidUserException.class,
                    "password size must be between 8 and 71"
            );
        }

        @Test
        @DisplayName("validates that a password with 72 characters fails")
        void testThatPasswordWithLengthOfSeventyTwoCharactersFails() throws InvalidUserException {
            String password = "aBcD1234!*".repeat(7) + "aB";
            assertEquals(72, password.length());
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .email(null)
                    .password(password)
                    .confirmationPassword(password)
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    userValidator,
                    createUserRequest,
                    InvalidUserException.class,
                    "password size must be between 8 and 71"
            );
        }
    }
}
