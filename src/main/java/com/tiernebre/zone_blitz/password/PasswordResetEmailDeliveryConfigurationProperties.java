package com.tiernebre.zone_blitz.password;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "tailgate.password-reset.email")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class PasswordResetEmailDeliveryConfigurationProperties {
    private final String subject;
    private final String message;
    private final String passwordResetTokenTag;
}
