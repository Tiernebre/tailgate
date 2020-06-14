package com.tiernebre.tailgate.user.service;

import com.tiernebre.tailgate.user.dto.ResetTokenUpdatePasswordRequest;

public interface UserPasswordService {
    /**
     * Updates a user password given a reset token
     * @param resetToken The reset token assigned from a forgotten password request.
     * @param updatePasswordRequest The request to handle a password reset.
     */
    void updateOneUsingResetToken(String resetToken, ResetTokenUpdatePasswordRequest updatePasswordRequest);
}
