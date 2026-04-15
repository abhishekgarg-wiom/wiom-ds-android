# WiomDropdown

Single-select picker. Closed field + expanded menu. Menu rows are `WiomListItem` instances — the selected row shows a brand dot + `surface.selected` background.

## When to use

- Single-select from 4+ options (Language, Plan, Sort by, State, Country)
- Inline filter rows ("Sort by: Most recent ▾")

## When NOT to use

- 2–3 mutually exclusive options → `WiomRadio` or tabs
- Binary toggle → `WiomSwitch`
- Free-text input → `WiomInput`
- Primary CTA → a button (coming)

## API

```kotlin
val languages = listOf(
    WiomDropdownOption("en", "English"),
    WiomDropdownOption("hi", "हिन्दी"),
    WiomDropdownOption("te", "తెలుగు"),
)

var selected by remember { mutableStateOf<String?>(null) }

WiomDropdown(
    value = selected,
    options = languages,
    onValueChange = { selected = it },
    label = "Language",
    helper = "Used across the app",
    isError = false,
)
```

## Wiom use cases

- **Language picker.** English / हिन्दी / తెలుగు / தமிழ்.
- **Plan sort.** "Most popular", "Price: Low→High", "Newest".
- **State/city.** When user must pick from a long list (dropdown, not radio).
- **Filter.** Inline under a list ("Filter: All bills ▾").

## Rules

1. **Menu rows reuse `WiomListItem`.** The `selected` flag on the chosen row gives the brand dot + `surface.selected` bg.
2. **Field visual mirrors `WiomInput`.** Same container shape, stroke, radius, padding.
3. **Expanded border = brand.** 2dp `brand.primary` when open — matches error state width, different color.
4. **No shadow + border together.** Menu uses shadow via Material3's `DropdownMenu`; the field uses border. They're separate surfaces.
5. **Chevron rotates** 180° when open.

## Tokens

- Field padding: `space.lg` H × `space.md` V
- Field radius: `radius.medium` (12dp)
- Field fill (rest): `surface.base` → disabled: `surface.subtle`
- Border (rest): `stroke.small` + `border.default`
- Border (expanded): `stroke.medium` + `brand.primary`
- Border (error): `stroke.medium` + `negative.primary`
- Chevron: 20dp · color matches state
- Value: `type.bodyLg` · `text.primary` (selected) / `text.disabled` (placeholder)
- Label: `type.labelMd` · `text.primary`
- Helper: `type.bodySm` · `text.secondary` / `negative.primary`
