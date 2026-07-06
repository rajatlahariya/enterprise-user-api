pipeline {
    agent any

    parameters {
        choice(name: 'AUTH_TYPE', choices: ['jwt', 'basic', 'oauth2'], description: 'Authentication mode')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Jar') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t enterprise-user-api:latest .'
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                docker rm -f enterprise-user-api || true

                docker run -d \
                  --name enterprise-user-api \
                  --network user-api_enterprise-user-api-network \
                  -p 8081:8081 \
                  -e DB_URL=jdbc:postgresql://enterprise-user-api-db:5432/automationdb \
                  -e DB_USERNAME=postgres \
                  -e DB_PASSWORD=postgres \
                  -e AUTH_TYPE=${AUTH_TYPE} \
                  enterprise-user-api:latest
                '''
            }
        }
    }
}