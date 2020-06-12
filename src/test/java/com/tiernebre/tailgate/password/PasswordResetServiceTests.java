package com.tiernebre.tailgate.password;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class PasswordResetServiceTests {
    @InjectMocks
    private PasswordResetService passwordResetService;

    @DisplayName("createOne")
    @Nested
    public class CreateOneTests {
        @Test
        @DisplayName("sends off an email with the password reset token if the username provided in the request is legitimate")
        public void sendsOffAnEmailWithThePasswordResetTokenIfTheUsernameProvidedInTheRequestIsLegitimate() {
            PasswordResetRequest passwordResetRequest = PasswordResetRequest.builder()
                    .email(UUID.randomUUID().toString() + ".com")
                    .build();
            passwordResetService.createOne(passwordResetRequest);
        }
    }
}
