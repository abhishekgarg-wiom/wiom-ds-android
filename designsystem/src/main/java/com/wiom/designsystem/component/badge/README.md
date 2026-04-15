# WiomBadge

Passive status indicator. Never tappable. Three types: Dot, Count, Label.

## When to use which

| Need | Component |
|---|---|
| "Something new" / unread dot — no number or text | `WiomBadgeDot` |
| Numeric count (notifications, unread items) | `WiomBadgeCount` |
| Text status / state / role ("असफल", "पक्का", "एडमिन") | `WiomBadgeLabel` |

## Sizing (Label only)

- **Default (28dp)** — badge IS the information. Use `Filled` for terminal states, `Tinted` for transitional.
- **Small (24dp)** — badge supports other content. Always `Tinted` (the enum ignores `Filled` for Small).

Test: remove the badge — does the row still make sense? If yes, use `Small`; if no, use `Default`.

## Style — Filled vs Tinted

- **Filled** (loud, high contrast): terminal, won't change. "पूरा हुआ", "असफल", "Confirmed".
- **Tinted** (soft, low contrast): transitional, may change. "प्रोसेसिंग", "Active", "पक्का".

## API

```kotlin
WiomBadgeDot(color = WiomBadgeColor.Negative)

WiomBadgeCount(count = 5, color = WiomBadgeColor.Negative)
// count <= 0 → nothing rendered (hide at zero rule)
// count > 9 → "9+" by default; pass maxOneDigit = false for "99+"

WiomBadgeLabel(
    text = "असफल",
    size = WiomBadgeSize.Default,
    color = WiomBadgeColor.Negative,
    style = WiomBadgeStyle.Filled,
)
```

## Wiom use cases

- **Transaction row (Default + Filled + Negative):** `निकासी — ₹2,000 → SBI  [असफल]` — user is checking the outcome.
- **Wallet inline (Small + Tinted + Positive):** `₹8,200  [पक्का]` — amount is primary, badge confirms certainty.
- **Header role (Small + Tinted + Info):** `Rajesh  [एडमिन]` — name is primary, role supports.
- **Bell with count (Count + Negative, overlayed via `Box` + `Alignment.TopEnd`):** shows unread notifications.
- **Plan status (Default + Tinted + Info):** `[प्रोसेसिंग]` during an activation flow.

## Rules

1. **Passive only.** If it needs a tap target, it's a Chip, not a badge.
2. **No icons in badges.** Color + text is enough.
3. **Small = Tinted only.** The composable enforces this.
4. **Count overflow.** Default: `9+`. Set `maxOneDigit = false` for `99+`.
5. **Hide at zero.** Count badge with `count <= 0` renders nothing.
6. **One badge per row.** Don't stack badges on the same line.
7. **Color matches status family.** Positive badge never sits on a Negative row.

## Tokens

- Dot: 8dp · `radius.full` · family primary
- Count: min 18dp · `radius.full` · `type.metaXs` · `text.onColor` on family primary
- Label (Default): 28dp height (from 4+20+4 padding) · `radius.tiny` · `type.labelMd` · `space.md` horizontal padding
- Label (Small): 24dp height (from 4+16+4) · `radius.tiny` · `type.labelSm` · `space.sm` horizontal padding
- Shadow: `shadow.none` always
