package com.tiernebre.zone_blitz.captcha.google;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "google.recaptcha")
@ConstructorBinding
@Getter
@RequiredArgsConstructor
public class GoogleReCaptchaConfigurationProperties {
    private final String url;
    private final String secret;
}
