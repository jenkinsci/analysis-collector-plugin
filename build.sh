rm -rf $JENKINS_HOME/plugins/analysis-collector*

mvn install
cp -f target/*.hpi $JENKINS_HOME/plugins/

