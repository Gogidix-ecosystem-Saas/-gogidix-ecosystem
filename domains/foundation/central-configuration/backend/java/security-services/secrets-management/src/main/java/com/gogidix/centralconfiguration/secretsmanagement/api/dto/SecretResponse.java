package com.gogidix.centralconfiguration.secretsmanagement.api.dto;

import java.time.LocalDateTime;

/**
 Response DTO for secret management data.
 */
public final class SecretResponse {

    private String key;
    private String value; // Masked for security
    private String description;
    private String environment;
    private String application;
    private boolean encrypted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String version;
    private String status;

    // Default constructor
    public SecretResponse() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.version = "1.0.0";
        this.status = "ACTIVE";
    }

    // Getters and Setters
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getDescription() { return description; }
    public void setDescription(
        String description) { this.description = description; }

    public String getEnvironment() { return environment; }
    public void setEnvironment(
        String environment) { this.environment = environment; }

    public String getApplication() { return application; }
    public void setApplication(
        String application) { this.application = application; }

    public boolean isEncrypted() { return encrypted; }
    public void setEncrypted(boolean encrypted) { this.encrypted = encrypted; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(
        LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(
        LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
