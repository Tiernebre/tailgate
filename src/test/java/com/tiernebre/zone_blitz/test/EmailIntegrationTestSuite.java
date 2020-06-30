package com.tiernebre.zone_blitz.test;

import com.tiernebre.zone_blitz.test.email.mailhog.ZoneBlitzMailhogContainer;
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
public abstract class EmailIntegrationTestSuite extends SpringIntegrationTestingSuite {
    @ClassRule
    private static final ZoneBlitzMailhogContainer ZONE_BLITZ_MAILHOG_CONTAINER = ZoneBlitzMailhogContainer.getInstance();

    protected static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            ZONE_BLITZ_MAILHOG_CONTAINER.start();
            int dynamicallyAllocatedSmtpPort = ZONE_BLITZ_MAILHOG_CONTAINER.getMappedPort(ZONE_BLITZ_MAILHOG_CONTAINER.getSmtpPort());
            int dynamicallyAllocatedApiPort = ZONE_BLITZ_MAILHOG_CONTAINER.getMappedPort(ZONE_BLITZ_MAILHOG_CONTAINER.getApiPort());
            configurableApplicationContext.getEnvironment().getSystemProperties().put("tailgate.email.port", dynamicallyAllocatedSmtpPort);
            configurableApplicationContext.getEnvironment().getSystemProperties().put("mailhog.port", dynamicallyAllocatedApiPort);
        }
    }
}
