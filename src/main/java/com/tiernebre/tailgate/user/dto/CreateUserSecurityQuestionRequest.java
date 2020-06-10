package com.tiernebre.tailgate.user.dto;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.tiernebre.tailgate.user.validator.UserValidationConstants.NULL_SECURITY_QUESTION_ANSWER_VALIDATION_MESSAGE;
import static com.tiernebre.tailgate.user.validator.UserValidationConstants.NULL_SECURITY_QUESTION_ID_VALIDATION_MESSAGE;

@Value
@Builder
public class CreateUserSecurityQuestionRequest {
    @NotNull(message =  NULL_SECURITY_QUESTION_ID_VALIDATION_MESSAGE)
    Long id;

    @NotBlank(message = NULL_SECURITY_QUESTION_ANSWER_VALIDATION_MESSAGE)
    @With
    String answer;
}
