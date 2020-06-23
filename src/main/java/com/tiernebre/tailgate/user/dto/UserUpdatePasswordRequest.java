package com.tiernebre.tailgate.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserUpdatePasswordRequest extends UpdatePasswordRequest {
    @Builder
    public UserUpdatePasswordRequest(
            String oldPassword,
            String newPassword,
            String confirmationNewPassword
    ) {
        super(newPassword, confirmationNewPassword);
        this.oldPassword = oldPassword;
    }

    String oldPassword;
}
