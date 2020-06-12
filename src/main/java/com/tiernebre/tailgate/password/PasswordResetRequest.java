package com.tiernebre.tailgate.password;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PasswordResetRequest {
    String email;
}
