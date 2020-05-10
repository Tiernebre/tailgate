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

public class AccessTokenValidatorImplIntegrationTests extends SpringIntegrationTestingSuite {
    @Autowired
    private AccessTokenValidatorImpl tokenValidator;

    @Nested
    @DisplayName("validate")
    public class ValidateTests {
        @Test
        @DisplayName("validates that the email must not be null")
        void testNullEmail() {
            CreateAccessTokenRequest createAccessTokenRequest = CreateAccessTokenRequest.builder()
                    .email(null)
                    .password(UUID.randomUUID().toString())
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    tokenValidator,
                    createAccessTokenRequest,
                    InvalidCreateAccessTokenRequestException.class,
                    "email must not be blank"
            );
        }

        @ParameterizedTest(name = "validates that the password must not equal \"{0}\"")
        @ValueSource(strings = { "", " " })
        void testBlankEmail(String email) {
            CreateAccessTokenRequest createAccessTokenRequest = CreateAccessTokenRequest.builder()
                    .email(email)
                    .password(UUID.randomUUID().toString())
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    tokenValidator,
                    createAccessTokenRequest,
                    InvalidCreateAccessTokenRequestException.class,
                    "email must not be blank"
            );
        }

        @Test
        @DisplayName("validates that the password must not be null")
        void testNullPassword() {
            CreateAccessTokenRequest createAccessTokenRequest = CreateAccessTokenRequest.builder()
                    .password(null)
                    .email(UUID.randomUUID().toString() + ".com")
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    tokenValidator,
                    createAccessTokenRequest,
                    InvalidCreateAccessTokenRequestException.class,
                    "password must not be blank"
            );
        }

        @ParameterizedTest(name = "validates that the password must not equal \"{0}\"")
        @ValueSource(strings = { "", " " })
        void testBlankPassword(String password) {
            CreateAccessTokenRequest createAccessTokenRequest = CreateAccessTokenRequest.builder()
                    .password(password)
                    .email(UUID.randomUUID().toString() + ".com")
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    tokenValidator,
                    createAccessTokenRequest,
                    InvalidCreateAccessTokenRequestException.class,
                    "password must not be blank"
            );
        }

        @Test
        @DisplayName("lets through a completely valid request")
        void testValidRequest() {
            CreateAccessTokenRequest createAccessTokenRequest = CreateAccessTokenRequest.builder()
                    .password(UUID.randomUUID().toString())
                    .email(UUID.randomUUID().toString() + ".com")
                    .build();
            assertDoesNotThrow(() -> tokenValidator.validate(createAccessTokenRequest));
        }
    }
}
