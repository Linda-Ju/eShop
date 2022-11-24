pipeline {
    agent any
    tools{
        gradle 'Gradle'
    }

    stages {
        stage('Cloning Git') {
          steps {
            git url: 'https://github.com/Linda-Ju/eShop.git',
                credentialsId: 'git-token'
          }
        stage('Build') {
            steps {
                echo 'executing gradle...'
                withGradle(){
                sh './gradlew -v'
                }
            }
        }
        stage('Test') {
            steps {
                sh './gradlew test'
            }
        }

    }
}