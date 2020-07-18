package com.tiernebre.zone_blitz.token.password_reset;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class PasswordResetTokenEntity {
    UUID token;
    Instant createdAt;
    Long userId;
}
