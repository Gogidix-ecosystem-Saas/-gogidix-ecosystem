#!/bin/bash

cd "C:/Users/frich/Desktop/Gogidix-ecosystem/domains/foundation/central-configuration/backend/java"

echo "Fixing checkstyle violations across all Java files..."

# Find all Java files and fix common violations
find . -name "*.java" -type f | while read file; do
    echo "Processing: $file"

    # 1. Fix missing braces for if statements
    sed -i 's/if (\([^)]*\)) return true;/if (\1) {\n        return true;\n    }/g' "$file"
    sed -i 's/if (\([^)]*\)) return false;/if (\1) {\n        return false;\n    }/g' "$file"

    # 2. Fix operator wrapping - move && to new line
    sed -i 's/ && \([^(]*\)/ \&\&\n            \1/g' "$file"

    # 3. Ensure file ends with newline
    if [ -n "$(tail -c 1 "$file")" ]; then
        echo >> "$file"
    fi

    # 4. Add final to parameters in equals method (first line only)
    sed -i 's/public boolean equals(Object o)/public boolean equals(final Object o)/g' "$file"

    echo "Fixed: $file"
done

echo "All checkstyle violations fixed!"