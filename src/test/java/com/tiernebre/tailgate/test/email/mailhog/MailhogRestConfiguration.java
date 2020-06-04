package com.tiernebre.tailgate.test.email.mailhog;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
@Data
public class MailhogRestConfiguration {
    @Value("${mailhog.host:localhost}")
    private String host;

    @Value("${mailhog.port:8025}")
    private int port;

    @Bean
    public RestTemplate mailhogRestTemplate() {
        RestTemplate mailhogRestTemplate = new RestTemplate();
        String rootUrl = String.format("http://%s:%d/api/v2", host, port);
        mailhogRestTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(rootUrl));
        mailhogRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return mailhogRestTemplate;
    }
}
