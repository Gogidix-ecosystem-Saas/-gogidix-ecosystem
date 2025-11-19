package com.gogidix.centralconfiguration.dbmigrations.application.service;

import com.gogidix.centralconfiguration.dbmigrations.api.dto.MigrationRequest;
import com.gogidix.centralconfiguration.dbmigrations.api.dto.MigrationResponse;
import
    com.gogidix.centralconfiguration.dbmigrations.api.dto.MigrationStatusResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 Service interface for database migration operations.
 */
public interface MigrationService {

    /**
     Execute database migration.
     */
    MigrationResponse executeMigration(MigrationRequest request);

    /**
     Perform dry run migration (simulation).
     */
    MigrationResponse dryRunMigration(MigrationRequest request);

    /**
     Get migration status for a database.
     */
    MigrationStatusResponse getMigrationStatus(String databaseName);

    /**
     Get migration history with pagination.
     */
    Page<MigrationResponse> getMigrationHistory(String databaseName, Pageable pageable);

    /**
     Rollback migration to specific version.
     */
    MigrationResponse rollbackMigration(MigrationRequest request);

    /**
     Validate migration scripts and database state.
     */
    MigrationStatusResponse validateMigration(String databaseName);
}
