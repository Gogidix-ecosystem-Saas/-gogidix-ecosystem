#!/bin/bash

echo "Updating pom.xml files with necessary dependencies and plugins..."

cd domains/foundation/central-configuration/backend/java

# Find all services with pom.xml
find . -name "pom.xml" -exec dirname {} \; | while read service; do
    echo "Processing service: $service"

    # Skip if service doesn't have src/main/java
    if [ ! -d "$service/src/main/java" ]; then
        echo "Skipping $service (no src/main/java directory)"
        continue
    fi

    # Add missing dependencies and plugins to pom.xml
    xmlstarlet ed -L \
        -s "//_:project/_:dependencies" -t elem -n dependency \
        -v "<groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-actuator</artifactId>" \
        -s "//_:project/_:dependencies" -t elem -n dependency \
        -v "<groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-web</artifactId>" \
        -s "//_:project/_:dependencies" -t elem -n dependency \
        -v "<groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-test</artifactId><scope>test</scope>" \
        -s "//_:project/_:dependencies" -t elem -n dependency \
        -v "<groupId>com.h2database</groupId><artifactId>h2</artifactId><scope>test</scope>" \
        "$service/pom.xml" 2>/dev/null || true

    # Add or update maven-surefire-plugin configuration
    xmlstarlet ed -L \
        -s "//_:project/_:build/_:plugins" -t elem -n plugin \
        -v "<groupId>org.apache.maven.plugins</groupId><artifactId>maven-surefire-plugin</artifactId><version>3.1.2</version><configuration><reportsDirectory>\${project.build.directory}/test-reports</reportsDirectory></configuration>" \
        "$service/pom.xml" 2>/dev/null || true

    # Add or update maven-failsafe-plugin for integration tests
    xmlstarlet ed -L \
        -s "//_:project/_:build/_:plugins" -t elem -n plugin \
        -v "<groupId>org.apache.maven.plugins</groupId><artifactId>maven-failsafe-plugin</artifactId><version>3.1.2</version><executions><execution><goals><goal>integration-test</goal></goals></execution></executions>" \
        "$service/pom.xml" 2>/dev/null || true

    # Add JaCoCo plugin for code coverage
    xmlstarlet ed -L \
        -s "//_:project/_:build/_:plugins" -t elem -n plugin \
        -v "<groupId>org.jacoco</groupId><artifactId>jacoco-maven-plugin</artifactId><version>0.8.8</version><executions><execution><goals><goal>prepare-agent</goal></goals></execution><execution><id>report</id><phase>test</phase><goals><goal>report</goal></goals></execution></executions>" \
        "$service/pom.xml" 2>/dev/null || true

    echo "Updated pom.xml for $service"
done

echo "All pom.xml files have been updated!"