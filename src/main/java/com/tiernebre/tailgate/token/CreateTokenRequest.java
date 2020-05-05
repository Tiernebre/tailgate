package com.tiernebre.tailgate.token;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@Builder
public class CreateTokenRequest {
    @NotBlank
    String email;
    @NotBlank
    String password;
}
