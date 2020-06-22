package com.tiernebre.tailgate.user.service;

import com.tiernebre.tailgate.mail.TailgateEmailConfigurationProperties;
import com.tiernebre.tailgate.token.user_confirmation.UserConfirmationTokenService;
import com.tiernebre.tailgate.user.configuration.UserEmailConfirmationConfigurationProperties;
import com.tiernebre.tailgate.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEmailConfirmationService implements UserConfirmationService {
    private final JavaMailSender mailSender;
    private final TailgateEmailConfigurationProperties tailgateEmailConfigurationProperties;
    private final UserEmailConfirmationConfigurationProperties configurationProperties;
    private final UserConfirmationTokenService tokenService;

    @Override
    public void sendOne(UserDto userToConfirm) {
        SimpleMailMessage confirmationEmail = new SimpleMailMessage();
        confirmationEmail.setTo(userToConfirm.getEmail());
        confirmationEmail.setFrom(tailgateEmailConfigurationProperties.getFrom());
        confirmationEmail.setSubject(configurationProperties.getSubject());
        confirmationEmail.setText(getText(userToConfirm));
        mailSender.send(confirmationEmail);
    }

    private String getText(UserDto user) {
        String originalMessage = configurationProperties.getMessage();
        String tagToReplace = configurationProperties.getConfirmationTokenTag();
        String confirmationToken = tokenService.findOrGenerateForUser(user);
        return originalMessage.replace(tagToReplace, confirmationToken);
    }
}
