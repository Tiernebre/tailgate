package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.session.CreateSessionRequest;
import com.tiernebre.tailgate.session.SessionValidatorImpl;
import com.tiernebre.tailgate.test.SpringIntegrationTestingSuite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static com.tiernebre.tailgate.test.ValidatorTestUtils.assertThatValidationInvalidatedCorrectly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SessionValidatorImplIntegrationTests extends SpringIntegrationTestingSuite {
    @Autowired
    private SessionValidatorImpl tokenValidator;

    @Nested
    @DisplayName("validate")
    public class ValidateTests {
        @Test
        @DisplayName("validates that the email must not be null")
        void testNullEmail() {
            CreateSessionRequest createSessionRequest = CreateSessionRequest.builder()
                    .email(null)
                    .password(UUID.randomUUID().toString())
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    tokenValidator,
                    createSessionRequest,
                    InvalidCreateAccessTokenRequestException.class,
                    "email must not be blank"
            );
        }

        @ParameterizedTest(name = "validates that the password must not equal \"{0}\"")
        @ValueSource(strings = { "", " " })
        void testBlankEmail(String email) {
            CreateSessionRequest createSessionRequest = CreateSessionRequest.builder()
                    .email(email)
                    .password(UUID.randomUUID().toString())
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    tokenValidator,
                    createSessionRequest,
                    InvalidCreateAccessTokenRequestException.class,
                    "email must not be blank"
            );
        }

        @Test
        @DisplayName("validates that the password must not be null")
        void testNullPassword() {
            CreateSessionRequest createSessionRequest = CreateSessionRequest.builder()
                    .password(null)
                    .email(UUID.randomUUID().toString() + ".com")
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    tokenValidator,
                    createSessionRequest,
                    InvalidCreateAccessTokenRequestException.class,
                    "password must not be blank"
            );
        }

        @ParameterizedTest(name = "validates that the password must not equal \"{0}\"")
        @ValueSource(strings = { "", " " })
        void testBlankPassword(String password) {
            CreateSessionRequest createSessionRequest = CreateSessionRequest.builder()
                    .password(password)
                    .email(UUID.randomUUID().toString() + ".com")
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    tokenValidator,
                    createSessionRequest,
                    InvalidCreateAccessTokenRequestException.class,
                    "password must not be blank"
            );
        }

        @Test
        @DisplayName("lets through a completely valid request")
        void testValidRequest() {
            CreateSessionRequest createSessionRequest = CreateSessionRequest.builder()
                    .password(UUID.randomUUID().toString())
                    .email(UUID.randomUUID().toString() + ".com")
                    .build();
            assertDoesNotThrow(() -> tokenValidator.validate(createSessionRequest));
        }
    }
}
