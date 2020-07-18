package com.tiernebre.zone_blitz.token.access.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tiernebre.zone_blitz.token.access.AccessTokenDto;
import com.tiernebre.zone_blitz.token.access.AccessTokenInvalidException;
import com.tiernebre.zone_blitz.token.access.GenerateAccessTokenException;
import com.tiernebre.zone_blitz.token.access.fingerprint.AccessTokenFingerprintGenerator;
import com.tiernebre.zone_blitz.token.access.fingerprint.AccessTokenFingerprintHasher;
import com.tiernebre.zone_blitz.user.UserFactory;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.tiernebre.zone_blitz.token.access.jwt.JwtTokenConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderTests {
    private static final String TEST_SECRET = "secret!";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(TEST_SECRET);
    private static final int TEST_EXPIRATION_WINDOW_IN_MINUTES = 15;

    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private JwtTokenConfigurationProperties jwtTokenConfigurationProperties;

    @Mock
    private AccessTokenFingerprintHasher fingerprintHasher;

    @Mock
    private AccessTokenFingerprintGenerator fingerprintGenerator;

    private Clock fixedTestClock;

    @BeforeEach
    public void setup() {
        fixedTestClock = Clock.fixed(Instant.now(), ZoneId.of("UTC"));
        jwtTokenProvider = new JwtTokenProvider(
                ALGORITHM,
                jwtTokenConfigurationProperties,
                Clock.fixed(Instant.now(), ZoneId.of("UTC")),
                fingerprintHasher,
                fingerprintGenerator
        );
    }

    @Nested
    @DisplayName("generateOne")
    public class GenerateOneTests {

        @Test
        @DisplayName("returns the generated JSON web token with the correct claims and fingerprint")
        void returnsTheGeneratedJSONWebToken() throws GenerateAccessTokenException {
            when(jwtTokenConfigurationProperties.getExpirationWindowInMinutes()).thenReturn(TEST_EXPIRATION_WINDOW_IN_MINUTES);
            String fingerprint = UUID.randomUUID().toString();
            String expectedHashedFingerprint = UUID.randomUUID().toString();
            when(fingerprintGenerator.generateOne()).thenReturn(fingerprint);
            when(fingerprintHasher.hashFingerprint(eq(fingerprint))).thenReturn(expectedHashedFingerprint);
            UserDto userDTO = UserFactory.generateOneDto();
            // JWT expiration cuts off the last three digits, we have to do so here as well
            long expectedMillisForExpiration = (fixedTestClock.millis() + TimeUnit.MINUTES.toMillis(TEST_EXPIRATION_WINDOW_IN_MINUTES)) / 1000 * 1000;
            Date expectedExpiresAt = new Date(expectedMillisForExpiration);
            AccessTokenDto generatedToken = jwtTokenProvider.generateOne(userDTO);
            JWTVerifier jwtVerifier = JWT.require(ALGORITHM)
                    .withIssuer(ISSUER)
                    .build();
            DecodedJWT decodedJWT = jwtVerifier.verify(generatedToken.getToken());
            assertAll(
                    () -> assertEquals(fingerprint, generatedToken.getFingerprint()),
                    () -> assertEquals(ISSUER, decodedJWT.getIssuer()),
                    () -> assertEquals(userDTO.getId().toString(), decodedJWT.getSubject()),
                    () -> assertEquals(userDTO.getEmail(), decodedJWT.getClaim(EMAIL_CLAIM).asString()),
                    () -> assertEquals(userDTO.isConfirmed(), decodedJWT.getClaim(IS_CONFIRMED_CLAIM).asBoolean()),
                    () -> assertEquals(expectedExpiresAt, decodedJWT.getExpiresAt()),
                    () -> assertEquals(expectedHashedFingerprint, decodedJWT.getClaim(FINGERPRINT_CLAIM).asString())
            );
        }

        @Test
        @DisplayName("throws a GenerateTokenException if the JWT token completely failed to sign")
        void throwsGenerateTokenExceptionIfTokenCannotBeSigned() {
            when(jwtTokenConfigurationProperties.getExpirationWindowInMinutes()).thenReturn(TEST_EXPIRATION_WINDOW_IN_MINUTES);
            JwtTokenProvider jwtTokenServiceWithBorkedAlgorithm = new JwtTokenProvider(
                    null,
                    jwtTokenConfigurationProperties,
                    fixedTestClock,
                    fingerprintHasher,
                    fingerprintGenerator
            );
            UserDto userDTO = UserFactory.generateOneDto();
            assertThrows(GenerateAccessTokenException.class, () -> jwtTokenServiceWithBorkedAlgorithm.generateOne(userDTO));
        }

        @Test
        @DisplayName("throws a NullPointerException if the user passed in is null with a helpful message")
        void throwsNullPointerExceptionIfTheUserPassedInIsNullWithAHelpfulMessage() {
            NullPointerException thrownException = assertThrows(NullPointerException.class, () -> jwtTokenProvider.generateOne(null));
            assertEquals(NULL_USER_ERROR_MESSAGE, thrownException.getMessage());
        }
    }

    @Nested
    @DisplayName("validateOne")
    public class ValidateOneTests {
        @Test
        @DisplayName("returns the decoded User if the JWT token provided is valid")
        void returnsTheDecodedUserForValidJWT() throws AccessTokenInvalidException {
            UserDto expectedUser = UserFactory.generateOneDto();
            String fingerprint = UUID.randomUUID().toString();
            String expectedHashedFingerprint = UUID.randomUUID().toString();
            when(fingerprintHasher.hashFingerprint(eq(fingerprint))).thenReturn(expectedHashedFingerprint);
            String testToken = JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(expectedUser.getId().toString())
                    .withClaim(EMAIL_CLAIM, expectedUser.getEmail())
                    .withClaim(IS_CONFIRMED_CLAIM, expectedUser.isConfirmed())
                    .withClaim(FINGERPRINT_CLAIM, expectedHashedFingerprint)
                    .sign(ALGORITHM);
            UserDto foundUser = jwtTokenProvider.validateOne(testToken, fingerprint);
            assertEquals(expectedUser, foundUser);
        }

        @Test
        @DisplayName("throws an error if given an incorrect fingerprint")
        void throwsAnErrorIfGivenAnIncorrectFingerprint() {
            UserDto expectedUser = UserFactory.generateOneDto();
            String fingerprint = UUID.randomUUID().toString();
            String providedFingerprintHash = UUID.randomUUID().toString();
            String expectedHashedFingerprint = UUID.randomUUID().toString();
            when(fingerprintHasher.hashFingerprint(eq(fingerprint))).thenReturn(providedFingerprintHash);
            String testToken = JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(expectedUser.getId().toString())
                    .withClaim(EMAIL_CLAIM, expectedUser.getEmail())
                    .withClaim(IS_CONFIRMED_CLAIM, expectedUser.isConfirmed())
                    .withClaim(FINGERPRINT_CLAIM, expectedHashedFingerprint)
                    .sign(ALGORITHM);
            assertThrows(AccessTokenInvalidException.class, () -> jwtTokenProvider.validateOne(testToken, fingerprint));
        }

        @EmptySource
        @NullSource
        @ParameterizedTest(name = "throws an error if given a fingerprint = \"{0}\"")
        void throwsAnErrorIfGivenAnIncorrectFingerprint(String fingerprintProvided) {
            UserDto expectedUser = UserFactory.generateOneDto();
            String expectedHashedFingerprint = UUID.randomUUID().toString();
            lenient().when(fingerprintHasher.hashFingerprint(eq(fingerprintProvided))).thenReturn(fingerprintProvided);
            String testToken = JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(expectedUser.getId().toString())
                    .withClaim(EMAIL_CLAIM, expectedUser.getEmail())
                    .withClaim(IS_CONFIRMED_CLAIM, expectedUser.isConfirmed())
                    .withClaim(FINGERPRINT_CLAIM, expectedHashedFingerprint)
                    .sign(ALGORITHM);
            assertThrows(AccessTokenInvalidException.class, () -> jwtTokenProvider.validateOne(testToken, fingerprintProvided));
        }
    }
}
