package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.mail.TailgateEmailConfigurationProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserEmailConfirmationServiceTests {
    @InjectMocks
    private UserEmailConfirmationService userEmailConfirmationService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private TailgateEmailConfigurationProperties tailgateEmailConfigurationProperties;

    @Mock
    private UserEmailConfirmationConfigurationProperties configurationProperties;

    @Nested
    @DisplayName("sendOne")
    public class SendOneTests {
        @Test
        @DisplayName("sends off a formatted email from the provided user")
        public void sendsOffAFormattedEmailFromTheProvidedUser() {
            String from = "expectedTest@tailgate.io";
            when(tailgateEmailConfigurationProperties.getFrom()).thenReturn(from);
            String subject = "Confirm Your Account With Tailgate";
            String message = "Navigate to <link> to confirm your account";
            when(configurationProperties.getSubject()).thenReturn(subject);
            when(configurationProperties.getMessage()).thenReturn(message);
            UserDto user = UserFactory.generateOneDto();
            userEmailConfirmationService.sendOne(user);
            SimpleMailMessage expectedMessage = new SimpleMailMessage();
            expectedMessage.setTo(user.getEmail());
            expectedMessage.setFrom(from);
            expectedMessage.setSubject(subject);
            expectedMessage.setText(message);
            verify(javaMailSender, times(1)).send(eq(expectedMessage));
        }
    }
}
