package com.gogidix.centralconfiguration.disasterrecovery;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = {
        "eureka.client.enabled=false",
        "spring.cloud.config.enabled=false",
        "management.endpoints.web.exposure.include=health,info"
    }
)
@ActiveProfiles("test")
public final class ApplicationTest {

    @Test
    public void contextLoads() {
        // Test passes if Spring context loads successfully
        System.out.println("✅ disaster-recovery context loaded successfully");
    }
}
