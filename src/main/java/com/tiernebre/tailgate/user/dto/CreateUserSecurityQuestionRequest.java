package com.tiernebre.tailgate.user.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

import static com.tiernebre.tailgate.user.validator.UserValidationConstants.NULL_SECURITY_QUESTION_ID_VALIDATION_MESSAGE;

@Value
@Builder
public class CreateUserSecurityQuestionRequest {
    @NotNull(message =  NULL_SECURITY_QUESTION_ID_VALIDATION_MESSAGE)
    Long id;
    String answer;
}
