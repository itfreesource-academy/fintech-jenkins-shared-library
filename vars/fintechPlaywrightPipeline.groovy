/**
 * Global Jenkins Shared Library Step: fintechPlaywrightPipeline
 * Standardizes execution for Playwright + TypeScript Cross-Browser E2E Suites.
 */
def call(Map config = [:]) {
    pipeline {
        agent {
            docker {
                image config.dockerImage ?: 'mcr.microsoft.com/playwright:v1.43.0-jammy'
            }
        }

        options {
            timeout(time: config.timeoutMinutes ?: 20, unit: 'MINUTES')
            ansiColor('xterm')
            buildDiscarder(logRotator(numToKeepStr: '10'))
            disableConcurrentBuilds()
        }

        environment {
            CI = 'true'
            PLAYWRIGHT_BROWSERS_PATH = '/ms-playwright'
        }

        stages {
            stage('Prepare SUT WebApp') {
                steps {
                    echo "Cloning target FinTech WebApp for local headless testing..."
                    dir('fintech-payment-portal') {
                        git url: 'https://github.com/vishalprajapati2k25/fintech-payment-portal.git', branch: 'main'
                    }
                }
            }

            stage('Install NPM Dependencies') {
                steps {
                    echo "Installing Playwright and TypeScript runtime dependencies..."
                    sh 'npm ci'
                }
            }

            stage('Execute Playwright Tests') {
                steps {
                    echo "Executing Playwright E2E browser tests (Chromium / Firefox / WebKit)..."
                    sh """
                        export BASE_URL="file://${WORKSPACE}/fintech-payment-portal/index.html"
                        npx playwright test ${config.browser ? "--project=" + config.browser : "--project=chromium"}
                    """
                }
            }

            stage('Publish HTML Quality Dashboard') {
                steps {
                    echo "Publishing interactive Playwright HTML report..."
                    publishHTML target: [
                        allowMissing: true,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'playwright-report',
                        reportFiles: 'index.html',
                        reportName: 'Playwright E2E Quality Dashboard'
                    ]
                }
            }
        }

        post {
            always {
                archiveArtifacts artifacts: 'playwright-report/**', allowEmptyArchive: true
                archiveArtifacts artifacts: 'test-results/**', allowEmptyArchive: true
                cleanWs notFailBuild: true
            }
            success {
                echo "Playwright E2E Pipeline PASSED."
            }
            failure {
                echo "Playwright E2E Pipeline FAILED - Inspect archived traces and report."
            }
        }
    }
}
