package com.tiernebre.zone_blitz.session;

import java.util.UUID;

public class SessionFactory {
    public static SessionDto generateOne() {
        return SessionDto.builder()
                .accessToken(UUID.randomUUID().toString())
                .refreshToken(UUID.randomUUID())
                .fingerprint(UUID.randomUUID().toString())
                .build();
    }
}
