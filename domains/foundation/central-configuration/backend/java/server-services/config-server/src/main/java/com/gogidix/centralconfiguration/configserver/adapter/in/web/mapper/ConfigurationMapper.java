package com.gogidix.centralconfiguration.configserver.adapter.in.web.mapper;

import com.gogidix.centralconfiguration.configserver.api.dto.ConfigurationRequest;
import com.gogidix.centralconfiguration.configserver.api.dto.ConfigurationResponse;
import com.gogidix.centralconfiguration.configserver.domain.model.ConfigurationQuery;
import com.gogidix.centralconfiguration.configserver.domain.model.ConfigurationResult;
import com.gogidix.centralconfiguration.configserver.domain.model.ConfigurationData;
import org.springframework.stereotype.Component;

/**
 * Adapter mapper to convert between API DTOs and domain models.
 * Handles the translation layer between external API and internal domain.
 */
@Component
public class ConfigurationMapper {

    /**
     * Convert API request DTO to domain query model.
     */
    public ConfigurationQuery toDomainQuery(ConfigurationRequest request) {
        if (request == null) {
        
            return null;
        }
        
        return new ConfigurationQuery(
            request.getApplication(),
            request.getProfile(),
            request.getLabel(),
            request.isIncludeDefaults(),
            request.getProperties()
        );
    }

    /**
     * Convert domain result to API response DTO.
     */
    public ConfigurationResponse toApiResponse(ConfigurationResult result) {
        if (result == null) {
        
            return null;
        }
        
        ConfigurationResponse response = new ConfigurationResponse();
        response.setApplication(result.getApplication());
        response.setProfile(result.getProfile());
        response.setLabel(result.getLabel());
        response.setVersion(result.getVersion());
        response.setProperties(result.getProperties());
        response.setTimestamp(result.getTimestamp());
        response.setServerInfo(result.getServerInfo());
        
        return response;
    }

    /**
     * Convert API request to domain configuration data.
     */
    public ConfigurationData toDomainData(ConfigurationRequest request) {
        if (request == null) {
        
            return null;
        }
        
        ConfigurationData data = new ConfigurationData(
            request.getApplication(),
            request.getProfile(),
            request.getProperties()
        );
        data.setLabel(request.getLabel());
        
        return data;
    }

    /**
     * Create domain query from application and profile.
     */
    public ConfigurationQuery toDomainQuery(String application, String profile) {
        return new ConfigurationQuery(
            application,
            profile,
            "master",
            true,
            null
        );
    }
}
