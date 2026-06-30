s# garden — Plant Catalog & AI Care Assistant

An Android app that helps users catalog and maintain plants using generative AI for plant identification, health analysis, and care scheduling.

## Quick Start

### For Developers

**Setup:**
```bash
git clone <repo> garden
cd garden
./gradlew sync          # Sync Gradle
./gradlew test          # Run unit tests
./gradlew assembleDebug # Build APK
```

**Run locally:**
- Open Android Studio → File → Open → select garden folder
- Android Studio auto-syncs Gradle
- Run → Run 'app' → select emulator or device

### Gemini API Key Setup

The app uses the Gemini API for plant identification and health analysis.  
You must configure your API key **before** building or running the app.

1. Copy the example template:
   ```bash
   cp local.properties.example local.properties
   ```
2. Open `local.properties` and replace the placeholder with your real key:
   ```properties
   GEMINI_API_KEY=AIza...your_actual_key_here
   ```
3. Get a free key at <https://aistudio.google.com/app/apikey>

> **⚠️ Never commit `local.properties`** — it is listed in `.gitignore`.  
> The key is injected into `BuildConfig.GEMINI_API_KEY` at build time and is never visible in source code.
>
> If the key is missing, the build will still succeed but a warning is printed at configuration time and Gemini API calls will fail at runtime with a clear error message.

### For the AI Agent Team

**Start here:**
1. Read [`docs/project-documentation/index.md`](./docs/project-documentation/index.md) — single source of truth (arc42 architecture)
2. Read `.github/copilot-instructions.md` — project-specific coding standards
3. Read `.github/instructions/garden-plant-features.instructions.md` — plant app domain patterns

**Agents available:**
- `@ai-team-producer` (Remy) — planning, PRs, coordination
- `@ai-team-dev` (Nova/Sage/Milo) — implementation
- `@ai-team-qa` (Ivy) — testing, QA sign-off

**Workflow:**
1. Remy plans sprint → creates `docs/sprint-N/plan.md`
2. Dev team implements → Nova (UI), Sage (data/AI), Milo (polish)
3. Ivy (QA) tests → files issues if bugs found
4. Remy merges after QA sign-off

---

## Project Structure

```
garden/
├── docs/
│   ├── project-documentation/    ← Single source of truth (arc42)
│   │   ├── index.md
│   │   ├── 01-introduction-and-goals/
│   │   ├── 02-architecture-constraints/
│   │   ├── ... (10 more chapters)
│   │   └── 12-glossary/
│   └── sprint-N/                 ← Sprint planning & progress
├── README.md                     ← This file
├── .github/
│   ├── copilot-instructions.md   ← Project coding standards
│   ├── agents/
│   │   ├── ai-team-producer.agent.md      (Remy)
│   │   ├── ai-team-dev.agent.md           (Nova/Sage/Milo)
│   │   ├── ai-team-qa.agent.md            (Ivy)
│   │   ├── android-maintainer.agent.md
│   │   └── repo-architect.agent.md
│   ├── instructions/
│   │   ├── android-java.instructions.md
│   │   ├── agent-safety.instructions.md
│   │   └── garden-plant-features.instructions.md
│   ├── prompts/
│   │   ├── test-gen.prompt.md
│   │   ├── doc-gen.prompt.md
│   │   └── explain-code.prompt.md
│   └── skills/
│       ├── ai-team-orchestration/SKILL.md
│       └── android-testing-workflow.md
└── app/
    ├── src/main/java/com/gersseba/garden/
    │   ├── MainActivity.java
    │   ├── fragment/          (GardenFragment, PlantDetailFragment, etc.)
    │   ├── viewmodel/         (PlantViewModel, PhotoAnalysisViewModel, etc.)
    │   ├── repository/        (PlantRepository, TaskRepository, etc.)
    │   ├── service/           (GeminiAIService, CareScheduleService, etc.)
    │   ├── database/          (Room entities, DAOs)
    │   ├── model/             (Plant, PlantPhoto, CareTask, etc.)
    │   └── util/              (ImageUtils, DateUtils, etc.)
    ├── src/main/res/
    │   ├── layout/
    │   ├── values/
    │   └── navigation/
    ├── src/test/
    ├── src/androidTest/
    └── build.gradle.kts
```

---

## Features

### Phase 1: Plant Identification
- User captures/uploads plant photo
- Gemini AI identifies species
- App saves plant profile with general care plan

### Phase 2: Plant Management
- View list of all plants in local database
- Open plant detail view:
  - Photo gallery (timestamped uploads)
  - General care instructions (yearly/seasonal plan)
  - Current requirements (AI-suggested immediate tasks)
- Upload new photos for health analysis

### Phase 3: Care Planning (Two Tiers)
- **General Care Plan:** Yearly/seasonal schedule per plant type (e.g., tomato: prune in spring, fertilize in summer)
- **Current Care Tasks:** AI-generated immediate actions (e.g., water today, check for pests) based on photo analysis

