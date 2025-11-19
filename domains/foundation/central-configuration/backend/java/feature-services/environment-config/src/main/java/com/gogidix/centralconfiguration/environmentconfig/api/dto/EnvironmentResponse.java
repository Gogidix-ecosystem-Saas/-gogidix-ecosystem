package com.gogidix.centralconfiguration.environmentconfig.api.dto;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Response DTO for environment configuration data.
 * Contains environment configuration and metadata.
 */
public class EnvironmentResponse {
    
    private String environment;
    private String application;
    private String profile;
    private Map<String, Object> variables;
    private boolean active;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String version;
    private String status;
    
    // Default constructor
    public EnvironmentResponse() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.version = "1.0.0";
        this.status = "ACTIVE";
    }
    
    // Constructor with essential fields
    public EnvironmentResponse(String environment, String application, Map<String, Object> variables) {
        this();
        this.environment = environment;
        this.application = application;
        this.variables = variables;
        this.active = true;
    }
    
    // Getters and Setters
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
        this.status = active ? "ACTIVE" : "INACTIVE";
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "EnvironmentResponse{" +
                "environment='" + environment + '\'' +
                ", application='" + application + '\'' +
                ", profile='" + profile + '\'' +
                ", active=" + active +
                ", status='" + status + '\'' +
                ", version='" + version + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
