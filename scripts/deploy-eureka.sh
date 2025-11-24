#!/bin/bash

# Deploy Eureka Service Registry - The Foundation Backbone

echo "=========================================="
echo "   DEPLOYING EUREKA SERVICE REGISTRY"
echo "   Foundation for All Services"
echo "=========================================="

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
T3_SMALL_IP="54.234.49.55"
SSH_KEY="$HOME/.ssh/gogidix-enterprise-key-new"

# Build Eureka Service
echo "Building Eureka Service Registry..."
cd "$PROJECT_DIR/domains/foundation/shared-infrastructure/backend/java/core-infrastructure/service-registry"

mvn clean package -DskipTests
if [ $? -eq 0 ]; then
    echo "✓ Eureka built successfully"
else
    echo "✗ Failed to build Eureka"
    exit 1
fi

# Deploy to AWS
echo "Deploying Eureka to AWS..."

# Check SSH connection
if ssh -i $SSH_KEY -o ConnectTimeout=5 -o StrictHostKeyChecking=no ubuntu@$T3_SMALL_IP "echo 'SSH OK'" 2>/dev/null; then
    echo "✓ SSH connection available"

    # Deploy via SSH
    ssh -i $SSH_KEY ubuntu@$T3_SMALL_IP "
        sudo docker stop service-registry 2>/dev/null || true
        sudo docker rm service-registry 2>/dev/null || true
        sudo mkdir -p /opt/gogidix/foundation
    "

    # Upload JAR
    scp -i $SSH_KEY target/*.jar ubuntu@$T3_SMALL_IP:/opt/gogidix/foundation/

    # Deploy container
    ssh -i $SSH_KEY ubuntu@$T3_SMALL_IP "
        cd /opt/gogidix/foundation

        cat > Dockerfile << 'EOF'
FROM openjdk:17-slim
WORKDIR /app
COPY *.jar app.jar
EXPOSE 8761
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \\
    CMD curl -f http://localhost:8761/actuator/health || exit 1
ENTRYPOINT [\"java\", \"-jar\", \"app.jar\"]
EOF

        sudo docker build -t gogidix/service-registry:latest .
        sudo docker run -d --name service-registry -p 8761:8761 --restart unless-stopped gogidix/service-registry:latest

        echo '✓ Eureka container started'
    "

    echo "✓ Eureka deployed successfully"

    # Wait and check health
    echo "Waiting for Eureka to start..."
    sleep 30

    echo "Checking Eureka health..."
    if curl -f http://$T3_SMALL_IP:8761/actuator/health >/dev/null 2>&1; then
        echo "✓ Eureka is healthy!"
        echo ""
        echo "=========================================="
        echo "✅ EUREKA DEPLOYMENT COMPLETE!"
        echo "=========================================="
        echo ""
        echo "Eureka Dashboard: http://$T3_SMALL_IP:8761"
        echo "Health Check: http://$T3_SMALL_IP:8761/actuator/health"
        echo ""
        echo "Foundation is ready for other services!"

        # Open dashboard
        powershell.exe -Command "Start-Process 'http://$T3_SMALL_IP:8761'"
    else
        echo "✗ Eureka health check failed"
    fi
else
    echo "✗ SSH not available"
    echo "Please run DEPLOY_NOW.bat to update AWS security group first"
fi