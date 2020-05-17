package com.tiernebre.tailgate.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "tailgate.refresh-token")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class RefreshTokenConfigurationProperties {
    private final Integer expirationWindowInMinutes;
}
