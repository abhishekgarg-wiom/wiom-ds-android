# WiomTopBar

Header at the top of every screen. Covers 3 sizes × 4 states in one component.

## Size — pick based on visual weight

| Size | Height | Use |
|---|---|---|
| **Small** (default) | 64dp | 90% of screens. Detail, settings, forms, modals. If unsure, use Small. |
| **Medium** | 112dp | Content-heavy screens needing a prominent title. "Payment history", "All invoices". ~10% of screens. |
| **Large** | 152dp | Hero/landing only. Home tab root, dashboard greeting, profile root. **One per flow.** |

## States

- **Default** — standard header
- **Centered** — `centered = true`. Modal / bottom-sheet headers only (Small only). Title is truly centered.
- **Scrolled** — `scrolled = true`. Set programmatically when scroll offset > 0. Adds `shadow.sm`.
- **Search** — not a state here; compose `WiomInput` with leading icon yourself when search mode is active.

## API

```kotlin
// Most common: secondary screen
WiomTopBar(
    title = "Profile",
    actions = {
        WiomTopBarIconAction(icon = { WiomIcon(WiomIcons.search, "Search") }, onClick = onSearch)
    },
)

// Modal with Save CTA
WiomTopBar(
    title = "Edit profile",
    centered = true,
    leading = { WiomIcon(WiomIcons.close, "Close") },
    actions = { WiomTopBarTextAction(text = "Save", onClick = onSave) },
)

// Home / root (no back)
WiomTopBar(
    title = "Wiom",
    leading = null,
    actions = {
        WiomTopBarIconAction(icon = { WiomIcon(WiomIcons.search, "Search") }, onClick = onSearch)
        WiomTopBarIconAction(icon = { WiomIcon(WiomIcons.moreVert, "More") }, onClick = onMore)
    },
)

// Hero landing
WiomTopBar(title = "Good morning,\nAbhishek", size = WiomTopBarSize.Large)
```

## Rules

1. **Small by default.** Only go Medium/Large if you can name a specific reason.
2. **Large: one per flow.** Never on list/form/detail/modal/settings screens.
3. **Centered for modals only.** Never for full-screen navigation.
4. **Scrolled is runtime only.** Ship `Default` at rest; flip on scroll > 0.
5. **Cap trailing actions at 3.** Beyond 3, move to overflow (`moreVert`).
6. **Text CTA is never destructive.** For delete/cancel-subscription, use an in-screen Button.
7. **Titles ≤ 24 chars** on 360dp screens — longer truncates with ellipsis.
8. **Heights hug from padding + line-height** — never pin a fixed height.

## Tokens

- Container: `surface.base`
- Title (Small): `type.titleLg` · `text.primary`
- Title (Medium): `type.headingLg` · `text.primary`
- Title (Large): `type.headingXl` · `text.primary`
- Subtitle: `type.bodySm` · `text.secondary`
- Text CTA: `type.labelMd` · `brand.primary`
- Icons: `icon.md` (24dp) · `text.secondary` · 48dp touch target
- Scrolled elevation: `shadow.sm`
