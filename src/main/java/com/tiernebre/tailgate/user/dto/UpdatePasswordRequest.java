package com.tiernebre.tailgate.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.tiernebre.tailgate.user.validator.UserValidationConstants.MAXIMUM_PASSWORD_LENGTH;
import static com.tiernebre.tailgate.user.validator.UserValidationConstants.MINIMUM_PASSWORD_LENGTH;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public abstract class UpdatePasswordRequest {
    @NotBlank
    @Size(min = MINIMUM_PASSWORD_LENGTH, max = MAXIMUM_PASSWORD_LENGTH)
    String newPassword;

    String confirmationNewPassword;
}
