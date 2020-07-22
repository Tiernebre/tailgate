package com.tiernebre.zone_blitz.exception;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.Set;

@Value
@Builder
public class ErrorResponse {
    public static ErrorResponse of(String message) {
        return ErrorResponse.builder()
                .errors(Collections.singleton(message))
                .build();
    }

    Set<String> errors;
}
