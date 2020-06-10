package com.tiernebre.tailgate.user.dto;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import javax.validation.constraints.*;
import java.util.Collection;

@Value
@Builder
public class CreateUserRequest {
    public static final int NUMBER_OF_ALLOWED_SECURITY_QUESTIONS = 2;
    public static final String NUMBER_OF_SECURITY_QUESTIONS_VALIDATION_MESSAGE =
            "must have exactly " +
            NUMBER_OF_ALLOWED_SECURITY_QUESTIONS +
            " entries.";

    private static final int MINIMUM_PASSWORD_LENGTH = 8;
    private static final int MAXIMUM_PASSWORD_LENGTH = 71;

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
    Collection<@NotNull CreateUserSecurityQuestionRequest> securityQuestions;
}
