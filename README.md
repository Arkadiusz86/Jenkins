# Jenkins Pipeline - Automatyczna instalacja i testowanie Nginx

## Opis
Ten pipeline w Jenkins automatyzuje proces aktualizacji systemu, wdrożenia Nginx w kontenerze Docker oraz testowania jego działania.

## Wymagania
- Jenkins
- Agent Jenkins z systemem **RHEL** (Red Hat Enterprise Linux) lub kompatybilnym
- Docker
- Dostęp do internetu

## Pipeline

```groovy
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
```

## Instrukcja użycia
1. Skonfiguruj agenta Jenkins z etykietą **rhel**.
2. Upewnij się, że Docker jest zainstalowany i skonfigurowany.
3. Skopiuj powyższy kod do definicji pipeline w Jenkins.
4. Uruchom pipeline i sprawdź logi.

## Działanie
- **Aktualizacja**: Wykonuje aktualizację pakietów systemowych za pomocą `dnf`.
- **Wdrożenie Nginx**: Pobiera najnowszy obraz Nginx z Docker Hub.
- **Testowanie**: Uruchamia kontener, testuje dostępność serwera, a następnie go usuwa.

## Autor
[Arkadiusz Siczek](https://github.com/ArkadiuszSiczek)

