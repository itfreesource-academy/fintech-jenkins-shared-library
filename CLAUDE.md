# CLAUDE.md: FinTech Jenkins Shared Pipeline Library

## Overview
`fintech-jenkins-shared-library` provides reusable Jenkins declarative pipeline definitions and Groovy steps for Java microservice harnesses, Playwright web suites, SonarQube quality gates, and Slack notifications.

## Tech Stack
- **Language**: Groovy
- **Automation**: Jenkins Pipeline DSL
- **Containers**: Docker-in-Docker (DinD), Eclipse Temurin 17, Microsoft Playwright Docker images
- **Reporting**: Allure, JUnit XML, HTML reports

## Directory Structure
- `vars/`: Callable pipeline templates (`fintechJavaPipeline.groovy`, `fintechPlaywrightPipeline.groovy`, `sonarQualityGate.groovy`, `notifySlack.groovy`).
- `src/io/defendloop/pipeline/`: Helper Groovy classes (`DockerHelper.groovy`, `QualityReportParser.groovy`).
- `docker/`: Local testbed `docker-compose.yml` and `plugins.txt` to run Jenkins locally on `http://localhost:8085`.

## Usage in Jenkinsfiles
```groovy
@Library('fintech-shared-library') _
fintechJavaPipeline()
```

## Guidelines
- Write Jenkins pipeline code defensively; handle null stages and verify tool availability within container steps.
- Ensure Docker container agents cleanup spawned processes and volumes after test stages complete.
