package com.tiernebre.zone_blitz.test.email.mailhog;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;

/**
 * Singleton Container Representation of a MailHog container to allow for easy-to-spin-up
 * email tests:
 */
public class ZoneBlitzMailhogContainer extends GenericContainer {
    private static final int SMTP_PORT = 1025;
    private static final int API_PORT = 8025;

    private static final String IMAGE_VERSION = "mailhog/mailhog";
    private static ZoneBlitzMailhogContainer container;

    private ZoneBlitzMailhogContainer() {
        super(IMAGE_VERSION);
        setExposedPorts(ImmutableList.of(SMTP_PORT, API_PORT));
    }

    public static ZoneBlitzMailhogContainer getInstance() {
        if (container == null) {
            container = new ZoneBlitzMailhogContainer();
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

    public int getSmtpPort() {
        return SMTP_PORT;
    }

    public int getApiPort() {
        return API_PORT;
    }
}
