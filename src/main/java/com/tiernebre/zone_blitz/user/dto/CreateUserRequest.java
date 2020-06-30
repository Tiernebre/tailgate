package com.tiernebre.zone_blitz.user.dto;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import javax.validation.constraints.*;
import java.util.List;

import static com.tiernebre.zone_blitz.user.validator.UserValidationConstants.*;

@Value
@Builder
public class CreateUserRequest {
    @NotBlank
    @Email
    String email;

    @NotBlank
    @Size(min = MINIMUM_PASSWORD_LENGTH, max = MAXIMUM_PASSWORD_LENGTH)
    @With
    String password;

    String confirmationPassword;

    @NotEmpty
    @Size(
            min = NUMBER_OF_ALLOWED_SECURITY_QUESTIONS,
            max = NUMBER_OF_ALLOWED_SECURITY_QUESTIONS,
            message = NUMBER_OF_SECURITY_QUESTIONS_VALIDATION_MESSAGE
    )
    @With
    List<@NotNull(message = NULL_SECURITY_QUESTION_ENTRIES_VALIDATION_MESSAGE) CreateUserSecurityQuestionRequest> securityQuestions;
}
