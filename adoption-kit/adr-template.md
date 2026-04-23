# ADR <NNNN> — Not Using Wiom Design System for <Feature / Screen name>

- **Status:** Proposed | Accepted | Superseded
- **Date:** YYYY-MM-DD
- **Owner:** @your-github-handle
- **Revisit by:** YYYY-MM-DD (≤ 90 days from today)

## Context

What's the feature? What Wiom component would we normally reach for?

## Why Wiom doesn't fit (yet)

Be specific. Pick as many as apply and expand:

- [ ] **Missing variant** — The required variant (e.g., …) isn't in the Wiom component.
- [ ] **Missing component** — Wiom has no component for this pattern.
- [ ] **Platform constraint** — e.g., needs a specific Material3 API Wiom doesn't expose.
- [ ] **Performance** — Benchmarked the Wiom component and it's X× slower for this use case.
- [ ] **Third-party lock-in** — Feature is built on top of vendor SDK that expects Material3 APIs.
- [ ] **Time** — Wiom equivalent is viable but would cost >N days; deliverable due sooner.

## What we're building instead

Link the PR. Which Material3 / custom components? What tokens / values do they use?

## What would make Wiom fit

Concrete ask of the Wiom DS team. Examples:

- "A new `variant = Floating` on `WiomButton`."
- "A `WiomDataTable` component."
- "Expose `WiomInput`'s internal `TextFieldColors` so I can override focus color."

File an issue at https://github.com/abhishekgarg-wiom/wiom-ds-android/issues with the ✨ **Component / enhancement request** template and link it here: `<url>`.

## Follow-up

- **When Wiom ships the fix:** I will migrate this feature within N weeks.
- **If Wiom won't ship the fix:** This ADR supersedes to "Accepted permanent exception" and documents the divergence for future maintainers.
