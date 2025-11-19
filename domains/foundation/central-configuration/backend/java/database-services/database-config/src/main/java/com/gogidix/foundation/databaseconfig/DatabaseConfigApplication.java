package com.gogidix.foundation.databaseconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Database Configuration Service
 * Provides database configuration management for GOGIDIX Ecosystem
 */
@SpringBootApplication
@EnableDiscoveryClient
public class DatabaseConfigApplication {

    public static void main(final String[] args) {
        SpringApplication.run(DatabaseConfigApplication.class, args);
    }
}
