package com.tiernebre.zone_blitz.converter;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class StubDto {
    long id;
    String name;
    Instant createdAt;
    Instant updatedAt;
}
