package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.mail.TailgateEmailConfigurationProperties;
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

    @Override
    public void sendOne(UserDto userToConfirm) {
        SimpleMailMessage confirmationEmail = new SimpleMailMessage();
        confirmationEmail.setTo(userToConfirm.getEmail());
        confirmationEmail.setFrom(tailgateEmailConfigurationProperties.getFrom());
        confirmationEmail.setSubject(configurationProperties.getSubject());
        confirmationEmail.setText(configurationProperties.getMessage());
        mailSender.send(confirmationEmail);
    }
}
