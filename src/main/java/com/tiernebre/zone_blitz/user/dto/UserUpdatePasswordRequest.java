package com.tiernebre.zone_blitz.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

import static com.tiernebre.zone_blitz.user.validator.UserValidationConstants.REQUIRED_OLD_PASSWORD_VALIDATION_MESSAGE;

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

    @NotBlank(message = REQUIRED_OLD_PASSWORD_VALIDATION_MESSAGE)
    String oldPassword;
}
