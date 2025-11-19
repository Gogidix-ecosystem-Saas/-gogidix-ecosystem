package com.gogidix.centralconfiguration.configserver.domain.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * Domain model for Configuration Result.
 * Pure domain representation of configuration response.
 */
public class ConfigurationResult {
    
    private final String application;
    private final String profile;
    private final String label;
    private final String version;
    private final Map<String, Object> properties;
    private final LocalDateTime timestamp;
    private final String serverInfo;

    // Constructor
    public ConfigurationResult(String application, String profile, String label,
                              String version, Map<String, Object> properties, 
                              String serverInfo) {
        this.application = application;
        this.profile = profile;
        this.label = label;
        this.version = version;
        this.properties = properties;
        this.timestamp = LocalDateTime.now();
        this.serverInfo = serverInfo;
    }

    // Business logic methods
    public boolean hasProperties() {
        return properties != null && !properties.isEmpty();
    }

    public boolean isComplete() {
        return application != null && profile != null && properties != null;
    }

    public int getPropertyCount() {
        return properties != null ? properties.size() : 0;
    }

    // Getters (immutable)
    public String getApplication() {
        return application;
    }

    public String getProfile() {
        return profile;
    }

    public String getLabel() {
        return label;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getServerInfo() {
        return serverInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigurationResult that = (ConfigurationResult) o;
        return Objects.equals(application, that.application) &&
               Objects.equals(profile, that.profile) &&
               Objects.equals(label, that.label) &&
               Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(application, profile, label, version);
    }

    @Override
    public String toString() {
        return "ConfigurationResult{" +
                "application='" + application + '\'' +
                ", profile='" + profile + '\'' +
                ", label='" + label + '\'' +
                ", version='" + version + '\'' +
                ", propertyCount=" + getPropertyCount() +
                ", timestamp=" + timestamp +
                '}';
    }
}