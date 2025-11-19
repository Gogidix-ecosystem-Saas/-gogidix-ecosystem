package com.gogidix.centralconfiguration.environmentconfig.domain.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * Domain model for Environment Configuration.
 * Represents environment-specific configuration settings.
 */
public class EnvironmentConfiguration {
    
    private String id;
    private String environment;
    private String application;
    private String profile;
    private Map<String, Object> variables;
    private boolean active;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String version;
    private String createdBy;
    
    // Default constructor
    public EnvironmentConfiguration() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.version = "1.0.0";
        this.active = true;
    }
    
    // Constructor with essential fields
    public EnvironmentConfiguration(String environment, String application, Map<String, Object> variables) {
        this();
        this.environment = environment;
        this.application = application;
        this.variables = variables;
        this.id = generateId(environment, application);
    }
    
    // Business logic methods
    public boolean isValidConfiguration() {
        return environment != null && !environment.trim().isEmpty() &&
               application != null && !application.trim().isEmpty() &&
               variables != null;
    }
    
    public void updateVariables(Map<String, Object> newVariables) {
        if (newVariables != null) {
            this.variables = newVariables;
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isDevelopment() {
        return "development".equalsIgnoreCase(environment) || "dev".equalsIgnoreCase(environment);
    }
    
    public boolean isProduction() {
        return "production".equalsIgnoreCase(environment) || "prod".equalsIgnoreCase(environment);
    }
    
    public boolean isStaging() {
        return "staging".equalsIgnoreCase(environment) || "stage".equalsIgnoreCase(environment);
    }
    
    private String generateId(String environment, String application) {
        return environment + "-" + application + "-" + System.currentTimeMillis();
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getEnvironment() {
        return environment;
    }
    
    public void setEnvironment(String environment) {
        this.environment = environment;
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
    
    public Map<String, Object> getVariables() {
        return variables;
    }
    
    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnvironmentConfiguration that = (EnvironmentConfiguration) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(environment, that.environment) &&
               Objects.equals(application, that.application);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, environment, application);
    }
    
    @Override
    public String toString() {
        return "EnvironmentConfiguration{" +
                "id='" + id + '\'' +
                ", environment='" + environment + '\'' +
                ", application='" + application + '\'' +
                ", profile='" + profile + '\'' +
                ", active=" + active +
                ", version='" + version + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
