package com.tiernebre.zone_blitz.password;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "zone-blitz.password-reset.email")
@ConstructorBinding
@Value
public class PasswordResetEmailDeliveryConfigurationProperties {
    String subject;
    String message;
    String passwordResetTokenTag;
}
