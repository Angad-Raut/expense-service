pipeline {
    environment {
       registry = "9766945760/expense-details"
       registryCredential = 'dockerhub-credentials'
       dockerImage = ''
    }
    agent any
    tools {
        jdk 'Jdk17'
        maven 'maven-3.8.6'
    }
    stages {
        stage('Git Checkout') {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'github-secret', url: 'https://github.com/Angad-Raut/expense-service.git']])
                bat 'mvn clean install'
                echo 'Git Checkout Completed'
            }
        }
        stage('Code Compile') {
            steps {
                bat 'mvn clean compile'
            }
        }
        stage('Unit Tests') {
            steps {
                bat 'mvn test'
            }
        }
        stage('Build Artifact') {
            steps {
                bat 'mvn clean package'
            }
        }
        stage('Docker Build') {
            steps{
                script {
                    dockerImage = docker.build registry
                    echo 'Build Image Completed'
                }
            }
        }
        stage('Docker Push') {
            steps {
                script {
                    docker.withRegistry( '', registryCredential ) {
                       dockerImage.push('latest')
                       echo 'Push Image Completed'
                    }
                }
            }
        }
        stage('Deployment') {
             steps {
                  bat 'docker-compose up --build -d'
                  echo 'SUCCESS'
                  bat 'docker logout'
                  bat 'docker rmi 9766945760/expense-details:latest'
             }
        }
    }
}
