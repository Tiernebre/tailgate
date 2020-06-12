package com.tiernebre.tailgate.password;

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

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PasswordResetServiceImplTests {
    @InjectMocks
    private PasswordResetServiceImpl passwordResetService;

    @Mock
    private PasswordResetTokenDeliveryService passwordResetTokenDeliveryService;

    @Mock
    private UserService userService;

    @DisplayName("createOne")
    @Nested
    public class CreateOneTests {
        @Test
        @DisplayName("sends off an email with the password reset token if the username provided in the request is legitimate")
        public void sendsOffAnEmailWithThePasswordResetTokenIfTheUsernameProvidedInTheRequestIsLegitimate() {
            PasswordResetRequest passwordResetRequest = PasswordResetRequest.builder()
                    .email(UUID.randomUUID().toString() + ".com")
                    .build();
            UserDto expectedUser = UserFactory.generateOneDto();
            String passwordResetToken = UUID.randomUUID().toString();
            passwordResetService.createOne(passwordResetRequest);
            verify(passwordResetTokenDeliveryService, times(1)).sendOne(eq(expectedUser), eq(passwordResetToken));
        }
    }
}
