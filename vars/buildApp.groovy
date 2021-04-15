import org.shared-library-test.Globals

def call(Map config=[:], Closure body) {
   stage("Build") {
      steps {
         echo "TAG = ${TAG}"
         echo "CONTINUE = ${CONTINUE}"

         // Get some code from a GitHub repository
         git 'https://github.com/jglick/simple-maven-project-with-tests.git'

         // Run Maven on a Unix agent.
         sh "mvn -Dmaven.test.failure.ignore=true clean package"
      }

      body()
   }
}

