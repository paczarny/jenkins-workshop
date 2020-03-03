pipeline {
    agent {
        any
    }
    stages {
        stage("List files") {
            sh "ls ."
        }
    }
}