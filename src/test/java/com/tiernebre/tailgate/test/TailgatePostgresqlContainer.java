package com.tiernebre.tailgate.test;

import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Specific implementation of the Testcontainer's PostgreSQLContainer that allows
 * for a Singleton approach of spinning up a Database Container. This means that
 * for all of the integration tests that are derived from this, only _one_ PostgreSQL
 * container is used, rather than several.
 */
public class TailgatePostgresqlContainer extends PostgreSQLContainer<TailgatePostgresqlContainer> {
    private static final String IMAGE_VERSION = "postgres:12.3";
    private static TailgatePostgresqlContainer container;

    private TailgatePostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    public static TailgatePostgresqlContainer getInstance() {
        if (container == null) {
            container = new TailgatePostgresqlContainer();
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