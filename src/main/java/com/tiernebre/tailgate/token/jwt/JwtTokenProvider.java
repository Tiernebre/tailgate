package com.tiernebre.tailgate.token.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tiernebre.tailgate.token.GenerateAccessTokenException;
import com.tiernebre.tailgate.token.AccessTokenProvider;
import com.tiernebre.tailgate.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Generates / Validates JSON Web Tokens.
 */
@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements AccessTokenProvider {
    static final String ISSUER = "tailgate";
    static final String EMAIL_CLAIM = "email";

    private final Algorithm algorithm;
    private final JwtTokenConfigurationProperties configurationProperties;
    private final Clock clock;

    @Override
    public String generateOne(UserDto user) throws GenerateAccessTokenException {
        try {
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getId().toString())
                    .withClaim(EMAIL_CLAIM, user.getEmail())
                    .withExpiresAt(generateExpiresAt())
                    .sign(algorithm);
        } catch (Exception exception){
            throw new GenerateAccessTokenException(exception.getMessage());
        }
    }

    @Override
    public UserDto validateOne(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return mapDecodedJWTToUser(decodedJWT);
    }

    private UserDto mapDecodedJWTToUser(DecodedJWT decodedJWT) {
        return UserDto.builder()
                .id(Long.parseLong(decodedJWT.getSubject()))
                .email(decodedJWT.getClaim(EMAIL_CLAIM).asString())
                .build();
    }

    private Date generateExpiresAt() {
        return new Date(clock.millis() + TimeUnit.MINUTES.toMillis(configurationProperties.getExpirationWindowInMinutes()));
    }
}
