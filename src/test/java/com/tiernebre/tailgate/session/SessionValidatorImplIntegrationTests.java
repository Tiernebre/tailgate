package com.tiernebre.tailgate.session;

import com.tiernebre.tailgate.test.SpringIntegrationTestingSuite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static com.tiernebre.tailgate.session.SessionValidatorImpl.INVALID_REFRESH_TOKEN_REQUEST_ERROR;
import static com.tiernebre.tailgate.test.ValidatorTestUtils.assertThatValidationInvalidatedCorrectly;
import static org.junit.jupiter.api.Assertions.*;

public class SessionValidatorImplIntegrationTests extends SpringIntegrationTestingSuite {
    @Autowired
    private SessionValidatorImpl sessionValidator;

    @Nested
    @DisplayName("validate")
    public class ValidateTests {
        @ParameterizedTest(name = "validates that the password must not equal \"{0}\"")
        @ValueSource(strings = { "", " " })
        @NullSource
        void testBlankEmail(String email) {
            CreateSessionRequest createSessionRequest = CreateSessionRequest.builder()
                    .email(email)
                    .password(UUID.randomUUID().toString())
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    sessionValidator,
                    createSessionRequest,
                    InvalidCreateSessionRequestException.class,
                    "email must not be blank"
            );
        }

        @ParameterizedTest(name = "validates that the password must not equal \"{0}\"")
        @ValueSource(strings = { "", " " })
        @NullSource
        void testBlankPassword(String password) {
            CreateSessionRequest createSessionRequest = CreateSessionRequest.builder()
                    .password(password)
                    .email(UUID.randomUUID().toString() + ".com")
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    sessionValidator,
                    createSessionRequest,
                    InvalidCreateSessionRequestException.class,
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
            assertDoesNotThrow(() -> sessionValidator.validate(createSessionRequest));
        }
    }

    @Nested
    @DisplayName("validateRefreshToken")
    public class ValidateRefreshTokenTests {
        @ParameterizedTest(name = "throws an invalid refresh request exception if refresh token is equal to \"{0}\"")
        @ValueSource(strings = {
                "",
                " ",
                "    "
        })
        @NullSource
        public void throwsAnInvalidRefreshRequestExceptionIfRefreshTokenIsEqualTo(String refreshToken) {
            InvalidRefreshSessionRequestException thrownException = assertThrows(InvalidRefreshSessionRequestException.class, () -> sessionValidator.validateRefreshToken(refreshToken));
            assertEquals(INVALID_REFRESH_TOKEN_REQUEST_ERROR, thrownException.getMessage());
        }

        @ParameterizedTest(name = "does not throw an invalid refresh request exception if refresh token is equal to \"{0}\"")
        @ValueSource(strings = {
                "test",
                "a",
                "123",
                "b072efd5-a8be-449c-bf2d-572e562f4087"
        })
        public void doesNotThrowAnInvalidRefreshRequestExceptionIfRefreshTokenIsEqualTo(String refreshToken) {
            assertDoesNotThrow(() -> sessionValidator.validateRefreshToken(refreshToken));
        }
    }
}
