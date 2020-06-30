package com.tiernebre.zone_blitz.test.email;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TestEmail {
    String from;
    String to;
    String subject;
    String text;
}
