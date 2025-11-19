#!/bin/bash

echo "Adding health check configurations to all services..."

cd domains/foundation/central-configuration/backend/java

# Find all services with pom.xml
find . -name "pom.xml" -exec dirname {} \; | while read service; do
    echo "Processing service: $service"

    # Skip if service doesn't have src/main/java
    if [ ! -d "$service/src/main/java" ]; then
        echo "Skipping $service (no src/main/java directory)"
        continue
    fi

    # Create main resources directory if it doesn't exist
    mkdir -p "$service/src/main/resources"

    # Create application.yml
    cat > "$service/src/main/resources/application.yml" << 'EOF'
spring:
  application:
    name: @project.artifactId@
  profiles:
    active: dev

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always
  health:
    defaults:
      enabled: true

logging:
  level:
    com.gogidix: INFO
    org.springframework: WARN
    org.hibernate: WARN
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"

server:
  port: 8080
EOF

    echo "Created application.yml for $service"
done

echo "All health configurations have been added!"