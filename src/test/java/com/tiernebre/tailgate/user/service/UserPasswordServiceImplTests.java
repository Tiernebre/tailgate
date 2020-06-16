package com.tiernebre.tailgate.user.service;

import com.tiernebre.tailgate.token.password_reset.PasswordResetTokenService;
import com.tiernebre.tailgate.user.dto.ResetTokenUpdatePasswordRequest;
import com.tiernebre.tailgate.user.exception.InvalidPasswordResetTokenException;
import com.tiernebre.tailgate.user.exception.InvalidUpdatePasswordRequestException;
import com.tiernebre.tailgate.user.exception.UserNotFoundForPasswordUpdateException;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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

    @Mock
    private PasswordResetTokenService passwordResetTokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

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
        void updatesPasswordForAUser() throws InvalidUpdatePasswordRequestException, InvalidPasswordResetTokenException, UserNotFoundForPasswordUpdateException {
            String newPassword = UUID.randomUUID().toString();
            String email = UUID.randomUUID().toString() + "@test.com";
            String resetToken = UUID.randomUUID().toString();
            ResetTokenUpdatePasswordRequest resetTokenUpdatePasswordRequest = ResetTokenUpdatePasswordRequest.builder()
                    .newPassword(newPassword)
                    .confirmationNewPassword(newPassword)
                    .email(email)
                    .build();
            doNothing().when(validator).validateUpdateRequest(eq(resetTokenUpdatePasswordRequest));
            String hashedNewPassword = UUID.randomUUID().toString();
            when(passwordEncoder.encode(eq(newPassword))).thenReturn(hashedNewPassword);
            when(repository.updateOneWithEmailAndNonExpiredResetToken(
                    eq(hashedNewPassword),
                    eq(email),
                    eq(resetToken)
            )).thenReturn(true);
            userPasswordService.updateOneUsingResetToken(resetToken, resetTokenUpdatePasswordRequest);
            verify(repository, times(1)).updateOneWithEmailAndNonExpiredResetToken(
                    eq(hashedNewPassword),
                    eq(email),
                    eq(resetToken)
            );
            verify(passwordResetTokenService, times(1)).deleteOneAsynchronously(resetToken);
        }

        @Test
        @DisplayName("throws not found error if the update did not occur")
        void throwsNotFoundErrorIfUpdateDidNotOccur() throws InvalidUpdatePasswordRequestException {
            String newPassword = UUID.randomUUID().toString();
            String email = UUID.randomUUID().toString() + "@test.com";
            String resetToken = UUID.randomUUID().toString();
            ResetTokenUpdatePasswordRequest resetTokenUpdatePasswordRequest = ResetTokenUpdatePasswordRequest.builder()
                    .newPassword(newPassword)
                    .confirmationNewPassword(newPassword)
                    .email(email)
                    .build();
            doNothing().when(validator).validateUpdateRequest(eq(resetTokenUpdatePasswordRequest));
            String hashedNewPassword = UUID.randomUUID().toString();
            when(passwordEncoder.encode(eq(newPassword))).thenReturn(hashedNewPassword);
            when(repository.updateOneWithEmailAndNonExpiredResetToken(
                    eq(hashedNewPassword),
                    eq(email),
                    eq(resetToken)
            )).thenReturn(false);
            assertThrows(
                    UserNotFoundForPasswordUpdateException.class,
                    () -> userPasswordService.updateOneUsingResetToken(resetToken, resetTokenUpdatePasswordRequest)
            );
        }

        @Test
        @DisplayName("throws not found error if the update did not occur")
        void throwsInvalidErrorIfAnAnswerDoesNotMatchWithAFoundOne() throws InvalidUpdatePasswordRequestException {
            String newPassword = UUID.randomUUID().toString();
            String email = UUID.randomUUID().toString() + "@test.com";
            String resetToken = UUID.randomUUID().toString();
            Map<Long, String> expectedSecurityQuestionAnswers = new HashMap<>();
            Map<Long, String> providedSecurityQuestionAnswers = new HashMap<>();
            for (long i = 0; i < 2; i++) {
                String originalHashedAnswer = UUID.randomUUID().toString();
                expectedSecurityQuestionAnswers.put(i, originalHashedAnswer);
                String plaintextAnswer = UUID.randomUUID().toString();
                providedSecurityQuestionAnswers.put(i, plaintextAnswer);
                lenient().when(passwordEncoder.matches(eq(plaintextAnswer.toLowerCase()), eq(originalHashedAnswer))).thenReturn(false);
            }
            lenient().when(repository.getSecurityQuestionAnswersForEmailAndNonExpiredResetToken(
                    eq(email),
                    eq(resetToken)
            )).thenReturn(expectedSecurityQuestionAnswers);
            ResetTokenUpdatePasswordRequest resetTokenUpdatePasswordRequest = ResetTokenUpdatePasswordRequest.builder()
                    .newPassword(newPassword)
                    .confirmationNewPassword(newPassword)
                    .email(email)
                    .securityQuestionAnswers(providedSecurityQuestionAnswers)
                    .build();
            doNothing().when(validator).validateUpdateRequest(eq(resetTokenUpdatePasswordRequest));
            assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordService.updateOneUsingResetToken(resetToken, resetTokenUpdatePasswordRequest)
            );
        }
    }
}
