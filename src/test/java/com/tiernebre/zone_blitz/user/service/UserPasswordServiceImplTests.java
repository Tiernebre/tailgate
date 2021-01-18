package com.tiernebre.zone_blitz.user.service;

import com.tiernebre.zone_blitz.token.password_reset.PasswordResetTokenService;
import com.tiernebre.zone_blitz.user.UpdatePasswordRequestFactory;
import com.tiernebre.zone_blitz.user.UserFactory;
import com.tiernebre.zone_blitz.user.dto.ResetTokenUpdatePasswordRequest;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import com.tiernebre.zone_blitz.user.dto.UserUpdatePasswordRequest;
import com.tiernebre.zone_blitz.user.exception.InvalidPasswordResetTokenException;
import com.tiernebre.zone_blitz.user.exception.InvalidSecurityQuestionAnswerException;
import com.tiernebre.zone_blitz.user.exception.InvalidUpdatePasswordRequestException;
import com.tiernebre.zone_blitz.user.exception.UserNotFoundForPasswordUpdateException;
import com.tiernebre.zone_blitz.user.repository.UserPasswordRepository;
import com.tiernebre.zone_blitz.user.validator.UserPasswordValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.tiernebre.zone_blitz.user.service.UserPasswordServiceImpl.REQUIRED_USER_VALIDATION_MESSAGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
                    .validateUpdateRequest(eq(resetTokenUpdatePasswordRequest));
            assertThrows(
                    InvalidUpdatePasswordRequestException.class,
                    () -> userPasswordService.updateOneUsingResetToken(UUID.randomUUID(), resetTokenUpdatePasswordRequest)
            );
        }

        @Test
        @DisplayName("throws invalid error if security question answers are invalid")
        void throwsInvalidErrorIfSecurityQuestionAnswersAreInvalid() throws InvalidUpdatePasswordRequestException, InvalidSecurityQuestionAnswerException {
            String newPassword = UUID.randomUUID().toString();
            String email = UUID.randomUUID().toString() + "@test.com";
            UUID resetToken = UUID.randomUUID();
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
            doNothing().when(validator).validateUpdateRequest(eq(resetTokenUpdatePasswordRequest));
            doThrow(new InvalidSecurityQuestionAnswerException(Collections.emptySet()))
                    .when(userSecurityQuestionsService)
                    .validateAnswersForUserWithEmailAndResetToken(eq(email), eq(resetToken), eq(securityQuestionAnswers));
            assertThrows(
                    InvalidSecurityQuestionAnswerException.class,
                    () -> userPasswordService.updateOneUsingResetToken(resetToken, resetTokenUpdatePasswordRequest)
            );
        }

        @Test
        @DisplayName("updates password for a user")
        void updatesPasswordForAUser() throws InvalidUpdatePasswordRequestException, InvalidPasswordResetTokenException, UserNotFoundForPasswordUpdateException, InvalidSecurityQuestionAnswerException {
            String newPassword = UUID.randomUUID().toString();
            String email = UUID.randomUUID().toString() + "@test.com";
            UUID resetToken = UUID.randomUUID();
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
            doNothing().when(validator).validateUpdateRequest(eq(resetTokenUpdatePasswordRequest));
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
            UUID resetToken = UUID.randomUUID();
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
    }

    @Nested
    @DisplayName("updateOneForUser")
    public class UpdateOneForUserTests {
        @Test
        @DisplayName("does not allow a null user")
        void doesNotAllowANullUser() {
            UserUpdatePasswordRequest updatePasswordRequest = UpdatePasswordRequestFactory.generateOne();
            NullPointerException thrown = assertThrows(
                    NullPointerException.class,
                    () -> userPasswordService.updateOneForUser(null, updatePasswordRequest)
            );
            assertEquals(REQUIRED_USER_VALIDATION_MESSAGE, thrown.getMessage());
        }

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
            String oldHashedPassword = UUID.randomUUID().toString();
            String newHashedPassword = UUID.randomUUID().toString();
            when(repository.findOneForId(eq(user.getId()))).thenReturn(Optional.of(oldHashedPassword));
            when(passwordEncoder.matches(eq(updatePasswordRequest.getOldPassword()), eq(oldHashedPassword))).thenReturn(true);
            when(passwordEncoder.encode((eq(updatePasswordRequest.getNewPassword())))).thenReturn(newHashedPassword);
            when(repository.updateOneForId(eq(user.getId()), eq(newHashedPassword))).thenReturn(false);
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

        @Test
        @DisplayName("updates a valid users password with a valid request")
        void updatesAValidUsersPasswordWithAValidRequest() {
            UserDto user = UserFactory.generateOneDto();
            UserUpdatePasswordRequest updatePasswordRequest = UpdatePasswordRequestFactory.generateOne();
            String oldHashedPassword =  UUID.randomUUID().toString();
            String newHashedPassword = UUID.randomUUID().toString();
            when(repository.findOneForId(eq(user.getId()))).thenReturn(Optional.of(oldHashedPassword));
            when(passwordEncoder.matches(eq(updatePasswordRequest.getOldPassword()), eq(oldHashedPassword))).thenReturn(true);
            when(passwordEncoder.encode((eq(updatePasswordRequest.getNewPassword())))).thenReturn(newHashedPassword);
            when(repository.updateOneForId(eq(user.getId()), eq(newHashedPassword))).thenReturn(true);
            assertDoesNotThrow(() -> userPasswordService.updateOneForUser(user, updatePasswordRequest));
        }
    }
}
