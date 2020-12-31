package com.tiernebre.zone_blitz.token.access;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccessTokenDto {
    String token;
    String fingerprint;
}
