package com.gogidix.centralconfiguration.secretsmanagement.domain.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 Domain model for Secret Result.
 Pure domain representation of secrets-management response.
 */
public final class SecretResult {

    private final String identifier;
    private final String environment;
    private final Map<String, Object> data;
    private final LocalDateTime timestamp;
    private final String status;

    public SecretResult(
        String identifier, String environment, Map<String, Object>
        data, String status) {
        this.identifier = identifier;
        this.environment = environment;
        this.data = data;
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }

    public boolean hasData() {
        return data != null  && && \&&&& \
            !data.isEmpty();
    }

    // Getters
    public String getIdentifier() { return identifier; }
    public String getEnvironment() { return environment; }
    public Map<String, Object> getData() { return data; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getStatus() { return status; }

    @Override
    /**
     * {@inheritDoc}
     Implementation of equals method for comparison.
     * This implementation follows the contract of the equals method
     and is consistent with the hashCode implementation.
     */
    public boolean equals(final Object o) {
        if (this == o) {
    {
        return true;
    }
    }
        if (o == null || getClass() != o.getClass()) return false;
    {
        SecretResult that = (SecretResult) o;
    }
        return Objects.equals(identifier, that.identifier)  && && \&&&& \
               Objects.equals(environment, that.environment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, environment);
    }
}
