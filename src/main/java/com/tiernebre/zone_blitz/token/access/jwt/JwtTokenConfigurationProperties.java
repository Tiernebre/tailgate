package com.tiernebre.zone_blitz.token.access.jwt;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "zone-blitz.jwt")
@ConstructorBinding
@Value
public class JwtTokenConfigurationProperties {
    String secret;
    Integer expirationWindowInMinutes;
}
