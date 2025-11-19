package com.gogidix.centralconfiguration.dbmigrations.domain.model;

/**
 * Enumeration for migration status values.
 */
public enum MigrationStatus {
    PENDING,
    RUNNING,
    SUCCESS,
    FAILED,
    ROLLED_BACK,
    VALIDATING,
    VALIDATION_FAILED,
    BACKING_UP,
    BACKUP_FAILED
}
