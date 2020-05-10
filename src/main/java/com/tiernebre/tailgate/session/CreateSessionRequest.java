package com.tiernebre.tailgate.session;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@Builder
public class CreateSessionRequest {
    @NotBlank
    String email;
    @NotBlank
    String password;
}
