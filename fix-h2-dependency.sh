#!/bin/bash

echo "Fixing H2 database dependency version in all pom.xml files..."

cd domains/foundation/central-configuration/backend/java

# Find all services with pom.xml
find . -name "pom.xml" -exec dirname {} \; | while read service; do
    echo "Processing service: $service"

    # Fix H2 dependency - add version
    sed -i 's|<artifactId>h2</artifactId>|<artifactId>h2</artifactId>\n            <version>2.2.224</version>|g' "$service/pom.xml"

    echo "Fixed H2 dependency in $service"
done

echo "All H2 dependencies have been fixed!"