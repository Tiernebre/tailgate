package com.tiernebre.tailgate.user.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "tailgate.user.confirmation.email")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class UserEmailConfirmationConfigurationProperties {
    private final String subject;
    private final String message;
    private final String confirmationTokenTag;
}
