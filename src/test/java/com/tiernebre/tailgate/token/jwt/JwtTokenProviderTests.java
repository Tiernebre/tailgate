package com.tiernebre.tailgate.token.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tiernebre.tailgate.token.GenerateTokenException;
import com.tiernebre.tailgate.user.UserDto;
import com.tiernebre.tailgate.user.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.tiernebre.tailgate.token.jwt.JwtTokenProvider.EMAIL_CLAIM;
import static com.tiernebre.tailgate.token.jwt.JwtTokenProvider.ISSUER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderTests {
    private static final String TEST_SECRET = "secret!";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(TEST_SECRET);
    private static final int TEST_EXPIRATION_WINDOW_IN_MINUTES = 15;

    private JwtTokenProvider jwtTokenService;

    @Mock
    private JwtTokenConfigurationProperties jwtTokenConfigurationProperties;

    @BeforeEach
    public void setup() {
        when(jwtTokenConfigurationProperties.getExpirationWindowInMinutes()).thenReturn(TEST_EXPIRATION_WINDOW_IN_MINUTES);
        jwtTokenService = new JwtTokenProvider(
                ALGORITHM,
                jwtTokenConfigurationProperties
        );
    }

    @Nested
    @DisplayName("generateOne")
    public class GenerateOneTests {
        @Test
        @DisplayName("returns the generated JSON web token with the correct claims")
        void returnsTheGeneratedJSONWebToken() throws GenerateTokenException {
            UserDto userDTO = UserFactory.generateOneDto();
            Clock fixedTestClock = Clock.fixed(Instant.now(), ZoneId.of("UTC"));
            long expectedMillisForExpiration = (fixedTestClock.millis() + TimeUnit.MINUTES.toMillis(TEST_EXPIRATION_WINDOW_IN_MINUTES)) / 1000 * 1000;
            Date expectedExpiresAt = new Date(expectedMillisForExpiration);
            String generatedToken = jwtTokenService.generateOne(userDTO, fixedTestClock);
            JWTVerifier jwtVerifier = JWT.require(ALGORITHM)
                    .withIssuer(ISSUER)
                    .build();
            DecodedJWT decodedJWT = jwtVerifier.verify(generatedToken);
            assertAll(
                    () -> assertEquals(ISSUER, decodedJWT.getIssuer()),
                    () -> assertEquals(userDTO.getId().toString(), decodedJWT.getSubject()),
                    () -> assertEquals(userDTO.getEmail(), decodedJWT.getClaim(EMAIL_CLAIM).asString()),
                    () -> assertEquals(expectedExpiresAt, decodedJWT.getExpiresAt())
            );
        }

        @Test
        @DisplayName("throws a GenerateTokenException if the JWT token completely failed to sign")
        void throwsGenerateTokenExceptionIfTokenCannotBeSigned() throws GenerateTokenException {
            JwtTokenProvider jwtTokenServiceWithBorkedAlgorithm = new JwtTokenProvider(
                    null,
                    jwtTokenConfigurationProperties
            );
            UserDto userDTO = UserFactory.generateOneDto();
            assertThrows(GenerateTokenException.class, () -> jwtTokenServiceWithBorkedAlgorithm.generateOne(userDTO, Clock.systemUTC()));
        }
    }

    @Nested
    @DisplayName("validateOne")
    public class ValidateOneTests {
        @Test
        @DisplayName("returns the decoded User if the JWT token provided is valid")
        void returnsTheDecodedUserForValidJWT() {
            UserDto expectedUser = UserFactory.generateOneDto();
            String testToken = JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(expectedUser.getId().toString())
                    .withClaim(EMAIL_CLAIM, expectedUser.getEmail())
                    .sign(ALGORITHM);
            UserDto foundUser = jwtTokenService.validateOne(testToken);
            assertEquals(expectedUser, foundUser);
        }
    }
}
