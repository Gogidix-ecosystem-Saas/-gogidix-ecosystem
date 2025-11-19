#!/bin/bash

# Fix all empty properties files by adding a newline
cd "C:/Users/frich/Desktop/Gogidix-ecosystem/domains/foundation/central-configuration"

find . -name "*.properties" -type f -size 0 | while read file; do
    echo "" > "$file"
    echo "Fixed empty file: $file"
done