---

## Tech Stack

- **Android:** API 24+ (Android 8.0+), target SDK 36
- **Language:** Java 24 (compile), Kotlin DSL (Gradle)
- **UI:** AndroidX, Material Design 3, ViewBinding, Navigation
- **Database:** Room (SQLite)
- **AI/ML:** Google Generative AI SDK (Gemini)
- **Networking:** Retrofit, OkHttp
- **Testing:** JUnit 4, AndroidX Espresso

---

## Development Workflow

### Create a Branch
```bash
git checkout -b feature/sprint-1-plant-id
# or
git checkout -b bugfix/issue-42-camera-crash
```

### Code & Test
```bash
./gradlew test                    # Unit tests
./gradlew connectedAndroidTest    # Device tests
./gradlew lint                    # Lint check
```

### Commit & Push
```bash
git commit -m "feat: add plant photo capture (Fixes #42)"
git push origin feature/sprint-1-plant-id
```

### Create PR
- Push branch and create PR on GitHub
- Request QA sign-off from @ai-team-qa
- After approval, Remy (Producer) merges with: `git merge --no-ff`

---

## Code Standards

- **Naming:** `UpperCamelCase` classes, `lowerCamelCase` methods/fields
- **ViewBinding:** Always use; never `findViewById`
- **Database:** Use Room entities + DAOs only; no raw SQL
- **AI Calls:** All through `GeminiAIService`; mock for tests
- **Threading:** Coroutines for async; never block main thread
- **Strings:** Always in `strings.xml`; no hardcoding
- **Accessibility:** Content descriptions, keyboard nav, contrast ratio >= 4.5:1

---

## Important Patterns

### AI Integration
- Photos are stripped of EXIF before sending to Gemini
- API key in `local.properties` (never committed)
- Mock Gemini responses in tests for reliability

### Database
- All entities inherit from `BaseEntity` with `id`, `createdAt`, `updatedAt`
- DAOs return `LiveData<T>` or `StateFlow<T>` for reactive UI
- Queries use Room's type-safe API

### ViewModels
- Expose `LiveData<T>` or `StateFlow<T>`
- Do NOT hold Activity/Fragment references
- Use `viewModelScope` for coroutines

### Fragments
- Use `ViewBinding` in `onCreateView` + `onDestroyView`
- Observe ViewModels in `onViewCreated`
- Use XML navigation; never hardcode Fragment transactions

---

## Testing

### Unit Tests
Run: `./gradlew test`
- ViewModels, repositories, services
- Mock database and AI service
- Test business logic in isolation

### Instrumentation Tests
Run: `./gradlew connectedAndroidTest`
- Fragment UI flows
- RecyclerView interactions
- Navigation between Fragments
- Requires device or emulator

### Coverage Target
Aim for >80% coverage on:
- ViewModels
- Repositories
- Services (especially GeminiAIService)

---

## Security

- Never hardcode API keys (use `local.properties`)
- Strip EXIF from photos before sending to AI
- Validate all user input
- Use HTTPS for all network calls
- Never log sensitive data (tokens, passwords, PII)

---

## Deployment

### Release Checklist
- [ ] All sprint issues closed
- [ ] `./gradlew test` passes
- [ ] `./gradlew connectedAndroidTest` passes
- [ ] `./gradlew lint` shows 0 warnings
- [ ] QA sign-off: ✅
- [ ] Version bumped in `build.gradle.kts`
- [ ] Branch merged to main

### Publish (Future)
```bash
./gradlew bundleRelease  # Create signed AAB
# Upload to Google Play Console
# Staged rollout: 10% → 25% → 100%
```

---

## Resources

- **Architecture (arc42):** [`docs/project-documentation/index.md`](./docs/project-documentation/index.md)
- **Code Standards:** [`.github/copilot-instructions.md`](./.github/copilot-instructions.md)
- **Domain Patterns:** [`.github/instructions/garden-plant-features.instructions.md`](./.github/instructions/garden-plant-features.instructions.md)
- **Android Best Practices:** [`.github/instructions/android-java.instructions.md`](./.github/instructions/android-java.instructions.md)
- **Agent Safety:** [`.github/instructions/agent-safety.instructions.md`](./.github/instructions/agent-safety.instructions.md)

---

## Contributing

1. Create a focused branch for your feature or bug fix
2. Follow code standards and test thoroughly
3. Write a clear commit message with issue reference
4. Request QA review before merging
5. Producer merges with `--no-ff` to preserve history

---

## Support

For questions or blockers:
- Check `docs/project-documentation/` for architecture context
- Review `.github/instructions/` for coding patterns
- Ask Remy (Producer) for sprint/scope questions
- Ask Ivy (QA) for testing strategy

---

**Last Updated:** June 30, 2026






