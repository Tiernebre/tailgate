package com.tiernebre.zone_blitz.test;

import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Specific implementation of the Testcontainer's PostgreSQLContainer that allows
 * for a Singleton approach of spinning up a Database Container. This means that
 * for all of the integration tests that are derived from this, only _one_ PostgreSQL
 * container is used, rather than several.
 */
public class ZoneBlitzPostgresqlContainer extends PostgreSQLContainer<ZoneBlitzPostgresqlContainer> {
    private static final String IMAGE_VERSION = "postgres:12.3";
    private static ZoneBlitzPostgresqlContainer container;

    private ZoneBlitzPostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    public static ZoneBlitzPostgresqlContainer getInstance() {
        if (container == null) {
            container = new ZoneBlitzPostgresqlContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles own
    }
}