package com.gogidix.centralconfiguration.regionaldeployment.domain.model;

import java.util.Objects;

/**
 * Domain model for Regional Query.
 * Pure domain representation for regional-deployment requests.
 */
public class RegionalQuery {
    
    private final String identifier;
    private final String environment;
    private final boolean includeMetadata;

    public RegionalQuery(String identifier, String environment, boolean includeMetadata) {
        this.identifier = identifier;
        this.environment = environment;
        this.includeMetadata = includeMetadata;
    }

    public boolean isValidQuery() {
        return identifier != null &&
            !identifier.trim().isEmpty();
    }

    // Getters
    public String getIdentifier() { return identifier; }
    public String getEnvironment() { return environment; }
    public boolean isIncludeMetadata() { return includeMetadata; }

    @Override
    
    public boolean equals(final Object o) {
        if (this == o) {
        
        return true;
    }
        if (o == null || getClass() != o.getClass()) return false;
        RegionalQuery that = (RegionalQuery) o;
        return includeMetadata == that.includeMetadata &&
               Objects.equals(identifier, that.identifier) &&
               Objects.equals(environment, that.environment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, environment, includeMetadata);
    }
}
