package com.tiernebre.tailgate.user.service;

import com.tiernebre.tailgate.user.dto.ResetTokenUpdatePasswordRequest;
import com.tiernebre.tailgate.user.dto.UpdatePasswordRequest;
import com.tiernebre.tailgate.user.dto.UserDto;
import com.tiernebre.tailgate.user.exception.InvalidPasswordResetTokenException;
import com.tiernebre.tailgate.user.exception.InvalidSecurityQuestionAnswerException;
import com.tiernebre.tailgate.user.exception.InvalidUpdatePasswordRequestException;
import com.tiernebre.tailgate.user.exception.UserNotFoundForPasswordUpdateException;

public interface UserPasswordService {
    /**
     * Updates a user password given a reset token.
     *
     * This is useful for situations where a user has forgotten their old password
     * and can't use it to verify a normal password update. Instead, they receive
     * a reset password token from a secure side channel that is used here along
     * with their email as a means to safely perform a password update.
     *
     * @param resetToken The reset token assigned from a forgotten password request.
     * @param updatePasswordRequest The request to handle a password reset.
     * @throws InvalidUpdatePasswordRequestException If the request to update a password is invalid.
     * @throws InvalidPasswordResetTokenException If the password reset token provided is invalid.
     * @throws UserNotFoundForPasswordUpdateException If the information provided did not lead to an update.
     */
    void updateOneUsingResetToken(String resetToken, ResetTokenUpdatePasswordRequest updatePasswordRequest)
            throws InvalidUpdatePasswordRequestException, InvalidPasswordResetTokenException, UserNotFoundForPasswordUpdateException, InvalidSecurityQuestionAnswerException;

    /**
     * Updates a password for a given user.
     *
     * Will check and ensure that a user needs to provide their correct old password in order
     * to properly update.
     *
     * @param userDto The user to update a password for.
     * @param updatePasswordRequest The details about the password update to perform.
     */
    void updateOneForUser(UserDto userDto, UpdatePasswordRequest updatePasswordRequest) throws UserNotFoundForPasswordUpdateException;
}
