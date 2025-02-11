pipeline {
    agent { label 'rhel' }
    stages {
        stage('Aktualizacja') {
            steps {
                sh 'sudo dnf update -y'
            }
        }
        stage('Wdrożenie Nginx') {
            steps {
                sh 'docker pull nginx:latest'
            }
        }
        stage('Testowanie') {
            steps {
                script {
                    sh '''
                        # Uruchomienie kontenera z Nginx
                        docker run -d --name test-nginx -p 8080:80 nginx:latest
                        sleep 5

                        # Test, czy Nginx działa
                        if curl -s http://localhost:8080 | grep -q "Welcome to nginx"; then
                            echo "Nginx działa poprawnie"
                        else
                            echo "Błąd: Nginx nie działa!" >&2
                            exit 1
                        fi

                        # Usunięcie kontenera po teście
                        docker stop test-nginx
                        docker rm test-nginx
                    '''
                }
            }
        }
    }
}


