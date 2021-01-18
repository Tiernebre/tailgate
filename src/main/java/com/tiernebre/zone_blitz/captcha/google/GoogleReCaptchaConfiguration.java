package com.tiernebre.zone_blitz.captcha.google;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
@RequiredArgsConstructor
public class GoogleReCaptchaConfiguration {
    private final GoogleReCaptchaConfigurationProperties configurationProperties;

    @Bean
    public RestTemplate googleReCaptchaRestTemplate(RestTemplateBuilder templateBuilder) {
        return templateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(configurationProperties.getUrl()))
                .build();
    }
}
