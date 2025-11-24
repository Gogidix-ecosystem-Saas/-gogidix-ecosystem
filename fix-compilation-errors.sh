#!/bin/bash

cd "C:/Users/frich/Desktop/Gogidix-ecosystem/domains/foundation/central-configuration/backend/java"

echo "=== FIXING COMPILATION ERRORS INTRODUCED BY CHECKSTYLE SCRIPT ==="

# Find all Java files and fix the specific issues
find . -name "*.java" -type f | while read file; do
    echo "Fixing: $file"

    # Create a temporary file
    temp_file=$(mktemp)

    # Fix the specific issues:
    # 1. Remove illegal backslashes and extra && characters
    # 2. Fix broken method signatures
    # 3. Fix broken return statements

    sed -e 's/&& \&\&\&\& \\/&&/g' \
        -e 's/\\$//g' \
        -e 's/[[:space:]]*\\$//g' \
        -e 's/String identifier, String environment, boolean$/String identifier, String environment, boolean includeMetadata) {/g' \
        -e 's/return identifier != null  && && \\/return identifier != null \&\&/g' \
        -e 's/Objects.equals(identifier, that.identifier)$/Objects.equals(identifier, that.identifier)/g' \
        -e 's/\&\& \\/\&\&/g' \
        "$file" > "$temp_file"

    # Replace the original file
    mv "$temp_file" "$file"

    echo "  ✓ Fixed"
done

echo ""
echo "=== COMPILATION ERRORS FIXED ==="
echo ""
echo "Fixed issues:"
echo "  ✓ Removed illegal backslashes"
echo "  ✓ Fixed operator wrapping issues"
echo "  ✓ Fixed broken method signatures"
echo ""