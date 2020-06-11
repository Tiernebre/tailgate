package com.tiernebre.tailgate.token.password_reset;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class PasswordResetTokenEntity {
    String token;
    Instant createdAt;
    Long userId;
}
