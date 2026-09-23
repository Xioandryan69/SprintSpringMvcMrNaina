#!/bin/bash

OUTPUT_FILE="source.txt"

# Réinitialiser le fichier
> "$OUTPUT_FILE"

echo "==================================================" >> "$OUTPUT_FILE"
echo "EXPORT COMPLET DU PROJET" >> "$OUTPUT_FILE"
echo "Date : $(date)" >> "$OUTPUT_FILE"
echo "Répertoire : $(pwd)" >> "$OUTPUT_FILE"
echo "==================================================" >> "$OUTPUT_FILE"
echo "" >> "$OUTPUT_FILE"

find . \
    \( -path "./spring0/out" -o -path "./target" -o -path "./.git" \) -prune \
    -o \
    \( \
        -name "*.java" -o \
        -name "*.xml" -o \
        -name "*.jsp" -o \
        -name "*.html" -o \
        -name "*.css" -o \
        -name "*.js" -o \
        -name "*.properties" -o \
        -name "*.txt" \
    \) \
    -type f | sort | while read file
do
    echo "" >> "$OUTPUT_FILE"
    echo "==================================================" >> "$OUTPUT_FILE"
    echo "FICHIER : $file" >> "$OUTPUT_FILE"
    echo "==================================================" >> "$OUTPUT_FILE"
    cat "$file" >> "$OUTPUT_FILE"
    echo "" >> "$OUTPUT_FILE"
done

echo "Export terminé dans $OUTPUT_FILE"