# WiomPillTabs · WiomUnderlineFilter · WiomChip

Three components, three semantics. Pick by **what kind of filtering** the user is doing.

## Decision tree

```
Does this change the WHOLE dataset (different source, different shape)?
    → WiomPillTabs          (max 4, context switch)

Is this single-select narrowing within the SAME dataset (status/state/category)?
    → WiomUnderlineFilter   (3–6 options, max ~8)

Can MULTIPLE filters be active at once (additive)?
    → WiomChip              (wrap in WiomChipRow)
```

## WiomPillTabs

```kotlin
WiomPillTabs(
    tabs = listOf("मेरे टिकट", "टीम के टिकट"),
    selectedIndex = 0,
    onTabSelect = { selected = it },
)
```

- 2–4 options. Active pill: white fill + `shadow.sm`. Inactive: `text.secondary`.
- Use for "mine vs team", "today / week / month", "customer / partner".

## WiomUnderlineFilter

```kotlin
WiomUnderlineFilter(
    filters = listOf("सभी", "पेंडिंग", "बंद", "एक्सपायर्ड"),
    selectedIndex = 1,
    onFilterSelect = { selected = it },
    scrollable = false,    // true for 5–6 items
)
```

- Active: 2dp brand underline + brand label. Inactive: `text.secondary`.
- Use for status/state filtering where only one can be active.
- For 7+ filters, use a trailing More button (build separately — opens `WiomBottomSheet` with radios).

## WiomChip + WiomChipRow

```kotlin
WiomChipRow {
    WiomChip(label = "इंस्टॉलेशन", selected = type == "install", onClick = { toggle("install") })
    WiomChip(label = "रिपेयर",    selected = type == "repair",  onClick = { toggle("repair") })
}
```

- Selected = `surface.selected` fill + `brand.primary` text, no border.
- Unselected = `border.subtle` outline + `text.primary`, no fill.
- Multi-select is the whole point.

## Valid combinations

| Combination | When |
|---|---|
| Pill + Underline | Two-level split — mode + status. Pill above, Underline below. |
| Pill + Chips | Rare. Mode switch + attribute filter. |
| Underline + Chips | Rare. Status + type filter. |

**Never** stack all three on one screen. **Never** put Pill below Underline.

## Rules

1. **Max 2 filter levels per screen.** Third level = section headers in content.
2. **Pill above Underline — always.** Broader before narrower.
3. **Selected chip has no border.** Fill IS the boundary (foundation rule).
4. **Pill max 4 options.** Beyond that, rethink IA.
5. **Underline max ~8.** Beyond that, restructure.
6. **All flat** — `shadow.none` except pill active (`shadow.sm`).

## Tokens

| Part | Token |
|---|---|
| Pill track | `surface.muted` · `radius.medium` (12dp) · `space.xs` padding |
| Active pill | `surface.base` · `radius.small` (8dp) · `shadow.sm` |
| Pill text (active) | `type.labelMd` · `brand.primary` |
| Pill text (inactive) | `type.bodyMd` · `text.secondary` |
| Underline indicator | `stroke.medium` (2dp) · `radius.full` · `brand.primary` |
| Underline bottom border | `stroke.small` · `border.default` |
| Chip radius | `radius.small` (8dp) |
| Chip selected bg | `surface.selected` |
| Chip unselected border | `stroke.small` · `border.subtle` |
| Chip padding | `space.md` H · `space.sm` V |
| Chip row gap | `space.sm` |
