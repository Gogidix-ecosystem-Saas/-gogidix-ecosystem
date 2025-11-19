package com.gogidix.centralconfiguration.environmentconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Environment Configuration Service Application
 *
 * Provides comprehensive environment configuration management for the
 * GOGIDIX Social E-commerce Ecosystem with enterprise-grade features:
 * - Multi-environment configuration inheritance
 * - Configuration approval workflows
 * - Version control and rollback capabilities
 * - Real-time configuration validation
 * - Secure configuration encryption
 * - Compliance tracking and audit trails
 *
 * @author GOGIDIX Development Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@SpringBootApplication
@SuppressWarnings("HideUtilityClassConstructor")
public final class EnvironmentConfigApplication {

    /**
     Main method for the application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(EnvironmentConfigApplication.class, args);
    }
}
