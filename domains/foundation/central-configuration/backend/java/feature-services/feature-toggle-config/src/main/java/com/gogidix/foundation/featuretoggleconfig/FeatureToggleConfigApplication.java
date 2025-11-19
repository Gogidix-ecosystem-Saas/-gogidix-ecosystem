package com.gogidix.foundation.featuretoggleconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Feature Toggle Configuration Service
 * Provides feature flag management for GOGIDIX Ecosystem
 */
@SpringBootApplication
@EnableDiscoveryClient
public class FeatureToggleConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeatureToggleConfigApplication.class, args);
    }
}
