rm -rf $HUDSON_HOME/plugins/analysis-collector*

mvn clean install
cp -f target/*.hpi $HUDSON_HOME/plugins/

