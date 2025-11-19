package com.gogidix.centralconfiguration.configserver.api.controller;

import com.gogidix.centralconfiguration.configserver.api.dto.ConfigurationRequest;
import com.gogidix.centralconfiguration.configserver.api.dto.ConfigurationResponse;
import com.gogidix.centralconfiguration.configserver.api.dto.HealthCheckResponse;
import com.gogidix.centralconfiguration.configserver.domain.port.ConfigurationServicePort;
import com.gogidix.centralconfiguration.configserver.adapter.in.web.mapper.ConfigurationMapper;
import com.gogidix.centralconfiguration.configserver.domain.model.ConfigurationQuery;
import com.gogidix.centralconfiguration.configserver.domain.model.ConfigurationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Configuration Server API endpoints.
 * Provides configuration management API for microservices.
 */
@RestController
@RequestMapping("/api/config")
@CrossOrigin(origins = "*")
@Validated
public class ConfigController {

    @Autowired
    private ConfigurationServicePort configurationService;
    
    @Autowired
    private ConfigurationMapper configurationMapper;

    /**
     * Get configuration properties for a service.
     */
    @GetMapping("/properties/{application}")
    public ResponseEntity<ConfigurationResponse> getConfiguration(
            @PathVariable @NotBlank String application,
            @RequestParam(defaultValue = "default") @NotBlank String profile) {
        
        ConfigurationResult result = configurationService.getConfiguration(application, profile);
        ConfigurationResponse response = configurationMapper.toApiResponse(result);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get configuration using POST request with detailed parameters.
     */
    @PostMapping("/properties")
    public ResponseEntity<ConfigurationResponse> getConfigurationPost(
            @Valid @RequestBody ConfigurationRequest request) {
        
        ConfigurationQuery query = configurationMapper.toDomainQuery(request);
        ConfigurationResult result = configurationService.getConfiguration(query);
        ConfigurationResponse response = configurationMapper.toApiResponse(result);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Update configuration for a service.
     */
    @PutMapping("/properties/{application}/{profile}")
    public ResponseEntity<String> updateConfiguration(
            @PathVariable @NotBlank String application,
            @PathVariable @NotBlank String profile,
            @Valid @RequestBody ConfigurationRequest request) {
        
        var configurationData = configurationMapper.toDomainData(request);
        configurationService.updateConfiguration(application, profile, configurationData);
        return ResponseEntity.ok("Configuration updated successfully");
    }
    
    /**
     * Delete configuration for a service.
     */
    @DeleteMapping("/properties/{application}/{profile}")
    public ResponseEntity<String> deleteConfiguration(
            @PathVariable @NotBlank String application,
            @PathVariable @NotBlank String profile) {
        
        configurationService.deleteConfiguration(application, profile);
        return ResponseEntity.ok("Configuration deleted successfully");
    }
    
    /**
     * Get all applications.
     */
    @GetMapping("/applications")
    public ResponseEntity<List<String>> getAllApplications() {
        List<String> applications = configurationService.getAllApplications();
        return ResponseEntity.ok(applications);
    }
    
    /**
     * Get profiles for an application.
     */
    @GetMapping("/applications/{application}/profiles")
    public ResponseEntity<List<String>> getProfiles(@PathVariable @NotBlank String application) {
        List<String> profiles = configurationService.getProfilesForApplication(application);
        return ResponseEntity.ok(profiles);
    }
    
    /**
     * Refresh configuration cache.
     */
    @PostMapping("/refresh")
    public ResponseEntity<String> refreshConfiguration() {
        configurationService.refreshConfiguration();
        return ResponseEntity.ok("Configuration cache refreshed successfully");
    }

    /**
     * Health check endpoint for config server.
     */
    @GetMapping("/health")
    public ResponseEntity<HealthCheckResponse> health() {
        HealthCheckResponse healthResponse = new HealthCheckResponse("UP");
        healthResponse.setServiceName("config-server");
        healthResponse.setUptime(System.currentTimeMillis());
        healthResponse.setGitRepositoryStatus("Connected");
        
        Map<String, String> details = new HashMap<>();
        details.put("timestamp", LocalDateTime.now().toString());
        details.put("port", "8888");
        details.put("profiles", "default,git,native");
        healthResponse.setDetails(details);
        
        return ResponseEntity.ok(healthResponse);
    }

    /**
     * Get server information.
     */
    @GetMapping("/info")
    public ResponseEntity<ConfigurationResponse> info() {
        ConfigurationResult result = configurationService.getServerInfo();
        ConfigurationResponse response = configurationMapper.toApiResponse(result);
        return ResponseEntity.ok(response);
    }
}