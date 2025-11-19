package com.gogidix.centralconfiguration.configserver.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Map;

/**
 * Request DTO for configuration queries.
 * Contains parameters for configuration retrieval.
 */
public class ConfigurationRequest {
    
    @NotBlank(message = "Application name is required")
    @Size(min = 2, max = 50, message = "Application name must be between 2 and 50 characters")
    private String application;
    
    @NotBlank(message = "Profile is required")
    @Size(min = 2, max = 30, message = "Profile must be between 2 and 30 characters")
    private String profile;
    
    @Size(max = 50, message = "Label must not exceed 50 characters")
    private String label;
    
    private boolean includeDefaults;
    private Map<String, Object> properties;

    // Default constructor
    public ConfigurationRequest() {
        this.profile = "default";
        this.label = "master";
        this.includeDefaults = true;
    }

    // Constructor with application
    public ConfigurationRequest(String application) {
        this();
        this.application = application;
    }

    // Constructor with application and profile
    public ConfigurationRequest(String application, String profile) {
        this();
        this.application = application;
        this.profile = profile;
    }

    // Getters and Setters
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

    public boolean isIncludeDefaults() {
        return includeDefaults;
    }

    public void setIncludeDefaults(boolean includeDefaults) {
        this.includeDefaults = includeDefaults;
    }
    
    public Map<String, Object> getProperties() {
        return properties;
    }
    
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "ConfigurationRequest{" +
                "application='" + application + '\'' +
                ", profile='" + profile + '\'' +
                ", label='" + label + '\'' +
                ", includeDefaults=" + includeDefaults +
                '}';
    }
}