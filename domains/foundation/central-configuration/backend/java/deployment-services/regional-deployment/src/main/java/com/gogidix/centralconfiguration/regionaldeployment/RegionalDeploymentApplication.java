package com.gogidix.centralconfiguration.regionaldeployment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 Regional Deployment Application.
 This service manages multi-region deployment strategies across the ecosystem.
 */
@SpringBootApplication
@SuppressWarnings("HideUtilityClassConstructor")
@EnableDiscoveryClient
public final class RegionalDeploymentApplication {

    /**
     Main method for the application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(RegionalDeploymentApplication.class, args);
    }
}
