package com.tiernebre.zone_blitz.password;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class PasswordResetRequest {
    String email;
}
