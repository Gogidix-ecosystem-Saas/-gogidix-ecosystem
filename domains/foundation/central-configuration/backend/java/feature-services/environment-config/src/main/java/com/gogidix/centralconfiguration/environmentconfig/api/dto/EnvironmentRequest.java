package com.gogidix.centralconfiguration.environmentconfig.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Map;

/**
 Request DTO for environment configuration operations.
 Contains parameters for environment management.
 */
public final class EnvironmentRequest {

    @NotBlank(message = "Environment name is required")
    @Size(
        min = 2, max = 50, message = "Environment name must be
        between 2 and 50 characters")
    private String environment;

    @NotBlank(message = "Application name is required")
    @Size(
        min = 2, max = 50, message = "Application name must be
        between 2 and 50 characters")
    private String application;

    @Size(max = 30, message = "Profile must not exceed 30 characters")
    private String profile;

    private Map<String, Object> variables;
    private boolean active = true;
    private String description;

    // Default constructor
    public EnvironmentRequest() {
        this.profile = "default";
    }

    // Constructor with essential fields
    public EnvironmentRequest(String environment, String application) {
        this();
        this.environment = environment;
        this.application = application;
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
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "EnvironmentRequest{" +
                "environment='" + environment + '\'' +
                ", application='" + application + '\'' +
                ", profile='" + profile + '\'' +
                ", active=" + active +
                ", description='" + description + '\'' +
                '}';
    }
}
