package com.tiernebre.zone_blitz.test;

import com.tiernebre.zone_blitz.test.email.mailhog.ZoneBlitzMailhogContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public abstract class AbstractIntegrationTestingSuite {
    static {
        ZoneBlitzPostgresqlContainer.getInstance().start();
        ZoneBlitzMailhogContainer.getInstance().start();
    }
}
