# CLAUDE.md — Wiom Design System rules for `<consumer-app>`

Loaded on every Claude Code session in this repo. New UI code in this app defaults to Wiom Design System. Existing code stays as-is.

---

## The rule

**New feature code in this repo MUST use `wiom-ds-android` components and tokens.**

If you genuinely can't:

1. Open an ADR at `docs/adr/<nnnn>-not-using-wiom-for-<feature>.md` using the template at https://github.com/abhishekgarg-wiom/wiom-ds-android/blob/main/adoption-kit/adr-template.md
2. Add `@file:Suppress("WiomNotUsed")` at the top of the file
3. Add a `// non-wiom: <short reason>` comment on the Composable declaration
4. Link the ADR in the PR description

CI will flag violations via `designsystem-rules` (published by the library).

---

## The API (quick)

```kotlin
// Wrap your activity once
WiomTheme { AppNavHost(...) }

// Tokens
WiomTheme.color.bg.default / .text.subtle / .stroke.brandFocus / .icon.action
WiomTheme.type.bodyLg / .labelLg / .headingLg
WiomTheme.spacing.lg / .md / .sm / .xl
WiomTheme.radius.large / .medium / .full
WiomTheme.stroke.small / .medium
WiomTheme.shadow.md / .lg / .xl
WiomTheme.iconSize.sm / .md

// Icons
WiomIcon(Icons.Rounded.Search, contentDescription = "Search", tint = WiomTheme.color.icon.action)

// Components — browse per-component READMEs in the library:
WiomButton(text = "Pay", onClick = { ... }, type = WiomButtonType.Primary)
WiomInput(value = x, onValueChange = { ... }, title = "Mobile number", leadingIcon = { WiomIcon(Icons.Rounded.Phone, null) })
WiomListItem(primary = "Plans", trailingMeta = "3 active", onClick = { ... })
WiomTopBar(title = "Home")
WiomNavigationBar(items = navItems, selectedIndex = idx, onSelect = { ... })
WiomBottomSheet(onDismissRequest = { ... }) { ... }
```

Full list: https://github.com/abhishekgarg-wiom/wiom-ds-android#whats-in-v100

---

## Hard no

- No raw `Color(0xFF…)` / `.sp` / `.dp` in Composables — use tokens
- No `Icons.Default.*` / `Icons.Filled.*` / `Icons.Outlined.*` / `Icons.Sharp.*` / `Icons.TwoTone.*` — only `Icons.Rounded.*`
- No direct Material3 `Button`, `OutlinedTextField`, `Switch`, `Checkbox`, `RadioButton`, `TopAppBar`, `NavigationBar`, `ModalBottomSheet`, `Snackbar`, `AlertDialog`, `FilterChip`, `TabRow`, `LinearProgressIndicator`, `CircularProgressIndicator` — use the Wiom equivalent
- No `painterResource` for standard Material icons — reserved for `ic_wiom_*` / `ic_partner_*` brand assets only
- No `@Suppress("WiomNotUsed")` without an inline `// non-wiom:` reason AND a linked ADR

---

## When the library is missing something

1. Check the component list on the Wiom DS README first — most things exist.
2. If it genuinely doesn't, open a component request:
   https://github.com/abhishekgarg-wiom/wiom-ds-android/issues/new/choose → ✨ Component / enhancement request
3. Unblock yourself with an ADR in the meantime (see top of this file).

---

## When working on **existing** screens

If you're fixing a bug in old Material / hand-rolled UI — **don't migrate it in the same PR.** Scope the fix, ship it. If it's obvious low-effort to swap a widget (e.g., one call site), feel free. Otherwise, file a dedicated migration PR or ignore it — old code doesn't have to convert.
