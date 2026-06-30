---
title: "2. Architecture Constraints"
chapter: "2"
doc_type: "arc42-chapter"
slug: "02-architecture-constraints"
---

# 2. Architecture Constraints

### 2.1 Technical Constraints
- Platform: Android 8.0+ (API 24+)
- Language: Java 24 (app code)
- Build: Gradle 9.4.1 with Kotlin DSL + version catalog
- UI: AndroidX + Material Design 3 + Navigation XML + ViewBinding
- Storage: Room (SQLite), local-first
- AI: Google Generative AI SDK (Gemini)
- Testing: JUnit 4 + AndroidX Espresso

### 2.2 Organizational/Process Constraints
- Keep PRs focused; avoid broad unrelated refactors.
- Run `./gradlew test lint` before push.
- Follow repository conventions in `.github/copilot-instructions.md` and `.github/instructions/*`.

### 2.3 Security and Compliance Constraints
- Never hardcode or commit Gemini API keys.
- Remove/strip EXIF metadata before sending photos to AI.
- Avoid sensitive data in logs.

