package com.tiernebre.zone_blitz.time;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ClockConfiguration {
    /**
     * Wires up a clock that can be used across other beans for easier to test and maintain
     * code that relies upon system specific times.
     *
     * @return A Clock instance for time-based code.
     */
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
