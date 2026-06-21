#!/bin/bash
TOMCAT_SOURCE="/home/andrianandrainy/Documents/S4/Mr Aina & Mr Rindra/tomcat2"
# creer le dossier out 
rm -rf out 
mkdir -p out
# compiler les classes java
javac -cp "lib/*" -d out $(find . -name "*.java")

#ne pas  déployer les classes NON empaquetées  dans classe app-test
#cp -r out/* "../app-test/WEB-INF/classes/"

# transformer en jar dossier out  
jar cf spring1.jar -C out .
# transferer vers apptest 
rm -rf ../app-test/WEB-INF/lib/spring1.jar
cp spring1.jar ../app-test/WEB-INF/lib/

# Deployer vers tomcat # stop tomcat
"$TOMCAT_SOURCE/bin/shutdown.sh"

# supprime ancien déploiement
rm -rf $TOMCAT_SOURCE/webapps/app-test



# redémarre
"$TOMCAT_SOURCE/bin/startup.sh"

cp -r ../app-test "$TOMCAT_SOURCE/webapps/"


echo "http://localhost:8080/app-test/"