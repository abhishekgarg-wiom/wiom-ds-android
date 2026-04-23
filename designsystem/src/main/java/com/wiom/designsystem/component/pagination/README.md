# WiomPagination

Three semantics, four composables. Pick the component that matches the **kind of progress** you're showing — not the one that fits the empty space.

| Composable | Use for | Avoid for |
|---|---|---|
| `WiomPaginationDots` (Simple) | Onboarding carousels, 2–4 intro slides. Low visual weight. | Long paginated lists. |
| `WiomPaginationDots` (Expanded) | Promo / banner carousels where the current slide must stand out. | Tight layouts where the pill adds clutter. |
| `WiomPaginationBars` | Multi-step wizards, recharge / payment flows, story indicators. | Long lists. Continuous scroll. |
| `WiomPaginationCounter` | Paginated lists without swipe — bills, transactions, order history. | Step flows. 2–6 page carousels. |
| `WiomPaginationScrollIndicator` | Horizontal rails with continuous scroll — plans, offers, products. | Discrete paged content. |

## API

```kotlin
WiomPaginationDots(
    total: Int,                            // 2..6
    current: Int,                          // 1..total
    modifier: Modifier = Modifier,
    style: WiomPaginationDotStyle = Simple, // Simple (8×8 circle) | Expanded (24×8 pill active)
    counterLabel: String? = null,          // optional "1 / 4" text below
)

WiomPaginationBars(
    total: Int,                            // 2..6
    current: Int,                          // 1..total
    modifier: Modifier = Modifier,
    counterLabel: String? = null,
)

WiomPaginationCounter(
    current: Int,
    total: Int,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
)

WiomPaginationScrollIndicator(
    progress: Float,                       // 0f..1f
    modifier: Modifier = Modifier,
    visibleFraction: Float = 0.3f,         // thumb width share
)
```

## Variants

**Dots — Simple:** every slot is an 8×8 circle; the active slot uses `bg.brand`.
**Dots — Expanded:** the active slot becomes a 24×8 pill; all others remain 8×8 circles.
**Bars:** slots 1..`current` are filled `bg.brand` (accumulated progress); others `stroke.subtle`.
**Counter:** "N / Total" label centered between chevrons. Chevrons **hide** (don't disable) at bounds; a 48dp spacer reserves their width so the label doesn't jump.
**Scroll Indicator:** 4dp rail in `bg.muted` with a 30%-wide thumb in `bg.brand` that slides along the rail.

## Wiom use cases

- **Onboarding** → `WiomPaginationDots` (Simple), 2–4 slots, no counter.
- **Home promo banners** → `WiomPaginationDots` (Expanded), 3–4 slots.
- **Recharge / KYC wizard** → `WiomPaginationBars`, pinned at the top of every step screen.
- **Bills / transactions list** → `WiomPaginationCounter` below the list.
- **Plans rail** → `WiomPaginationScrollIndicator` centered under the rail, driven by `LazyListState`.

## Rules

1. **Max 6 slots** for Dots / Bars. Beyond that switch to `WiomPaginationCounter`.
2. **Never stack Bars + Counter** — Bars already show position; a counter below repeats information.
3. **Hide (don't disable) prev/next** when out of bounds. Counter takes care of this automatically.
4. **Exactly one active dot.** Bars fill 1..current; Dots highlight only the current slot.
5. **Counter label is centered** by default. Re-align per instance only when content around it is off-center.
6. **Single page → no pagination.** Don't render "1 / 1".
7. **Token-only values.** Colors, sizes, spacing, radius come from `WiomTheme.*`.

## Tokens

| Part | Token |
|---|---|
| Dot / bar active fill | `bg.brand` |
| Dot / bar inactive | `stroke.subtle` |
| Dot dimensions | 8×8 dp (Simple); 24×8 dp active (Expanded) |
| Bar height | 4 dp |
| Dot row gap | `space.sm` (8 dp) |
| Bar row gap | `space.xs` (4 dp) |
| Counter label typography | `type.labelMd` · `text.subtle` · `TextAlign.Center` |
| Chevron icon | `Icons.Rounded.ChevronLeft` / `.ChevronRight`, 24 dp, `icon.nonAction` |
| Chevron touch target | 48×48 dp |
| Scroll-indicator rail | 4 dp · `bg.muted` · `radius.full` |
| Scroll-indicator thumb | `bg.brand` · `radius.full` · `visibleFraction` of rail width |

No shadows. No hardcoded hex, sp, or dp literals outside the foundation values listed above.
