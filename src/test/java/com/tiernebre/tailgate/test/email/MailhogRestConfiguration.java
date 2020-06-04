package com.tiernebre.tailgate.test.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class MailhogRestConfiguration {
    @Bean
    public RestTemplate mailhogRestTemplate() {
        RestTemplate mailhogRestTemplate = new RestTemplate();
        mailhogRestTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:8025/api/v2"));
        mailhogRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return mailhogRestTemplate;
    }
}
