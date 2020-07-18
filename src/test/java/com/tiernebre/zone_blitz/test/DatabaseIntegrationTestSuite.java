package com.tiernebre.zone_blitz.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Provides the necessary setup for spooling up a Database to perform Integration Tests against.
 */
@SpringBootTest
@Transactional
public abstract class DatabaseIntegrationTestSuite {
    static {
        ZoneBlitzPostgresqlContainer.getInstance().start();
    }
}
