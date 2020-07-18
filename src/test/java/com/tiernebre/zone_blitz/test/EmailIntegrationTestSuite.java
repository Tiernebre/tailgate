package com.tiernebre.zone_blitz.test;

import com.tiernebre.zone_blitz.test.email.mailhog.ZoneBlitzMailhogContainer;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Provides the necessary setup for spooling up a Database to perform Integration Tests against.
 */
@SpringBootTest
public abstract class EmailIntegrationTestSuite extends SpringIntegrationTestingSuite {
    static {
        ZoneBlitzMailhogContainer.getInstance().start();
    }
}
