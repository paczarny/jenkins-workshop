pipeline {
    agent {
        docker {
            image "adoptopenjdk/maven-openjdk11"
            args '-v /root/.m2:/root/.m2'
        }
    }

    environment {
        PASS1 = credentials('PASSWORD')
        PASS2 = credentials('USERNAME')
        PASS3 = credentials('CLIENT_ID')
        PASS4 = credentials('CLIENT_SECRET')
    }

    parameters {
        choice(name: 'PROFILE', choices: ['local', 'dev', 'other'], description: '')
        string(name: 'PRODUCT_NAME', defaultValue: 'default value', description: '')
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
                dir('simple-backend/target') {
                    sh "java -jar app.jar --spring.profiles.active=$params.PROFILE --productName=$params.PRODUCT_NAME"
                }
            }
        }
        stage('list credentials') {
            steps {
                sh "echo $PASS1"
                sh "echo $PASS2"
                sh "echo $PASS3"
                sh "echo $PASS4"
            }
        }
    }
}