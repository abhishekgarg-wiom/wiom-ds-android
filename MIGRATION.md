# Migrating Your App to the Wiom Design System

A guide for Android developers on the Wiom team to adopt this design system in existing apps.

**Target audience:** Developers maintaining Wiom customer / partner apps. Familiar with Gradle, Jetpack Compose, and Material3.

**Goal:** Replace hardcoded UI (raw hex, dp values, custom composables) with `Wiom*` components from this library, so every app looks identical, responds to design changes via version bumps, and stops maintaining UI primitives locally.

---

## Table of contents

1. [Install the library](#1-install-the-library)
2. [Wrap your app in `WiomTheme`](#2-wrap-your-app-in-wiomtheme)
3. [Migration playbook — screen by screen](#3-migration-playbook--screen-by-screen)
4. [Mapping table — what to replace with what](#4-mapping-table--what-to-replace-with-what)
5. [Common gotchas](#5-common-gotchas)
6. [Upgrading versions](#6-upgrading-versions)
7. [Requesting changes / reporting bugs](#7-requesting-changes--reporting-bugs)

---

## 1. Install the library

The library is hosted on [JitPack](https://jitpack.io) — it builds directly from GitHub, no internal Maven server needed.

### Step 1 — add JitPack to `settings.gradle.kts` (root)

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }   // ← add this line
    }
}
```

### Step 2 — add the dependency in your `app/build.gradle.kts`

```kotlin
dependencies {
    implementation("com.github.abhishekgarg-wiom.wiom-ds-android:designsystem:v0.1.1")
}
```

> **Coordinate format (important):** `com.github.<user>.<repo>:<module>:<tag>`. The `.wiom-ds-android` (repo name) sits after the dot — not after the colon — because it's a multi-module project on JitPack. The `:designsystem` is the library module we publish; `:sample` exists in the repo but is not published.

### Step 3 — sync Gradle

Run Gradle sync in Android Studio or `./gradlew :app:dependencies`. The first sync takes ~2 minutes because JitPack builds the library on-demand. Subsequent syncs are cached.

### Step 4 — verify

Add this anywhere under `WiomTheme { }` and run your app:

```kotlin
WiomBadgeLabel(
    text = "Installed!",
    color = WiomBadgeColor.Positive,
    style = WiomBadgeStyle.Filled,
)
```

If you see a green pill saying "Installed!", you're done with setup.

---

## 2. Wrap your app in `WiomTheme`

Find your `setContent { ... }` call (usually in `MainActivity`). Wrap it:

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WiomTheme {               // ← add this
                YourAppContent()
            }
        }
    }
}
```

After this, anywhere inside `YourAppContent()` you can use:

- `WiomTheme.colors.brand.primary`, `WiomTheme.colors.text.primary`, etc.
- `WiomTheme.type.headingLg`, `WiomTheme.type.bodyMd`, etc.
- `WiomTheme.spacing.lg` (16dp), `WiomTheme.radius.medium` (12dp), etc.
- All `Wiom*` components.

Material3 components (`Button`, `OutlinedTextField`, etc.) also start picking up approximate Wiom colors thanks to the `MaterialTheme` bridge inside `WiomTheme`. You can migrate incrementally — Material and Wiom components coexist.

---

## 3. Migration playbook — screen by screen

**Don't big-bang migrate.** Go screen by screen, starting with the highest-traffic / simplest ones. Each migration is a separate PR so it's easy to review and rollback.

### Recommended order

1. **Home / dashboard screen** — touches most components (TopBar, NavigationBar, cards, CTA). Migrate this first; everyone sees the biggest visual change.
2. **Form screens** — onboarding, KYC, recharge. High value: `WiomInput` replaces custom TextField + validation logic.
3. **List screens** — bills, transactions, plans. `WiomListItem` replaces custom row composables.
4. **Settings / profile** — `WiomSwitch`, `WiomListItem` with trailing meta.
5. **Modals / sheets** — `WiomBottomSheet` replaces custom bottom sheets.
6. **Navigation** — `WiomTopBar` + `WiomNavigationBar` once everything else is in.

### Per-screen checklist

- [ ] Identify every UI primitive on the screen (button, input, chip, etc.)
- [ ] Map each to a `Wiom*` component (see [Mapping table](#4-mapping-table--what-to-replace-with-what))
- [ ] Replace hardcoded values (`Color(0xFFD9008D)`, `14.sp`, `16.dp`) with `WiomTheme.colors.*`, `WiomTheme.type.*`, `WiomTheme.spacing.*`
- [ ] Delete now-unused local composables
- [ ] Visually diff against the current screenshot — flag any discrepancies before merging
- [ ] Test with font scaling (Settings → Accessibility → Font size → Largest) — no fixed heights should overflow
- [ ] Test with Hindi content — Devanagari wraps differently

### Per-screen PR template

```
## Migrated: <Screen Name>

### Changes
- Replaced <custom component> with WiomXxx
- Removed hardcoded tokens in favor of WiomTheme.*
- Deleted <local composable files that are now unused>

### Visual diff
[Attach before/after screenshots — default font + Largest font]

### Tested
- [ ] Default font size
- [ ] Largest font size (accessibility)
- [ ] English content
- [ ] Hindi content
- [ ] Error / loading / empty states still work
```

---

## 4. Mapping table — what to replace with what

Typical hardcoded patterns you'll find in an existing Wiom app and their Wiom DS replacement.

### Buttons / CTAs

| You have | Replace with |
|---|---|
| Custom `Button` composable or `Material3 Button` with brand hex | **`WiomCta` — coming in v0.2.0.** Until then, keep your current button but replace its color with `WiomTheme.colors.brand.primary` and typography with `WiomTheme.type.labelLg`. |

### Inputs

| You have | Replace with |
|---|---|
| `OutlinedTextField` | `WiomInput` |
| `OutlinedTextField` with `+91 ` prefix in a Text | `WiomInput(leadingIcon = { WiomIcon(WiomIcons.phone, ...) })` — **drop the `+91`**, we're India-only |
| Password field with visibility eye | `WiomInput(visualTransformation = ..., trailingIcon = { WiomIcon(WiomIcons.visibility / visibilityOff, ...) })` |
| OTP 6-box custom field | `WiomInput(title = "Enter 6-digit OTP", counter = timerText)` |
| Search bar at top of list | `WiomInput(leadingIcon = { WiomIcon(WiomIcons.search, ...) }, placeholder = "Search...")` |
| Multi-line address | `WiomTextarea` |

### Selection controls

| You have | Replace with |
|---|---|
| `Checkbox` + manual Text label | `WiomCheckbox(checked, onCheckedChange, label)` |
| `RadioButton` + Text in a Row | `WiomRadio(selected, onClick, label)` |
| `Switch` with separate label | `WiomSwitch(checked, onCheckedChange, label)` |

### Lists

| You have | Replace with |
|---|---|
| Custom `Row { Icon + Column { Text + Text } + Icon }` | `WiomListItem(primary, secondary, leadingIcon = { ... }, trailingIcon = { ... }, onClick)` |
| Row with trailing meta ("English") and chevron | `WiomListItem(primary = "Language", trailingMeta = "English", onClick)` |

### Headers / nav

| You have | Replace with |
|---|---|
| `TopAppBar` from Material3 | `WiomTopBar(title, leading = { ... }, actions = { ... })` |
| `NavigationBar` (Material3) | `WiomNavigationBar(items, selectedIndex, onSelect)` |

### Filters / tabs

| You have | Replace with |
|---|---|
| `TabRow` for mode switch ("My tickets / Team tickets") | `WiomPillTabs(tabs, selectedIndex, onTabSelect)` |
| `TabRow` for status filter | `WiomUnderlineFilter(filters, selectedIndex, onFilterSelect)` |
| `FilterChip` from Material3 (multi-select) | `WiomChip` inside `WiomChipRow` |

### Status indicators

| You have | Replace with |
|---|---|
| Badge with count on bell icon | `Box { Icon(...); WiomBadgeCount(count = n, modifier = Modifier.align(TopEnd)) }` |
| Red dot "new" indicator | `WiomBadgeDot(color = WiomBadgeColor.Negative)` |
| Status pill ("Confirmed", "Failed", "Pending") | `WiomBadgeLabel(text, color, style)` |

### Sheets / overlays

| You have | Replace with |
|---|---|
| Custom bottom sheet | `WiomBottomSheet { ... content with WiomBottomSheetHeader + WiomBottomSheetListItem }` |
| Confirmation dialog | `WiomBottomSheet` with `WiomBottomSheetIllustration` (illustration layout) |

### Hardcoded values (search-and-replace priorities)

Search your codebase for these patterns; each is a migration opportunity.

| Pattern | Replacement |
|---|---|
| `Color(0xFFD9008D)` or any raw hex | `WiomTheme.colors.brand.primary` (or whichever semantic token matches) |
| `14.sp` / `16.sp` in `fontSize` | `WiomTheme.type.bodyMd` / `WiomTheme.type.bodyLg` (whole text style) |
| `16.dp` / `12.dp` / `24.dp` in `padding` | `WiomTheme.spacing.lg` / `.md` / `.xl` |
| `RoundedCornerShape(12.dp)` | `RoundedCornerShape(WiomTheme.radius.medium)` |
| `Modifier.border(1.dp, Color(...))` | `Modifier.border(WiomTheme.stroke.small, WiomTheme.colors.border.default)` |
| `Modifier.size(48.dp)` on a Row/Column with text | **Delete it.** Wiom components hug content — fixed sizes break font scaling. |

---

## 5. Common gotchas

### "My custom Button still looks different"

Until `WiomCta` ships (v0.2.0), keep using your current button but swap its internals to Wiom tokens:

```kotlin
@Composable
fun LegacyButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(WiomTheme.radius.large),
        colors = ButtonDefaults.buttonColors(
            containerColor = WiomTheme.colors.brand.primary,
            contentColor = WiomTheme.colors.text.onColor,
        ),
    ) {
        Text(text, style = WiomTheme.type.labelLg)
    }
}
```

### "`Modifier.height(48.dp)` on my button is clipping text when font size is Large"

Remove the fixed height. Use `Modifier.padding(vertical = WiomTheme.spacing.md)` instead. Height emerges from line-height + padding. This is a **hard rule** — see [CLAUDE.md](./CLAUDE.md) rule #2.

### "I need a status color that isn't in the Wiom palette"

You don't. If the status doesn't fit Positive / Negative / Warning / Info, it's probably wrong. Open an issue against this repo before inventing a new color — we'd rather extend the palette in a controlled way than let one-off colors drift in.

### "The icon I need isn't in `WiomIcons`"

Two options:

1. **Preferred:** Ask us to add it (or PR it yourself). Download the SVG from [fonts.google.com/icons](https://fonts.google.com/icons?icon.style=Rounded) (filled=1, weight=400), convert to Android Vector Drawable, add to `designsystem/src/main/res/drawable/wiom_ic_<name>.xml`, add a line to `WiomIcons.kt`.
2. **If the icon is genuinely product-specific** (your CSP partner logo, a custom illustration): ship it as a local drawable in your app and pass it directly to any component slot that accepts an icon painter. Don't add product-specific icons to the design system.

### "The menu bar looks off-position on my device"

`WiomNavigationBar` has intrinsic bottom padding (`space.md`). Add OS-level safe-area padding on top of that so the bar doesn't collide with gesture bars / home indicators. In your `Scaffold` or custom layout:

```kotlin
Modifier.windowInsetsPadding(WindowInsets.navigationBars)
```

### "`WiomInput` doesn't accept my custom trailing icon that clears the field"

It does — pass a `@Composable` to `trailingIcon`:

```kotlin
WiomInput(
    value = query,
    onValueChange = { query = it },
    trailingIcon = if (query.isNotEmpty()) {
        {
            WiomIcon(
                id = WiomIcons.cancel,
                contentDescription = "Clear",
                modifier = Modifier.clickable { query = "" },
            )
        }
    } else null,
)
```

If `status` is set to `Error`/`Success`/`Warning`, the status icon **overrides** your trailing icon — that's intentional.

### "I'm getting weird line-ending warnings on Windows"

Harmless — Git converts LF/CRLF automatically. Ignore.

---

## 6. Upgrading versions

When a new version ships (say v0.2.0), update your `build.gradle.kts`:

```kotlin
implementation("com.github.abhishekgarg-wiom.wiom-ds-android:designsystem:v0.2.0")
```

Then:

1. Read the [CHANGELOG.md](./CHANGELOG.md) for breaking changes.
2. Sync Gradle. Fix any compile errors flagged by Kotlin.
3. Run the app, exercise the migrated screens, verify nothing regressed.
4. If something looks different, check if the design intentionally changed (see changelog) or it's a bug (report it).

**Semver:** `major.minor.patch`.
- **Major** (breaking API change) — update with care, follow changelog migration notes.
- **Minor** (new component / new optional param) — should be safe.
- **Patch** (bug fix / visual tweak) — always safe.

Pre-`v1.0.0`, minor bumps may include breaking changes — always check the changelog.

---

## 7. Requesting changes / reporting bugs

- **Bug in a component** (it renders wrong, crashes, typed params don't compile): open an issue at https://github.com/abhishekgarg-wiom/wiom-ds-android/issues with a minimal repro.
- **New component request:** open a GitHub discussion or message the design team. New components come from skills first (authored by design), then translated to code.
- **New token / color / size:** same — talk to design. Don't add it locally as a workaround; let's extend the system.
- **Design seems visually different from the Figma** on a specific component: open an issue with the Figma node link and a screenshot of the code rendering.

The design system evolves with the product. Your feedback after migrating real screens is the only way we find the rough edges.

---

## Quick reference — one-liner for every component

```kotlin
// Badges
WiomBadgeDot(color = WiomBadgeColor.Negative)
WiomBadgeCount(count = 5)
WiomBadgeLabel("Pending", color = WiomBadgeColor.Warning)

// Selection
WiomCheckbox(checked, onCheckedChange, label, helper?, isError?, errorText?)
WiomRadio(selected, onClick, label, helper?, isError?)
WiomSwitch(checked, onCheckedChange, label, helper?)

// Input
WiomInput(value, onValueChange, title?, placeholder?, leadingIcon?, trailingIcon?, prefix?, suffix?, helper?, counter?, status?, readOnly?, enabled?)
WiomTextarea(value, onValueChange, title?, placeholder?, helper?, counter?, minLines?, status?)

// Lists
WiomListItem(primary, secondary?, leadingIcon?, trailingIcon?, trailingMeta?, selected?, onClick?)

// Nav
WiomTopBar(title, size?, subtitle?, centered?, scrolled?, leading?, actions?)
WiomNavigationBar(items, selectedIndex, onSelect)

// Dropdown
WiomDropdown(value, options, onValueChange, label?, placeholder?, helper?, isError?, enabled?, leadingIcon?)

// Tabs / filters
WiomPillTabs(tabs, selectedIndex, onTabSelect)
WiomUnderlineFilter(filters, selectedIndex, onFilterSelect, scrollable?)
WiomChipRow { WiomChip(label, selected, onClick) … }

// Pagination
WiomPaginationDots(total, current, style?)
WiomPaginationBars(total, current, counterLabel?)
WiomPaginationCounter(current, total, onPrev, onNext)
WiomPaginationScrollIndicator(progress, visibleFraction?)

// Sheet
WiomBottomSheet(onDismissRequest) {
    WiomBottomSheetHeader(title, subtitle?)
    WiomBottomSheetListItem(label, description?, icon?, onClick)   // or
    WiomBottomSheetIllustration(icon, heading, subtext)
    WiomBottomSheetActions { /* buttons */ }
}
```

---

Good luck. The point of this library is to stop each app maintaining its own UI — ship once, use everywhere, update with a version bump. Lean on it hard.
