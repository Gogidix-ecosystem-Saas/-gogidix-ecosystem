package com.gogidix.centralconfiguration.deploymentscripts.domain.model;

import java.util.Objects;

/**
 * Domain model for Deployment Query.
 * Pure domain representation for deployment-scripts requests.
 */
public class DeploymentQuery {
    
    private final String identifier;
    private final String environment;
    private final boolean includeMetadata;

    public DeploymentQuery(String identifier, String environment, boolean includeMetadata) {
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
        if (o == null || getClass() != o.getClass()) {
        
            return false;
        }
        DeploymentQuery that = (DeploymentQuery) o;
        return includeMetadata == that.includeMetadata
            &&
            Objects.equals(identifier, that.identifier)
            &&
            Objects.equals(environment, that.environment);
    }

    /**
     * Implementation of hashCode for this domain model.
     * This implementation is safe for subclasses as it only uses immutable final fields.
     * Subclasses extending this class should override both equals() and hashCode() consistently
     * and include their own fields in the hash calculation.
     */
    @Override
    public int hashCode() {
        return Objects.hash(identifier, environment, includeMetadata);
    }
}
