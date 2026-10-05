#!/bin/bash
set -euo pipefail

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
REPOSITORY_DIR="$(cd "$PROJECT_DIR/.." && pwd)"

JAR_DESTINATION="${JAR_DESTINATION:-$PROJECT_DIR/dist}"
WEB_INF_LIB="$REPOSITORY_DIR/app-test/WEB-INF/lib"

cd "$PROJECT_DIR"
mvn clean package

mkdir -p "$JAR_DESTINATION"
rm -rf target/lib
mvn dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=target/lib
jar cf "$JAR_DESTINATION/entreprise-springboot.jar" -C target/classes .
cp target/entreprise-springboot-0.0.1-SNAPSHOT.jar "$JAR_DESTINATION/entreprise-springboot-executable.jar"
mkdir -p "$WEB_INF_LIB"
cp "$JAR_DESTINATION/entreprise-springboot.jar" "$WEB_INF_LIB/entreprise-springboot.jar"
cp target/lib/*.jar "$WEB_INF_LIB/"
echo "Jar transporte vers : $JAR_DESTINATION/entreprise-springboot.jar"
echo "Jar executable vers : $JAR_DESTINATION/entreprise-springboot-executable.jar"
echo "Jar copie vers : $WEB_INF_LIB/entreprise-springboot.jar"