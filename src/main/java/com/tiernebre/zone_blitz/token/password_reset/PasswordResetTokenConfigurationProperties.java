package com.tiernebre.zone_blitz.token.password_reset;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "tailgate.password-reset.token")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class PasswordResetTokenConfigurationProperties {
    private final Integer expirationWindowInMinutes;
}
