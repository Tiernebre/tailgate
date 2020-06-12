package com.tiernebre.tailgate.password;

import com.tiernebre.tailgate.user.UserFactory;
import com.tiernebre.tailgate.user.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AsyncPasswordResetTokenDeliveryServiceTests {
    @InjectMocks
    private AsyncPasswordResetTokenDeliveryService asyncPasswordResetTokenDeliveryService;

    @Mock
    private PasswordResetTokenDeliveryService passwordResetTokenDeliveryService;

    @DisplayName("sendOne")
    @Nested
    public class SendOneTests {
        @Test
        @DisplayName("calls the password reset token delivery service")
        public void callsThePasswordResetTokenDeliveryService() {
            UserDto user = UserFactory.generateOneDto();
            String passwordResetToken = UUID.randomUUID().toString();
            asyncPasswordResetTokenDeliveryService.sendOne(user, passwordResetToken);
            verify(passwordResetTokenDeliveryService, times(1)).sendOne(eq(user), eq(passwordResetToken));
        }
    }
}
