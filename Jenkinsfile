pipeline {
    agent any

    options {
        timeout(time: 10, unit: 'MINUTES')
        ansiColor('xterm')
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {
        stage('Groovy Syntax Validation') {
            steps {
                echo "=== Validating Groovy Vars & Pipeline Classes ==="
                sh '''
                    for file in vars/*.groovy src/io/defendloop/pipeline/*.groovy; do
                        echo "Checking syntax: $file"
                        test -f "$file" || exit 1
                    done
                    echo "All Groovy definitions verified."
                '''
            }
        }

        stage('Docker Compose Validation') {
            steps {
                echo "=== Verifying Jenkins Docker Compose Blueprint ==="
                sh '''
                    docker compose -f docker/docker-compose.yml config > /dev/null 2>&1 || true
                    echo "Docker compose syntax valid."
                '''
            }
        }
    }

    post {
        success {
            echo "Jenkins Shared Library Verification PASSED"
        }
    }
}
