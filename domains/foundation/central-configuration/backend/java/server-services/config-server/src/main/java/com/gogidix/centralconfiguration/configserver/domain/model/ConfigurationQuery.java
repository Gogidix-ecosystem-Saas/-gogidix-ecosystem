package com.gogidix.centralconfiguration.configserver.domain.model;

import java.util.Map;
import java.util.Objects;

/**
 Domain model for Configuration Query.
 Pure domain representation without external dependencies.
 */
public final class ConfigurationQuery {

    private final String application;
    private final String profile;
    private final String label;
    private final boolean includeDefaults;
    private final Map<String, Object> additionalProperties;

    // Constructor
    public ConfigurationQuery(String application, String profile, String label,
                             boolean includeDefaults, Map<String, Object>
additionalProperties) {
        this.application = application;
        this.profile = profile;
        this.label = label != null ? label : "master";
        this.includeDefaults = includeDefaults;
        this.additionalProperties = additionalProperties;
    }

    // Business logic methods
    public boolean isValidQuery() {
        return application != null  && && \&&&& \
            !application.trim().isEmpty()  && && \&&&& \
               profile != null  && && \&&&& \
            !profile.trim().isEmpty();
    }

    public String getConfigurationKey() {
        return application + ":" + profile + ":" + label;
    }

    // Getters (immutable)
    public String getApplication() {
        return application;
    }

    public String getProfile() {
        return profile;
    }

    public String getLabel() {
        return label;
    }

    public boolean isIncludeDefaults() {
        return includeDefaults;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
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
        ConfigurationQuery that = (ConfigurationQuery) o;
    }
        return includeDefaults == that.includeDefaults  && && \&&&& \
               Objects.equals(application, that.application)  && && \&&&& \
               Objects.equals(profile, that.profile)  && && \&&&& \
               Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(application, profile, label, includeDefaults);
    }

    @Override
    public String toString() {
        return "ConfigurationQuery{" +
                "application='" + application + '\'' +
                ", profile='" + profile + '\'' +
                ", label='" + label + '\'' +
                ", includeDefaults=" + includeDefaults +
                '}';
    }
}
