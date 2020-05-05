package com.tiernebre.tailgate.token.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JwtTokenConfiguration {
    private final JwtTokenConfigurationProperties properties;

    @Bean
    public Algorithm jwtAlgorithm() {
        return Algorithm.HMAC256(properties.getSecret());
    }
}
