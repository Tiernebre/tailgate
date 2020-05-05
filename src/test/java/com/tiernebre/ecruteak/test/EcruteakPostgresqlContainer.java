package com.tiernebre.tailgate.test;

import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Specific implementation of the Testcontainer's PostgreSQLContainer that allows
 * for a Singleton approach of spinning up a Database Container. This means that
 * for all of the integration tests that are derived from this, only _one_ PostgreSQL
 * container is used, rather than several.
 */
public class tailgatePostgresqlContainer extends PostgreSQLContainer<tailgatePostgresqlContainer> {
    private static final String IMAGE_VERSION = "postgres:11.1";
    private static tailgatePostgresqlContainer container;

    private tailgatePostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    public static tailgatePostgresqlContainer getInstance() {
        if (container == null) {
            container = new tailgatePostgresqlContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        //do nothing, JVM handles own
    }
}