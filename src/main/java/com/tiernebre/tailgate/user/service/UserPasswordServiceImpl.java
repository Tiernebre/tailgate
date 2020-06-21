package com.tiernebre.tailgate.user.service;

import com.tiernebre.tailgate.token.password_reset.PasswordResetTokenService;
import com.tiernebre.tailgate.user.dto.ResetTokenUpdatePasswordRequest;
import com.tiernebre.tailgate.user.exception.InvalidPasswordResetTokenException;
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

    @Override
    public void updateOneUsingResetToken(String resetToken, ResetTokenUpdatePasswordRequest updatePasswordRequest)
            throws InvalidUpdatePasswordRequestException, InvalidPasswordResetTokenException, UserNotFoundForPasswordUpdateException {
        validateResetToken(resetToken);
        validator.validateUpdateRequest(updatePasswordRequest);
        boolean updateOccurred = repository.updateOneWithEmailAndNonExpiredResetToken(
                passwordEncoder.encode(updatePasswordRequest.getNewPassword()),
                updatePasswordRequest.getEmail(),
                resetToken
        );
        if (updateOccurred) {
            passwordResetTokenService.deleteOneAsynchronously(resetToken);
        } else {
            throw new UserNotFoundForPasswordUpdateException();
        }
    }

    private void validateResetToken(String resetToken) throws InvalidPasswordResetTokenException {
        if (StringUtils.isBlank(resetToken)) {
            throw new InvalidPasswordResetTokenException(Collections.singleton("The reset token must not be blank"));
        }
    }
}
