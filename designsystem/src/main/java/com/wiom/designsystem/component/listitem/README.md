# WiomListItem

Single unified row component for every list pattern: navigation, settings, selection, toggle, info display.

## API

```kotlin
WiomListItem(
    primary = "Account",
    secondary = "Personal details, security",      // optional
    leadingIcon = { WiomIcon(WiomIcons.phone, null, tint = WiomTheme.colors.text.secondary) },
    trailingMeta = null,                            // optional, e.g. "English"
    trailingIcon = { WiomIcon(WiomIcons.expandMore, null, size = 20.dp) }, // default chevron
    selected = false,                               // true shows brand dot + surface.selected bg
    enabled = true,
    onClick = { /* navigate */ },
)
```

Set `trailingIcon = null` to hide the chevron (use for terminal/info rows).

## Variants (composed by passing different leading/trailing slots)

| Pattern | How |
|---|---|
| **Navigation row** | Default — icon leading, chevron trailing |
| **Settings row** | Swap leading to category icon (payments, bolt, etc.) |
| **Info row** | `trailingIcon = null`, use `trailingMeta` for the value |
| **Multi-select** | Pass `WiomCheckbox` as `leadingIcon`, `trailingIcon = null` |
| **Single-select (dropdown row)** | `trailingIcon = null`, set `selected = true` on the active row |
| **Toggle setting** | Pass category icon as `leadingIcon`, `WiomSwitch` as `trailingIcon` |

## Wiom use cases

- **Settings list.** Account, Notifications, Language (with `trailingMeta = "English"`), Privacy.
- **Status list.** Storage, Plan (with `trailingMeta`), Build version (no chevron).
- **Profile row.** Avatar as leading, name as primary, phone as secondary.
- **Language picker row** (inside `WiomDropdown` / `WiomBottomSheet`). `selected = true` on the chosen row.

## Rules

1. **Min 48dp height** — never below. WCAG touch target.
2. **Padding is fixed** — 16h × 12v. Don't override.
3. **Max 2 text lines** (primary + secondary). 3+ means you need a Card, not a list row.
4. **One row = one tap target.** The whole row is clickable when `onClick != null`.
5. **Dividers are inset** when stacked in a card — 16dp from both edges. Never full-bleed inside cards.
6. **`selected` is for dropdown/picker rows.** Shows `surface.selected` background + 8dp brand dot. Don't use as a persistent "active" state.

## Tokens

- Padding: `space.lg` horizontal, `space.md` vertical
- Leading container: 40dp (icon 24dp inside)
- Primary: `type.bodyLg` · `text.primary`
- Secondary: `type.bodySm` · `text.secondary` (primary → secondary gap: `space.xs`)
- Trailing meta: `type.bodySm` · `text.secondary`
- Selected bg: `surface.selected` (#FFE5F6)
- Selected dot: 8dp · `brand.primary` · `CircleShape`
- Min height: 48dp
