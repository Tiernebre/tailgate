package com.tiernebre.zone_blitz.token.password_reset;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "zone-blitz.password-reset.token")
@ConstructorBinding
@Value
public class PasswordResetTokenConfigurationProperties {
    Integer expirationWindowInMinutes;
}
