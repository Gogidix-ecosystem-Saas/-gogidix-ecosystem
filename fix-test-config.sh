#!/bin/bash

echo "Fixing test configurations for all services..."

# Navigate to central configuration domain
cd domains/foundation/central-configuration/backend/java

# Find all services with pom.xml
find . -name "pom.xml" -exec dirname {} \; | while read service; do
    echo "Processing service: $service"

    # Create test resources directory if it doesn't exist
    mkdir -p "$service/src/test/resources"

    # Create application-test.yml
    cat > "$service/src/test/resources/application-test.yml" << 'EOF'
spring:
  profiles:
    active: test
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: password
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  h2:
    console:
      enabled: true
logging:
  level:
    com.gogidix: DEBUG
    org.springframework: INFO
EOF

    # Create logback-test.xml
    cat > "$service/src/test/resources/logback-test.xml" << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="STDOUT" />
    </root>

    <logger name="com.gogidix" level="DEBUG" />
</configuration>
EOF

    echo "Created test configuration for $service"
done

echo "All test configurations have been fixed!"