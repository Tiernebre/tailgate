package com.tiernebre.zone_blitz.token.refresh;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class RefreshTokenEntity {
    String token;
    Instant createdAt;
    Long userId;
}
