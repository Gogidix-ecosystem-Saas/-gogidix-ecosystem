package com.gogidix.centralconfiguration.dbmigrations.api.controller;

import com.gogidix.centralconfiguration.dbmigrations.api.dto.MigrationRequest;
import com.gogidix.centralconfiguration.dbmigrations.api.dto.MigrationResponse;
import com.gogidix.centralconfiguration.dbmigrations.api.dto.MigrationStatusResponse;
import com.gogidix.centralconfiguration.dbmigrations.application.service.MigrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Database Migration operations.
 */
@RestController
@RequestMapping("/api/v1/migrations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Database Migrations", description = "Database migration management operations")
public class MigrationController {

    private final MigrationService migrationService;

    @PostMapping("/execute")
    @Operation(summary = "Execute database migration", description = "Execute migration for specified database")
    @ApiResponse(responseCode = "200", description = "Migration executed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid migration request")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DBA')")
    public ResponseEntity<MigrationResponse> executeMigration(
            @Valid @RequestBody MigrationRequest request) {
        log.info("Executing migration for database: {}", request.getDatabaseName());
        MigrationResponse response = migrationService.executeMigration(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/dry-run")
    @Operation(summary = "Dry run migration", description = "Simulate migration without executing")
    @ApiResponse(responseCode = "200", description = "Dry run completed successfully")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DBA')")
    public ResponseEntity<MigrationResponse> dryRunMigration(
            @Valid @RequestBody MigrationRequest request) {
        log.info("Performing dry run for database: {}", request.getDatabaseName());
        MigrationResponse response = migrationService.dryRunMigration(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{databaseName}")
    @Operation(summary = "Get migration status", description = "Get current migration status for database")
    @ApiResponse(responseCode = "200", description = "Migration status retrieved successfully")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DBA') or hasRole('DEVELOPER')")
    public ResponseEntity<MigrationStatusResponse> getMigrationStatus(
            @Parameter(description = "Database name") @PathVariable String databaseName) {
        log.info("Getting migration status for database: {}", databaseName);
        MigrationStatusResponse response = migrationService.getMigrationStatus(databaseName);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    @Operation(summary = "Get migration history", description = "Get paginated migration history")
    @ApiResponse(responseCode = "200", description = "Migration history retrieved successfully")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DBA') or hasRole('DEVELOPER')")
    public ResponseEntity<Page<MigrationResponse>> getMigrationHistory(
            @Parameter(description = "Database name filter") @RequestParam(required = false) String databaseName,
            Pageable pageable) {
        log.info("Getting migration history for database: {}", databaseName);
        Page<MigrationResponse> history = migrationService.getMigrationHistory(databaseName, pageable);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/rollback")
    @Operation(summary = "Rollback migration", description = "Rollback to specific migration version")
    @ApiResponse(responseCode = "200", description = "Rollback completed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid rollback request")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MigrationResponse> rollbackMigration(
            @Valid @RequestBody MigrationRequest request) {
        log.info("Rolling back migration for database: {} to version: {}", 
                request.getDatabaseName(), request.getTargetVersion());
        MigrationResponse response = migrationService.rollbackMigration(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate migration", description = "Validate migration scripts and database state")
    @ApiResponse(responseCode = "200", description = "Validation completed successfully")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DBA')")
    public ResponseEntity<MigrationStatusResponse> validateMigration(
            @Parameter(description = "Database name") @RequestParam String databaseName) {
        log.info("Validating migration for database: {}", databaseName);
        MigrationStatusResponse response = migrationService.validateMigration(databaseName);
        return ResponseEntity.ok(response);
    }
}
