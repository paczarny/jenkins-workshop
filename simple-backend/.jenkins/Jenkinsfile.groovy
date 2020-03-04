pipeline {
    agent {
        docker {
            image "adoptopenjdk/maven-openjdk11"
            args '-v /root/.m2:/root/.m2'
        }
    }

    environment {
        PASS = credentials('PASSWORD')
        PASS_USERNAME = credentials('USERNAME')
        PASS_CLIENT_ID = credentials('CLIENT_ID')
        PASS_CLIENT_SECRET = credentials('CLIENT_SECRET')
    }

    parameters {
        choice(name: 'PROFILE', choices: ['local', 'dev', 'custom'], description: '')
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
        stage("Run jar with dev env") {
            when {
                expression {
                    params.PROFILE == 'custom'
                }
            }
            steps {
                dir('simple-backend/target') {
                    sh "java -jar app.jar --spring.profiles.active=$params.PROFILE --productName=$params.PRODUCT_NAME"
                }
            }
        }
        stage("Run jar with custom") {
            when {
                expression {
                    params.PROFILE != 'custom'
                }
            }
            steps {
                dir('simple-backend/target') {
                    sh """java -jar app.jar \
                        --spring.profiles.active=$params.PROFILE \
                        --productName=$params.PRODUCT_NAME \
                        --salesforce.username=$PASS_USERNAME \
                        --salesforce.password=$PASS \
                        --salesforce.clientId=$PASS_CLIENT_ID
                        --salesforce.clientSecret=$PASS_CLIENT_SECRET
                      """
                }
            }
        }
    }
}