package com.tiernebre.zone_blitz.password;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PasswordResetRestfulControllerTests {
    @InjectMocks
    private PasswordResetRestfulController passwordResetRestfulController;

    @Mock
    private PasswordResetService passwordResetService;

    @DisplayName("resetPassword")
    @Nested
    public class ResetPasswordTests {
        @Test
        @DisplayName("resets a password for the given request")
        public void resetsAPasswordForTheGivenRequest() {
            PasswordResetRequest passwordResetRequest = PasswordResetFactory.generateOneRequest();
            passwordResetRestfulController.resetPassword(passwordResetRequest);
            verify(passwordResetService, times(1)).createOne(eq(passwordResetRequest));
        }
    }
}
