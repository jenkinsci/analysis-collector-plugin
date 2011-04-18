rm -rf $HUDSON_HOME/plugins/analysis-collector*

mvn clean install
cp -f target/*.hpi $HUDSON_HOME/plugins/

cd $HUDSON_HOME
java -jar jenkins.war
