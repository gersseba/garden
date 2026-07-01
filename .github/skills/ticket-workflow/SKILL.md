---
name: ticket-workflow
description: 'Ticket and work-package workflow for AI-assisted software teams: producer creates tickets, waits for go-ahead, orchestrates dev/reviewer loops, and closes work.'
---

# Ticket and Work-Package Workflow

## Overview

This workflow is for teams using three roles:
- Producer: plans and orchestrates
- Dev: implements and tests
- Reviewer: reviews and approves

Work is organized as **tickets** grouped into **work packages**.

```mermaid
sequenceDiagram
    participant User
    participant Producer
    participant Dev
    participant Reviewer
    participant GitHub

    User->>Producer: Describe tasks/features
    Producer->>GitHub: Create issues and dependencies
    Producer->>User: Present work-package plan
    User->>Producer: "go" (all or selected package)

    loop selected tickets in dependency order
      Producer->>Dev: Implement ticket #{N}
      Dev->>GitHub: Branch + changes + tests + PR
      Producer->>Reviewer: Review PR #{N}
      Reviewer->>GitHub: REQUEST_CHANGES or APPROVE

      alt changes requested
        Producer->>Dev: Address feedback
        Dev->>GitHub: Push fixes + thread replies
        Producer->>Reviewer: Re-review
      end

      Producer->>Dev: Merge approved PR
      Dev->>GitHub: Merge and close ticket
    end

    Producer->>User: Completion report
```

## Naming Convention

- Branch: `feature/{N}-kebab-title`
- PR title: `#{N} {Title}`
- Commit: `type: summary (Fixes #{N})`

## Quality Gates

Before PR:
- Dev must run relevant tests and include evidence in PR.

Before merge:
- Reviewer approval required.
- Blocking review threads resolved.

## MCP-First Guidance

Prefer GitHub MCP tools for issue/PR/review/merge operations.
Use terminal/git only if MCP lacks required capability.
