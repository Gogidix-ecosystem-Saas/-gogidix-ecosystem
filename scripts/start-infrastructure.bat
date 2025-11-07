@echo off
REM =====================================================
REM GOGIDIX ECOSYSTEM - INFRASTRUCTURE STARTUP SCRIPT
REM =====================================================
REM Quick startup script for local infrastructure testing
REM Replaces GitLab local development environment
REM =====================================================

setlocal enabledelayedexpansion

REM Configuration
set PROJECT_ROOT=%~dp0..\..
set COMPOSE_FILE=%PROJECT_ROOT%\infrastructure\docker\docker-compose.infrastructure.yml
set ENV_FILE=%PROJECT_ROOT%\configs\environments\.env

REM Colors for Windows console
set "RED=[91m"
set "GREEN=[92m"
set "YELLOW=[93m"
set "BLUE=[94m"
set "NC=[0m"

REM Logging function
:log
echo %BLUE%[%date% %time%] %~1%NC%
goto :eof

:error
echo %RED%[ERROR] %~1%NC%
pause
exit /b 1

:success
echo %GREEN%[SUCCESS] %~1%NC%
goto :eof

:warning
echo %YELLOW%[WARNING] %~1%NC%
goto :eof

REM Check prerequisites
:check_prerequisites
call :log "Checking prerequisites..."

REM Check Docker
docker --version >nul 2>&1
if errorlevel 1 (
    call :error "Docker is not installed. Please install Docker Desktop first."
)

REM Check Docker Compose
docker-compose --version >nul 2>&1
if errorlevel 1 (
    call :error "Docker Compose is not installed. Please install Docker Compose first."
)

REM Check if .env file exists
if not exist "%ENV_FILE%" (
    call :warning ".env file not found. Creating from template..."
    copy "%PROJECT_ROOT%\configs\environments\.env.example" "%ENV_FILE%" >nul
    call :warning "Please update %ENV_FILE% with your actual configurations before proceeding."
    pause
)

call :success "Prerequisites check completed"
goto :eof

REM Docker system cleanup
:cleanup_docker
call :log "Cleaning up Docker system..."
docker system prune -f
docker volume prune -f
call :success "Docker cleanup completed"
goto :eof

REM Build and start infrastructure
:start_infrastructure
call :log "Starting Gogidix Ecosystem Infrastructure..."

cd /d "%PROJECT_ROOT%"

REM Start infrastructure services
docker-compose -f "%COMPOSE_FILE%" --env-file "%ENV_FILE%" up -d
if errorlevel 1 (
    call :error "Failed to start infrastructure"
)

call :success "Infrastructure started successfully"
goto :eof

REM Wait for services to be healthy
:wait_for_services
call :log "Waiting for services to be healthy..."

echo Waiting for services to initialize...
timeout /t 30 /nobreak >nul

call :success "Service wait completed"
goto :eof

REM Display service URLs
:display_service_urls
call :log "Service Access URLs:"
echo.
echo 🎯 Development Tools:
echo   • Grafana Dashboard:       http://localhost:3000 (admin/admin123)
echo   • Prometheus Metrics:      http://localhost:9090
echo   • PostgreSQL Admin:        http://localhost:5050
echo   • Redis Commander:         http://localhost:8081
echo   • Jupyter Notebook:        http://localhost:8888
echo   • MailHog (Email Test):    http://localhost:8025
echo   • Elasticsearch Head:      http://localhost:9100
echo.
echo 🗄️ Database Connections:
echo   • PostgreSQL:              localhost:5432
echo   • MongoDB:                 localhost:27017
echo   • Redis:                   localhost:6379
echo   • Elasticsearch:           localhost:9200
echo   • InfluxDB:                localhost:8086
echo.
echo 📡 Message Queue:
echo   • Kafka Bootstrap:         localhost:9092
echo   • Zookeeper:               localhost:2181
echo.
echo ⚡ Service Discovery:
echo   • Eureka Server:           http://localhost:8761
echo   • API Gateway:             http://localhost:8080
echo.
echo 🔧 Big Data Services:
echo   • Spark Master Web UI:     http://localhost:8080
echo   • Spark Worker:            localhost:7077
echo.
goto :eof

REM Show container status
:show_container_status
call :log "Container Status:"
docker-compose -f "%COMPOSE_FILE%" ps
goto :eof

REM Stop infrastructure
:stop_infrastructure
call :log "Stopping Gogidix Ecosystem Infrastructure..."

cd /d "%PROJECT_ROOT%"

docker-compose -f "%COMPOSE_FILE%" down
if errorlevel 1 (
    call :error "Failed to stop infrastructure"
)

call :success "Infrastructure stopped successfully"
goto :eof

REM Reset infrastructure (remove volumes)
:reset_infrastructure
call :warning "This will remove all data volumes. Are you sure? (y/N)"
set /p response=

if /i "!response!"=="y" (
    call :log "Resetting infrastructure (removing volumes)..."
    cd /d "%PROJECT_ROOT%"
    docker-compose -f "%COMPOSE_FILE%" down -v
    docker system prune -f
    call :success "Infrastructure reset completed"
) else (
    call :log "Reset cancelled"
)
goto :eof

REM Show help
:show_help
echo Gogidix Ecosystem Infrastructure Manager
echo.
echo Usage: %~nx0 [COMMAND]
echo.
echo Commands:
echo   start       Start the infrastructure (default)
echo   stop        Stop the infrastructure
echo   restart     Restart the infrastructure
echo   status      Show container status
echo   cleanup     Clean up Docker system
echo   reset       Reset infrastructure (removes volumes)
echo   help        Show this help message
echo.
echo Examples:
echo   %~nx0 start                    # Start all services
echo   %~nx0 status                   # Show container status
echo.
goto :eof

REM Main execution
:main
set command=%1
if "%command%"=="" set command=start

if /i "%command%"=="start" (
    call :check_prerequisites
    call :cleanup_docker
    call :start_infrastructure
    call :wait_for_services
    call :display_service_urls
    call :show_container_status
) else if /i "%command%"=="stop" (
    call :stop_infrastructure
) else if /i "%command%"=="restart" (
    call :stop_infrastructure
    timeout /t 5 /nobreak >nul
    call :main start
) else if /i "%command%"=="status" (
    call :show_container_status
) else if /i "%command%"=="cleanup" (
    call :cleanup_docker
) else if /i "%command%"=="reset" (
    call :reset_infrastructure
) else if /i "%command%"=="help" (
    call :show_help
) else (
    call :error "Unknown command: %command%. Use 'help' for available commands."
)

if not "%command%"=="help" pause
goto :eof

REM Execute main function
call :main %*