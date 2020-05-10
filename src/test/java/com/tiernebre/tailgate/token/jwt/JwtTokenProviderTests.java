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

import static com.tiernebre.tailgate.token.jwt.JwtTokenProvider.EMAIL_CLAIM;
import static com.tiernebre.tailgate.token.jwt.JwtTokenProvider.ISSUER;
import static org.junit.jupiter.api.Assertions.*;

public class JwtTokenProviderTests {
    private static final String TEST_SECRET = "secret!";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(TEST_SECRET);

    private JwtTokenProvider jwtTokenService;

    @BeforeEach
    public void setup() {
        jwtTokenService = new JwtTokenProvider(
                ALGORITHM
        );
    }

    @Nested
    @DisplayName("generateOne")
    public class GenerateOneTests {
        @Test
        @DisplayName("returns the generated JSON web token with the correct claims")
        void returnsTheGeneratedJSONWebToken() throws GenerateTokenException {
            UserDto userDTO = UserFactory.generateOneDto();
            String generatedToken = jwtTokenService.generateOne(userDTO);
            JWTVerifier jwtVerifier = JWT.require(ALGORITHM)
                    .withIssuer(ISSUER)
                    .build();
            DecodedJWT decodedJWT = jwtVerifier.verify(generatedToken);
            assertAll(
                    () -> assertEquals(ISSUER, decodedJWT.getIssuer()),
                    () -> assertEquals(userDTO.getId().toString(), decodedJWT.getSubject()),
                    () -> assertEquals(userDTO.getEmail(), decodedJWT.getClaim("email").asString())
            );
        }

        @Test
        @DisplayName("throws a GenerateTokenException if the JWT token completely failed to sign")
        void throwsGenerateTokenExceptionIfTokenCannotBeSigned() throws GenerateTokenException {
            JwtTokenProvider jwtTokenServiceWithBorkedAlgorithm = new JwtTokenProvider(
                    null
            );
            UserDto userDTO = UserFactory.generateOneDto();
            assertThrows(GenerateTokenException.class, () -> jwtTokenServiceWithBorkedAlgorithm.generateOne(userDTO));
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
