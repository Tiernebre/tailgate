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
public class AsyncPasswordResetServiceTests {
    @InjectMocks
    private AsyncPasswordResetService asyncPasswordResetService;

    @Mock
    private PasswordResetService passwordResetService;

    @DisplayName("createOne")
    @Nested
    public class CreateOneTests {
        @Test
        @DisplayName("correctly passes request to synchronous service")
        public void correctlyPassesRequestToSynchronousService() {
            PasswordResetRequest passwordResetRequest = PasswordResetFactory.generateOneRequest();
            asyncPasswordResetService.createOne(passwordResetRequest);
            verify(passwordResetService, times(1)).createOne(eq(passwordResetRequest));
        }
    }
}
