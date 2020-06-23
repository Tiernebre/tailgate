package com.tiernebre.tailgate.user.service;

import com.tiernebre.tailgate.token.password_reset.PasswordResetTokenService;
import com.tiernebre.tailgate.user.UpdatePasswordRequestFactory;
import com.tiernebre.tailgate.user.UserFactory;
import com.tiernebre.tailgate.user.dto.ResetTokenUpdatePasswordRequest;
import com.tiernebre.tailgate.user.dto.UserUpdatePasswordRequest;
import com.tiernebre.tailgate.user.dto.UserDto;
import com.tiernebre.tailgate.user.exception.InvalidPasswordResetTokenException;
import com.tiernebre.tailgate.user.exception.InvalidSecurityQuestionAnswerException;
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
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
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

    @Mock
    private UserSecurityQuestionsService userSecurityQuestionsService;

    @Nested
    @DisplayName("updateOneUsingResetToken")
    class UpdateOneUsingResetTokenTests {
        @Test
        @DisplayName("throws invalid error if the request is invalid")
        void throwsInvalidErrorIfTheRequestIsInvalid() throws InvalidUpdatePasswordRequestException {
            ResetTokenUpdatePasswordRequest resetTokenUpdatePasswordRequest = ResetTokenUpdatePasswordRequest.builder().build();
            doThrow(new InvalidUpdatePasswordRequestException(Collections.emptySet()))
                    .when(validator)
                    .validateResetTokenUpdateRequest(eq(resetTokenUpdatePasswordRequest));
            assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordService.updateOneUsingResetToken(UUID.randomUUID().toString(), resetTokenUpdatePasswordRequest)
            );
        }

        @Test
        @DisplayName("throws invalid error if security question answers are invalid")
        void throwsInvalidErrorIfSecurityQuestionAnswersAreInvalid() throws InvalidUpdatePasswordRequestException, InvalidSecurityQuestionAnswerException {
            String newPassword = UUID.randomUUID().toString();
            String email = UUID.randomUUID().toString() + "@test.com";
            String resetToken = UUID.randomUUID().toString();
            Map<Long, String> securityQuestionAnswers = ImmutableMap.of(
                    1L, UUID.randomUUID().toString(),
                    2L, UUID.randomUUID().toString()
            );
            ResetTokenUpdatePasswordRequest resetTokenUpdatePasswordRequest = ResetTokenUpdatePasswordRequest.builder()
                    .newPassword(newPassword)
                    .confirmationNewPassword(newPassword)
                    .email(email)
                    .securityQuestionAnswers(securityQuestionAnswers)
                    .build();
            doNothing().when(validator).validateResetTokenUpdateRequest(eq(resetTokenUpdatePasswordRequest));
            doThrow(new InvalidSecurityQuestionAnswerException(Collections.emptySet()))
                    .when(userSecurityQuestionsService)
                    .validateAnswersForUserWithEmailAndResetToken(eq(email), eq(resetToken), eq(securityQuestionAnswers));
            assertThrows(
                    InvalidSecurityQuestionAnswerException.class,
                    () -> userPasswordService.updateOneUsingResetToken(resetToken, resetTokenUpdatePasswordRequest)
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
        void updatesPasswordForAUser() throws InvalidUpdatePasswordRequestException, InvalidPasswordResetTokenException, UserNotFoundForPasswordUpdateException, InvalidSecurityQuestionAnswerException {
            String newPassword = UUID.randomUUID().toString();
            String email = UUID.randomUUID().toString() + "@test.com";
            String resetToken = UUID.randomUUID().toString();
            Map<Long, String> securityQuestionAnswers = ImmutableMap.of(
                    1L, UUID.randomUUID().toString(),
                    2L, UUID.randomUUID().toString()
            );
            ResetTokenUpdatePasswordRequest resetTokenUpdatePasswordRequest = ResetTokenUpdatePasswordRequest.builder()
                    .newPassword(newPassword)
                    .confirmationNewPassword(newPassword)
                    .email(email)
                    .securityQuestionAnswers(securityQuestionAnswers)
                    .build();
            doNothing().when(validator).validateResetTokenUpdateRequest(eq(resetTokenUpdatePasswordRequest));
            doNothing().when(userSecurityQuestionsService).validateAnswersForUserWithEmailAndResetToken(eq(email), eq(resetToken), eq(securityQuestionAnswers));
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
            doNothing().when(validator).validateResetTokenUpdateRequest(eq(resetTokenUpdatePasswordRequest));
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
    }

    @Nested
    @DisplayName("updateOneForUser")
    public class UpdateOneForUserTests {
        @Test
        @DisplayName("throws a not found error if an old password was not found for the provided user")
        void throwsANotFoundErrorIfAnOldPasswordWasNotFoundForTheProvidedUser() {
            UserDto user = UserFactory.generateOneDto();
            UserUpdatePasswordRequest updatePasswordRequest = UpdatePasswordRequestFactory.generateOne();
            when(repository.findOneForId(eq(user.getId()))).thenReturn(Optional.empty());
            assertThrows(
                UserNotFoundForPasswordUpdateException.class,
                () -> userPasswordService.updateOneForUser(user, updatePasswordRequest)
            );
        }

        @Test
        @DisplayName("throws a not found error if a password update did not occur")
        void throwsANotFoundErrorIfAPasswordUpdateDidNotOccur() {
            UserDto user = UserFactory.generateOneDto();
            UserUpdatePasswordRequest updatePasswordRequest = UpdatePasswordRequestFactory.generateOne();
            String oldHashedPassword =  UUID.randomUUID().toString();
            when(repository.findOneForId(eq(user.getId()))).thenReturn(Optional.of(oldHashedPassword));
            when(passwordEncoder.matches(eq(updatePasswordRequest.getOldPassword()), eq(oldHashedPassword))).thenReturn(true);
            when(repository.updateOneForId(eq(user.getId()), eq(updatePasswordRequest.getNewPassword()))).thenReturn(false);
            assertThrows(
                    UserNotFoundForPasswordUpdateException.class,
                    () -> userPasswordService.updateOneForUser(user, updatePasswordRequest)
            );
        }

        @Test
        @DisplayName("throws an invalid error if a password update contained the incorrect old password")
        void throwsAnInvalidErrorIfAPasswordUpdateContainedTheIncorrectOldPassword() {
            UserDto user = UserFactory.generateOneDto();
            UserUpdatePasswordRequest updatePasswordRequest = UpdatePasswordRequestFactory.generateOne();
            String oldHashedPassword =  UUID.randomUUID().toString();
            when(repository.findOneForId(eq(user.getId()))).thenReturn(Optional.of(oldHashedPassword));
            when(passwordEncoder.matches(eq(updatePasswordRequest.getOldPassword()), eq(oldHashedPassword))).thenReturn(false);
            assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordService.updateOneForUser(user, updatePasswordRequest)
            );
        }
    }
}
