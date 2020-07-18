package com.tiernebre.zone_blitz.token.refresh;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class RefreshTokenEntity {
    UUID token;
    Instant createdAt;
    Long userId;
}
