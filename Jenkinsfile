pipeline {
    agent any
    stages {
        stage('Preparing for build') {
            steps {
                script {
                    def isContainerExists = sh(script: "docker-compose ps -q -f name='rabbit_mq_bot'", returnStdout: true) != ""

                    echo "${isContainerExists}"

                    if (isContainerExists) {
                        sh 'docker-compose stop'
                    }

                }
            }
        }
        stage('Build Docker Image') {
        steps {
                sh 'docker-compose build'
            }
        }
        stage ('Start Application in Docker') {
            steps {
                sh 'docker-compose up -d'
            }
        }
    }
}