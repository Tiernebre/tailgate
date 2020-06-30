package com.tiernebre.zone_blitz.token.access.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "zone-blitz.jwt")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class JwtTokenConfigurationProperties {
    private final String secret;
    private final Integer expirationWindowInMinutes;
}
