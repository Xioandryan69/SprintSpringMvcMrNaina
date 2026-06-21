#!/bin/bash
TOMCAT_SOURCE="/home/andrianandrainy/Documents/S4/Mr Aina & Mr Rindra/tomcat2"
# creer le dossier out 
rm -rf out 
mkdir -p out
# compiler les classes java
# transformer en jar dossier out  
jar cf spring1.jar -C out .
# transferer vers apptest 
cp spring1.jar ../app-test/WEB-INF/lib/

# Deployer vers tomcat 
cp -r ../app-test "$TOMCAT_SOURCE/webapps/"

"$TOMCAT_SOURCE/bin/startup.sh"

echo "http://localhost:8080/app-test/"