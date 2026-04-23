# WiomBadge

Passive status / indicator / count chips. Never tappable — if it needs a tap
target, it's a Chip, not a badge.

Source of truth: `.skills-cache/wiom-badge.md`. Distinct from
[`WiomIconBadge`](../iconbadge/README.md) — Icon Badge is a glyph-in-circle
affordance (no text). This file covers the three text/count badge variants:
Dot · Count · Label.

---

## Variants

### `WiomBadgeDot(tone)`
8 dp filled circle — unread / active indicator with no text or number.
Overlay-only — position on top of an icon or avatar with
`Modifier.align(Alignment.TopEnd)` inside a parent `Box`.

Tones: `Brand` · `Critical` · `Neutral`.

### `WiomBadgeCount(count, tone, cap = 9)`
Numeric badge. Hidden at `count <= 0`. Caps at `"$cap+"` (default `9+`).
Uses `type.metaXs` (10 sp) — one of the allowed sub-14sp exceptions.

Tones: `Brand` · `Critical`.

### `WiomBadgeLabel(text, tone, size, style)`
Text status chip. Two sizes, two styles.

| Size     | Height | Style constraint         | Typography  |
| -------- | ------ | ------------------------ | ----------- |
| Default  | 28 dp  | Filled OR Tinted          | `labelMd`   |
| Small    | 24 dp  | **Tinted only** (Filled falls back) | `labelSm`   |

Tones: `Positive` · `Warning` · `Critical` · `Info` · `Neutral` · `Brand`
(Brand is Tinted-only).

## Decision tree

```
Just "something new / unread" with NO number/text?         → WiomBadgeDot
Numeric count of notifications / unread items?              → WiomBadgeCount
Text status / state / role label?                           → WiomBadgeLabel
  Does the row still work if the badge is removed?
    YES  → Small (supplementary context)
    NO   → Default (badge IS the information)
  Is this state final / terminal?
    YES  → Filled (available on Default only)
    NO   → Tinted (transitional)
  What does the status represent?
    Success/confirmed/complete   → Positive
    Attention/pending/expiring   → Warning
    Failed/expired/revoked       → Critical
    Active/in-progress/info      → Info
    Default/proposed/inactive    → Neutral
    Featured/brand-flagged       → Brand (Tinted only)
```

## API

```kotlin
@Composable
fun WiomBadgeDot(tone: WiomBadgeDotTone, modifier: Modifier = Modifier)

@Composable
fun WiomBadgeCount(
    count: Int,
    tone: WiomBadgeCountTone,
    modifier: Modifier = Modifier,
    cap: Int = 9,
)

@Composable
fun WiomBadgeLabel(
    text: String,
    tone: WiomBadgeLabelTone,
    modifier: Modifier = Modifier,
    size: WiomBadgeLabelSize = WiomBadgeLabelSize.Default,
    style: WiomBadgeLabelStyle = WiomBadgeLabelStyle.Tinted,
)
```

## Wiom use cases

- **Dot** — red dot on the notification bell, brand dot on a newly available
  plan tile, neutral dot on draft rows.
- **Count** — unread count on the bell, tab count ("टिकट `5`").
- **Label Default Filled** — transaction outcome ("असफल"), slot state ("Confirmed"),
  device state ("Deployed").
- **Label Default Tinted** — provisional status ("प्रोसेसिंग"), pending state.
- **Label Small** — inline confirmations ("₹8,200 पक्का"), role tags ("Rajesh एडमिन"),
  tab secondary context.

## Rules

1. Badges are passive — never tappable.
2. No icons in badges — color + text is sufficient.
3. Small = Tinted only. Filled-small is silently downgraded to Tinted.
4. Hide count at 0. Never show "0".
5. Cap count overflow — "9+" or "99+", never raw large numbers.
6. One badge per row. Don't stack multiple badges on a line item.
7. Color matches status family. Don't mix (no green badge on a red card).
8. Dot is overlay-only — never inline.
9. Label text never wraps — single line.
10. No generic text ("Status", "State") — use specific Hindi/English copy.

## Tokens

- Dot fill: `bg.brand` · `bg.critical` · `bg.muted`
- Count fill: `bg.brand` / `bg.critical` · label `text.onBrand` / `text.onCritical`
- Count typography: `type.metaXs` (10 sp Regular)
- Label Default typography: `type.labelMd` (14 sp SemiBold)
- Label Small typography: `type.labelSm` (12 sp SemiBold)
- Label radius: `radius.tiny` (4 dp)
- Label padding: `spacing.md` H / `spacing.xs` V (Default) · `spacing.sm` H / `spacing.xs` V (Small)
- Dot + Count radius: `radius.full`
- Warning-family label text: `text.onWarning` (#372902 olive) — dark on gold for AA
