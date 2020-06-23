package com.tiernebre.tailgate.user.service;

import com.tiernebre.tailgate.token.password_reset.PasswordResetTokenService;
import com.tiernebre.tailgate.user.dto.ResetTokenUpdatePasswordRequest;
import com.tiernebre.tailgate.user.dto.UserUpdatePasswordRequest;
import com.tiernebre.tailgate.user.dto.UserDto;
import com.tiernebre.tailgate.user.exception.InvalidPasswordResetTokenException;
import com.tiernebre.tailgate.user.exception.InvalidSecurityQuestionAnswerException;
import com.tiernebre.tailgate.user.exception.InvalidUpdatePasswordRequestException;
import com.tiernebre.tailgate.user.exception.UserNotFoundForPasswordUpdateException;
import com.tiernebre.tailgate.user.repository.UserPasswordRepository;
import com.tiernebre.tailgate.user.validator.UserPasswordValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserPasswordServiceImpl implements UserPasswordService {
    private final UserPasswordValidator validator;
    private final UserPasswordRepository repository;
    private final PasswordResetTokenService passwordResetTokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserSecurityQuestionsService userSecurityQuestionsService;

    @Override
    public void updateOneUsingResetToken(String resetToken, ResetTokenUpdatePasswordRequest updatePasswordRequest) throws
            InvalidUpdatePasswordRequestException,
            InvalidPasswordResetTokenException,
            UserNotFoundForPasswordUpdateException,
            InvalidSecurityQuestionAnswerException
    {
        validateResetToken(resetToken);
        validator.validateResetTokenUpdateRequest(updatePasswordRequest);
        String email = updatePasswordRequest.getEmail();
        userSecurityQuestionsService.validateAnswersForUserWithEmailAndResetToken(
                email,
                resetToken,
                updatePasswordRequest.getSecurityQuestionAnswers()
        );
        boolean updateOccurred = repository.updateOneWithEmailAndNonExpiredResetToken(
                passwordEncoder.encode(updatePasswordRequest.getNewPassword()),
                email,
                resetToken
        );
        if (updateOccurred) {
            passwordResetTokenService.deleteOneAsynchronously(resetToken);
        } else {
            throw new UserNotFoundForPasswordUpdateException();
        }
    }

    @Override
    public void updateOneForUser(UserDto userDto, UserUpdatePasswordRequest userUpdatePasswordRequest) throws UserNotFoundForPasswordUpdateException, InvalidUpdatePasswordRequestException {
        Long userId = userDto.getId();
        validateProvidedOldPassword(userId, userUpdatePasswordRequest.getOldPassword());
        boolean passwordUpdated = repository.updateOneForId(userId, userUpdatePasswordRequest.getNewPassword());
        if (!passwordUpdated) {
            throw new UserNotFoundForPasswordUpdateException();
        }
    }

    private void validateProvidedOldPassword(Long userId, String oldPassword) throws UserNotFoundForPasswordUpdateException, InvalidUpdatePasswordRequestException {
        String foundOldHashedPassword = repository
                .findOneForId(userId)
                .orElseThrow(UserNotFoundForPasswordUpdateException::new);
        if (!passwordEncoder.matches(oldPassword, foundOldHashedPassword)) {
            throw new InvalidUpdatePasswordRequestException(Collections.singleton("The password update contained invalid information."));
        }
    }

    private void validateResetToken(String resetToken) throws InvalidPasswordResetTokenException {
        if (StringUtils.isBlank(resetToken)) {
            throw new InvalidPasswordResetTokenException(Collections.singleton("The reset token must not be blank"));
        }
    }
}
