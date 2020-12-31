package com.tiernebre.zone_blitz.password;

import com.tiernebre.zone_blitz.mail.ZoneBlitzEmailConfigurationProperties;
import com.tiernebre.zone_blitz.user.UserFactory;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordResetTokenEmailDeliveryServiceTests {
    private final static String EXPECTED_EMAIL_PASSWORD_RESET_TOKEN_TAG = "{{ passwordResetToken }}";
    private final static String EXPECTED_EMAIL_MESSAGE = "Please reset your password at https://zoneblitz.app/" + EXPECTED_EMAIL_PASSWORD_RESET_TOKEN_TAG;

    @InjectMocks
    private PasswordResetTokenEmailDeliveryService passwordResetTokenEmailDeliveryService;

    @Mock
    private MailSender mailSender;

    @Mock
    private ZoneBlitzEmailConfigurationProperties zoneBlitzEmailConfigurationProperties;

    @Mock
    private PasswordResetEmailDeliveryConfigurationProperties passwordResetEmailDeliveryConfigurationProperties;

    @BeforeEach
    public void setup() {
        when(zoneBlitzEmailConfigurationProperties.getFrom()).thenReturn("Zone Blitz From");
        when(passwordResetEmailDeliveryConfigurationProperties.getSubject()).thenReturn("Reset Your Password");
        when(passwordResetEmailDeliveryConfigurationProperties.getPasswordResetTokenTag()).thenReturn(EXPECTED_EMAIL_PASSWORD_RESET_TOKEN_TAG);
        when(passwordResetEmailDeliveryConfigurationProperties.getMessage()).thenReturn(EXPECTED_EMAIL_MESSAGE);
    }

    @Nested
    @DisplayName("sendOne")
    public class SendOneTests {
        @Test
        @DisplayName("sends off a properly formatted email to the user with a password reset token")
        void sendsOffAProperlyFormattedEmailToTheUserWithAPasswordResetToken() {
            UserDto user = UserFactory.generateOneDto();
            UUID passwordResetToken = UUID.randomUUID();
            passwordResetTokenEmailDeliveryService.sendOne(user, passwordResetToken);
            SimpleMailMessage expectedMailMessage = new SimpleMailMessage();
            expectedMailMessage.setTo(user.getEmail());
            expectedMailMessage.setFrom(zoneBlitzEmailConfigurationProperties.getFrom());
            expectedMailMessage.setSubject(passwordResetEmailDeliveryConfigurationProperties.getSubject());
            String expectedMessage = EXPECTED_EMAIL_MESSAGE.replace(EXPECTED_EMAIL_PASSWORD_RESET_TOKEN_TAG, passwordResetToken.toString());
            expectedMailMessage.setText(expectedMessage);
            verify(mailSender, times(1)).send(eq(expectedMailMessage));
       }
    }
}
