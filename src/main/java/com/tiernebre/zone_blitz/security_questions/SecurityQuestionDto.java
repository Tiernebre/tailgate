package com.tiernebre.zone_blitz.security_questions;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SecurityQuestionDto {
    Long id;
    String question;
}
