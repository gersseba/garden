---
title: "4. Solution Strategy"
chapter: "4"
doc_type: "arc42-chapter"
slug: "04-solution-strategy"
---

# 4. Solution Strategy

- Use **MVVM with Repository pattern** to separate UI orchestration from business/data logic.
- Keep all persistent data local-first with Room entities/DAOs.
- Encapsulate AI interactions in service classes and parse structured responses.
- Distinguish care planning into:
  - **General care:** species-based, relatively static reference guidance.
  - **Current care:** dynamic tasks generated from recent photos and elapsed time.
- Keep fragments focused on presentation and navigation; move logic to ViewModels/repositories.

