package com.tiernebre.tailgate.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("h2")
@SpringBootTest
public abstract class SpringIntegrationTestingSuite {
}
