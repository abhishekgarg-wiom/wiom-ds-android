# MIGRATION — v0.3.x → v1.0.0

**If you're starting fresh, skip this file — read [ADOPTION.md](./ADOPTION.md) instead.**

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

1. Update version: `com.github.abhishekgarg-wiom.wiom-ds-android:designsystem:v1.0.0`
2. Run build. Every `WiomTheme.colors.*` call site becomes a compile error — mechanically rewrite to `WiomTheme.color.<namespace>.<token>`.
3. Every `WiomIcons.X` becomes a compile error — replace with `Icons.Rounded.X` import + direct reference.
4. Every `WiomDropdown(...)` call site becomes a compile error — replace with the `WiomInput(readOnly)` + bottom-sheet pattern above.
5. Visually diff each migrated screen against the corresponding Figma — flag any regressions as issues on this repo.

## New rule for new code

In v1.0.0 we ship the **Adoption Kit**. New feature code uses Wiom by default; Detekt rules in CI enforce it. See [ADOPTION.md](./ADOPTION.md) for the workflow and opt-out mechanics.
