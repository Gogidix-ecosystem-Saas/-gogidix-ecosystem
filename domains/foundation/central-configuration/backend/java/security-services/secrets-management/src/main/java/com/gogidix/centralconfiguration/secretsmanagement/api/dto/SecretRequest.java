package com.gogidix.centralconfiguration.secretsmanagement.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for secret management operations.
 */
public class SecretRequest {
    
    @NotBlank(message = "Secret key is required")
    @Size(min = 2, max = 100, message = "Secret key must be between 2 and 100 characters")
    private String key;
    
    @NotBlank(message = "Secret value is required")
    private String value;
    
    private String description;
    private String environment;
    private String application;
    private boolean encrypted = true;
    
    // Constructors
    public SecretRequest() {}
    
    public SecretRequest(String key, String value) {
        this.key = key;
        this.value = value;
    }
    
    // Getters and Setters
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }
    
    public String getApplication() { return application; }
    public void setApplication(String application) { this.application = application; }
    
    public boolean isEncrypted() { return encrypted; }
    public void setEncrypted(boolean encrypted) { this.encrypted = encrypted; }
}