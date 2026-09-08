/**
 * Global Jenkins Shared Library Step: notifySlack
 * Sends rich formatted deployment & test status alerts.
 */
def call(String status, String channel = '#fintech-qa-alerts') {
    def color = (status == 'SUCCESS') ? '#10b981' : '#ef4444'
    def message = """
    *FinTech Quality Gate Alert:*
    • *Job:* ${env.JOB_NAME} [Build #${env.BUILD_NUMBER}]
    • *Status:* ${status}
    • *Branch:* ${env.BRANCH_NAME ?: 'main'}
    • *Commit:* ${env.GIT_COMMIT ?: 'HEAD'}
    • *Build URL:* ${env.BUILD_URL}
    """
    echo "Simulating Slack Webhook notification to ${channel} [${status}]: ${message}"
}
