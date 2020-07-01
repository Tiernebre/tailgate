package com.tiernebre.zone_blitz.captcha.google;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "google.recaptcha")
@Value
@ConstructorBinding
public class GoogleReCaptchaConfigurationProperties {
    String url;
}
