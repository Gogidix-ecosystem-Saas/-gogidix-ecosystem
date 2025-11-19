package com.gogidix.centralconfiguration.dbmigrations.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO for migration requests.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MigrationRequest {

    @NotBlank(message = "Database name is required")
    private String databaseName;

    @NotBlank(message = "Migration type is required")
    private String migrationType; // FORWARD, ROLLBACK, BASELINE

    private String targetVersion;

    private String environment; // DEV, STAGING, PRODUCTION

    @Builder.Default
    private boolean dryRun = false;

    @Builder.Default
    private boolean validateBeforeMigrate = true;

    @Builder.Default
    private boolean backupBeforeMigrate = true;

    private List<String> migrationScripts;

    private Map<String, String> parameters;

    private String comment;

    @NotNull(message = "Requested by is required")
    private String requestedBy;
}