# Project: garden — Plant Catalog & AI Care Assistant

## Overview
`garden` is an Android application that helps users catalog and maintain plants using generative AI. Users photograph plants, get AI identification, receive care recommendations, and manage a calendar of maintenance tasks. Built with Java 24, Material Design 3, and Google Generative AI (Gemini).

## Tech Stack
- Platform: Android 8.0+ (API 24+)
- Language: Java 24 (app code), Kotlin DSL (Gradle build files)
- Build System: Gradle 9.4.1 with version catalog
- UI: AndroidX + Material Design 3, Navigation (XML), ViewBinding
- Database: Room (SQLite) for local plant/task storage
- AI/ML: Google Generative AI SDK (Gemini API) for plant ID + health analysis
- Testing: JUnit 4 (unit), AndroidX Espresso (instrumentation)
- Image Processing: Glide or Coil for photo gallery

## Code Standards
- Follow Android and Java naming conventions (`UpperCamelCase` classes, `lowerCamelCase` methods/fields).
- Keep fragments focused on UI presentation; move business logic to ViewModels and repositories.
- Prefer small, testable methods over nested control flow.
- Keep resource names lowercase with underscores (`fragment_garden.xml`, `item_plant_card.xml`).
- Store all user-facing strings in `strings.xml` (no hardcoding).
- Use Room entities + DAOs for all database access (no raw SQL).
- ViewModels must not hold references to Activities or Fragments (use lifecycle-aware data).
- Run `./gradlew test lint` before pushing PRs.

## Architecture
- **MVVM with Repository Pattern:**
  - Activities/Fragments handle UI + navigation
  - ViewModels manage state and business logic
  - Repositories abstract data sources (Room DB, API calls)
  - Services handle AI calls, photo processing, task scheduling

- **Module Structure:**
  - `app/src/main/java/com/gersseba/garden/` — main source
  - `fragment/` — UI fragments (GardenFragment, PlantDetailFragment, CalendarFragment, etc.)
  - `viewmodel/` — ViewModels (PlantViewModel, PhotoAnalysisViewModel, CalendarViewModel)
  - `repository/` — Data access (PlantRepository, TaskRepository, PhotoRepository)
  - `service/` — Business logic (GeminiAIService, CareScheduleService, etc.)
  - `database/` — Room entities, DAOs, AppDatabase
  - `model/` — Data models (Plant, PlantPhoto, CareTask, AnalysisResult)
  - `util/` — Helpers (ImageUtils, DateUtils, PermissionHelper)

## Development Workflow
1. Read `docs/project-brief/index.md` for feature context and current sprint.
2. Create a focused branch: `feature/sprint-N-description` or `bugfix/issue-NNN`.
3. Implement feature with tests:
   - Write unit tests for ViewModels and repositories.
   - Write instrumentation tests for Fragment UI flows.
   - Test AI integration with mocked API responses.
4. Run checks: `./gradlew test lint connectedAndroidTest`.
5. Commit with issue reference: `feat: add plant photo capture (Fixes #42)`.
6. Push and create PR; request QA sign-off before Producer merges.

## Important Patterns
- **AI Integration:** Use `GeminiAIService` for plant identification and health analysis. Filter photo metadata before sending to API.
- **Plant-Centric Design:** Plant is the central entity. Photos, care plans, and tasks are all associated with a plant.
- **ViewBinding:** Always use ViewBinding; never use findViewById.
- **Database:** Use Room entities and DAOs; queries should return LiveData or StateFlow for reactive updates. All data stored locally.
- **Care Planning:** Two distinct types:
  - **General:** Yearly/seasonal plan based on plant species (fixed per plant type)
  - **Current:** Immediate tasks generated from photo analysis + time elapsed (regenerated when user uploads photo or checks in)
- **Photo Gallery:** Store photos locally (Room) with timestamps; lazy-load using Glide/Coil.
- **Error Handling:** Catch API errors (rate limits, invalid auth) gracefully; display user-friendly messages.
- **Architecture Docs:** Keep arc42 docs in `docs/project-brief/<chapter-slug>/index.md` with frontmatter.
- **Diagrams:** Use Mermaid for architecture/runtime diagrams in markdown documentation.

## Do Not
- Do not hardcode Gemini API keys; use `local.properties` or GitHub Secrets for CI.
- Do not commit photos or build outputs from `build/` directories.
- Do not perform network or AI calls on the main thread; use coroutines or background threads.
- Do not store raw plant photos in SharedPreferences (use Room + file storage).
- Do not skip tests for AI integration (mock API responses for reliable test runs).
- Do not introduce broad refactors unrelated to the feature; keep PRs focused.




