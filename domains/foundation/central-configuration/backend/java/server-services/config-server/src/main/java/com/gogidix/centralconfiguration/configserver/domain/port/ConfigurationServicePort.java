package com.gogidix.centralconfiguration.configserver.domain.port;

import com.gogidix.centralconfiguration.configserver.domain.model.ConfigurationQuery;
import com.gogidix.centralconfiguration.configserver.domain.model.ConfigurationResult;
import com.gogidix.centralconfiguration.configserver.domain.model.ConfigurationData;

import java.util.List;

/**
 * Domain port interface for Configuration Service operations.
 * Defines the contract for configuration business logic using pure domain models.
 */
public interface ConfigurationServicePort {
    
    ConfigurationResult getConfiguration(ConfigurationQuery query);
    
    ConfigurationResult getConfiguration(String application, String profile);
    
    ConfigurationResult getServerInfo();
    
    void refreshConfiguration();
    
    List<String> getAllApplications();
    
    List<String> getProfilesForApplication(String application);
    
    void updateConfiguration(String application, String profile, ConfigurationData configurationData);
    
    void deleteConfiguration(String application, String profile);
    
    boolean configurationExists(String application, String profile);
}