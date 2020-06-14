package com.tiernebre.tailgate.user.service;

public interface UserPasswordService {
    /**
     * Updates a user password given a reset token
     * @param resetToken The reset token assigned from a forgotten password request.
     */
    void updateOneWithResetToken(String resetToken);
}
