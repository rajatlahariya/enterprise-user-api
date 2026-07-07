pipeline {
    agent any

    parameters {
        choice(name: 'AUTH_TYPE', choices: ['jwt', 'basic', 'oauth2'], description: 'Authentication mode')
    }

    environment {
        IMAGE_NAME = 'enterprise-user-api:latest'
        CONTAINER_NAME = 'enterprise-user-api'
        NETWORK_NAME = 'user-api_enterprise-user-api-network'
        DB_URL = 'jdbc:postgresql://enterprise-user-api-db:5432/automationdb'
        DB_USERNAME = 'postgres'
        DB_PASSWORD = 'postgres'
        GOOGLE_CLIENT_ID = credentials('google-client-id')
        GOOGLE_CLIENT_SECRET = credentials('google-client-secret')
    }

    stages {
        stage('Build Jar') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME .'
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                docker rm -f $CONTAINER_NAME || true

                docker run -d \
                  --name $CONTAINER_NAME \
                  --network $NETWORK_NAME \
                  -p 8081:8081 \
                  -e DB_URL=$DB_URL \
                  -e DB_USERNAME=$DB_USERNAME \
                  -e DB_PASSWORD=$DB_PASSWORD \
                  -e AUTH_TYPE=$AUTH_TYPE \
                  -e SPRING_PROFILES_ACTIVE=$AUTH_TYPE \
                  -e GOOGLE_CLIENT_ID=$GOOGLE_CLIENT_ID \
                  -e GOOGLE_CLIENT_SECRET=$GOOGLE_CLIENT_SECRET \
                  $IMAGE_NAME
                '''
            }
        }

        stage('Health Check') {
            steps {
                sh '''
                echo "Waiting for application..."

                for i in $(seq 1 24)
                do
                  if curl -fs http://enterprise-user-api:8081/actuator/health; then
                    echo "Application is UP"
                    exit 0
                  fi

                  echo "Health check attempt $i failed. Retrying..."
                  sleep 5
                done

                echo "Application failed health check. Printing container logs."
                docker logs enterprise-user-api || true
                exit 1
                '''
            }
        }
    }

    post {
        success {
            echo "Deployment successful with AUTH_TYPE=${AUTH_TYPE}"
        }

        failure {
            echo "Deployment failed with AUTH_TYPE=${AUTH_TYPE}"
            sh 'docker logs enterprise-user-api || true'
        }
    }
}
