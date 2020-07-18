package com.tiernebre.zone_blitz.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class SessionDto {
    String accessToken;
    @JsonIgnore
    UUID refreshToken;
    @JsonIgnore
    String fingerprint;
}
