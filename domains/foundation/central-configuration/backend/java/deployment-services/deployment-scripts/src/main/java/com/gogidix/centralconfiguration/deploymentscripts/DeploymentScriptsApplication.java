package com.gogidix.centralconfiguration.deploymentscripts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 Deployment Scripts Application.
 This service manages deployment automation and scripts across the ecosystem.
 */
@SpringBootApplication
@SuppressWarnings("HideUtilityClassConstructor")
@EnableDiscoveryClient
public final class DeploymentScriptsApplication {

    /**
     Main method for the application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(DeploymentScriptsApplication.class, args);
    }
}
