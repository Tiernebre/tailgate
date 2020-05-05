package com.tiernebre.tailgate.test;

import org.junit.ClassRule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.validation.constraints.NotNull;

/**
 * Provides the necessary setup for spooling up a Database to perform Integration Tests against.
 */
@Testcontainers
@ContextConfiguration(initializers = DatabaseIntegrationTestSuite.Initializer.class)
@SpringBootTest
public abstract class DatabaseIntegrationTestSuite {
    @ClassRule
    private static final tailgatePostgresqlContainer tailgatePostgresqlContainer = tailgatePostgresqlContainer.getInstance();

    /**
     * Allows for injection of dynamically generated database configurations before Spring fully initializes.
     */
    protected static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            tailgatePostgresqlContainer.start();
            configurableApplicationContext.getEnvironment().getSystemProperties().put("spring.datasource.url", tailgatePostgresqlContainer.getJdbcUrl());
            configurableApplicationContext.getEnvironment().getSystemProperties().put("spring.datasource.username", tailgatePostgresqlContainer.getUsername());
            configurableApplicationContext.getEnvironment().getSystemProperties().put("spring.datasource.password", tailgatePostgresqlContainer.getPassword());
        }
    }
}
