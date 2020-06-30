package com.tiernebre.zone_blitz.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@RequiredArgsConstructor
public class MailSenderConfiguration {
    private final TailgateEmailConfigurationProperties configurationProperties;

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(configurationProperties.getHost());
        javaMailSender.setPort(configurationProperties.getPort());
        return javaMailSender;
    }
}
