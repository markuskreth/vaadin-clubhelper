def branch = 'master'
def scmUrl = 'git@github.com:markuskreth/vaadin-clubhelper.git'
def server = Artifactory.server 'krethartifactory'
def rtMaven = Artifactory.newMavenBuild()
   
node {
    stage('checkout git') {
      git branch: branch, credentialsId: 'f45bc46c-b473-4912-97e5-4b6ab5ad4165', url: scmUrl
    }
 
    stage('build') {
    	sh 'mvn -Dmaven.test.failure.ignore=true install' 
    }
 
    stage('sonar') {
		junit 'target/surefire-reports/**/*.xml' 
		sh 'mvn sonar:sonar'
    }
    
}