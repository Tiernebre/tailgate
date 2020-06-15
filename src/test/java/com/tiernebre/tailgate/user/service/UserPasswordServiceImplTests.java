package com.tiernebre.tailgate.user.service;

import com.tiernebre.tailgate.user.dto.ResetTokenUpdatePasswordRequest;
import com.tiernebre.tailgate.user.exception.InvalidUpdatePasswordRequestException;
import com.tiernebre.tailgate.user.validator.UserPasswordValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class UserPasswordServiceImplTests {
    @InjectMocks
    private UserPasswordServiceImpl userPasswordService;

    @Mock
    private UserPasswordValidator validator;

    @Nested
    @DisplayName("updateOneUsingResetToken")
    class UpdateOneUsingResetTokenTests {
        @Test
        @DisplayName("throws invalid error if the request is invalid")
        void throwsInvalidErrorIfTheRequestIsInvalid() throws InvalidUpdatePasswordRequestException {
            ResetTokenUpdatePasswordRequest resetTokenUpdatePasswordRequest = ResetTokenUpdatePasswordRequest.builder().build();
            doThrow(new InvalidUpdatePasswordRequestException(Collections.emptySet()))
                    .when(validator)
                    .validateUpdateRequest(eq(resetTokenUpdatePasswordRequest));
            assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordService.updateOneUsingResetToken(UUID.randomUUID().toString(), resetTokenUpdatePasswordRequest)
            );
        }
    }
}
