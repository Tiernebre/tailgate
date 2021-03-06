package com.tiernebre.zone_blitz.token.password_reset;

import com.tiernebre.zone_blitz.user.UserFactory;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordResetTokenServiceImplTests {
    @InjectMocks
    private PasswordResetTokenServiceImpl passwordResetTokenService;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Nested
    @DisplayName("createOneForUser")
    public class CreateOneForUserTests {
        @Test
        @DisplayName("returns the created password reset token")
        public void returnsTheCreatedPasswordResetToken() {
            UserDto user = UserFactory.generateOneDto();
            UUID expectedPasswordResetToken = UUID.randomUUID();
            when(passwordResetTokenRepository.createOneForUser(eq(user))).thenReturn(PasswordResetTokenEntity.builder()
                    .token(expectedPasswordResetToken)
                    .build()
            );
            UUID gottenConfirmationToken = passwordResetTokenService.createOneForUser(user);
            assertEquals(expectedPasswordResetToken, gottenConfirmationToken);
        }
    }

    @Nested
    @DisplayName("deleteOne")
    public class DeleteOneTests {
        @Test
        @DisplayName("deletes the given token")
        public void deletesTheGivenToken() {
            UUID passwordTokenToDelete = UUID.randomUUID();
            passwordResetTokenService.deleteOneAsynchronously(passwordTokenToDelete);
            verify(passwordResetTokenRepository, times(1)).deleteOne(eq(passwordTokenToDelete));
        }
    }
}
