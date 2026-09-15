# AGENTS.md: FinTech Jenkins Shared Pipeline Library Context

## 🌟 Executive Summary
`fintech-jenkins-shared-library` is an enterprise Groovy Jenkins Shared Pipeline Library that standardizes CI/CD pipelines, containerized build agents (Docker-in-Docker), SonarQube quality gates, and automated test reporting across all repositories in the FinTech ecosystem.

---

## 🏛️ Architecture & Components
- **Language**: Groovy DSL
- **Controller & Agents**: Jenkins LTS (port 8085 via Docker Compose) with dynamic Docker-in-Docker agents (`maven:3.9.6-eclipse-temurin-17`, `mcr.microsoft.com/playwright:v1.43.0-jammy`).
- **Global Variables (`vars/`)**:
  - `fintechJavaPipeline.groovy`: Declarative pipeline for Java 17 + Maven + Kafka test harnesses with Surefire & Allure report generation.
  - `fintechPlaywrightPipeline.groovy`: Declarative pipeline for Playwright TypeScript suites with multi-browser runs and artifact archiving.
  - `sonarQualityGate.groovy`: Static code analysis enforcement with SonarQube quality gate verification.
  - `notifySlack.groovy`: Formatted Slack notifications on build status changes.
- **Classes (`src/io/defendloop/pipeline/`)**:
  - `DockerHelper.groovy`: Container volume mounting and agent lifecycle management.
  - `QualityReportParser.groovy`: Test metrics console summarization.
- **Docker Setup (`docker/`)**:
  - `docker-compose.yml`: Ready-to-run local Jenkins controller with DinD socket mount and recommended plugins list.

---

## 🚀 Registration & Usage
1. In Jenkins Controller (**Manage Jenkins &rarr; System &rarr; Global Pipeline Libraries**):
   - Name: `fintech-shared-library`
   - Default Version: `main`
   - URL: `https://github.com/itfreesource-academy/fintech-jenkins-shared-library.git`
2. In downstream repos (`Jenkinsfile`):
   ```groovy
   @Library('fintech-shared-library') _
   fintechJavaPipeline()
   ```
   or
   ```groovy
   @Library('fintech-shared-library') _
   fintechPlaywrightPipeline()
   ```

---

## 🎯 Best Practices
- Keep shared steps idempotent and containerized to avoid host environment contamination.
- Ensure Docker socket (`/var/run/docker.sock`) is properly mounted for DinD agent execution.
- Maintain fallback notifications so failed builds alert teams with exact stage failure context.
