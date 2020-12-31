package com.tiernebre.zone_blitz.token.user_confirmation;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class UserConfirmationTokenEntity {
    UUID token;
    Instant createdAt;
    Long userId;
}
