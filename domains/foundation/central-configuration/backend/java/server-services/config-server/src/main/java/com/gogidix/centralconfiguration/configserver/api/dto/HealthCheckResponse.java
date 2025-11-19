package com.gogidix.centralconfiguration.configserver.api.dto;

import java.time.LocalDateTime;
import java.util.Map;

/**
 DTO for health check responses.
 Contains server health and status information.
 */
public final class HealthCheckResponse {

    private String status;
    private String serviceName;
    private String version;
    private LocalDateTime timestamp;
    private Map<String, String> details;
    private long uptime;
    private String gitRepositoryStatus;

    // Default constructor
    public HealthCheckResponse() {
        this.timestamp = LocalDateTime.now();
        this.serviceName = "config-server";
        this.version = "1.0.0";
    }

    // Constructor with status
    public HealthCheckResponse(String status) {
        this();
        this.status = status;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    public String getGitRepositoryStatus() {
        return gitRepositoryStatus;
    }

    public void setGitRepositoryStatus(String gitRepositoryStatus) {
        this.gitRepositoryStatus = gitRepositoryStatus;
    }

    @Override
    public String toString() {
        return "HealthCheckResponse{" +
                "status='" + status + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", version='" + version + '\'' +
                ", timestamp=" + timestamp +
                ", uptime=" + uptime +
                ", gitRepositoryStatus='" + gitRepositoryStatus + '\'' +
                '}';
    }
}
