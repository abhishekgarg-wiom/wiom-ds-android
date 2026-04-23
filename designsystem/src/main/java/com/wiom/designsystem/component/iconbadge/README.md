# WiomIconBadge

A single Material Rounded glyph inside a filled, always-circular container.
Leading affordance for list rows, empty-state heros, settings entries, status
summaries, inline information hits.

Source of truth: `.skills-cache/wiom-icon-badge.md`. Distinct from
[`WiomBadge`](../badge/README.md) — Badge is a count / dot / label chip; Icon
Badge is a single glyph in a colored circle (no text).

---

## Sizes

| Size  | Container | Glyph             | Padding          |
| ----- | --------- | ----------------- | ---------------- |
| `Sm`  | 24 dp     | `iconSize.xs` (16) | `spacing.xs` (4) |
| `Md`  | 48 dp     | `iconSize.md` (24) | `spacing.md` (12) |
| `Lg`  | 96 dp     | `iconSize.lg` (48) | `spacing.xl` (24) |

`Sm` is NOT a touch target — do not put the badge itself behind a tap. It
inherits the parent row's hit area.

## Tones

| Tone       | Container            | Glyph tint        |
| ---------- | -------------------- | ----------------- |
| `Neutral`  | `bg.subtle`          | `icon.nonAction`  |
| `Brand`    | `bg.brandSubtle`     | `icon.brand`      |
| `Positive` | `bg.positiveSubtle`  | `icon.positive`   |
| `Warning`  | `bg.warningSubtle`   | `icon.warning`    |
| `Critical` | `bg.criticalSubtle`  | `icon.critical`   |
| `Info`     | `bg.infoSubtle`      | `icon.info`       |

Tones are paired — a brand bg with a positive glyph is invalid.
`icon.warning` is only legible on a warning-tinted surface; that's why the
badge container uses `bg.warningSubtle`.

## API

```kotlin
@Composable
fun WiomIconBadge(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: WiomIconBadgeSize = WiomIconBadgeSize.Md,
    tone: WiomIconBadgeTone = WiomIconBadgeTone.Neutral,
    contentDescription: String? = null,
)
```

## Wiom use cases

- **List rows** — leading slot of a `WiomListItem` at `Sm` (compact) or `Md`
  (comfortable). Settings entries, account sections, service listings.
- **Empty states** — `Lg` hero when an illustration is overkill ("No transactions
  yet" with a wallet glyph).
- **Status summary cards** — `Md` with `Positive` + check glyph ("Plan active")
  or `Warning` + alert glyph ("Action required").
- **Inline toast / chat decorations** — `Sm` with a tone-appropriate glyph.

## Rules

1. Always full-round. A rounded-square container is a different component — flag it.
2. Always filled. No border, no shadow.
3. Single glyph only — no text, no count overlay, no multi-icon composition.
4. Display-only. Tap belongs to the parent. `Sm` is too small to be a tap target.
5. Size comes from the enum — do NOT pass `.size(...)` / `.width(...)` / `.height(...)`.
6. Don't mix tones. Container bg + glyph tint are a pair.
7. Icons from `Icons.Rounded.*` (or `painterResource(R.drawable.ic_wiom_*)`) only.

## Tokens

- Container bg: `bg.subtle` · `bg.brandSubtle` · `bg.positiveSubtle` · `bg.warningSubtle` · `bg.criticalSubtle` · `bg.infoSubtle`
- Glyph tint: `icon.nonAction` · `icon.brand` · `icon.positive` · `icon.warning` · `icon.critical` · `icon.info`
- Radius: circle (`radius.full`)
- Padding: `spacing.xs` (Sm) · `spacing.md` (Md) · `spacing.xl` (Lg)
- Shadow: none
