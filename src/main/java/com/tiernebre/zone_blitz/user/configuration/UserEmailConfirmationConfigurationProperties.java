package com.tiernebre.zone_blitz.user.configuration;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "zone-blitz.user.confirmation.email")
@ConstructorBinding
@Value
public class UserEmailConfirmationConfigurationProperties {
    String subject;
    String message;
    String confirmationTokenTag;
}
