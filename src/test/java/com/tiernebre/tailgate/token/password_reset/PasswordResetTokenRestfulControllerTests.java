package com.tiernebre.tailgate.token.password_reset;

import com.tiernebre.tailgate.user.dto.ResetTokenUpdatePasswordRequest;
import com.tiernebre.tailgate.user.exception.InvalidPasswordResetTokenException;
import com.tiernebre.tailgate.user.exception.InvalidUpdatePasswordRequestException;
import com.tiernebre.tailgate.user.exception.UserNotFoundForPasswordUpdateException;
import com.tiernebre.tailgate.user.service.UserPasswordService;
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
public class PasswordResetTokenRestfulControllerTests {
    @InjectMocks
    private PasswordResetTokenRestfulController passwordResetTokenRestfulController;

    @Mock
    private UserPasswordService userPasswordService;

    @Nested
    @DisplayName("updatePasswordUsingResetToken")
    class UpdatePasswordUsingResetTokenTests {
        @Test
        @DisplayName("passes through the request and params properly")
        void passesThroughTheRequestAndParamsProperly() throws InvalidUpdatePasswordRequestException, InvalidPasswordResetTokenException, UserNotFoundForPasswordUpdateException {
            String resetToken = UUID.randomUUID().toString();
            ResetTokenUpdatePasswordRequest resetTokenUpdatePasswordRequest = ResetTokenUpdatePasswordRequest.builder()
                    .email(UUID.randomUUID().toString() + "@test.com")
                    .newPassword(UUID.randomUUID().toString())
                    .confirmationNewPassword(UUID.randomUUID().toString())
                    .build();
            passwordResetTokenRestfulController.updatePasswordUsingResetToken(resetToken, resetTokenUpdatePasswordRequest);
            verify(userPasswordService, times(1)).updateOneUsingResetToken(eq(resetToken), eq(resetTokenUpdatePasswordRequest));
        }
    }
}
