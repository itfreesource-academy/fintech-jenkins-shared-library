# FinTech Jenkins Shared Pipeline Library

[![Jenkins Pipeline-as-Code](https://img.shields.io/badge/Jenkins-Pipeline--as--Code-red?logo=jenkins)](https://www.jenkins.io/)
![Groovy](https://img.shields.io/badge/Groovy-DSL-blue?logo=apachegroovy)
![Docker](https://img.shields.io/badge/Docker-In--Docker%20(DinD)-2496ED?logo=docker)
![Allure](https://img.shields.io/badge/Allure-Reports-blueviolet)

An enterprise-grade **Jenkins Shared Library** written in Groovy, standardizing continuous integration quality gates, containerized Docker build agents, and automated test reporting for distributed FinTech architectures.

Engineered by **[Vishal Prajapati](https://defendloop.io)** (*Senior Automation & Tools Development Engineer*) to showcase production-grade DevOps and pipeline tooling.

---

## 🏛️ Architecture & Pipeline Flow

```mermaid
flowchart TD
    subgraph SCM ["Source Code Management"]
        JAVA_REPO["fintech-test-platform-harness"] -->|References Shared Library| CONTROLLER
        TS_REPO["fintech-playwright-ts-harness"] -->|References Shared Library| CONTROLLER
        PORTAL_REPO["fintech-payment-portal"] -->|References Shared Library| CONTROLLER
    end

    subgraph CONTROLLER ["Jenkins Controller (Port 8085)"]
        LIB["fintech-jenkins-shared-library (@Library)"]
        LIB --> VARS_JAVA["vars/fintechJavaPipeline.groovy"]
        LIB --> VARS_PW["vars/fintechPlaywrightPipeline.groovy"]
        LIB --> VARS_SONAR["vars/sonarQualityGate.groovy"]
        LIB --> VARS_SLACK["vars/notifySlack.groovy"]
    end

    subgraph AGENTS ["Dynamic Docker Agents (DinD)"]
        VARS_JAVA -->|Spins Container| AGENT_JAVA["maven:3.9.6-eclipse-temurin-17"]
        VARS_PW -->|Spins Container| AGENT_PW["mcr.microsoft.com/playwright:v1.43.0-jammy"]
    end

    subgraph GATES ["Observability & Reports"]
        AGENT_JAVA --> ALLURE["Allure Quality Dashboard"]
        AGENT_JAVA --> JUNIT["Surefire XML Reports"]
        AGENT_PW --> PW_HTML["Playwright HTML Traces"]
        GATES --> SLACK["Slack / Email Alerts"]
    end
```

---

## 📂 Library Directory Structure

```
fintech-jenkins-shared-library/
├── vars/
│   ├── fintechJavaPipeline.groovy        # Global pipeline wrapper for Java 17 / Kafka suites
│   ├── fintechPlaywrightPipeline.groovy  # Global pipeline wrapper for Playwright TS suites
│   ├── sonarQualityGate.groovy          # Enforces SonarQube quality gate thresholds
│   └── notifySlack.groovy               # Formatted build notifications
├── src/
│   └── io/defendloop/pipeline/
│       ├── DockerHelper.groovy          # Container agent volume & lifecycle management
│       └── QualityReportParser.groovy   # Formatted test metrics console summarizer
├── resources/
│   └── templates/
│       └── build-summary-email.html     # Branded HTML email alert template
├── docker/
│   ├── docker-compose.yml               # Ready-to-run local Jenkins Controller + DinD setup
│   └── plugins.txt                      # Pre-configured recommended plugins
└── README.md
```

---

## 🚀 One-Command Local Jenkins Execution

Spin up a pre-configured Jenkins Controller with Docker-in-Docker build agents locally:

```bash
cd docker
docker compose up -d

# Open Jenkins Controller in your browser:
# http://localhost:8085
```

---

## 🔗 How to Register in Jenkins
1. Navigate to: **Manage Jenkins** &rarr; **System** &rarr; **Global Pipeline Libraries**.
2. Click **Add**:
   * **Name:** `fintech-shared-library`
   * **Default version:** `main`
   * **Retrieval method:** Modern SCM &rarr; Git
   * **Project Repository:** `https://github.com/itfreesource-academy/fintech-jenkins-shared-library.git`
3. Now any repository `Jenkinsfile` can use this entire library with one line:
   ```groovy
   @Library('fintech-shared-library') _
   fintechJavaPipeline()
   ```
