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

### 🔁 Development Workflow

#### Feature/Temp Branch → Develop (via PR)
- Developers raise a PR from their working branch (e.g., `temp/feature-x`) to `develop`.
- The PR triggers the **Dev Environment CI/CD Pipeline**:
  - Unit tests, build, SAST/DAST scans are performed.
  - Application is deployed into the **Dev** environment.
- Once the PR is merged into `develop`, the **QA pipeline** is triggered:
  - Deployment happens to the **QA** environment.
  - Ensures the code in `develop` is verified by QA.

#### Develop → Release (via PR)
- A PR is created from `develop` to `release` for pre-prod readiness.
- This PR triggers the **Staging Environment Deployment**:
  - Staging deployment happens **as part of the PR, before merging**.
  - Final integration, UAT, performance, and smoke testing is done in staging.
  - Prevents bugs from entering the release pipeline before verification.

#### Release → Production
- Once the staging validation is complete, the `develop` branch PR is merged into `release`.
- This **merge** triggers the **Production CD Pipeline**:
  - Production deployment occurs automatically or with an approval step.
  - Ensures only staging-tested code reaches production.

#### Bug Fix Flow (from `develop`)
- A **bug fix** branch is created from `develop` (e.g., `bugfix/fix-issue-123`).
- Follows the **same process**:
  - PR → `develop` (triggers Dev pipeline)
  - Merge → triggers QA pipeline
  - Later promotion to `release` and then `prod` via PRs.

#### Hotfix Flow (from `release`)
- A **hotfix** branch is created from `release` (e.g., `hotfix/fix-prod-issue`).
- Follows a **faster path** for urgent production issues:
  - PR → `release` (triggers **Staging deployment**)
  - Merge → triggers **Production pipeline** (with approval if required)
- After production deployment:
  - The **hotfix is back-merged** into both `develop` and `main` to maintain consistency.


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
| Prod        | ✅ Promotion requires **proper reviewers** and approval before deployment |

---

### ✅  Environment → Trigger Event → Pipeline Source → Deployment Timing

| Environment | Trigger Event   | Pipeline Source | Deployment Timing         |
|-------------|------------------|------------------|----------------------------|
| dev         | PR to `develop`  | `develop`        | On PR creation             |
| qa          | Merge to `develop` | `develop`      | On PR merge                |
| staging     | PR to `release` (pre-merge) | `release` | Before merging to `release` |
| prod        | Merge to `release` | `release`      | On merge                   |

---

## 🔄 Merge Strategy

| Merge Type   | Description                                            |
| ------------ | ------------------------------------------------------ |
| Squash Merge | Preferred for short-lived branches (clean history)     |
| Merge Commit | Used for `release → main` or multi-commit hotfix flows |
| Rebase       | Optional for local cleanups, not for shared branches   |

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
| 2.0     | 2025-07-15 | Initial GitHub-based strategy | Vikas  |
