---
name: 'android-testing-workflow'
description: 'Defines a reliable workflow for selecting, running, and validating Android unit and instrumentation tests in this repository.'
---

# Android Testing Workflow

## Purpose
Provide a repeatable way to validate behavior changes quickly and consistently.

## Instructions
1. Identify edited areas (`java`, `xml`, manifest, Gradle) and map to likely test scope.
2. Run fast local checks first:
   - `./gradlew test`
3. If UI, navigation, or platform behavior changed, run instrumentation checks:
   - `./gradlew connectedAndroidTest`
4. If build config or resources changed broadly, run lint and full assemble:
   - `./gradlew lint assembleDebug`
5. Report failures with file path, test name, and shortest repro steps.

## Assets
- Unit tests: `app/src/test/`
- Instrumentation tests: `app/src/androidTest/`
- Gradle wrapper: `./gradlew`

