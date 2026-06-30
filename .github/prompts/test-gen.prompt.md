---
agent: 'agent'
description: 'Generate or update Android unit/instrumentation tests for a focused code change.'
---

Create tests for the change I am making in this Android project.

Requirements:
- Prefer JUnit tests under `app/src/test/` for pure logic.
- Use instrumentation/Espresso tests under `app/src/androidTest/` for UI/device behavior.
- Cover happy path plus at least one edge case.
- Keep test names descriptive and behavior-oriented.
- Include any minimal production code adjustments needed to make tests pass.

Return:
1. The test file changes
2. Any production changes required
3. Gradle commands to run the new tests

