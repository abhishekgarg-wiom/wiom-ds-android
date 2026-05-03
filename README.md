# Wiom Design System — Android

Jetpack Compose library for the Wiom apps.

- **Foundations:** element-first tokens (`bg.*`, `text.*`, `stroke.*`, `icon.*`), Noto Sans via Google Fonts, Material 3 Icons Rounded.
- **15 components** covering the V2 skill set.
- **Adoption Kit** — Detekt rules + PR/ADR templates + drop-in CLAUDE.md for consumer repos.

---

## Install

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

// app/build.gradle.kts
dependencies {
    implementation("com.github.abhishekgarg-wiom.wiom-ds-android:designsystem:v2.0.0")
    // Enforce the design system in CI — see ADOPTION.md
    detektPlugins("com.github.abhishekgarg-wiom.wiom-ds-android:designsystem-rules:v2.0.0")
}
```

---

## Use

### Wrap the app

```kotlin
setContent {
    WiomTheme {
        AppNavHost()
    }
}
```

### Tokens

```kotlin
Modifier.background(WiomTheme.color.bg.default)
Text("Hi", style = WiomTheme.type.bodyLg, color = WiomTheme.color.text.default)
Modifier.border(WiomTheme.stroke.small, WiomTheme.color.stroke.subtle)
Modifier.padding(WiomTheme.spacing.lg)
```

### Icons

```kotlin
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search

WiomIcon(Icons.Rounded.Search, contentDescription = "Search", tint = WiomTheme.color.icon.action)
```

---

## What's in v2.0.0

### Foundations
- 4 color namespaces (`bg` / `text` / `stroke` / `icon`) + `overlay.scrim`
- 13 typography tokens on Noto Sans (Google Fonts)
- Spacing on 4px grid (11 semantic tokens)
- 7 radius tokens · 2 stroke widths · 5 shadow tokens · 4 icon sizes

### Components

| Component | Purpose |
|---|---|
| `WiomButton` + `WiomAcknowledge` | Primary / Secondary / Tertiary / Pre-booking / Destructive CTAs |
| `WiomBadge` (Dot / Count / Label) | Passive status indicator |
| `WiomIconBadge` | Filled icon container — 3 sizes × 6 tones |
| `WiomSelectionControl` (Checkbox / Radio / Switch) | Indicator-only; labels on `WiomListItem` |
| `WiomInput` / `WiomTextarea` | Form field — status axis independent of enabled/readOnly |
| `WiomListItem` | Unified list row — 5 Types (Default / IconWithBg / Checkbox / Radio / Switch) |
| `WiomTopBar` | Screen header — 3 sizes × 4 states |
| `WiomNavigationBar` | Bottom nav — 2–5 items, badge support |
| `WiomPillTabs` / `WiomUnderlineFilter` / `WiomChip` | 3-level filter system |
| `WiomPagination` (Dots / Bars / Counter / ScrollIndicator) | Position indicator |
| `WiomBottomSheet` | 8 sizes via enum + content helpers |
| `WiomDialog` | Alert / Input / Selection / Illustration / Loading |
| `WiomLoader` | Spinner / skeleton / full-screen |
| `WiomToast` | 5 statuses with status-family text rules |
| `WiomProgressIndicator` | Linear + circular × determinate + indeterminate |

### Deleted from older versions
- **`WiomDropdown`** — replaced by `WiomInput(readOnly=true)` + `Icons.Rounded.KeyboardArrowDown` + onClick → `WiomBottomSheet` picker

---

## Adopt this in your app

See [ADOPTION.md](./ADOPTION.md). 15 minutes to set up; new feature code is Wiom by default; existing screens stay untouched.

## Contributing / reporting issues

See [CONTRIBUTING.md](./CONTRIBUTING.md) and use the issue templates at https://github.com/abhishekgarg-wiom/wiom-ds-android/issues/new/choose.

## Rules for Claude Code

See [CLAUDE.md](./CLAUDE.md). Loaded automatically every session in this repo.

---

## Context

Wiom serves Indian households. Design assumptions baked in:

- India-only userbase (no country-code prefixes in phone fields)
- INR (`₹`) only
- Hindi + English (budget 1.3× text expansion)
- Budget Android, min SDK 24, outdoor-readable
- **Partner app** (active Flutter → Kotlin rewrite, daily new code) and **Customer app** (live Kotlin) are the consumers

## License

Apache License 2.0. See [LICENSE](./LICENSE).
