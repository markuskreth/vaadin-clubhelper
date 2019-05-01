def branch = 'master'
def scmUrl = 'git@github.com:markuskreth/vaadin-clubhelper.git'
def server = Artifactory.server 'krethartifactory'
def devServerPort = '8080'
   
node {
    stage('checkout git') {
      git branch: branch, credentialsId: 'f45bc46c-b473-4912-97e5-4b6ab5ad4165', url: scmUrl
    }
 
    stage('build') {
      sh 'mvn clean package -DskipTests'
    }
 
    stage('test') {
      sh 'mvn test'
    }
 
    stage('install') {
      sh 'mvn install '
    }
 
    stage('sonar') {
      sh 'mvn sonar:sonar '
    }
 
}