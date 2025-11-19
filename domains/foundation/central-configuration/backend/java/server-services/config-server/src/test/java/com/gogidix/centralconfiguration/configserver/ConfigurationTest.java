package com.gogidix.centralconfiguration.configserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = {
        "eureka.client.enabled=false",
        "spring.cloud.config.server.git.clone-on-start=false",
        "spring.cloud.config.server.health.enabled=false"
    }
)
@ActiveProfiles("test")
public class ConfigurationTest {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port:0}")
    private int serverPort;

    @Test
    public void testApplicationName() {
        assertNotNull(applicationName);
        assertEquals("config-server-test", applicationName);
    }

    @Test
    public void testServerPort() {
        // In test profile with WebEnvironment.NONE, port should be 0
        assertEquals(0, serverPort);
    }
}
