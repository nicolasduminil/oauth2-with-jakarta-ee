export JAVA_HOME=$(dirname $(dirname $(readlink -f /etc/alternatives/java)))
skip=true
while getopts "f:" option
do
  case $option in
    f) skip=$OPTARG
       if mvn -DskipTests -Dfirst.exec=$skip clean install
       then
         while ! docker ps -q -f name=payara5 > /dev/null 2>&1
         do
           echo ">>> Waiting for the Payara Server 5 to start"
           sleep 3
         done
         while ! $(nc -z localhost 4848)
         do
           sleep 3
         done
         echo ">>> The Payara Server 5 has started"
         sleep 5
         mvn -pl authorization-service-war verify
       fi
       exit;;
    \?) echo ">>> Error: Invalid option"
        exit;;
  esac
done
