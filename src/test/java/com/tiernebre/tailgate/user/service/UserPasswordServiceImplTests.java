package com.tiernebre.tailgate.user.service;

import com.tiernebre.tailgate.user.dto.ResetTokenUpdatePasswordRequest;
import com.tiernebre.tailgate.user.exception.InvalidPasswordResetTokenException;
import com.tiernebre.tailgate.user.exception.InvalidUpdatePasswordRequestException;
import com.tiernebre.tailgate.user.repository.UserPasswordRepository;
import com.tiernebre.tailgate.user.validator.UserPasswordValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserPasswordServiceImplTests {
    @InjectMocks
    private UserPasswordServiceImpl userPasswordService;

    @Mock
    private UserPasswordValidator validator;

    @Mock
    private UserPasswordRepository repository;

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

        @EmptySource
        @NullSource
        @ValueSource(strings = { " " })
        @ParameterizedTest(name = "throws invalid error if the reset token is \"{0}\"")
        void throwsInvalidErrorIfTheResetTokenIsBlank(String resetToken) {
            assertThrows(
                    InvalidPasswordResetTokenException.class,
                    () -> userPasswordService.updateOneUsingResetToken(resetToken, ResetTokenUpdatePasswordRequest.builder().build())
            );
        }

        @Test
        @DisplayName("updates password for a user")
        void updatesPasswordForAUser() throws InvalidUpdatePasswordRequestException, InvalidPasswordResetTokenException {
            String newPassword = UUID.randomUUID().toString();
            String email = UUID.randomUUID().toString() + "@test.com";
            String resetToken = UUID.randomUUID().toString();
            ResetTokenUpdatePasswordRequest resetTokenUpdatePasswordRequest = ResetTokenUpdatePasswordRequest.builder()
                    .newPassword(newPassword)
                    .confirmationNewPassword(newPassword)
                    .email(email)
                    .build();
            doNothing().when(validator).validateUpdateRequest(eq(resetTokenUpdatePasswordRequest));
            userPasswordService.updateOneUsingResetToken(resetToken, resetTokenUpdatePasswordRequest);
            verify(repository, times(1)).updateOneWithEmailAndNonExpiredResetToken(
                    eq(newPassword),
                    eq(email),
                    eq(resetToken)
            );
        }
    }
}
