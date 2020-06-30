package com.tiernebre.zone_blitz.session;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SessionDto {
    String accessToken;
    String refreshToken;
}
