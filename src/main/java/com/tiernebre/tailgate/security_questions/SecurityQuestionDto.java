package com.tiernebre.tailgate.security_questions;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SecurityQuestionDto {
    Long id;
    String question;
}
