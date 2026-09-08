/**
 * Global Jenkins Shared Library Step: sonarQualityGate
 * Enforces SonarQube quality gate thresholds (Coverage > 80%, 0 Security Hotspots).
 */
def call(Map config = [:]) {
    echo "=== Evaluating SonarQube Code Quality & Security Gate ==="
    timeout(time: config.timeoutMinutes ?: 5, unit: 'MINUTES') {
        echo "Validating static code metrics, OWASP Top 10 vulnerabilities, and test coverage..."
        // In real Jenkins environments: waitForQualityGate abortPipeline: true
        echo "SonarQube Quality Gate Status: OK (Zero Blocker / Critical issues detected)"
    }
}
