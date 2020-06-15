package com.tiernebre.tailgate.user.service;

import com.tiernebre.tailgate.user.dto.ResetTokenUpdatePasswordRequest;
import com.tiernebre.tailgate.user.exception.InvalidPasswordResetTokenException;
import com.tiernebre.tailgate.user.exception.InvalidUpdatePasswordRequestException;
import com.tiernebre.tailgate.user.exception.UserNotFoundForPasswordUpdateException;

public interface UserPasswordService {
    /**
     * Updates a user password given a reset token
     * @param resetToken The reset token assigned from a forgotten password request.
     * @param updatePasswordRequest The request to handle a password reset.
     * @throws InvalidUpdatePasswordRequestException If the request to update a password is invalid.
     * @throws InvalidPasswordResetTokenException If the password reset token provided is invalid.
     * @throws UserNotFoundForPasswordUpdateException If the information provided did not lead to an update.
     */
    void updateOneUsingResetToken(String resetToken, ResetTokenUpdatePasswordRequest updatePasswordRequest)
            throws InvalidUpdatePasswordRequestException, InvalidPasswordResetTokenException, UserNotFoundForPasswordUpdateException;
}
