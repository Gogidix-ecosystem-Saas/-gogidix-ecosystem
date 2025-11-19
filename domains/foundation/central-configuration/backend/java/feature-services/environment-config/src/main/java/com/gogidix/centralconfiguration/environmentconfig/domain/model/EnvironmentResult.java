package com.gogidix.centralconfiguration.environmentconfig.domain.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * Domain model for Environment Result.
 * Pure domain representation of environment configuration response.
 */
public class EnvironmentResult {
    
    private final String environment;
    private final String application;
    private final String region;
    private final Map<String, Object> configurations;
    private final LocalDateTime timestamp;
    private final boolean hasSecrets;

    public EnvironmentResult(String environment, String application, String region, 
                           Map<String, Object> configurations, boolean hasSecrets) {
        this.environment = environment;
        this.application = application;
        this.region = region;
        this.configurations = configurations;
        this.timestamp = LocalDateTime.now();
        this.hasSecrets = hasSecrets;
    }

    public boolean hasConfigurations() {
        return configurations != null &&
            !configurations.isEmpty();
    }

    public int getConfigurationCount() {
        return configurations != null ? configurations.size() : 0;
    }

    // Getters
    public String getEnvironment() { return environment; }
    public String getApplication() { return application; }
    public String getRegion() { return region; }
    public Map<String, Object> getConfigurations() { return configurations; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public boolean isHasSecrets() { return hasSecrets; }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
        return true;
    }
        if (o == null || getClass() != o.getClass()) return false;
        EnvironmentResult that = (EnvironmentResult) o;
        return Objects.equals(environment, that.environment) &&
               Objects.equals(application, that.application) &&
               Objects.equals(region, that.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(environment, application, region);
    }
}
