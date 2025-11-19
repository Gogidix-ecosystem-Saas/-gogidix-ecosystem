package com.gogidix.centralconfiguration.configserver.domain.port;

import
    com.gogidix.centralconfiguration.configserver.domain.model.ConfigurationData;

import java.util.List;
import java.util.Optional;

/**
 Domain port interface for Configuration operations.
 Defines the contract for configuration data access.
 */
public interface ConfigurationPort {

    Optional<ConfigurationData> findByApplicationAndProfile(String application, String profile);

    List<ConfigurationData> findByApplication(String application);

    void save(ConfigurationData configurationData);

    void delete(String application, String profile);

    List<String> getAllApplications();

    List<String> getProfilesForApplication(String application);

    void clearCache();

    boolean exists(String application, String profile);
}
