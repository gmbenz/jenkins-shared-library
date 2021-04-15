def call(Map config=[:], Closure body) {

   String TAG = new Date().format("yyyyMMddHHmmss")
   boolean CONTINUE = true

   pipeline {
      agent any
   
      tools {
         // Install the Maven version configured as "M3" and add it to the path.
         maven "M3"
      }
   
      stages {
         stage("Build") {
            steps { script {
               echo "TAG = ${TAG}"
               echo "CONTINUE = ${CONTINUE}"
      
               // Get some code from a GitHub repository
               git 'https://github.com/jglick/simple-maven-project-with-tests.git'
      
               // Run Maven on a Unix agent.
               sh "mvn -Dmaven.test.failure.ignore=true clean package"

               body()
            }}
      
            post {
               // If Maven was able to run the tests, even if some of the test
               // failed, record the test results and archive the jar file.
               success {
                  junit '**/target/surefire-reports/TEST-*.xml'
                  archiveArtifacts 'target/*.jar'
               }
            }
         }
      }
   }

}
