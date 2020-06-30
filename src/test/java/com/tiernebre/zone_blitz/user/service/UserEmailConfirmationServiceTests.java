package com.tiernebre.zone_blitz.user.service;

import com.tiernebre.zone_blitz.mail.ZoneBlitzEmailConfigurationProperties;
import com.tiernebre.zone_blitz.token.user_confirmation.UserConfirmationTokenService;
import com.tiernebre.zone_blitz.user.UserFactory;
import com.tiernebre.zone_blitz.user.configuration.UserEmailConfirmationConfigurationProperties;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserEmailConfirmationServiceTests {
    @InjectMocks
    private UserEmailConfirmationService userEmailConfirmationService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private ZoneBlitzEmailConfigurationProperties zoneBlitzEmailConfigurationProperties;

    @Mock
    private UserEmailConfirmationConfigurationProperties configurationProperties;

    @Mock
    private UserConfirmationTokenService tokenService;

    @Nested
    @DisplayName("sendOne")
    public class SendOneTests {
        @Test
        @DisplayName("sends off a formatted email from the provided user")
        public void sendsOffAFormattedEmailFromTheProvidedUser() {
            String from = "expectedTest@zoneblitz.app";
            when(zoneBlitzEmailConfigurationProperties.getFrom()).thenReturn(from);
            String subject = "Confirm Your Account With Zone Blitz";
            String confirmationToken = UUID.randomUUID().toString();
            String confirmationTokenTag = "{{ confirmationToken }}";
            String message = "Navigate to " + confirmationTokenTag + " to confirm your account";
            when(configurationProperties.getSubject()).thenReturn(subject);
            when(configurationProperties.getMessage()).thenReturn(message);
            when(configurationProperties.getConfirmationTokenTag()).thenReturn(confirmationTokenTag);
            UserDto user = UserFactory.generateOneDto();
            when(tokenService.findOrGenerateForUser(eq(user))).thenReturn(confirmationToken);
            userEmailConfirmationService.sendOne(user);
            String expectedFormattedTextMessage = message.replace(confirmationTokenTag, confirmationToken);
            SimpleMailMessage expectedEmailSent = new SimpleMailMessage();
            expectedEmailSent.setTo(user.getEmail());
            expectedEmailSent.setFrom(from);
            expectedEmailSent.setSubject(subject);
            expectedEmailSent.setText(expectedFormattedTextMessage);
            verify(javaMailSender, times(1)).send(eq(expectedEmailSent));
        }
    }
}
