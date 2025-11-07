#!/bin/bash

# =====================================================
# GOGIDIX ECOSYSTEM - INFRASTRUCTURE STARTUP SCRIPT
# =====================================================
# Quick startup script for local infrastructure testing
# Replaces GitLab local development environment
# =====================================================

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
COMPOSE_FILE="$PROJECT_ROOT/infrastructure/docker/docker-compose.infrastructure.yml"
ENV_FILE="$PROJECT_ROOT/configs/environments/.env"

# Logging function
log() {
    echo -e "${BLUE}[$(date '+%Y-%m-%d %H:%M:%S')] $1${NC}"
}

error() {
    echo -e "${RED}[ERROR] $1${NC}"
    exit 1
}

success() {
    echo -e "${GREEN}[SUCCESS] $1${NC}"
}

warning() {
    echo -e "${YELLOW}[WARNING] $1${NC}"
}

# Check prerequisites
check_prerequisites() {
    log "Checking prerequisites..."

    # Check Docker
    if ! command -v docker &> /dev/null; then
        error "Docker is not installed. Please install Docker first."
    fi

    # Check Docker Compose
    if ! command -v docker-compose &> /dev/null; then
        error "Docker Compose is not installed. Please install Docker Compose first."
    fi

    # Check if .env file exists
    if [ ! -f "$ENV_FILE" ]; then
        warning ".env file not found. Creating from template..."
        cp "$PROJECT_ROOT/configs/environments/.env.example" "$ENV_FILE"
        warning "Please update $ENV_FILE with your actual configurations before proceeding."
        read -p "Press Enter to continue or Ctrl+C to exit..."
    fi

    success "Prerequisites check completed"
}

# Docker system cleanup
cleanup_docker() {
    log "Cleaning up Docker system..."
    docker system prune -f
    docker volume prune -f
    success "Docker cleanup completed"
}

# Build and start infrastructure
start_infrastructure() {
    log "Starting Gogidix Ecosystem Infrastructure..."

    cd "$PROJECT_ROOT"

    # Start infrastructure services
    if docker-compose -f "$COMPOSE_FILE" --env-file "$ENV_FILE" up -d; then
        success "Infrastructure started successfully"
    else
        error "Failed to start infrastructure"
    fi
}

# Wait for services to be healthy
wait_for_services() {
    log "Waiting for services to be healthy..."

    # Service health check commands
    declare -A SERVICES=(
        ["postgres"]="docker exec gogidix-postgres pg_isready -U gogidix_user"
        ["mongodb"]="docker exec gogidix-mongodb mongo --eval 'db.adminCommand(\"ping\")'"
        ["redis"]="docker exec gogidix-redis redis-cli ping"
        ["elasticsearch"]="curl -f http://localhost:9200/_cluster/health"
        ["kafka"]="docker exec gogidix-kafka kafka-broker-api-versions --bootstrap-server localhost:9092"
    )

    local max_attempts=30
    local attempt=1

    for service in "${!SERVICES[@]}"; do
        log "Checking $service health..."

        while [ $attempt -le $max_attempts ]; do
            if eval "${SERVICES[$service]}" &>/dev/null; then
                success "$service is healthy"
                break
            else
                if [ $attempt -eq $max_attempts ]; then
                    warning "$service may not be fully ready, but continuing..."
                    break
                fi
                echo -n "."
                sleep 2
                ((attempt++))
            fi
        done
        attempt=1
    done
}

