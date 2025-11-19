#!/bin/bash

# Convert CRLF to LF and ensure files end with newline
cd "C:/Users/frich/Desktop/Gogidix-ecosystem/domains/foundation/central-configuration"

find . -name "*.properties" -type f | while read file; do
    # Convert CRLF to LF
    dos2unix "$file" 2>/dev/null || true

    # Check if file ends with newline, if not add one
    if [ -n "$(tail -c 1 "$file")" ]; then
        echo >> "$file"
        echo "Fixed: $file"
    else
        echo "OK: $file"
    fi
done