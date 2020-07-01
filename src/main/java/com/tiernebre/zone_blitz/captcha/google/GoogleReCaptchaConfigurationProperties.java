package com.tiernebre.zone_blitz.captcha.google;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "google.recaptcha")
@Value
public class GoogleReCaptchaConfigurationProperties {
    String url;
}
