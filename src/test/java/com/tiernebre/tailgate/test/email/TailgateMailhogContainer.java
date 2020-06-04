package com.tiernebre.tailgate.test.email;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;

/**
 * Singleton Container Representation of a MailHog container to allow for easy-to-spin-up
 * email tests:
 */
public class TailgateMailhogContainer extends GenericContainer {
    private static final int SMTP_PORT = 1025;
    private static final String SMTP_PORT_BINDING = String.format("%d:%d", SMTP_PORT, SMTP_PORT);
    private static final int API_PORT = 8025;
    private static final String API_PORT_BINDING = String.format("%d:%d", API_PORT, API_PORT);

    private static final String IMAGE_VERSION = "mailhog/mailhog";
    private static TailgateMailhogContainer container;

    private TailgateMailhogContainer() {
        super(IMAGE_VERSION);
        setPortBindings(ImmutableList.of(SMTP_PORT_BINDING, API_PORT_BINDING));
        setExposedPorts(ImmutableList.of(SMTP_PORT, API_PORT));
    }

    public static TailgateMailhogContainer getInstance() {
        if (container == null) {
            container = new TailgateMailhogContainer();
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
