# Enterprise-Grade CI/CD Pipeline Documentation for Java Spring Boot Pharmaceutical Platform

This document outlines the CI/CD pipeline implemented for deploying a Spring Boot microservice using GitHub Actions and GitOps principles with ArgoCD. The setup ensures security, reliability, and scalability across four environments: **Dev**, **QA**, **Staging**, and **Prod**.

---

## Purpose

To establish a production-ready, auditable **Continuous Delivery (CD)** pipeline that:

* Adopts a strict **branching strategy**
* Ensures **code quality and security** through SAST, dependency, and secret scanning
* Promotes **images** across environment-specific registries
* Leverages **GitOps** and **ArgoCD** for controlled deployment
* Sends **Slack notifications** for visibility

---

## Branching Strategy

| Branch      | Purpose                                             |
| ----------- | --------------------------------------------------- |
| `main`      | Golden copy for production/hotfix/DR releases       |
| `release`   | Triggers deployments for staging and production     |
| `develop`   | Active integration for development & QA deployments |
| `feature/*` | Developer-specific isolated work branches           |

---

## Pipeline Workflow Overview

![CI/CD Pipeline Diagram](../snapshots/csi-pharma.drawio.png)

The following image represents the CI/CD and GitOps-based deployment strategy used for the Spring Boot pharmaceutical platform. It includes build, test, scan, registry push, GitOps sync, and environment-specific deployment workflows.

### Developer Flow

1. Developer creates a `feature/*` branch
2. Raise PR to `develop`
3. CI Pipeline triggers and executes:

   * Checkstyle
   * Build + Test (JUnit)
   * Upload artifacts
   * SonarQube quality gate scan
   * Trivy filesystem scan
   * Docker image build + scan
   * Push to GitHub Container Registry (GHCR)
   * Patch GitOps `rollout-patch.yaml` for Dev
   * ArgoCD auto-syncs to Dev cluster
   * Slack notification sent

### Promotion to QA

* Triggered manually using `workflow_dispatch`
* Builds & scans Docker image using given SHA tag
* Pushes to DockerHub (QA registry)
* Updates QA GitOps manifest
* Triggers ArgoCD deployment
* Sends Slack notification

### Promotion to Staging

* Merge `develop` ➔ `release`
* Pipeline triggers conditionally
* Builds Docker image, scans, and pushes to **ECR**
* Updates `environments/staging/rollout-patch.yaml`
* ArgoCD syncs to staging
* Slack notification

### Promotion to Production

* Manually triggered via `workflow_dispatch`
* Builds Docker image, scans with Trivy
* Pushes to Azure ACR with **semantic version tag**
* Updates `environments/prod/rollout-patch.yaml`
* ArgoCD syncs and Slack notification

---

## Image Registry Strategy

| Environment | Registry                  | Tag Format        |
| ----------- | ------------------------- | ----------------- |
| Dev         | GitHub Container Registry | SHA tag           |
| QA          | DockerHub                 | SHA tag           |
| Staging     | AWS ECR                   | SHA tag           |
| Prod        | Azure Container Registry  | Semantic (vX.Y.Z) |

* This strategy maximizes the use of multiple registries to leverage different experiences and capabilities across environments. SHA tags are used for non-production environments to track the exact image built for testing, while semantic versioning is used for production to ensure controlled and predictable releases.
---

## ArgoCD GitOps Integration

* Watches `environments/*/rollout-patch.yaml`
* Sync policies:

  * Auto-sync for Dev
  * Manual or PR-based sync for QA, Staging, Prod
* Updates are triggered by PRs via `peter-evans/create-pull-request`
* GitOps repo path example:

```bash
environments/
├── dev/
│   └── rollout-patch.yaml
├── qa/
│   └── rollout-patch.yaml
├── staging/
│   └── rollout-patch.yaml
└── prod/
    └── rollout-patch.yaml
```

---

## Quality & Security Checks

