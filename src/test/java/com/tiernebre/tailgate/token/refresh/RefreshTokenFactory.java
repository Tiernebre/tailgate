package com.tiernebre.tailgate.token.refresh;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

public class RefreshTokenFactory {
    public static RefreshTokenEntity generateOneEntity() {
        return RefreshTokenEntity.builder()
                .token(UUID.randomUUID().toString())
                .userId(new Random().nextLong())
                .createdAt(Instant.now())
                .build();
    }
}
