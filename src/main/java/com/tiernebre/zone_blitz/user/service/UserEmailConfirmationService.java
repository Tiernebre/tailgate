package com.tiernebre.zone_blitz.user.service;

import com.tiernebre.zone_blitz.mail.ZoneBlitzEmailConfigurationProperties;
import com.tiernebre.zone_blitz.token.user_confirmation.UserConfirmationTokenService;
import com.tiernebre.zone_blitz.user.configuration.UserEmailConfirmationConfigurationProperties;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserEmailConfirmationService implements UserConfirmationService {
    private final JavaMailSender mailSender;
    private final ZoneBlitzEmailConfigurationProperties zoneBlitzEmailConfigurationProperties;
    private final UserEmailConfirmationConfigurationProperties configurationProperties;
    private final UserConfirmationTokenService tokenService;

    @Override
    public void sendOne(UserDto userToConfirm) {
        SimpleMailMessage confirmationEmail = new SimpleMailMessage();
        confirmationEmail.setTo(userToConfirm.getEmail());
        confirmationEmail.setFrom(zoneBlitzEmailConfigurationProperties.getFrom());
        confirmationEmail.setSubject(configurationProperties.getSubject());
        confirmationEmail.setText(getText(userToConfirm));
        mailSender.send(confirmationEmail);
    }

    private String getText(UserDto user) {
        String originalMessage = configurationProperties.getMessage();
        String tagToReplace = configurationProperties.getConfirmationTokenTag();
        UUID confirmationToken = tokenService.findOrGenerateForUser(user);
        return originalMessage.replace(tagToReplace, confirmationToken.toString());
    }
}
