package com.gogidix.centralconfiguration.environmentconfig.domain.port;

import com.gogidix.centralconfiguration.environmentconfig.domain.model.EnvironmentConfiguration;

import java.util.List;
import java.util.Optional;

/**
 * Domain port interface for Environment operations.
 * Defines the contract for environment configuration data access.
 */
public interface EnvironmentPort {
    
    Optional<EnvironmentConfiguration> findByEnvironmentAndApplication(String environment, String application);
    
    List<EnvironmentConfiguration> findByEnvironment(String environment);
    
    List<EnvironmentConfiguration> findByApplication(String application);
    
    List<EnvironmentConfiguration> findActiveConfigurations();
    
    void save(EnvironmentConfiguration configuration);
    
    void delete(String environment, String application);
    
    List<String> getAllEnvironments();
    
    List<String> getAllApplications();
    
    boolean exists(String environment, String application);
    
    void activateEnvironment(String environment, String application);
    
    void deactivateEnvironment(String environment, String application);
}
