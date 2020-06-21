package com.tiernebre.tailgate.exception;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Value
@Builder
public class ErrorResponse {
    Set<String> errors;
}
