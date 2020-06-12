package com.tiernebre.tailgate.password;

import com.tiernebre.tailgate.mail.TailgateEmailConfigurationProperties;
import com.tiernebre.tailgate.user.UserFactory;
import com.tiernebre.tailgate.user.dto.UserDto;
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
    private final static String EXPECTED_EMAIL_MESSAGE = "Please reset your password at https://tailgate.io/" + EXPECTED_EMAIL_PASSWORD_RESET_TOKEN_TAG;

    @InjectMocks
    private PasswordResetTokenEmailDeliveryService passwordResetTokenEmailDeliveryService;

    @Mock
    private MailSender mailSender;

    @Mock
    private TailgateEmailConfigurationProperties tailgateEmailConfigurationProperties;

    @Mock
    private PasswordResetEmailDeliveryConfigurationProperties passwordResetEmailDeliveryConfigurationProperties;

    @BeforeEach
    public void setup() {
        when(tailgateEmailConfigurationProperties.getFrom()).thenReturn("Tailgate From");
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
            String passwordResetToken = UUID.randomUUID().toString();
            passwordResetTokenEmailDeliveryService.sendOne(user, passwordResetToken);
            SimpleMailMessage expectedMailMessage = new SimpleMailMessage();
            expectedMailMessage.setTo(user.getEmail());
            expectedMailMessage.setFrom(tailgateEmailConfigurationProperties.getFrom());
            expectedMailMessage.setSubject(passwordResetEmailDeliveryConfigurationProperties.getSubject());
            String expectedMessage = EXPECTED_EMAIL_MESSAGE.replace(EXPECTED_EMAIL_PASSWORD_RESET_TOKEN_TAG, passwordResetToken);
            expectedMailMessage.setText(expectedMessage);
            verify(mailSender, times(1)).send(eq(expectedMailMessage));
       }
    }
}
