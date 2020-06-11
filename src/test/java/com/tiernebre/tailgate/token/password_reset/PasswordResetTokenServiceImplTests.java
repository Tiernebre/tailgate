package com.tiernebre.tailgate.token.password_reset;

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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordResetTokenServiceImplTests {
    @InjectMocks
    private PasswordResetTokenServiceImpl PasswordResetTokenService;

    @Mock
    private PasswordResetTokenRepository PasswordResetTokenRepository;

    @Nested
    @DisplayName("createOneForUser")
    public class CreateOneForUserTests {
        @Test
        @DisplayName("returns the created password reset token")
        public void returnsTheCreatedPasswordResetToken() {
            UserDto user = UserFactory.generateOneDto();
            String expectedPasswordResetToken = UUID.randomUUID().toString();
            when(PasswordResetTokenRepository.createOneForUser(eq(user))).thenReturn(PasswordResetTokenEntity.builder()
                    .token(expectedPasswordResetToken)
                    .build()
            );
            String gottenConfirmationToken = PasswordResetTokenService.createOneForUser(user);
            assertEquals(expectedPasswordResetToken, gottenConfirmationToken);
        }
    }

    @Nested
    @DisplayName("deleteOne")
    public class DeleteOneTests {
        @Test
        @DisplayName("deletes the given token")
        public void deletesTheGivenToken() {
            String passwordTokenToDelete = UUID.randomUUID().toString();
            PasswordResetTokenService.deleteOne(passwordTokenToDelete);
            verify(PasswordResetTokenRepository, times(1)).deleteOne(eq(passwordTokenToDelete));
        }
    }
}
