### Wiom Design System

- [ ] New UI in this PR uses Wiom components and tokens (`WiomButton`, `WiomInput`, `WiomTheme.color.*`, etc.)
- [ ] **If not:** link the ADR explaining why → `<url>`
- [ ] No `@file:Suppress("WiomNotUsed")` added without a `// non-wiom: <reason>` comment alongside it
- [ ] No new raw `Color(...)`, `.sp`, `.dp` literals in Composables
- [ ] No new `Icons.Default.*` / `Icons.Filled.*` / etc. imports — only `Icons.Rounded.*`

*Existing screens touched only to fix bugs don't trigger this checklist — old code stays as-is unless the PR is explicitly migrating it.*
