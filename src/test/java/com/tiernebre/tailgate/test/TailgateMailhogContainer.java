package com.tiernebre.tailgate.test;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;

/**
 * Singleton Container Representation of a MailHog container to allow for easy-to-spin-up
 * email tests:
 */
public class TailgateMailhogContainer extends GenericContainer {
    private static final String IMAGE_VERSION = "mailhog/mailhog";
    private static TailgateMailhogContainer container;

    private TailgateMailhogContainer() {
        super(IMAGE_VERSION);
        setExposedPorts(ImmutableList.of(1025, 8025));
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
