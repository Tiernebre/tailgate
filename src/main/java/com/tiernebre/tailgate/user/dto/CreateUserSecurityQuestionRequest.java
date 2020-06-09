package com.tiernebre.tailgate.user.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateUserSecurityQuestionRequest {
    Long id;
    String answer;
}
