package com.tiernebre.tailgate.password;

import com.tiernebre.tailgate.token.password_reset.PasswordResetTokenService;
import com.tiernebre.tailgate.user.UserFactory;
import com.tiernebre.tailgate.user.dto.UserDto;
import com.tiernebre.tailgate.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordResetServiceImplTests {
    @InjectMocks
    private PasswordResetServiceImpl passwordResetService;

    @Mock
    private PasswordResetTokenDeliveryService passwordResetTokenDeliveryService;

    @Mock
    private UserService userService;

    @Mock
    private PasswordResetTokenService passwordResetTokenService;

    @DisplayName("createOne")
    @Nested
    public class CreateOneTests {
        @Test
        @DisplayName("sends off a delivery with the password reset token if the username provided in the request is legitimate")
        public void sendsOffADeliveryWithThePasswordResetTokenIfTheUsernameProvidedInTheRequestIsLegitimate() {
            PasswordResetRequest passwordResetRequest = PasswordResetRequest.builder()
                    .email(UUID.randomUUID().toString() + ".com")
                    .build();
            UserDto expectedUser = UserFactory.generateOneDto();
            when(userService.findOneByEmail(eq(passwordResetRequest.getEmail()))).thenReturn(Optional.of(expectedUser));
            String passwordResetToken = UUID.randomUUID().toString();
            when(passwordResetTokenService.createOneForUser(eq(expectedUser))).thenReturn(passwordResetToken);
            passwordResetService.createOne(passwordResetRequest);
            verify(passwordResetTokenDeliveryService, times(1)).sendOne(eq(expectedUser), eq(passwordResetToken));
        }

        @Test
        @DisplayName("does not send off a delivery with the password reset token if the username provided in the request is illegitimate")
        public void doesNotSendOffADeliveryWithThePasswordResetTokenIfTheUsernameProvidedInTheRequestIsIllegitimate() {
            PasswordResetRequest passwordResetRequest = PasswordResetRequest.builder()
                    .email(UUID.randomUUID().toString() + ".com")
                    .build();
            when(userService.findOneByEmail(eq(passwordResetRequest.getEmail()))).thenReturn(Optional.empty());
            passwordResetService.createOne(passwordResetRequest);
            verify(passwordResetTokenService, times(0)).createOneForUser(any());
            verify(passwordResetTokenDeliveryService, times(0)).sendOne(any(), any());
        }
    }
}
