package com.tiernebre.tailgate.token;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RefreshTokenEntity {
    String token;
}
