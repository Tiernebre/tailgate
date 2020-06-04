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
@ContextConfiguration(initializers = EmailIntegrationTestSuite.Initializer.class)
@SpringBootTest
public abstract class EmailIntegrationTestSuite {
    @ClassRule
    private static final TailgateMailhogContainer tailgateMailhogContainer = TailgateMailhogContainer.getInstance();

    protected static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            tailgateMailhogContainer.start();
        }
    }
}
