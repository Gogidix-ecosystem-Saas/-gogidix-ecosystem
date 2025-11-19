package com.gogidix.centralconfiguration.configserver.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Map;

/**
 DTO for configuration update requests.
 Contains data for updating configuration properties.
 */
public final class ConfigurationUpdateRequest {

    @NotBlank(message = "Application name is required")
    @Size(
        min = 2, max = 50, message = "Application name must be
        between 2 and 50 characters")
    private String application;

    @NotBlank(message = "Profile is required")
    @Size(
        min = 2, max = 30, message = "Profile must be between 2 and
        30 characters")
    private String profile;

    @Size(max = 50, message = "Label must not exceed 50 characters")
    private String label;

    @NotNull(message = "Properties are required")
    private Map<String, Object> properties;

    @Size(max = 20, message = "Version must not exceed 20 characters")
    private String version;

    private boolean overwrite = false;

    // Default constructor
    public ConfigurationUpdateRequest() {
        this.label = "master";
        this.version = "1.0.0";
    }

    // Constructor with essential fields
    public ConfigurationUpdateRequest(
        String application, String profile, Map<String, Object>
        properties) {
        this();
        this.application = application;
        this.profile = profile;
        this.properties = properties;
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

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    @Override
    public String toString() {
        return "ConfigurationUpdateRequest{" +
                "application='" + application + '\'' +
                ", profile='" + profile + '\'' +
                ", label='" + label + '\'' +
                ", version='" + version + '\'' +
                ", overwrite=" + overwrite +
                '}';
    }
}
