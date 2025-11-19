package com.gogidix.centralconfiguration.configserver.domain.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * Domain model for Configuration Data.
 * Represents configuration data for applications.
 */
public class ConfigurationData {
    
    private String id;
    private String application;
    private String profile;
    private String label;
    private Map<String, Object> properties;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String version;

    // Default constructor
    public ConfigurationData() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.version = "1.0.0";
    }

    // Constructor with essential fields
    public ConfigurationData(String application, String profile, Map<String, Object> properties) {
        this();
        this.application = application;
        this.profile = profile;
        this.properties = properties;
        this.id = generateId(application, profile);
    }

    // Business logic methods
    public boolean isValidConfiguration() {
        return application != null &&
            !application.trim().isEmpty() &&
               profile != null &&
            !profile.trim().isEmpty() &&
               properties != null &&
            !properties.isEmpty();
    }

    public void updateProperties(Map<String, Object> newProperties) {
        if (newProperties != null) {
        
            this.properties = newProperties;
            this.updatedAt = LocalDateTime.now();
        }
    }

    private String generateId(String application, String profile) {
        return application + "-" + profile + "-" + System.currentTimeMillis();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    
    public boolean equals(final Object o) {
        if (this == o) {
        
        return true;
    }
        if (o == null || getClass() != o.getClass()) return false;
        ConfigurationData that = (ConfigurationData) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(application, that.application) &&
               Objects.equals(profile, that.profile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, application, profile);
    }

    @Override
    public String toString() {
        return "ConfigurationData{" +
                "id='" + id + '\'' +
                ", application='" + application + '\'' +
                ", profile='" + profile + '\'' +
                ", label='" + label + '\'' +
                ", version='" + version + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
