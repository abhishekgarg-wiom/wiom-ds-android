# MIGRATION

**If you're starting fresh, skip this file — read [ADOPTION.md](./ADOPTION.md) instead.**

Two upgrade paths are documented here:

- [v1.0.x → v2.0.0](#v10x--v200) — most consumers
- [v0.3.x → v1.0.0](#v03x--v100) — historical, for completeness

---

## v1.0.x → v2.0.0

The library was rebuilt against the V2 skill set in `wiom-design-system` (locked SHA `ed110b6`). 15 components re-derived from V2 ship values; many V1 drifts corrected. This section is a punch list of the breaking changes consumers will hit.

### 1. Bump dependency

```kotlin
// app/build.gradle.kts
dependencies {
    implementation("com.github.abhishekgarg-wiom.wiom-ds-android:designsystem:v2.0.0")
    detektPlugins("com.github.abhishekgarg-wiom.wiom-ds-android:designsystem-rules:v2.0.0")
}
```

### 2. Rename / API changes (compile errors you'll hit)

| Symbol | v1.0.x | v2.0.0 |
|---|---|---|
| Loader | `WiomBlockingLoader` / `WiomBlockingLoaderStyle` | `WiomBrandLoader` / `WiomBrandLoaderStyle` |
| ListItem (icon-bg variant) | `WiomListItemIconBadge(leadingBadge = { … })` | `WiomListItemIconBg(leadingIcon = …, leadingTone = …)` (instantiates `WiomIconBadge` internally) |
| ListItem (selection) | `WiomListItem*(enabled: Boolean)` | `WiomListItem*(state: WiomListItemState)` enum (`Default` / `Disabled`) |
| ListItem (Checkbox / Radio / Switch helpers) | took a slot `leadingCheckbox = { WiomCheckbox(…) }` | take `selection` / `radioSelected` / `checked` + their callbacks directly; helper instantiates the indicator |
| Toast | `WiomToastStatus.Neutral` | **dropped** — V2 has only `Critical · Warning · Info · Positive` |
| Toast container | `bg.{type}Subtle` per status | all 4 share `bg.default` (only the icon + Warning's body carry the status colour) |
| Toast typography | message `bodyMd` (14 sp) | message `bodyLg` (16 sp) |
| Toast radius | `radius.medium` (12) | `radius.small` (8) |
| Toast action colour | per-status | always `text.brand` |
| BottomSheet size | `WiomBottomSheetSize.IllustrationCta` | **dropped** — use `Illustration` + `WiomBottomSheetActions` slot |
| BottomSheet illustration | inline 120 dp circle | `WiomBottomSheetIllustration(icon, heading, subtext, tone, leftAligned)` — uses `WiomIconBadge.Lg` (96 dp) |
| Dialog | `dismissOnClickOutside = true` (default) | **`false` (default)** — scrim tap no longer dismisses |
| Dialog body text | `type.bodyLg` | `type.bodyMd` |
| Dialog illustration | 120 dp circle | 144 dp full-width rectangle with `radius.large` |
| Dialog selection rows | inline `SelectionRow` | `WiomListItemRadio` instances |
| Dialog buttons | stacked only | new `WiomDialogButtonsLayout { Stacked, SideBySide }` parameter |
| ListItem chevron tint | `icon.action` | **`icon.brand`** (chevron is the row's tap-affordance signal) |
| ListItem | "selected dot" 8 dp circle in trailing slot | **dropped** — Selected = `bg.selected` overlay only |
| TopBar | no fixed heights | heights locked: Small 64, Medium 112, Large 152 |
| TopBar | no `statusBarsPadding()` | bar consumes the OS status-bar inset |
| TopBar Search | display-only `Text` placeholder | wraps a real `BasicTextField` |
| TabsFilters | typography mixed | `labelMd` for **all states** |
| TabsFilters | underline indicator `stroke.brandFocus` | `bg.brand` (element-first; indicator is a fill, not a stroke) |
| Chip | Selected: no border | Selected: 1 dp brand border per Figma ship + trailing 16 dp close ✕ |
| Pagination | inactive tint `stroke.subtle` | `bg.muted` |
| Pagination Counter | hides chevron at bounds behind a `Spacer` | renders both chevrons; non-tappable side tints `icon.disabled` |

### 3. Removed components

- **`WiomStepper`** — no V2 upstream skill yet. Tracked for reinstatement once the skill exists. Workaround: compose with `WiomProgressMilestones` (new in v2.0.0) for status visuals, or `Column` + `WiomListItem` rows for per-step navigation.

### 4. New components you can adopt

| Component | Use for |
|---|---|
| `WiomOtp(value, onValueChange, length, helper, timer, status, enabled)` | Boxed verification-code field. Don't fake an OTP with a spaced-digits `WiomInput`. |
| `WiomProgressCompletion(title, value, state)` | Rich completion meter — KYC / referral / earnings cards. 4-state enum drives fill %, colour, and pill copy in lockstep. |
| `WiomProgressMilestones(title, subtitle, stages, currentStage, showLabels)` | 2..5-stage tracker — install / delivery / verification. |
| `WiomLinearProgress` (loader-sibling) | Short determinate waits (file upload). Distinct from `WiomProgressIndicator`'s linear, which is for completion meters. |
| `WiomDots` | Chat typing indicator. |
| `WiomTopBarStatusBar(isDarkVariant)` | Pairs the OS status-bar lightness with the top bar. Call once per screen after `enableEdgeToEdge`. |

### 5. Foundation token changes

- **New soft strokes**: `stroke.brand` / `stroke.positive` / `stroke.critical` / `stroke.info`. The previous `stroke.warningFocus` substitute is gone — use `stroke.warning` (gold soft stroke).
- **`spacing.xxs` (2 dp) dropped** — off the 4-dp grid per V2 spec. Use `spacing.xs` (4 dp) instead.
- `Neutral_700` hex corrected to `#5C5570` (`primitives.md` had `#665E75` upstream — fixed in `wiom-design-system#14`).

### 6. Step-by-step

1. Bump the dependency (above).
2. Run a build. Triage compile errors against the rename table — most are mechanical 1:1 swaps.
3. Re-run any UI screenshot tests. The two changes most likely to drop tests:
   - Toast surface colour (per-status tinted → uniform `bg.default`).
   - ListItem chevron tint (`icon.action` → `icon.brand` pink).
4. If you used `Modifier.alpha(0.38f)` to "disable" anything, replace with the appropriate `state: …Disabled` enum or `enabled = false` parameter — V2 enforces foundations Pattern A (token swap) over opacity.
5. If your screens render a `WiomTopBar`, add `WiomTopBarStatusBar(isDarkVariant)` and confirm `enableEdgeToEdge` is called in `Activity.onCreate`.

---

## v0.3.x → v1.0.0

This document covers the v0.3.x → v1.0.0 upgrade path for apps that were already on a pre-1.0 release. Based on internal signal, nobody is actually on v0.3.x in production — but this is here for completeness.

---

## The 3 breaking changes

### 1. Token API: role-first → element-first

**Before (v0.3.x):**
```kotlin
WiomTheme.colors.brand.primary       // role-first, plural "colors"
WiomTheme.colors.text.primary
WiomTheme.colors.surface.base
```

**After (v1.0.0):**
```kotlin
WiomTheme.color.bg.brand             // element-first, singular "color"
WiomTheme.color.text.default         // ".primary" is gone — use ".default"
WiomTheme.color.bg.default           // "surface" merged into "bg"
```

Four namespaces mapped 1:1 to Compose slots:
- `WiomTheme.color.bg.*` → `Modifier.background(...)`, `Surface(color=...)`
- `WiomTheme.color.text.*` → `Text(color=...)`
- `WiomTheme.color.stroke.*` → `Modifier.border(...)`, `BorderStroke(...)`
- `WiomTheme.color.icon.*` → `Icon(tint=...)`

### 2. Icons: hand-drawn drawables → Material 3 Rounded

**Before (v0.3.x):**
```kotlin
WiomIcon(WiomIcons.search, contentDescription = "Search")
```

**After (v1.0.0):**
```kotlin
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search

WiomIcon(Icons.Rounded.Search, contentDescription = "Search")
```

`WiomIcons` facade is **deleted**. Icons import directly from `Icons.Rounded.*`. The `WiomIcon` wrapper stays — same role, different parameter type (`ImageVector` instead of `@DrawableRes Int`).

### 3. `WiomDropdown` deleted

No stub, no alias. Replace every `WiomDropdown(...)` usage with:

```kotlin
WiomInput(
    value = selectedLabel,
    onValueChange = {},
    title = "Language",
    readOnly = true,
    trailingIcon = { WiomIcon(Icons.Rounded.KeyboardArrowDown, null) },
    modifier = Modifier.clickable { showPicker = true },
)

if (showPicker) {
    WiomBottomSheet(onDismissRequest = { showPicker = false }) {
        WiomBottomSheetHeader(title = "Language")
        options.forEach { option ->
            WiomListItem(
                primary = option.label,
                selected = option.value == selectedValue,
                onClick = { onSelect(option); showPicker = false },
            )
        }
    }
}
```

Same UX. Better accessibility. More flexibility.

---

## Component renames / merges

| v0.3.x | v1.0.0 |
|---|---|
| `WiomCheckbox` / `WiomRadio` / `WiomSwitch` (each with its own label param) | `WiomSelectionControl` (indicator-only). Move labels to `WiomListItem` via `WiomListItemCheckbox` / `WiomListItemRadio` / `WiomListItemSwitch`. |
| `WiomIcons.X` (Kotlin object with DrawableRes) | `Icons.Rounded.X` (ImageVector, imported directly) |
| `WiomDropdown(...)` | `WiomInput(readOnly=true)` + bottom-sheet picker |

## New components (opt-in)

- `WiomIconBadge` — filled circular icon container
- `WiomDialog` (Alert / Input / Selection / Illustration / Loading)
- `WiomLoader` (spinner / skeleton / shimmer / full-screen)
- `WiomToast` (5 statuses)
- `WiomProgressIndicator` (linear / circular × determinate / indeterminate)

## Typography

Defaults shifted:
- Default reading text is now `bodyLg` (16sp Regular) — was `bodyMd` (14sp)
- Default interactive text is now `labelLg` (16sp SemiBold) — was `labelMd` (14sp)
- Noto Sans resolves via Google Fonts Compose provider (runtime download via Play Services Fonts)

---

## Upgrade workflow

1. Update version: `com.github.abhishekgarg-wiom.wiom-ds-android:designsystem:v1.0.4`
2. Run build. Every `WiomTheme.colors.*` call site becomes a compile error — mechanically rewrite to `WiomTheme.color.<namespace>.<token>`.
3. Every `WiomIcons.X` becomes a compile error — replace with `Icons.Rounded.X` import + direct reference.
4. Every `WiomDropdown(...)` call site becomes a compile error — replace with the `WiomInput(readOnly)` + bottom-sheet pattern above.
5. Visually diff each migrated screen against the corresponding Figma — flag any regressions as issues on this repo.

## New rule for new code

In v1.0.0 we ship the **Adoption Kit**. New feature code uses Wiom by default; Detekt rules in CI enforce it. See [ADOPTION.md](./ADOPTION.md) for the workflow and opt-out mechanics.
