package com.gogidix.centralconfiguration.configserver.application.service;

import com.gogidix.centralconfiguration.configserver.domain.model.ConfigurationQuery;
import com.gogidix.centralconfiguration.configserver.domain.model.ConfigurationResult;
import com.gogidix.centralconfiguration.configserver.domain.model.ConfigurationData;
import com.gogidix.centralconfiguration.configserver.domain.port.ConfigurationPort;
import com.gogidix.centralconfiguration.configserver.domain.port.ConfigurationServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Application Service for Configuration management.
 * Implements business logic for configuration operations.
 */
@Service
public class ConfigurationService implements ConfigurationServicePort {

    @Autowired
    private ConfigurationPort configurationPort;

    @Value("${server.port:8888}")
    private String serverPort;

    @Value("${spring.application.name:config-server}")
    private String applicationName;

    /**
     * Get configuration for an application and profile.
     */
    @Override
    public ConfigurationResult getConfiguration(String application, String profile) {
        Optional<ConfigurationData> configDataOpt = configurationPort.findByApplicationAndProfile(application, profile);
        ConfigurationData configData = configDataOpt.orElse(null);
        
        Map<String, Object> properties;
        if (configData != null) {
            properties = configData.getProperties();
        } else {
            // Default configuration
            properties = new HashMap<>();
            properties.put("server.port", "8080");
            properties.put("spring.application.name", application);
            properties.put("management.endpoints.web.exposure.include", "health,info");
        }
        
        return new ConfigurationResult(
            application,
            profile,
            "master",
            "1.0.0",
            properties,
            null
        );
    }

    /**
     * Get configuration using domain query.
     */
    @Override
    public ConfigurationResult getConfiguration(ConfigurationQuery query) {
        return getConfiguration(query.getApplication(), query.getProfile());
    }
    
    /**
     * Get server information.
     */
    @Override
    public ConfigurationResult getServerInfo() {
        Map<String, Object> serverProps = new HashMap<>();
        serverProps.put("server.name", applicationName);
        serverProps.put("server.port", serverPort);
        serverProps.put("server.version", "1.0.0");
        serverProps.put("spring.cloud.config.server.git.uri", "file://config-repo");
        
        return new ConfigurationResult(
            applicationName,
            "server",
            "master",
            "1.0.0",
            serverProps,
            "Configuration Server - Port: " + serverPort
        );
    }

    /**
     * Refresh configuration cache.
     */
    @Override
    public void refreshConfiguration() {
        configurationPort.clearCache();
    }
    
    /**
     * Get all applications.
     */
    @Override
    public List<String> getAllApplications() {
        return configurationPort.getAllApplications();
    }
    
    /**
     * Get profiles for an application.
     */
    @Override
    public List<String> getProfilesForApplication(String application) {
        return configurationPort.getProfilesForApplication(application);
    }
    
    /**
     * Update configuration.
     */
    @Override
    public void updateConfiguration(String application, String profile, ConfigurationData configurationData) {
        configurationPort.save(configurationData);
    }
    
    /**
     * Delete configuration.
     */
    @Override
    public void deleteConfiguration(String application, String profile) {
        configurationPort.delete(application, profile);
    }
    
    /**
     * Check if configuration exists.
     */
    @Override
    public boolean configurationExists(String application, String profile) {
        return configurationPort.exists(application, profile);
    }
}
