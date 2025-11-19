package com.gogidix.centralconfiguration.dbmigrations.api.dto;

import
    com.gogidix.centralconfiguration.dbmigrations.domain.model.MigrationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 DTO for migration responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class MigrationResponse {

    private String migrationId;

    private String databaseName;

    private String migrationType;

    private String fromVersion;

    private String toVersion;

    private MigrationStatus status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long durationMs;

    private boolean success;

    private String errorMessage;

    private List<String> warnings;

    private List<String> appliedScripts;

    private String backupLocation;

    private String requestedBy;

    private String comment;

    private Object metadata;
}
