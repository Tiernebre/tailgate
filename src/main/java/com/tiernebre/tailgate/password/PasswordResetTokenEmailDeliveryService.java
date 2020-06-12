package com.tiernebre.tailgate.password;

import com.tiernebre.tailgate.user.dto.UserDto;

/**
 * Uses email as a channel to deliver a password reset token to a user.
 */
public class PasswordResetTokenEmailDeliveryService implements PasswordResetTokenDeliveryService {
    @Override
    public void sendOne(UserDto userDto, String passwordResetToken) {

    }
}
