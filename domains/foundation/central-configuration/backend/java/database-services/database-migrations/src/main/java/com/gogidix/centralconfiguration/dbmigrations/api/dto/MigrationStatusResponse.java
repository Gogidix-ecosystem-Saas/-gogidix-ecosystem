package com.gogidix.centralconfiguration.dbmigrations.api.dto;

import com.gogidix.centralconfiguration.dbmigrations.domain.model.MigrationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for migration status responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MigrationStatusResponse {

    private String databaseName;
    
    private String currentVersion;
    
    private String latestAvailableVersion;
    
    private MigrationStatus status;
    
    private LocalDateTime lastMigrationTime;
    
    private List<String> pendingMigrations;
    
    private List<String> appliedMigrations;
    
    private boolean isHealthy;
    
    private List<String> issues;
    
    private Object schemaInfo;
}