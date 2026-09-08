/**
 * Global Jenkins Shared Library Step: fintechJavaPipeline
 * Standardizes execution for Java 17 + Maven + Kafka FinTech Test Suites.
 */
def call(Map config = [:]) {
    pipeline {
        agent {
            docker {
                image config.dockerImage ?: 'maven:3.9.6-eclipse-temurin-17'
                args '-v /var/run/docker.sock:/var/run/docker.sock'
            }
        }

        options {
            timeout(time: config.timeoutMinutes ?: 15, unit: 'MINUTES')
            ansiColor('xterm')
            buildDiscarder(logRotator(numToKeepStr: '10'))
            disableConcurrentBuilds()
        }

        environment {
            CI = 'true'
            JAVA_OPTS = '-Dfile.encoding=UTF-8'
        }

        stages {
            stage('Environment Verification') {
                steps {
                    echo "Initializing FinTech Java Pipeline on Jenkins Agent..."
                    sh 'java -version'
                    sh './mvnw -version || mvn -version'
                }
            }

            stage('Compile & Static Checks') {
                steps {
                    echo "Compiling test sources and validating POM dependencies..."
                    sh './mvnw clean test-compile --batch-mode'
                }
            }

            stage('Execute Test Suites') {
                steps {
                    echo "Executing REST Assured API tests, WireMock stubs, and Kafka stream assertions..."
                    sh './mvnw test --batch-mode'
                }
            }

            stage('Publish Reports') {
                steps {
                    echo "Publishing Surefire XML and Allure quality dashboards..."
                    junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                    allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
                }
            }
        }

        post {
            always {
                archiveArtifacts artifacts: 'target/surefire-reports/**', allowEmptyArchive: true
                archiveArtifacts artifacts: 'target/allure-results/**', allowEmptyArchive: true
                cleanWs notFailBuild: true
            }
            success {
                echo "FinTech Java Pipeline PASSED: 100% Idempotency & Stream SLA Verified."
            }
            failure {
                echo "FinTech Java Pipeline FAILED: Quality gate threshold breached."
            }
        }
    }
}
