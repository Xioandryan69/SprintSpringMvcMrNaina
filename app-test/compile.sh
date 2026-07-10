#!/bin/bash
TOMCAT_SOURCE="/home/andrianandrainy/Documents/S4/Mr Aina & Mr Rindra/tomcat2"
# creer le dossier out 
# compiler les classes java
rm -rf WEB-INF/classes/*
mkdir -p WEB-INF/classes
javac -cp "lib/*:WEB-INF/lib/*" -d WEB-INF/classes $(find . -name "*.java")

# Deployer vers tomcat # stop tomcat
"$TOMCAT_SOURCE/bin/shutdown.sh"

# supprime ancien déploiement
cp -r ../app-test "$TOMCAT_SOURCE/webapps/"

"$TOMCAT_SOURCE/bin/startup.sh"

echo "http://localhost:8080/app-test/"