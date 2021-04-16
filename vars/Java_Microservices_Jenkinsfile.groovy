def String TAG = new Date().format("yyyyMMddHHmmss")
def boolean CONTINUE = true

def call() {

   pipeline {
      agent any
   
      tools {
         maven "M3"
      }
   
      stages {
         stage("Initialize") {
            steps { script {
               print_vars()
            }}
         }
         stage("Build") {
            when { expression{ CONTINUE } }
            steps { script {
      
               // Get some code from a GitHub repository
               git 'https://github.com/jglick/simple-maven-project-with-tests.git'
      
               // Run Maven on a Unix agent.
               sh "mvn -Dmaven.test.failure.ignore=true clean package"

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

def print_vars() {
    echo "TAG = ${TAG}"
    echo "CONTINUE = ${CONTINUE}"
    echo "JOB_VARIABLE = " + ((env.JOB_VARIABLE != null) ? "${env.JOB_VARIABLE}" : "null")
    echo "PARAMETER_VARIABLE = " + ((params.PARAMETER_VARIABLE != null) ? "${params.PARAMETER_VARIABLE}" : "null")
    withFolderProperties {
       echo "FOLDER_VARIABLE = " + ((env.FOLDER_VARIABLE != null) ? "${env.FOLDER_VARIABLE}" : "null")
    }
    echo "GLOBAL_VARIABLE = " + ((env.GLOBAL_VARIABLE != null) ? "${env.GLOBAL_VARIABLE}" : "null")
}
