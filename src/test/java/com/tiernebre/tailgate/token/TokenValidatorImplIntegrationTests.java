package com.tiernebre.tailgate.token;

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

public class TokenValidatorImplIntegrationTests extends SpringIntegrationTestingSuite {
    @Autowired
    private TokenValidatorImpl tokenValidator;

    @Nested
    @DisplayName("validate")
    public class ValidateTests {
        @Test
        @DisplayName("validates that the email must not be null")
        void testNullEmail() {
            CreateTokenRequest createTokenRequest = CreateTokenRequest.builder()
                    .email(null)
                    .password(UUID.randomUUID().toString())
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    tokenValidator,
                    createTokenRequest,
                    InvalidCreateTokenRequestException.class,
                    "email must not be blank"
            );
        }

        @ParameterizedTest(name = "validates that the password must not equal \"{0}\"")
        @ValueSource(strings = { "", " " })
        void testBlankEmail(String email) {
            CreateTokenRequest createTokenRequest = CreateTokenRequest.builder()
                    .email(email)
                    .password(UUID.randomUUID().toString())
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    tokenValidator,
                    createTokenRequest,
                    InvalidCreateTokenRequestException.class,
                    "email must not be blank"
            );
        }

        @Test
        @DisplayName("validates that the password must not be null")
        void testNullPassword() {
            CreateTokenRequest createTokenRequest = CreateTokenRequest.builder()
                    .password(null)
                    .email(UUID.randomUUID().toString() + ".com")
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    tokenValidator,
                    createTokenRequest,
                    InvalidCreateTokenRequestException.class,
                    "password must not be blank"
            );
        }

        @ParameterizedTest(name = "validates that the password must not equal \"{0}\"")
        @ValueSource(strings = { "", " " })
        void testBlankPassword(String password) {
            CreateTokenRequest createTokenRequest = CreateTokenRequest.builder()
                    .password(password)
                    .email(UUID.randomUUID().toString() + ".com")
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    tokenValidator,
                    createTokenRequest,
                    InvalidCreateTokenRequestException.class,
                    "password must not be blank"
            );
        }

        @Test
        @DisplayName("lets through a completely valid request")
        void testValidRequest() {
            CreateTokenRequest createTokenRequest = CreateTokenRequest.builder()
                    .password(UUID.randomUUID().toString())
                    .email(UUID.randomUUID().toString() + ".com")
                    .build();
            assertDoesNotThrow(() -> tokenValidator.validate(createTokenRequest));
        }
    }
}
