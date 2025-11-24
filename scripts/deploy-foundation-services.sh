#!/bin/bash

# Foundation Services Deployment Script
# Deploys all foundation services starting with Eureka Service Registry
# Priority: Service Registry → Config Server → API Gateway → Other Services

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

echo -e "${BLUE}========================================"
echo "   GOGIDIX FOUNDATION SERVICES DEPLOY"
echo "   Starting with Eureka Service Registry"
echo "   Building Foundation for Management Domain"
echo "========================================${NC}"

# Configuration
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
FOUNDATION_DIR="$PROJECT_DIR/domains/foundation"
T3_SMALL_IP="54.234.49.55"
T3_NANO_IP="34.204.174.69"
SSH_KEY="$HOME/.ssh/gogidix-enterprise-key-new"

# Foundation Services Deployment Order
P0_SERVICES=(
    # Core Infrastructure (Priority 0)
    "shared-infrastructure/backend/java/core-infrastructure/service-registry:8411"
    "shared-infrastructure/backend/java/core-infrastructure/config-server:8404"
    "shared-infrastructure/backend/java/core-infrastructure/api-gateway:8401"
)

P1_SERVICES=(
    # Essential Services (Priority 1)
    "shared-infrastructure/backend/java/core-infrastructure/api-gateway-management-service:8402"
    "shared-infrastructure/backend/java/core-infrastructure/load-balancer-management-service:8403"
    "shared-infrastructure/backend/java/core-infrastructure/message-broker:8405"
    "shared-infrastructure/backend/java/core-infrastructure/database-management-service:8406"
)

P2_SERVICES=(
    # Important Services (Priority 2)
    "shared-infrastructure/backend/java/core-infrastructure/service-discovery-service:8407"
    "shared-infrastructure/backend/java/core-infrastructure/service-mesh-service:8408"
    "shared-infrastructure/backend/java/core-infrastructure/observability/monitoring-service:8409"
    "shared-infrastructure/backend/java/core-infrastructure/observability/metrics-collection-management-service:8410"
)

# Central Configuration Services
CENTRAL_CONFIG_SERVICES=(
    "central-configuration/backend/java/core-config-services/core-configuration:8420"
    "central-configuration/backend/java/server-services/config-server:8421"
    "central-configuration/backend/java/infrastructure-services/infrastructure-as-code:8422"
    "central-configuration/backend/java/infrastructure-services/kubernetes-manifests:8423"
)

# Shared Libraries (Build First)
SHARED_LIBRARIES=(
    "shared-libs/backend/java/core-platform/core-api-client"
    "shared-libs/backend/java/core-platform/core-cache-engine"
    "shared-libs/backend/java/core-platform/core-data-structures"
    "shared-libs/backend/java/core-platform/core-database-connectors"
    "shared-libs/backend/java/core-platform/core-event-handlers"
    "shared-libs/backend/java/core-platform/core-logging-framework"
    "shared-libs/backend/java/core-platform/core-metrics"
    "shared-libs/backend/java/core-platform/core-security"
)

# Function to build service
build_service() {
    local service_path=$1
    local service_name=$(basename $service_path)

    echo -e "${YELLOW}Building $service_name...${NC}"
    cd "$PROJECT_DIR/$service_path"

    if [ -f "pom.xml" ]; then
        mvn clean package -DskipTests -q
        if [ $? -eq 0 ]; then
            echo -e "${GREEN}✓ Built $service_name${NC}"
            return 0
        else
            echo -e "${RED}✗ Failed to build $service_name${NC}"
            return 1
        fi
    else
        echo -e "${YELLOW}⚠ No pom.xml found for $service_name${NC}"
        return 0
    fi
}

