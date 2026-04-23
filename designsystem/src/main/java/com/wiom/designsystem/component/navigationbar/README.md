# WiomNavigationBar

Bottom bar for switching between 2–5 top-level destinations. Exactly one item is `Selected` at any time; reflect the current route.

## Variants

Driven by the `items: List<WiomNavItem>` size — 2 to 5.

| Count | When |
|---|---|
| 2 | Exactly two peer destinations. Rare. |
| 3 | Focused apps — cleanest option. |
| 4 | Genuinely needed 4th destination. |
| 5 | Hard cap. Only if every tab is weekly-used. |

Items beyond 5 are not supported — don't add a "More" menu. If you need 6, you have a scope problem.

## API

```kotlin
data class WiomNavItem(
    val label: String,
    val icon: ImageVector,        // Icons.Rounded.*
    val hasBadge: Boolean = false,
)

WiomNavigationBar(
    items = listOf(
        WiomNavItem("Home",    Icons.Rounded.Home),
        WiomNavItem("Plans",   Icons.Rounded.Payments),
        WiomNavItem("Bills",   Icons.Rounded.ReceiptLong, hasBadge = true),
        WiomNavItem("Profile", Icons.Rounded.Person),
    ),
    selectedIndex = currentRouteIndex,
    onItemSelect = { idx -> navigate(routes[idx]) },
)
```

## Wiom use cases

- Standard Wiom Customer app: Home · Plans · Bills · Profile (4 tabs).
- Partner app: Home · Tickets · Earnings · Profile (4 tabs).
- Compact Partner app: Home · Tickets · Profile (3 tabs).
- Badge `bg.critical` dot on Bills when a due invoice exists; remove on open.

## Rules

1. **One Selected at a time.** Reflects the route, not user preference.
2. **Labels stay on.** Never hide the label — accessibility regression.
3. **One-word labels.** "Home", "Plans", "Bills" — nouns, not verbs.
4. **Hide on push.** Non-root screens, flows, media, keyboard-open text entry.
5. **No CTAs in the bar.** No plus-FAB merged into a tab.
6. **No reshuffling.** Same order on every root destination. Role-gated destinations stay in their slot.
7. **Badges are rare.** Unread or actionable only — never decorative.
8. **No borders on the pill**, no shadow — flat. Only the 1dp top border separates the bar from content.

## Tokens

| Part | Token |
|---|---|
| Bar fill | `color.bg.default` |
| Top border | `stroke.small` + `color.stroke.subtle` |
| Bar padding | `space.xs` horizontal · `space.sm` vertical |
| Item column gap (pill ↔ label) | `space.xs` |
| Pill padding | `space.lg` H · `space.xs` V |
| Pill radius | `radius.full` |
| Selected pill fill | `color.bg.selected` |
| Selected icon | `color.icon.brand` |
| Inactive icon | `color.icon.nonAction` |
| Selected label | `type.labelSm` · `color.text.selected` |
| Inactive label | `type.labelSm` · `color.text.subtle` |
| Badge | `WiomBadgeDot(tone = Critical)` at `Alignment.TopEnd` of the icon |

No fixed item width — each item uses `Modifier.weight(1f)`, so the bar redistributes evenly when the item count changes.
