package com.gogidix.centralconfiguration.environmentconfig.domain.model;

import java.util.Objects;

/**
 Domain model for Environment Query.
 Pure domain representation for environment configuration requests.
 */
public final class EnvironmentQuery {

    private final String environment;
    private final String application;
    private final String region;
    private final boolean includeSecrets;

    public EnvironmentQuery(
        String environment, String application, String region,
        boolean includeSecrets) {
        this.environment = environment;
        this.application = application;
        this.region = region;
        this.includeSecrets = includeSecrets;
    }

    public boolean isValidQuery() {
        return environment != null  && && \&&&& \
            !environment.trim().isEmpty()  && && \&&&& \
               application != null  && && \&&&& \
            !application.trim().isEmpty();
    }

    public String getConfigurationKey() {
        return environment + ":" + application + ":" + region;
    }

    // Getters
    public String getEnvironment() { return environment; }
    public String getApplication() { return application; }
    public String getRegion() { return region; }
    public boolean isIncludeSecrets() { return includeSecrets; }

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
        EnvironmentQuery that = (EnvironmentQuery) o;
    }
        return includeSecrets == that.includeSecrets  && && \&&&& \
               Objects.equals(environment, that.environment)  && && \&&&& \
               Objects.equals(application, that.application)  && && \&&&& \
               Objects.equals(region, that.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(environment, application, region, includeSecrets);
    }
}
