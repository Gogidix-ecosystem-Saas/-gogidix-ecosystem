package com.gogidix.centralconfiguration.dbmigrations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 Database Migrations Application.
 This service manages database schema migrations across the ecosystem.
 */
@SpringBootApplication
@SuppressWarnings("HideUtilityClassConstructor")
@EnableDiscoveryClient
public final class DatabaseMigrationsApplication {

    /**
     Main method for the application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(DatabaseMigrationsApplication.class, args);
    }
}