# Display service URLs
display_service_urls() {
    log "Service Access URLs:"
    echo ""
    echo "🎯 Development Tools:"
    echo "  • Grafana Dashboard:       http://localhost:3000 (admin/admin123)"
    echo "  • Prometheus Metrics:      http://localhost:9090"
    echo "  • PostgreSQL Admin:        http://localhost:5050"
    echo "  • Redis Commander:         http://localhost:8081"
    echo "  • Jupyter Notebook:        http://localhost:8888"
    echo "  • MailHog (Email Test):    http://localhost:8025"
    echo "  • Elasticsearch Head:      http://localhost:9100"
    echo ""
    echo "🗄️ Database Connections:"
    echo "  • PostgreSQL:              localhost:5432"
    echo "  • MongoDB:                 localhost:27017"
    echo "  • Redis:                   localhost:6379"
    echo "  • Elasticsearch:           localhost:9200"
    echo "  • InfluxDB:                localhost:8086"
    echo ""
    echo "📡 Message Queue:"
    echo "  • Kafka Bootstrap:         localhost:9092"
    echo "  • Zookeeper:               localhost:2181"
    echo ""
    echo "⚡ Service Discovery:"
    echo "  • Eureka Server:           http://localhost:8761"
    echo "  • API Gateway:             http://localhost:8080"
    echo ""
    echo "🔧 Big Data Services:"
    echo "  • Spark Master Web UI:     http://localhost:8080"
    echo "  • Spark Worker:            localhost:7077"
    echo ""
}

# Show container status
show_container_status() {
    log "Container Status:"
    docker-compose -f "$COMPOSE_FILE" ps
}

# Run health checks
run_health_checks() {
    log "Running comprehensive health checks..."

    cd "$PROJECT_ROOT/scripts/monitoring"

    if [ -f "health-check.sh" ]; then
        chmod +x health-check.sh
        ./health-check.sh local
    else
        warning "Health check script not found. Basic checks performed."
    fi
}

# Show logs for troubleshooting
show_logs() {
    local service=${1:-""}

    if [ -n "$service" ]; then
        log "Showing logs for $service..."
        docker-compose -f "$COMPOSE_FILE" logs -f "$service"
    else
        log "Showing logs for all services..."
        docker-compose -f "$COMPOSE_FILE" logs -f
    fi
}

# Stop infrastructure
stop_infrastructure() {
    log "Stopping Gogidix Ecosystem Infrastructure..."

    cd "$PROJECT_ROOT"

    if docker-compose -f "$COMPOSE_FILE" down; then
        success "Infrastructure stopped successfully"
    else
        error "Failed to stop infrastructure"
    fi
}

# Reset infrastructure (remove volumes)
reset_infrastructure() {
    warning "This will remove all data volumes. Are you sure? (y/N)"
    read -r response

    if [[ "$response" =~ ^([yY][eE][sS]|[yY])$ ]]; then
        log "Resetting infrastructure (removing volumes)..."
        cd "$PROJECT_ROOT"
        docker-compose -f "$COMPOSE_FILE" down -v
        docker system prune -f
        success "Infrastructure reset completed"
    else
        log "Reset cancelled"
    fi
}

# Show help
show_help() {
    echo "Gogidix Ecosystem Infrastructure Manager"
    echo ""
    echo "Usage: $0 [COMMAND] [OPTIONS]"
    echo ""
    echo "Commands:"
    echo "  start       Start the infrastructure (default)"
    echo "  stop        Stop the infrastructure"
    echo "  restart     Restart the infrastructure"
    echo "  status      Show container status"
    echo "  logs        Show logs (service optional)"
    echo "  health      Run health checks"
    echo "  cleanup     Clean up Docker system"
    echo "  reset       Reset infrastructure (removes volumes)"
    echo "  help        Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 start                    # Start all services"
    echo "  $0 logs postgres            # Show PostgreSQL logs"
    echo "  $0 status                   # Show container status"
    echo ""
}

# Main execution
main() {
    local command=${1:-"start"}

    case "$command" in
        "start")
            check_prerequisites
            cleanup_docker
            start_infrastructure
            wait_for_services
            display_service_urls
            show_container_status
            run_health_checks
            ;;
        "stop")
            stop_infrastructure
            ;;
        "restart")
            stop_infrastructure
            sleep 5
            main "start"
            ;;
        "status")
            show_container_status
            ;;
        "logs")
            show_logs "$2"
            ;;
        "health")
            run_health_checks
            ;;
        "cleanup")
            cleanup_docker
            ;;
        "reset")
            reset_infrastructure
            ;;
        "help"|"-h"|"--help")
            show_help
            ;;
        *)
            error "Unknown command: $command. Use 'help' for available commands."
            ;;
    esac
}

# Execute main function
main "$@"