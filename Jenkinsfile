def branch = 'master'
def scmUrl = 'git@github.com:markuskreth/vaadin-clubhelper.git'
def server = Artifactory.server 'krethartifactory'
def MAVEN_TOOL = "Apache Maven 3.5.4"

node {
    stage('checkout git') {
      git branch: branch, credentialsId: 'f45bc46c-b473-4912-97e5-4b6ab5ad4165', url: scmUrl
    }
 
    stage('build') {
		def buildInfo = Artifactory.newBuildInfo()
		buildInfo.env.capture = true
		def rtMaven = Artifactory.newMavenBuild()
		rtMaven.tool = MAVEN_TOOL // Tool name from Jenkins configuration
		rtMaven.opts = "-Denv=dev"
		rtMaven.deployer releaseRepo:'libs-release-local', snapshotRepo:'libs-snapshot-local', server: server
		rtMaven.resolver releaseRepo:'libs-release', snapshotRepo:'libs-snapshot', server: server
		
		rtMaven.run pom: 'pom.xml', goals: '-B release:clean release:prepare', buildInfo: buildInfo
		
		// buildInfo.retention maxBuilds: 10, maxDays: 7, deleteBuildArtifacts: true
		// Publish build info.
		server.publishBuildInfo buildInfo
    }
 
    stage('sonar') {
		junit 'target/surefire-reports/**/*.xml' 
		sh 'mvn sonar:sonar'
    }
    
}