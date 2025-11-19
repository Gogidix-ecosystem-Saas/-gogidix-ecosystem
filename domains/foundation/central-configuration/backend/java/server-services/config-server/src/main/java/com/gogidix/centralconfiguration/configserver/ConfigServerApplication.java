package com.gogidix.centralconfiguration.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 Configuration Server Application.
 * This server centralizes configuration management for all microservices
 in the Social E-commerce Ecosystem.
 */
@SpringBootApplication
@SuppressWarnings("HideUtilityClassConstructor")
@EnableConfigServer
@EnableDiscoveryClient
public final class ConfigServerApplication {

    /**
     Main method for the application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
