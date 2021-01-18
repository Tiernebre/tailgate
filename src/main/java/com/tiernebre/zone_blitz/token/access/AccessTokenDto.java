package com.tiernebre.zone_blitz.token.access;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class AccessTokenDto {
    String token;
    String fingerprint;
}
