package com.tiernebre.zone_blitz.security_questions;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class SecurityQuestionDto {
    Long id;
    String question;
}
