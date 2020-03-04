pipeline {
    agent {
        docker {
            image "adoptopenjdk/maven-openjdk11"
            args '-v /root/.m2:/root/.m2'
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
        stage("Run jar") {
            steps {
                sh "cd simple-backend/target && java jar app.jar"
            }
        }
    }
}