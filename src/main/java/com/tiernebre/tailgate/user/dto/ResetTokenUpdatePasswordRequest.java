package com.tiernebre.tailgate.user.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.*;

import java.util.Map;

import static com.tiernebre.tailgate.user.validator.UserValidationConstants.*;

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

    @Size(
            min = NUMBER_OF_ALLOWED_SECURITY_QUESTIONS,
            max = NUMBER_OF_ALLOWED_SECURITY_QUESTIONS,
            message = NUMBER_OF_SECURITY_QUESTION_ANSWERS_VALIDATION_MESSAGE
    )
    @NotEmpty(message = EMPTY_SECURITY_QUESTION_ANSWERS_VALIDATION_MESSAGE)
    Map<Long, @NotBlank(message = BLANK_SECURITY_QUESTION_ANSWERS_ENTRIES_VALIDATION_MESSAGE) String> securityQuestionAnswers;
}
