---
title: "8. Crosscutting Concepts"
chapter: "8"
doc_type: "arc42-chapter"
slug: "08-crosscutting-concepts"
---

# 8. Crosscutting Concepts

### 8.1 Error Handling
- Handle AI/network failures (rate limit, timeout, invalid auth) with user-friendly messages.
- Avoid crashes for partial/failed analysis; preserve existing local state.

### 8.2 Privacy and Security
- API key from configuration (`local.properties`/BuildConfig), never in source.
- Strip photo metadata before upload.
- Restrict logs to non-sensitive operational details.

### 8.3 Data Management
- Room + DAO for persistent storage.
- Reactive data flows via lifecycle-aware observation.
- No cloud sync in current scope.

### 8.4 UI/UX and Accessibility
- ViewBinding only (no `findViewById`).
- Strings in `strings.xml`.
- Material 3 components, touch target and content description checks.

### 8.5 Testing
- Unit tests for ViewModels/repositories/services.
- Instrumentation tests for key UI/navigation flows.
- AI calls tested with mocked responses.

