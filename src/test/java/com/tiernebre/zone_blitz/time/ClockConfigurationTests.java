package com.tiernebre.zone_blitz.time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;

public class ClockConfigurationTests {
    @Nested
    @DisplayName("clock")
    class ClockTests {
        @Test
        public void returnsASystemDefaultClock() {
            ClockConfiguration clockConfiguration = new ClockConfiguration();
            Clock generatedClock = clockConfiguration.clock();
            assertEquals(ZoneId.systemDefault(), generatedClock.getZone());
        }
    }
}
