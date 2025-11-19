package com.gogidix.foundation.featuretoggleconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Feature Toggle Configuration Service
 * Provides feature flag management for GOGIDIX Ecosystem
 */
@SpringBootApplication
@SuppressWarnings("HideUtilityClassConstructor")
@EnableDiscoveryClient
public final class FeatureToggleConfigApplication {

    /**
     Main method for the application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(FeatureToggleConfigApplication.class, args);
    }
}
