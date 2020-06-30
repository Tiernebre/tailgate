package com.tiernebre.zone_blitz.password;

import com.tiernebre.zone_blitz.user.dto.UserDto;

public interface PasswordResetTokenDeliveryService {
   /**
    * Sends off a delivery containing the password reset token to a provided user.
    * @param userDto The user to send a delivery to.
    * @param passwordResetToken The password reset token to send off.
    */
   void sendOne(UserDto userDto, String passwordResetToken);
}
