pipeline {
    agent any

    stages {
        stage('Cloning Git') {
             steps {
        git url: 'https://github.com/Linda-Ju/eShop.git',
            credentialsId: 'new-pat'
      }
    }
        stage('Build') {
            steps {
                sh './gradlew build'
            }
        }
        stage('Test') {
            steps {
                sh './gradlew test'
            }
        }

    }
}
