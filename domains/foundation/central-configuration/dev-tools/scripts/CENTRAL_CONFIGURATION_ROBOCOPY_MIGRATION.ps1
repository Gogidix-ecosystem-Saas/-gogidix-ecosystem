# GOGIDIX CENTRAL-CONFIGURATION ROBOCOPY MIGRATION
# Certified production services migration to business groups

param(
    [string]$SourcePath = "C:\Users\frich\Desktop\Gogidix-Technology-Ecosystem\Gogidix-GitLab-Staging\Foundation-Domain\gogidix-foundation-central-configuration",
    [string]$DestinationPath = "C:\Users\frich\Desktop\Gogidix-ecosystem\domains\foundation\central-configuration"
)

$ErrorActionPreference = "Stop"
$StartTime = Get-Date

Write-Host "================================================================" -ForegroundColor Green
Write-Host "CENTRAL-CONFIGURATION ROBOCOPY MIGRATION" -ForegroundColor Green
Write-Host "Production Services Migration to Business Groups" -ForegroundColor Green
Write-Host "================================================================" -ForegroundColor Green
Write-Host "Source: $SourcePath"
Write-Host "Destination: $DestinationPath"
Write-Host "Migration Time: $($StartTime.ToString('yyyy-MM-dd HH:mm:ss'))"
Write-Host ""

# Service to Business Group Mapping
$ServiceMappings = @(
    @{ Service = "central-configuration-implementation"; BusinessGroup = "core-config-services" },
    @{ Service = "config-server"; BusinessGroup = "server-services" },
    @{ Service = "database-config"; BusinessGroup = "database-services" },
    @{ Service = "database-migrations"; BusinessGroup = "database-services" },
    @{ Service = "deployment-scripts"; BusinessGroup = "deployment-services" },
    @{ Service = "regional-deployment"; BusinessGroup = "deployment-services" },
    @{ Service = "infrastructure-as-code"; BusinessGroup = "infrastructure-services" },
    @{ Service = "kubernetes-manifests"; BusinessGroup = "infrastructure-services" },
    @{ Service = "secrets-management"; BusinessGroup = "security-services" },
    @{ Service = "disaster-recovery"; BusinessGroup = "security-services" },
    @{ Service = "feature-toggle-config"; BusinessGroup = "feature-services" },
    @{ Service = "environment-config"; BusinessGroup = "feature-services" }
)

$SuccessCount = 0
$TotalServices = $ServiceMappings.Count

Write-Host "🚀 Starting migration of $TotalServices production services..." -ForegroundColor Cyan
Write-Host ""

