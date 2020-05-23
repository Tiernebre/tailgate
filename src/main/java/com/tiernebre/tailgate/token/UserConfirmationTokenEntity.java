package com.tiernebre.tailgate.token;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class UserConfirmationTokenEntity {
    String token;
    Instant createdAt;
    Long userId;
}
