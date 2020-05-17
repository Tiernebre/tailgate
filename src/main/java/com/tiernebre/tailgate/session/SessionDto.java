package com.tiernebre.tailgate.session;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SessionDto {
    String accessToken;
    String refreshToken;
}
