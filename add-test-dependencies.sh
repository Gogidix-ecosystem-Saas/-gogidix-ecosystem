#!/bin/bash

echo "Adding test dependencies to all services..."

cd domains/foundation/central-configuration/backend/java

# Find all services with pom.xml
find . -name "pom.xml" -exec dirname {} \; | while read service; do
    echo "Processing service: $service"

    # Skip if service doesn't have src/main/java
    if [ ! -d "$service/src/main/java" ]; then
        echo "Skipping $service (no src/main/java directory)"
        continue
    fi

    # Read the pom.xml file
    pom_file="$service/pom.xml"

    # Check if dependencies section exists, if not create it
    if ! grep -q "<dependencies>" "$pom_file"; then
        # Insert dependencies section before build section
        sed -i '/<build>/i\    <dependencies>\n    </dependencies>\n' "$pom_file"
    fi

    # Add spring-boot-starter-actuator if not present
    if ! grep -q "spring-boot-starter-actuator" "$pom_file"; then
        sed -i '/<dependencies>/a\        <dependency>\n            <groupId>org.springframework.boot</groupId>\n            <artifactId>spring-boot-starter-actuator</artifactId>\n        </dependency>' "$pom_file"
    fi

    # Add spring-boot-starter-web if not present
    if ! grep -q "spring-boot-starter-web" "$pom_file"; then
        sed -i '/<dependencies>/a\        <dependency>\n            <groupId>org.springframework.boot</groupId>\n            <artifactId>spring-boot-starter-web</artifactId>\n        </dependency>' "$pom_file"
    fi

    # Add spring-boot-starter-test if not present
    if ! grep -q "spring-boot-starter-test" "$pom_file"; then
        sed -i '/<dependencies>/a\        <dependency>\n            <groupId>org.springframework.boot</groupId>\n            <artifactId>spring-boot-starter-test</artifactId>\n            <scope>test</scope>\n        </dependency>' "$pom_file"
    fi

    # Add H2 database for testing if not present
    if ! grep -q "<artifactId>h2</artifactId>" "$pom_file"; then
        sed -i '/<dependencies>/a\        <dependency>\n            <groupId>com.h2database</groupId>\n            <artifactId>h2</artifactId>\n            <scope>test</scope>\n        </dependency>' "$pom_file"
    fi

    echo "Updated dependencies for $service"
done

echo "All dependencies have been added!"