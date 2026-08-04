pipeline {
    agent { label 'java' }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B clean package'
            }
        }
        stage('Deploy') {
            steps {

                sh "cp ~/workspace/GD-Forth\\ unstable/target/FORTH-0.0.1-jar-with-dependencies.jar /import/sol/work/Jenkins-Builds/Java/GD-Forth/${env.BRANCH_NAME}/GD-Forth.jar"
            }
        }
    }
}