package com.tiernebre.zone_blitz.password;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PasswordResetRequest {
    String email;
}
