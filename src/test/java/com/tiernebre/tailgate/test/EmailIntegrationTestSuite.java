package com.tiernebre.tailgate.test;

import com.tiernebre.tailgate.test.email.mailhog.TailgateMailhogContainer;
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
    private static final TailgateMailhogContainer tailgateMailhogContainer = TailgateMailhogContainer.getInstance();

    protected static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            tailgateMailhogContainer.start();
            int dynamicallyAllocatedSmtpPort = tailgateMailhogContainer.getMappedPort(tailgateMailhogContainer.getSmtpPort());
            int dynamicallyAllocatedApiPort = tailgateMailhogContainer.getMappedPort(tailgateMailhogContainer.getApiPort());
            configurableApplicationContext.getEnvironment().getSystemProperties().put("tailgate.email.port", dynamicallyAllocatedSmtpPort);
            configurableApplicationContext.getEnvironment().getSystemProperties().put("mailhog.port", dynamicallyAllocatedApiPort);
        }
    }
}
