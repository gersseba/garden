---
title: "11. Risks and Technical Debt"
chapter: "11"
doc_type: "arc42-chapter"
slug: "11-risks-and-technical-debt"
---

# 11. Risks and Technical Debt

### 11.1 Current Risks
- AI output variability may affect recommendation consistency.
- Photo quality/device differences can reduce identification confidence.
- No cloud sync yet; multi-device continuity is out of scope.

### 11.2 Technical Debt / Open Topics
- **Mock Data in Production:** `MyPlantsViewModel` and `PlantDetailViewModel` Still contain hardcoded mock strings and tasks. Transition to full DB-backed care plans.
- **Fragile Dependency Management:** Manual instantiation of repositories in ViewModels with try-catch blocks for tests. Solution: Move to Standard Dependency Injection (Hilt).
- **Non-Reactive Locale Handling:** ViewModels often capture the locale at initialization rather than reacting to `LocaleManager` changes.
- **Java/Kotlin Mix:** Maintenance of two languages with varying levels of null safety and modern feature support.
- **DataStore runBlocking Use:** Synchronous bridge for DataStore in `SettingsDataStoreImpl` is a potential performance/deadlock risk.
