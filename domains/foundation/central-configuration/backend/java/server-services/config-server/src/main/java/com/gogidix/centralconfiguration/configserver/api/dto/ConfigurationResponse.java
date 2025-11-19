package com.gogidix.centralconfiguration.configserver.api.dto;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Response DTO for configuration data.
 * Contains configuration properties and metadata.
 */
public class ConfigurationResponse {
    
    private String application;
    private String profile;
    private String label;
    private Map<String, Object> properties;
    private LocalDateTime timestamp;
    private String version;
    private String serverInfo;

    // Default constructor
    public ConfigurationResponse() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor with essential fields
    public ConfigurationResponse(String application, String profile, Map<String, Object> properties) {
        this.application = application;
        this.profile = profile;
        this.properties = properties;
        this.timestamp = LocalDateTime.now();
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(String serverInfo) {
        this.serverInfo = serverInfo;
    }

    @Override
    public String toString() {
        return "ConfigurationResponse{" +
                "application='" + application + '\'' +
                ", profile='" + profile + '\'' +
                ", label='" + label + '\'' +
                ", timestamp=" + timestamp +
                ", version='" + version + '\'' +
                '}';
    }
}
