# WiomNavigationBar

Persistent bottom bar for switching between 2–5 top-level destinations.

## When to use

- Top-level destination switching (Home, Plans, Bills, Profile)
- Every tab independently reachable anytime

## When NOT to use

- Sections inside a single screen → `WiomTabsFilters`
- Linear flows (onboarding, KYC, checkout) — hide the bar
- Detail/edit/modal screens pushed on top of a tab — hide the bar (don't disable)

## API

```kotlin
val items = listOf(
    WiomNavItem(label = "Home",    icon = R.drawable.ic_home),
    WiomNavItem(label = "Plans",   icon = R.drawable.ic_bolt),
    WiomNavItem(label = "Bills",   icon = R.drawable.ic_receipt, hasBadge = true),
    WiomNavItem(label = "Profile", icon = R.drawable.ic_person),
)

var selected by remember { mutableStateOf(0) }

WiomNavigationBar(
    items = items,
    selectedIndex = selected,
    onSelect = { selected = it },
)
```

Tab count is driven by the list length — pass 2–5 items. The bar redistributes space evenly.

## Wiom configurations

- **3-tab** — Home / Plans / Profile (most focused)
- **4-tab** — Home / Plans / Bills / Profile (most common)
- **5-tab** — Home / Plans / Profile / Bills / Support (max)

## Rules

1. **Exactly one selected** at any time (`selectedIndex` is the source of truth).
2. **Labels always on.** Don't hide to "clean up" — fails first-time/low-vision users.
3. **One word per label.** "Home", "Plans" — not "My Bills".
4. **Icons should use filled glyphs on selected** — pass a filled icon. For now, pass the same icon for both states; future revision can accept separate outline/filled.
5. **Badges are rare and meaningful.** Only for unread/actionable items. Not for "new feature" marketing.
6. **Never put a CTA in the bar.** No plus/FAB merged in.
7. **Hide on push.** When navigating into detail/flow, bar goes away.
8. **Don't restyle per screen.** Same bar on every root destination.

## Tokens

- Bar bg: `surface.base`
- Top border: `stroke.small` · `border.default`
- Item pill (selected): `brand.primaryTint` · `radius.full`
- Item pill padding: `space.lg` H × `space.xs` V
- Icon size: `icon.md` (24dp)
- Icon color (default): `text.secondary` → (selected): `brand.primary`
- Label: `type.labelSm` · same color as icon
- Badge: 8dp · `negative.primary` · `radius.full` (via `WiomBadgeDot`)
