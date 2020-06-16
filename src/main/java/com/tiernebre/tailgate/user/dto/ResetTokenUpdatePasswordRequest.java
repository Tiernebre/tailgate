package com.tiernebre.tailgate.user.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.Map;

import static com.tiernebre.tailgate.user.validator.UserValidationConstants.MAXIMUM_PASSWORD_LENGTH;
import static com.tiernebre.tailgate.user.validator.UserValidationConstants.MINIMUM_PASSWORD_LENGTH;

@Value
@Builder
public class ResetTokenUpdatePasswordRequest {
    @NotBlank
    @Email
    String email;

    @NotBlank
    @Size(min = MINIMUM_PASSWORD_LENGTH, max = MAXIMUM_PASSWORD_LENGTH)
    String newPassword;

    String confirmationNewPassword;

    Map<Long, String> securityQuestionAnswers;
}
