package com.tiernebre.tailgate.password;

import com.tiernebre.tailgate.mail.TailgateEmailConfigurationProperties;
import com.tiernebre.tailgate.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * Uses email as a channel to deliver a password reset token to a user.
 */
@Service
@RequiredArgsConstructor
public class PasswordResetTokenEmailDeliveryService implements PasswordResetTokenDeliveryService {
    private final MailSender mailSender;
    private final TailgateEmailConfigurationProperties tailgateEmailConfigurationProperties;
    private final PasswordResetEmailDeliveryConfigurationProperties configurationProperties;

    @Override
    public void sendOne(UserDto userDto, String passwordResetToken) {
        SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
        passwordResetEmail.setSubject(configurationProperties.getSubject());
        passwordResetEmail.setTo(userDto.getEmail());
        passwordResetEmail.setFrom(tailgateEmailConfigurationProperties.getFrom());
        passwordResetEmail.setText(formatMessage(passwordResetToken));
        mailSender.send(passwordResetEmail);
    }

    private String formatMessage(String passwordResetToken) {
        return configurationProperties.getMessage().replace(configurationProperties.getPasswordResetTokenTag(), passwordResetToken);
    }
}
