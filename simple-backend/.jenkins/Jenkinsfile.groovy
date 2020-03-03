pipeline {
    agent {
        docker {
            image "adoptopenjdk/maven-openjdk11"
        }
    }
    stages {
        stage("List files") {
            steps {
                sh "ls ."
            }
        }
        stage("MVN install") {
            steps {
                sh "cd simple-backend && mvn clean install"
            }
        }
    }
}