# Function to migrate a service using robocopy
function Robocopy-Migrate-Service {
    param(
        [string]$ServiceName,
        [string]$SourceServicePath,
        [string]$DestinationServicePath,
        [string]$BusinessGroup
    )

    Write-Host "🔄 Migrating: $ServiceName -> $BusinessGroup/$ServiceName" -ForegroundColor Yellow
    Write-Host "  From: $SourceServicePath" -ForegroundColor Gray
    Write-Host "  To:   $DestinationServicePath" -ForegroundColor Gray

    # Check if source exists
    if (-not (Test-Path $SourceServicePath)) {
        Write-Host "  ❌ ERROR: Source path not found: $SourceServicePath" -ForegroundColor Red
        return $false
    }

    # Ensure destination business group directory exists
    $DestinationBusinessGroupPath = Split-Path $DestinationServicePath -Parent
    if (-not (Test-Path $DestinationBusinessGroupPath)) {
        Write-Host "  Creating business group directory: $DestinationBusinessGroupPath" -ForegroundColor Cyan
        New-Item -ItemType Directory -Path $DestinationBusinessGroupPath -Force | Out-Null
    }

    try {
        # Remove existing target directory first (excluding locked files)
        if (Test-Path $DestinationServicePath) {
            Write-Host "  Removing target directory: $DestinationServicePath" -ForegroundColor Yellow
            try {
                Remove-Item -Path $DestinationServicePath -Recurse -Force -ErrorAction Stop
            } catch {
                Write-Host "  Some files locked, continuing with copy..." -ForegroundColor Yellow
                Get-ChildItem -Path $DestinationServicePath -Recurse -File -ErrorAction SilentlyContinue | Remove-Item -Force -ErrorAction SilentlyContinue
                Remove-Item -Path $DestinationServicePath -Recurse -Force -ErrorAction SilentlyContinue
            }
        }

        # Use robocopy with proven options
        Write-Host "  Starting robocopy..." -ForegroundColor Cyan

        # Robocopy options:
        # /E - copy subdirectories including empty ones
        # /COPY:DAT - copy Data, Attributes, Timestamps
        # /R:1 - Retry 1 time
        # /W:5 - Wait 5 seconds between retries
        # /NFL - No file list logging
        # /NDL - No directory list logging

        $robocopyResult = robocopy $SourceServicePath $DestinationServicePath /E /COPY:DAT /R:1 /W:5 /NFL /NDL

        if ($robocopyResult -ge 8) {
            Write-Host "  ❌ ERROR: Robocopy failed with exit code $robocopyResult" -ForegroundColor Red
            return $false
        } else {
            # Count migrated files
            $javaFiles = Get-ChildItem $DestinationServicePath -Filter "*.java" -Recurse -File -ErrorAction SilentlyContinue
            $pomFiles = Get-ChildItem $DestinationServicePath -Filter "pom.xml" -Recurse -File -ErrorAction SilentlyContinue

            Write-Host "  ✅ SUCCESS: Service migrated completely" -ForegroundColor Green
            Write-Host "  Result: $($javaFiles.Count) Java files, $($pomFiles.Count) POM files migrated" -ForegroundColor Green
            return $true
        }
    }
    catch {
        Write-Host "  ❌ ERROR: $($_.Exception.Message)" -ForegroundColor Red
        return $false
    }
}

# Execute migration for all services
foreach ($Mapping in $ServiceMappings) {
    $ServiceName = $Mapping.Service
    $BusinessGroup = $Mapping.BusinessGroup
    
    $SourceServicePath = Join-Path $SourcePath "backend\java\$ServiceName"
    $DestinationServicePath = Join-Path $DestinationPath "backend\java\$BusinessGroup\$ServiceName"

    if (Robocopy-Migrate-Service -ServiceName $ServiceName -SourceServicePath $SourceServicePath -DestinationServicePath $DestinationServicePath -BusinessGroup $BusinessGroup) {
        $SuccessCount++
        Write-Host "✅ $ServiceName migration completed" -ForegroundColor Green
    } else {
        Write-Host "❌ $ServiceName migration failed" -ForegroundColor Red
    }
    Write-Host ""
}

# Final summary
$TotalDuration = "{0:N1}m" -f ((Get-Date) - $StartTime).TotalMinutes
$SuccessRate = "{0:N0}%" -f (($SuccessCount / $TotalServices) * 100)

Write-Host "================================================================" -ForegroundColor Green
Write-Host "CENTRAL-CONFIGURATION MIGRATION SUMMARY" -ForegroundColor Green
Write-Host "================================================================" -ForegroundColor Green
Write-Host "Total Services: $TotalServices"
Write-Host "Successful: $SuccessCount"
Write-Host "Success Rate: $SuccessRate"
Write-Host "Total Duration: $TotalDuration"
Write-Host ""

if ($SuccessCount -eq $TotalServices) {
    Write-Host "🎉 STATUS: SUCCESS - All production services migrated!" -ForegroundColor Green
    Write-Host "✅ Central-Configuration domain ready for destination validation" -ForegroundColor Cyan
    Write-Host "✅ Business group structure implemented successfully" -ForegroundColor Cyan
} else {
    Write-Host "❌ STATUS: PARTIAL - $($TotalServices - $SuccessCount) services need attention" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Migration completed at: $(Get-Date)" -ForegroundColor Cyan
Write-Host "================================================================" -ForegroundColor Green
