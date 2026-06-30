---
title: "3. Context and Scope"
chapter: "3"
doc_type: "arc42-chapter"
slug: "03-context-and-scope"
---

# 3. Context and Scope

### 3.1 Business Context
The app supports three main domains:
1. Plant identification and registration.
2. Plant profile management.
3. Care planning (general reference + current dynamic tasks).

### 3.2 Technical Context
**External systems/services**
- Gemini API: plant identification and photo-based health analysis.

**Internal boundaries**
- Android app (UI + business logic)
- Local Room database
- Local photo file/cache storage

### 3.3 Context Diagram
```mermaid
flowchart LR
    U[User] <--> A[garden Android App]
    A <--> DB[(Room Database)]
    A <--> FS[(Local Photo Storage)]
    A <--> G[Gemini API]
```

