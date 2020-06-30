package com.tiernebre.zone_blitz.user.entity;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class UserEntity {
    Long id;
    String email;
    String password;
    Instant createdAt;
    boolean isConfirmed;
}
