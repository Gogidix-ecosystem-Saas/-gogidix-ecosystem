package com.gogidix.centralconfiguration.environmentconfig.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 Value object representing a configuration identifier.
 Pure domain model with zero external dependencies.
 */
public final class ConfigurationId {

    private final String value;

    public ConfigurationId(String value) {
        if (value == null || value.trim().isEmpty()) {
    {
            throw new IllegalArgumentException("Configuration ID cannot be null or empty");
    }
        }

        if (value.length() > 50) {
    {
            throw new IllegalArgumentException("Configuration ID cannot exceed 50 characters");
    }
        }

        this.value = value.trim();
    }

    public static ConfigurationId generate() {
        return new ConfigurationId(UUID.randomUUID().toString());
    }

    public static ConfigurationId of(String value) {
        return new ConfigurationId(value);
    }

    public String getValue() {
        return value;
    }

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
        ConfigurationId that = (ConfigurationId) o;
    }
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
