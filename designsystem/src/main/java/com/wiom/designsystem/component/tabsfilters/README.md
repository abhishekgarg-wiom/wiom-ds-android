# WiomTabsFilters

Three components for the Wiom tabs / filters / chips hierarchy. One file, three distinct semantics.

| Component | Semantic | Select |
|---|---|---|
| `WiomPillTabs` | Context switch — whose data / which mode | Single |
| `WiomUnderlineFilter` | Single-select filter inside the same dataset | Single |
| `WiomChip` + `WiomChipRow` | Multi-select attribute toggle | Multi |

## Decision tree

```
1. Does selecting change the ENTIRE dataset (different source/structure)?
   YES → WiomPillTabs
   NO  → continue

2. Only ONE option active at a time (status / state / single category)?
   YES → WiomUnderlineFilter
   NO  → continue

3. MULTIPLE options active at once (combining attributes)?
   YES → WiomChip + WiomChipRow
   NO  → rethink
```

Never use Pill for status filtering. Never use Underline for multi-select. Never use Chips for mutually exclusive choices.

## API

### PillTabs — context switch

```kotlin
WiomPillTabs(
    tabs = listOf("मेरे टिकट", "टीम के टिकट"),
    selectedIndex = 0,
    onTabSelect = { idx -> ... },
)
```

- 2–4 tabs max. Beyond 4 = rethink IA.
- Track: `bg.muted` · `radius.medium` · `space.xs` inner padding.
- Active pill: `bg.default` + `shadow.sm` · `radius.small`.
- Active label: `type.labelMd` · `text.brand`. Inactive: `type.bodyMd` · `text.subtle`.

### UnderlineFilter — single-select

```kotlin
WiomUnderlineFilter(
    filters = listOf("सभी", "पेंडिंग", "बंद", "एक्सपायर्ड"),
    selectedIndex = 0,
    onFilterSelect = { idx -> ... },
    scrollable = false, // true for 5+ filters
)
```

- 3–4 filters → `scrollable = false`, filters fill equally.
- 5+ filters → `scrollable = true`, filters hug text and scroll horizontally.
- Active underline: 2dp (`stroke.medium`) · `bg.brand` (via `stroke.brandFocus`). Round caps.
- Bottom border: `stroke.small` + `stroke.subtle`.
- Min item height: 48dp via `defaultMinSize` (touch target) — grows with font scale.
- Active label: `type.labelMd` · `text.brand`. Inactive: `type.bodyMd` · `text.subtle`.

### Chip — multi-select

```kotlin
WiomChipRow {
    WiomChip(label = "Installation", selected = true,  onClick = { ... })
    WiomChip(label = "Repair",       selected = true,  onClick = { ... })
    WiomChip(label = "Upgrade",      selected = false, onClick = { ... })
}
```

- Selected: `bg.selected` + `text.selected` + `type.labelMd` — **no border** (filled-state rule).
- Unselected: 1dp `stroke.subtle` border + `text.default` + `type.bodyMd`.
- Padding: `space.md` H × `space.sm` V. Radius: `radius.small`.
- Row gap: `space.sm` (8dp).

## Wiom use cases

- **Partner app tickets screen:** `WiomPillTabs` (My vs Team) above `WiomUnderlineFilter` (All / Pending / Closed). Pill always above Underline.
- **Customer app plans filter:** `WiomChipRow` of connection types (WiFi / Fiber / 5G) — multi-select.
- **Payment history:** `WiomUnderlineFilter` (scrollable) for status categories.

## Rules

1. Max 2 filter levels per screen. 3-level → use section headers for the 3rd.
2. Pill is always above Underline — never reverse.
3. Never put two Pills or two Underlines on the same screen.
4. Don't add a Pill "just to have one" — skip it when there's only one dataset.
5. All components are flat — no shadow — except the active Pill (`shadow.sm`).
6. Chips never wear a border when selected. The fill is the boundary.
7. Heights are intrinsic — no fixed `height(...)` on text-bearing rows.

## Tokens

| Part | Token |
|---|---|
| Pill track fill | `color.bg.muted` |
| Pill track radius | `radius.medium` |
| Pill active fill | `color.bg.default` |
| Pill active shadow | `shadow.sm` |
| Pill radius | `radius.small` |
| Pill padding | `space.md` H · `space.sm` V |
| Pill active label | `type.labelMd` · `color.text.brand` |
| Pill inactive label | `type.bodyMd` · `color.text.subtle` |
| Underline active indicator | `stroke.medium` · `color.stroke.brandFocus` (= `bg.brand`) |
| Underline bottom border | `stroke.small` · `color.stroke.subtle` |
| Underline active label | `type.labelMd` · `color.text.brand` |
| Underline inactive label | `type.bodyMd` · `color.text.subtle` |
| Underline item min-height | 48dp (`space.huge`) |
| Chip selected fill | `color.bg.selected` |
| Chip selected text | `color.text.selected` · `type.labelMd` |
| Chip unselected border | `stroke.small` · `color.stroke.subtle` |
| Chip unselected text | `color.text.default` · `type.bodyMd` |
| Chip padding | `space.md` H · `space.sm` V |
| Chip radius | `radius.small` |
| Chip row gap | `space.sm` |
