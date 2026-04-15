# WiomPagination

Four pagination components, three semantics:

| Component | Semantic | Use |
|---|---|---|
| `WiomPaginationDots` (Simple / Expanded) | Discrete pages, swipeable | Onboarding, promo carousels |
| `WiomPaginationBars` | Discrete steps, accumulated | Wizards (recharge, KYC) |
| `WiomPaginationCounter` | Long paginated list | Bills, transactions, order history |
| `WiomPaginationScrollIndicator` | Continuous scroll | Plan rails, horizontal strips |

## Rule of thumb

**discrete + swipeable → Dots/Bars · discrete + tap-to-navigate → Counter · continuous → ScrollIndicator**

## Picking a Dots style

- **Simple** — low visual weight. Onboarding with 2–4 slides.
- **Expanded** — active slot stretches to 24dp pill. Use when the current slide must stand out in a busy feed (promo banners).

## API

```kotlin
// Onboarding — 3 slides
WiomPaginationDots(total = 3, current = page)

// Promo carousel — 4 banners, active pill standout
WiomPaginationDots(total = 4, current = page, style = WiomPaginationDotStyle.Expanded)

// 5-step wizard
WiomPaginationBars(total = 5, current = step, counterLabel = "Step $step of 5")

// Long list
WiomPaginationCounter(current = page, total = totalPages, onPrev = prev, onNext = next)

// Horizontal rail
WiomPaginationScrollIndicator(progress = scrollFraction, visibleFraction = 0.3f)
```

## Rules

1. **2–6 slots max** for Dots and Bars. 7+ → switch to `WiomPaginationCounter`.
2. **Never stack Bars + Counter below it.** Bars already shows position.
3. **Exactly one active in Dots.** For Bars, slots 1..current all fill.
4. **Hide prev/next when out of bounds** in Counter — don't disable, hide.
5. **Don't show pagination on single-page views.** Hide entirely.
6. **Scroll Indicator is purposeless with no overflow.** Hide when content fits.

## Tokens

- Dot (default): 8×8dp · `radius.full` · `border.default`
- Dot (active Simple): 8×8dp · `brand.primary`
- Dot (active Expanded): 24×8dp pill · `brand.primary`
- Bar: 4dp height · `radius.full` · `brand.primary` (active) / `border.default` (rest)
- Counter label: `type.labelMd` · `text.secondary`
- Counter chevron: `icon.md` (24dp) · 48dp touch target · `text.secondary`
- Scroll rail: 4dp · `surface.muted` · thumb: `brand.primary` at ~30% width
- Gap (indicator → counter): `space.sm`
- Gap (dots): `space.sm` · Gap (bars): `space.xs`
