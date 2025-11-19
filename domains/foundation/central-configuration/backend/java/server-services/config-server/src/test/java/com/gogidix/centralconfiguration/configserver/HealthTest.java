package com.gogidix.centralconfiguration.configserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = {
        "eureka.client.enabled=false",
        "spring.cloud.config.server.git.clone-on-start=false",
        "spring.cloud.config.server.health.enabled=false"
    }
)
@ActiveProfiles("test")
public class HealthTest implements HealthIndicator {

    @Test
    public void testHealthIndicator() {
        Health health = health();
        assertEquals(Health.up().build().getStatus(), health.getStatus());
    }

    @Override
    public Health health() {
        return Health.up()
                .withDetail("service", "config-server")
                .withDetail("status", "operational")
                .build();
    }
}
