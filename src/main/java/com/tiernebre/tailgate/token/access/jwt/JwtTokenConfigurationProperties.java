package com.tiernebre.tailgate.token.access.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "tailgate.jwt")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class JwtTokenConfigurationProperties {
    private final String secret;
    private final Integer expirationWindowInMinutes;
}