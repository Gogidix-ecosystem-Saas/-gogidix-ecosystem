package com.gogidix.centralconfiguration.configserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = {
        "eureka.client.enabled=false",
        "spring.cloud.config.server.git.clone-on-start=false",
        "spring.cloud.config.server.health.enabled=false"
    }
)
@ActiveProfiles("test")
public class SimpleContextTest {

    @Test
    public void contextLoads() {
        // If we get here, context loaded successfully
        assertTrue(true, "Application context loaded successfully");
    }
}
