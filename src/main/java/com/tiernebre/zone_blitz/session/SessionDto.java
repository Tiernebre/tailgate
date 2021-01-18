package com.tiernebre.zone_blitz.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
public class SessionDto {
    String accessToken;
    @JsonIgnore
    UUID refreshToken;
    @JsonIgnore
    String fingerprint;
}
