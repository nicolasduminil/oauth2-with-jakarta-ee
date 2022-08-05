echo ">>>>>>>>>>>>>>>>>>>>>>"
JAVA_HOME=$(dirname $(dirname $(readlink -f /etc/alternatives/java)))
echo ">>>>>>>>>>>>>>>> JAVA_HOME: $JAVA_HOME"
sudo keytool -delete -alias localhost -keystore $JAVA_HOME/lib/security/cacerts
sudo keytool -import -trustcacerts -alias localhost -file ./authorization-service-war/src/test/resources/localhost.crt -keystore $JAVA_HOME/lib/security/cacerts