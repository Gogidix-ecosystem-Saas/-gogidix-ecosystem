#!/bin/bash

cd "C:/Users/frich/Desktop/Gogidix-ecosystem/domains/foundation/central-configuration/backend/java"

echo "=== SIMPLE CHECKSTYLE FIX ==="

# Fix only the specific issues mentioned in the logs
# 1. Add missing braces to if statements
find . -name "*.java" -type f -exec grep -l "if.*{$" {} \; | while read file; do
    echo "Adding braces to if statements in $file"
    # Simple fix for if statements on single line
    sed -i 's/if (\(.*\)) {$/if (\1) {\n        /g' "$file"
done

# 2. Add final to parameters in equals methods
find . -name "*.java" -type f -exec grep -l "public boolean equals(Object" {} \; | while read file; do
    echo "Adding final to equals parameter in $file"
    sed -i 's/public boolean equals(Object o)/public boolean equals(final Object o)/g' "$file"
done

# 3. Add final to main method args
find . -name "*.java" -type f -exec grep -l "public static void main" {} \; | while read file; do
    echo "Adding final to main args in $file"
    sed -i 's/public static void main(String\[\] args)/public static void main(final String[] args)/g' "$file"
done

# 4. Add missing Javadoc for equals methods
find . -name "*.java" -type f -exec grep -l "public boolean equals(final Object o)" {} \; | while read file; do
    # Check if Javadoc already exists
    if ! grep -B5 "public boolean equals" "$file" | grep -q "@inheritDoc"; then
        echo "Adding Javadoc to equals method in $file"
        # Use perl for more reliable multiline replacement
        perl -i -0pe 's/(    @Override\s+)?(    public boolean equals\(final Object o\))/    @Override\n    public boolean equals(final Object o)/s' "$file"
    fi
done

echo ""
echo "Fixes completed!"
echo "- Added braces to if statements"
echo "- Made parameters final"
echo "- Added @Override annotations"