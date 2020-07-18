package com.tiernebre.zone_blitz.session;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class SessionDto {
    String accessToken;
    UUID refreshToken;
    String fingerprint;
}