| Check             | Tool                   |
| ----------------- | ---------------------- |
| Code Linting      | Checkstyle (Maven)     |
| Unit Tests        | JUnit                  |
| Test Reports      | `dorny/test-reporter`  |
| Code Coverage     | JaCoCo                 |
| Code Quality      | SonarQube              |
| Secrets Detection | Gitleaks & git-secrets |
| Dependency Scan   | Trivy (filesystem)     |
| Image Scan        | Trivy (image)          |

> **Quality Gates**: The SonarQube scan includes `qualitygate.wait=true`, and PRs are blocked on failed gates.

---

## Secret Management

* **GitHub Secrets** are scoped to environment and usage context:

  * `GHCR_TOKEN`, `GHCR_USERNAME`
  * `DOCKER_USERNAME`, `DOCKER_PASSWORD`
  * `SONAR_TOKEN`, `SONAR_HOST_URL`
  * `AZURE_CLIENT_ID`, `AZURE_TENANT_ID`, `AZURE_SUBSCRIPTION_ID`, `ACR_NAME`
  * `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_ECR_REGISTRY_URI`
  * `NEXUS_URL`, `NEXUS_USERNAME`, `NEXUS_PASSWORD`
  * `SLACK_WEBHOOK_URL`
* **Secret scanning**: All commits are scanned using Gitleaks and git-secrets

---

## Promotion Manual Triggers (`workflow_dispatch`)

### Promote to QA (GitHub Action)

```bash
type: workflow_dispatch
input:
  image_tag: <sha>
  promote_from: dev
  promote_to: qa
```

* Image is tagged, scanned, and pushed to DockerHub
* QA rollout YAML is patched
* Slack message sent

### Promote to Prod (GitHub Action)

```bash
type: workflow_dispatch
input:
  image_tag: v1.2.3
  promote_from: staging
  promote_to: prod
```

* Image built, scanned, pushed to Azure ACR
* GitOps prod rollout patched
* Slack message with full traceability

---

## Slack Notification Template

Slack messages include:

* Repo and branch name
* Commit and author
* JUnit test summary
* Trivy scan link
* SonarQube gate status
* Image tag & promotion info

---

### Sample PR Message for GitOps Update:

```bash
title: Deploy to Dev - <IMAGE_TAG>
body:
  Automated deployment to Dev.
  This PR updates pharma-app image to tag <IMAGE_TAG>
```

### Update Image Tag Script (Sed Command):

```bash
sed -i "s|image: .*|image: <REGISTRY>/pharma-<env>:<TAG>|" environments/dev/rollout-patch.yaml
```
* This script uses sed to replace the image tag in the rollout-patch.yaml file, ensuring that the correct version is deployed. The <REGISTRY> and <TAG> placeholders will be replaced dynamically during the CI/CD process.

### Common Artifacts Stored:

* `target/surefire-reports/*.xml`
* `trivy-report.txt`
* `trivy-image-report.txt`

---

## Tools Stack Overview

| Category         | Tool/Service               |
| ---------------- | -------------------------- |
| Build            | Maven                      |
| Code Linting     | Checkstyle                 |
| Unit Testing     | JUnit                      |
| Test Reporting   | dorny/test-reporter        |
| SAST             | SonarQube, Gitleaks        |
| Secret Scanning  | git-secrets                |
| Dependency Scan  | Trivy (filesystem & image) |
| Artifact Storage | Nexus                      |
| Registries       | GHCR, DockerHub, ECR, ACR  |
| GitOps           | ArgoCD                     |
| Notifications    | Slack                      |

---

## Designed for Enterprise Scalability

* Strict branching discipline with manual QA/Prod promotion
* Cluster-based environment segregation
* Image immutability across stages
* Separation of concerns: CI pipeline vs CD/GitOps

> This pipeline serves as a blueprint for modern Java microservice delivery pipelines across cloud-native environments.

---

## Change History

| Version | Date       | Changes                       | Author |
| ------- | ---------- | ----------------------------- | ------ |
| 1.0     | 2025-07-15 |  Architect CI/CD Pipeline using GitHub Actions | Vikas  |

---


