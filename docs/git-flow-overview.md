# 📘 GitFlow Branching Strategy — Java Spring Boot Pharmaceutical Platform

## 📌 Overview

This document describes the GitFlow branching strategy tailored for a Java Spring Boot Pharmaceutical platform using Continuous Delivery (CD) and GitOps principles via ArgoCD. It ensures collaborative development, multi-environment deployments, PR-enforced code quality, and disaster recovery resilience using a stable golden-copy `main` branch.

---

## 🔀 Branch Types

### 🔹 Long-Lived Branches

| Branch  | Description                                         | Deployment Target     |
| ------- | --------------------------------------------------- | --------------------- |
| develop | Main integration branch for features, bugfixes, UAT | Dev → QA              |
| release | Prepares production-grade builds from develop       | Staging → Prod        |
| main    | Golden copy for Disaster Recovery (DR)              | DR Only (manual sync) |

### 🔸 Short-Lived Branches

| Branch Pattern | Purpose                           | Created From | Merges Into   |
| -------------- | --------------------------------- | ------------ | ------------- |
| feature/\*     | New features or enhancements      | develop      | develop       |
| bugfix/\*      | Development or QA-stage bug fixes | develop      | develop       |
| hotfix/\*      | Emergency production fixes        | release      | release, main |

---

## 🔁 Development Workflow

### 👇 Standard Feature Workflow

* Developer creates: `feature/<ticket-id>-<desc>` from `develop`
* Code linked to GitHub Issues or Projects
* PR opened to `develop`
* CI pipeline triggers: Build → Checkstyle → Test → Secret Scan → SAST → Docker Build & Scan
* Merge into `develop`:
  * Updates GitOps `environments/dev/rollout-patch.yaml`
  * Auto-deploys to Dev (ArgoCD watches rollout)
  * Post verification, code auto-promotes to QA via pipeline
* If QA passes: Merge `develop` → `release`
* Pipeline builds staging artifact → updates GitOps `environments/staging/rollout-patch.yaml`
* Post-Staging → Manual promotion to Prod via `workflow_dispatch`
* `main` updated post-prod as golden DR copy

### 🚨 Hotfix Flow

* Create `hotfix/<issue>` from `release`
* Implement & test fix locally
* Open PR to `release` and `main`
* CI pipeline triggers secure build → Docker scan → GitOps patch (prod)
* Manual confirmation required
* Optionally cherry-pick to `develop`

---

## ✅ Branch Protection Rules

| Branch                 | Rules                                                                                      |
| ---------------------- | ------------------------------------------------------------------------------------------- |
| `main`, `release`, `develop` | ✅ Require PR, ✅ Require reviewer, ✅ Resolve all comments, ✅ CI success, ❌ No direct pushes |
| `feature/*`, `bugfix/*`    | ❌ Not protected (short-lived, deleted post-merge)                                          |

---

## ✅ Environment-Specific Promotion & Approval Rules

| Environment | Promotion/Approval Process                                           |
| ----------- | ------------------------------------------------------------------- |
| Dev         | ✅ Auto-deploy on PR merge to `develop`, no approval required        |
| QA          | ✅ Auto-deploy on PR merge to `develop`, no approval required        |
| Staging     | ✅ Requires PR-based sync with **approval reviewer**, must wait for reviewer confirmation |
| Prod        | ✅ Manual promotion via `workflow_dispatch`, requires **proper reviewers** and approval before deployment |

---

## 🚀 Deployment Mapping

| Branch     | Environment   | Trigger Type                  |
| ---------- | ------------- | ----------------------------- |
| develop    | Dev, QA       | Auto on PR merge              |
| release    | Staging, Prod | Auto (Staging), Manual (Prod) |
| main       | DR            | Manual update                 |
| feature/\* | UAT           | On-demand                     |

---

## 🔄 Merge Strategy

| Merge Type   | Description                                            |
| ------------ | ------------------------------------------------------ |
| Squash Merge | Preferred for short-lived branches (clean history)     |
| Merge Commit | Used for `release → main` or multi-commit hotfix flows |
| Rebase       | Optional for local cleanups, not for shared branches   |

---

## 🧪 CI/CD Triggers

| Event                   | Behavior                                            |
| ----------------------- | --------------------------------------------------- |
| PR to develop           | CI Build + SAST + GitLeaks + Docker push to GHCR    |
| Merge to develop        | Auto-deploy to Dev and Promote to QA (GitOps patch)            |
| Merge develop → release | Triggers build → push to ECR → GitOps patch Staging |
| Promote Prod (manual)   | `workflow_dispatch`: builds → ACR → patch prod      |

---

## 📘 Commit Message Standards

| Prefix   | Description                  | Example                                    |
| -------- | ---------------------------- | ------------------------------------------ |
| feat     | New feature                  | feat(user): add OTP-based login            |
| fix      | Bug fix                      | fix(auth): correct password regex          |
| chore    | Infrastructure/config change | chore(ci): integrate slack webhook         |
| docs     | Documentation update         | docs(branching): add UAT branch guidelines |
| refactor | Code reorganization          | refactor(order): split cart logic          |

---

## 📋 Change History

| Version | Date       | Changes                       | Author |
| ------- | ---------- | ----------------------------- | ------ |
| 1.0     | 2025-07-15 | Initial GitHub-based strategy | Vikas  |