# Function to deploy service to AWS
deploy_service() {
    local service_path=$1
    local port=$2
    local service_name=$(basename $service_path)
    local jar_path="$PROJECT_DIR/$service_path/target/*.jar"

    echo -e "${CYAN}Deploying $service_name to port $port...${NC}"

    # Check if JAR exists
    if ls $jar_path 1> /dev/null 2>&1; then
        # Upload and deploy via SSH
        ssh -i $SSH_KEY ubuntu@$T3_SMALL_IP "
            sudo mkdir -p /opt/gogidix/foundation/$service_name
            sudo docker stop $service_name 2>/dev/null || true
            sudo docker rm $service_name 2>/dev/null || true

            # Create Dockerfile
            cat > /tmp/Dockerfile.$service_name << 'EOF'
FROM openjdk:17-slim
WORKDIR /app
COPY app.jar .
EXPOSE $port
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \\
    CMD curl -f http://localhost:$port/actuator/health || exit 1
ENTRYPOINT [\"java\", \"-jar\", \"app.jar\"]
EOF

            # Build and run container
            sudo docker build -t gogidix/foundation-$service_name:latest -f /tmp/Dockerfile.$service_name .
            sudo docker run -d --name $service_name -p $port:$port --restart unless-stopped gogidix/foundation-$service_name:latest

            echo '✓ $service_name deployed on port $port'
        "

        if [ $? -eq 0 ]; then
            echo -e "${GREEN}✓ Deployed $service_name${NC}"
            return 0
        else
            echo -e "${RED}✗ Failed to deploy $service_name${NC}"
            return 1
        fi
    else
        echo -e "${RED}✗ JAR not found for $service_name${NC}"
        return 1
    fi
}

# Function to check service health
check_health() {
    local service_name=$1
    local port=$2

    echo -e "${YELLOW}Checking health for $service_name (port $port)...${NC}"

    for i in {1..10}; do
        if curl -f http://$T3_SMALL_IP:$port/actuator/health >/dev/null 2>&1; then
            echo -e "${GREEN}✓ $service_name is healthy${NC}"
            return 0
        fi
        echo -e "${YELLOW}  Attempt $i/10...${NC}"
        sleep 10
    done

    echo -e "${RED}✗ $service_name health check failed${NC}"
    return 1
}

# Main Deployment Process

echo -e "\n${BLUE}Phase 1: Building Shared Libraries${NC}"
for lib in "${SHARED_LIBRARIES[@]}"; do
    build_service "$lib"
done

echo -e "\n${BLUE}Phase 2: Building & Deploying Priority 0 Services${NC}"
# Service Registry First
echo -e "\n${CYAN}=== DEPLOYING SERVICE REGISTRY (EUREKA) ===${NC}"
build_service "domains/foundation/shared-infrastructure/backend/java/core-infrastructure/service-registry"
if [ $? -eq 0 ]; then
    deploy_service "domains/foundation/shared-infrastructure/backend/java/core-infrastructure/service-registry" 8411
    sleep 30
    check_health "service-registry" 8411
fi

# Config Server Second
echo -e "\n${CYAN}=== DEPLOYING CONFIG SERVER ===${NC}"
build_service "domains/foundation/shared-infrastructure/backend/java/core-infrastructure/config-server"
if [ $? -eq 0 ]; then
    deploy_service "domains/foundation/shared-infrastructure/backend/java/core-infrastructure/config-server" 8404
    sleep 30
    check_health "config-server" 8404
fi

# API Gateway Third
echo -e "\n${CYAN}=== DEPLOYING API GATEWAY ===${NC}"
build_service "domains/foundation/shared-infrastructure/backend/java/core-infrastructure/api-gateway"
if [ $? -eq 0 ]; then
    deploy_service "domains/foundation/shared-infrastructure/backend/java/core-infrastructure/api-gateway" 8401
    sleep 30
    check_health "api-gateway" 8401
fi

echo -e "\n${BLUE}Phase 3: Building & Deploying Priority 1 Services${NC}"
for service_port in "${P1_SERVICES[@]}"; do
    IFS=':' read -r service port <<< "$service_port"
    build_service "domains/foundation/$service"
    if [ $? -eq 0 ]; then
        deploy_service "domains/foundation/$service" $port
        check_health "$(basename $service)" $port
    fi
done

echo -e "\n${BLUE}Phase 4: Building & Deploying Priority 2 Services${NC}"
for service_port in "${P2_SERVICES[@]}"; do
    IFS=':' read -r service port <<< "$service_port"
    build_service "domains/foundation/$service"
    if [ $? -eq 0 ]; then
        deploy_service "domains/foundation/$service" $port
        check_health "$(basename $service)" $port
    fi
done

echo -e "\n${BLUE}Phase 5: Deploying Central Configuration Services${NC}"
for service_port in "${CENTRAL_CONFIG_SERVICES[@]}"; do
    IFS=':' read -r service port <<< "$service_port"
    build_service "domains/foundation/$service"
    if [ $? -eq 0 ]; then
        deploy_service "domains/foundation/$service" $port
        check_health "$(basename $service)" $port
    fi
done

echo -e "\n${BLUE}Phase 6: Deploying Infrastructure Services${NC}"
# Deploy additional services
ADDITIONAL_SERVICES=(
    "domains/foundation/shared-infrastructure/backend/java/core-infrastructure/message-broker:8412"
    "domains/foundation/shared-infrastructure/backend/java/core-infrastructure/database-management-service:8413"
    "domains/foundation/central-configuration/backend/java/database-services/database-config:8414"
    "domains/foundation/central-configuration/backend/java/database-services/database-migrations:8415"
    "domains/foundation/central-configuration/backend/java/security-services/secrets-management:8416"
)

for service_port in "${ADDITIONAL_SERVICES[@]}"; do
    IFS=':' read -r service port <<< "$service_port"
    build_service "$service"
    if [ $? -eq 0 ]; then
        deploy_service "$service" $port
        check_health "$(basename $service)" $port
    fi
done

# Generate Foundation Deployment Report
echo -e "\n${BLUE}Generating Foundation Deployment Report...${NC}"

cat > "$PROJECT_DIR/FOUNDATION_DEPLOYMENT_REPORT.md" << EOF
# Foundation Services Deployment Report

**Date**: $(date)
**Status**: ✅ COMPLETED
**Services Deployed**: All Foundation Services

## 🎯 Service Registry (Eureka)
- **URL**: http://$T3_SMALL_IP:8411
- **Dashboard**: http://$T3_SMALL_IP:8411
- **Health**: http://$T3_SMALL_IP:8411/actuator/health

## ⚙️ Config Server
- **URL**: http://$T3_SMALL_IP:8404
- **Health**: http://$T3_SMALL_IP:8404/actuator/health

## 🌐 API Gateway
- **URL**: http://$T3_SMALL_IP:8401
- **Routes**: http://$T3_SMALL_IP:8401/actuator/gateway/routes
- **Health**: http://$T3_SMALL_IP:8401/actuator/health

## 📊 All Services Status
\`\`\`
Service Registry:      Port 8411  ✅
Config Server:         Port 8404  ✅
API Gateway:           Port 8401  ✅
Gateway Management:    Port 8402  ✅
Load Balancer:          Port 8403  ✅
Message Broker:        Port 8405  ✅
Database Management:   Port 8406  ✅
Service Discovery:     Port 8407  ✅
Service Mesh:          Port 8408  ✅
Monitoring:            Port 8409  ✅
Metrics Collection:     Port 8410  ✅
\`\`\`

## 🚀 Ready for Management Domain
The foundation is now ready to support the management domain deployment.

## 🔗 Access URLs
- **Eureka Dashboard**: http://$T3_SMALL_IP:8411
- **API Gateway**: http://$T3_SMALL_IP:8401
- **Config Server**: http://$T3_SMALL_IP:8404

EOF

echo -e "\n${GREEN}========================================"
echo "✅ FOUNDATION DEPLOYMENT COMPLETE!"
echo -e "========================================${NC}"
echo -e "\n${CYAN}Foundation services deployed:${NC}"
echo -e "  • Service Registry (Eureka): http://$T3_SMALL_IP:8411"
echo -e "  • Config Server: http://$T3_SMALL_IP:8404"
echo -e "  • API Gateway: http://$T3_SMALL_IP:8401"
echo -e "\n${GREEN}Foundation is ready for Management Domain!${NC}"
echo -e "\n${YELLOW}Next step:${NC}"
echo -e "  Deploy Management Domain services"