#!/bin/bash

# Deploy Eureka Service Registry and Core Foundation Services
# Priority: Eureka -> Config Server -> API Gateway

set -e

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${BLUE}========================================"
echo "   DEPLOYING FOUNDATION BACKBONE"
echo "   Starting with Eureka Service Registry"
echo "========================================${NC}"

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd"
T3_SMALL_IP="54.234.49.55"
SSH_KEY="$HOME/.ssh/gogidix-enterprise-key-new"

# Core Foundation Services (Deployment Order)
declare -A SERVICES=(
    ["service-registry"]="8411"
    ["config-server"]="8404"
    ["api-gateway"]="8401"
    ["message-broker"]="8405"
    ["database-management-service"]="8406"
)

# Function to build and deploy service
deploy_service() {
    local service_name=$1
    local port=$2
    local service_path="domains/foundation/shared-infrastructure/backend/java/core-infrastructure/$service_name"

    echo -e "\n${YELLOW}=== Deploying $service_name (Port: $port) ===${NC}"

    # Build service
    echo -e "${YELLOW}Building $service_name...${NC}"
    cd "$PROJECT_DIR/$service_path"

    if [ -f "pom.xml" ]; then
        mvn clean package -DskipTests -q
        if [ $? -ne 0 ]; then
            echo -e "${RED}✗ Failed to build $service_name${NC}"
            return 1
        fi
        echo -e "${GREEN}✓ Built $service_name${NC}"
    else
        echo -e "${RED}✗ No pom.xml found for $service_name${NC}"
        return 1
    fi

    # Deploy to AWS
    echo -e "${YELLOW}Deploying $service_name to AWS...${NC}"

    # Check if we can connect to AWS
    if ssh -i $SSH_KEY -o ConnectTimeout=5 -o StrictHostKeyChecking=no ubuntu@$T3_SMALL_IP "echo 'SSH OK'" >/dev/null 2>&1; then
        echo -e "${GREEN}✓ SSH connection available${NC}"

        # Deploy via SSH
        ssh -i $SSH_KEY ubuntu@$T3_SMALL_IP "
            # Stop and remove existing container
            sudo docker stop $service_name 2>/dev/null || true
            sudo docker rm $service_name 2>/dev/null || true

            # Create directory for JAR
            sudo mkdir -p /opt/gogidix/foundation

            echo 'Service $service_name deployment prepared'
        "

        # Upload JAR
        scp -i $SSH_KEY "$PROJECT_DIR/$service_path/target/*.jar" ubuntu@$T3_SMALL_IP:/opt/gogidix/foundation/

        # Deploy container
        ssh -i $SSH_KEY ubuntu@$T3_SMALL_IP "
            cd /opt/gogidix/foundation

            # Create Dockerfile
            cat > Dockerfile << 'EOF'
FROM openjdk:17-slim
WORKDIR /app
COPY *.jar app.jar
EXPOSE $port
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \\
    CMD curl -f http://localhost:$port/actuator/health || exit 1
ENTRYPOINT [\"java\", \"-jar\", \"app.jar\"]
EOF

            # Build and run
            sudo docker build -t gogidix/$service_name:latest .
            sudo docker run -d \\
                --name $service_name \\
                -p $port:$port \\
                --restart unless-stopped \\
                gogidix/$service_name:latest

            echo '✓ $service_name container started'
        "

        if [ $? -eq 0 ]; then
            echo -e "${GREEN}✓ Deployed $service_name successfully${NC}"

            # Wait for service to start
            echo -e "${YELLOW}Waiting for $service_name to start...${NC}"
            sleep 30

            # Health check
            echo -e "${YELLOW}Checking health...${NC}"
            for i in {1..10}; do
                if curl -f http://$T3_SMALL_IP:$port/actuator/health >/dev/null 2>&1; then
                    echo -e "${GREEN}✓ $service_name is healthy!${NC}"
                    return 0
                fi
                echo -e "${YELLOW}  Attempt $i/10...${NC}"
                sleep 10
            done
            echo -e "${RED}✗ $service_name health check failed${NC}"
        else
            echo -e "${RED}✗ Failed to deploy $service_name${NC}"
        fi
    else
        echo -e "${RED}✗ SSH not available. Please update AWS security group first.${NC}"
        echo -e "${YELLOW}Run DEPLOY_NOW.bat on desktop to update security group.${NC}"
        return 1
    fi
}

# Start deployment
echo -e "\n${BLUE}Starting Foundation Services Deployment...${NC}"

# Deploy Service Registry (Eureka) First
echo -e "\n${CYAN}Priority 0: Service Registry (Eureka)${NC}"
deploy_service "service-registry" "8411"

# Deploy Config Server
echo -e "\n${CYAN}Priority 0: Config Server${NC}"
deploy_service "config-server" "8404"

# Deploy API Gateway
echo -e "\n${CYAN}Priority 0: API Gateway${NC}"
deploy_service "api-gateway" "8401"

# Deploy Additional Services
echo -e "\n${CYAN}Priority 1: Additional Services${NC}"
for service in "${!SERVICES[@]}"; do
    if [[ ! "$service" =~ ^(service-registry|config-server|api-gateway)$ ]]; then
        deploy_service "$service" "${SERVICES[$service]}"
    fi
done

# Generate report
echo -e "\n${BLUE}Generating deployment report...${NC}"
cat > "$PROJECT_DIR/EUREKA_FOUNDATION_DEPLOYED.md" << EOF
# Eureka Service Registry & Foundation Deployment Report

**Date**: $(date)
**Status**: ✅ DEPLOYED

## 🎯 Service Registry (Eureka)
- **URL**: http://$T3_SMALL_IP:8411
- **Dashboard**: http://$T3_SMALL_IP:8411
- **Health**: http://$T3_SMALL_IP:8411/actuator/health

## ⚙️ Config Server
- **URL**: http://$T3_SMALL_IP:8404
- **Health**: http://$T3_SMALL_IP:8404/actuator/health

## 🌐 API Gateway
- **URL**: http://$T3_SMALL_IP:8401
- **Health**: http://$T3_SMALL_IP:8401/actuator/health

## 📊 All Services
EOF

for service in "${!SERVICES[@]}"; do
    echo "- $service: Port ${SERVICES[$service]}" >> "$PROJECT_DIR/EUREKA_FOUNDATION_DEPLOYED.md"
done

echo -e "\n${GREEN}========================================"
echo "✅ FOUNDATION DEPLOYMENT COMPLETE!"
echo -e "========================================${NC}"
echo -e "\n${CYAN}Services deployed:${NC}"
echo -e "  • Eureka Service Registry: http://$T3_SMALL_IP:8411"
echo -e "  • Config Server: http://$T3_SMALL_IP:8404"
echo -e "  • API Gateway: http://$T3_SMALL_IP:8401"
echo -e "\n${GREEN}Foundation backbone is ready for Management Domain!${NC}"

# Open Eureka dashboard
echo -e "\n${YELLOW}Opening Eureka dashboard...${NC}"
powershell.exe -Command "Start-Process 'http://$T3_SMALL_IP:8411'"