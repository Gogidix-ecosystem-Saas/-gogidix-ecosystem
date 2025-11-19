#!/bin/bash

cd "C:/Users/frich/Desktop/Gogidix-ecosystem/domains/foundation/central-configuration/backend/java"

# Find all package directories that need package-info.java
find . -type d -path "*/src/main/java/*" | while read dir; do
    # Check if this directory contains Java files but no package-info.java
    if [ -f "$(find "$dir" -maxdepth 1 -name "*.java" | head -1)" ] && [ ! -f "$dir/package-info.java" ]; then
        # Convert directory path to package name
        pkg=$(echo "$dir" | sed 's|.*/src/main/java/||' | sed 's|/|.|g')

        echo "Creating package-info.java for: $pkg"

        cat > "$dir/package-info.java" << EOF
/**
 * $pkg package.
 *
 * <p>This package contains components for the $pkg module
 * within the Gogidix ecosystem.</p>
 *
 * @since 1.0.0
 */
package $pkg;
EOF
    fi
done

echo "All package-info.java files created!"