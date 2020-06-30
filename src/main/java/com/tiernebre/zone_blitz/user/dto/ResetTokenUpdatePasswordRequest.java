package com.tiernebre.zone_blitz.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;

import java.util.Map;

import static com.tiernebre.zone_blitz.user.validator.UserValidationConstants.*;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ResetTokenUpdatePasswordRequest extends UpdatePasswordRequest {
    @Builder
    public ResetTokenUpdatePasswordRequest(
            String email,
            Map<Long, String> securityQuestionAnswers,
            String newPassword,
            String confirmationNewPassword
    ) {
        super(newPassword, confirmationNewPassword);
        this.email = email;
        this.securityQuestionAnswers = securityQuestionAnswers;
    }

    @NotBlank
    @Email
    String email;

    @Size(
            min = NUMBER_OF_ALLOWED_SECURITY_QUESTIONS,
            max = NUMBER_OF_ALLOWED_SECURITY_QUESTIONS,
            message = NUMBER_OF_SECURITY_QUESTION_ANSWERS_VALIDATION_MESSAGE
    )
    @NotEmpty(message = EMPTY_SECURITY_QUESTION_ANSWERS_VALIDATION_MESSAGE)
    Map<Long, @NotBlank(message = BLANK_SECURITY_QUESTION_ANSWERS_ENTRIES_VALIDATION_MESSAGE) String> securityQuestionAnswers;
}
