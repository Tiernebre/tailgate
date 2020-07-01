package com.tiernebre.zone_blitz.token.refresh;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "zone-blitz.refresh-token")
@ConstructorBinding
@Value
public class RefreshTokenConfigurationProperties {
    Integer expirationWindowInMinutes;
}
