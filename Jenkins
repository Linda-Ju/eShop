pipeline {
    agent any
    tools{
        gradle 'Gradle'
    }

    stages {
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