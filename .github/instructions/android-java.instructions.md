---
description: 'Coding conventions for Android Java sources, XML resources, and Gradle Kotlin DSL in this repository.'
applyTo: '**/*.java, **/*.xml, **/*.gradle.kts'
---

# Android Java Instructions

## Conventions
- Keep Android component classes concise and focused on UI orchestration.
- Use descriptive method names and avoid long methods with mixed responsibilities.
- Prefer constants for repeated keys and request codes.
- Keep XML layout hierarchy shallow when possible.

## Patterns
- Use ViewBinding instead of repeated `findViewById` calls.
- Keep fragment transaction and navigation logic explicit and readable.
- Use resource files for strings, colors, dimensions, and styles.
- Align Gradle dependency declarations with version catalog usage (`libs.*`).

## Anti-patterns
- Avoid doing heavy work on the main thread.
- Avoid embedding hardcoded UI text in Java classes.
- Avoid unrelated formatting-only churn in functional PRs.

