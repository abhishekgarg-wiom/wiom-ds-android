# WiomProgressIndicator

Determinate and indeterminate progress bars / rings for the Wiom Android library. Built against the `wiom-progress-indicator` skill (linear · completion · milestones · circular).

## Purpose

Tell the user *how far along a task has progressed*. Use when the caller has a known 0..1 completion value (determinate) or when the task is active but its endpoint is unknown (indeterminate).

For indeterminate "something is happening" with no bar — reach for `WiomSpinner` or `WiomStatusLoader` from the Loader family instead. Named-stage trackers (installation, KYC) belong here as `WiomProgressMilestones`.

## Variants

| Composable | Form | Determinate | Use |
|---|---|---|---|
| `WiomLinearProgress` | Bar | yes | File upload with known %, payment charge with known %, profile completion card |
| `WiomLinearProgressIndeterminate` | Bar | no | Upload without progress events, streaming response |
| `WiomCircularProgress` | Ring | yes | Compact ring with a % — great inside a card badge |
| `WiomCircularProgressIndeterminate` | Ring | no | Inline wait inside a button / field / list row |

### Sizes

- Linear: `Small` 4dp (default, per deliverable spec) / `Medium` 8dp
- Circular: `Small` 24dp (default) / `Large` 48dp

### Tones

All four composables accept `WiomProgressTone`:

- `Brand` — pink `bg.brand` (default)
- `Info` — purple `bg.info`
- `Positive` — green `bg.positive`

The library does **not** auto-ladder tone based on `progress` value. Callers pick the tone per context: a long install might ladder info → brand → positive as progress climbs, while a quick KYC step stays brand throughout.

## API

```kotlin
WiomLinearProgress(
    progress = uploadProgress,
    modifier = Modifier.fillMaxWidth(),
)

WiomLinearProgress(
    progress = 0.88f,
    tone = WiomProgressTone.Positive,
    size = WiomLinearProgressSize.Medium,
    modifier = Modifier.fillMaxWidth(),
)

WiomLinearProgressIndeterminate(
    modifier = Modifier.fillMaxWidth(),
)

WiomCircularProgress(
    progress = 0.6f,
    size = WiomCircularProgressSize.Large,
)

WiomCircularProgressIndeterminate()
```

All composables take `modifier: Modifier = Modifier` as the first optional parameter. Width on linear bars comes from the parent — do not set a fixed height on the wrapper; pass a `size` enum instead.

## Tokens

| Element | Token |
|---|---|
| Track (linear + circular) | `WiomTheme.color.bg.muted` |
| Fill — Brand | `WiomTheme.color.bg.brand` |
| Fill — Info | `WiomTheme.color.bg.info` |
| Fill — Positive | `WiomTheme.color.bg.positive` |
| Corner radius (linear) | `WiomTheme.radius.full` |
| Stroke cap (circular) | `StrokeCap.Round` |

## Behavior

- `progress` is clamped to `[0f, 1f]`. Negative / >1 values are pinned silently, no exception.
- Indeterminate linear: 25%-wide head sweeps across the full track on a 1.4s linear loop.
- Indeterminate circular: 270° arc rotates continuously at 1.2s per turn; no track drawn.
- No border or shadow on any variant (CLAUDE.md § 8).

## Rules enforced

- Track: `bg.muted` only. Never `stroke.*` (skill drifted to `stroke.subtle` for track — `bg.muted` is the correct foundation token for a *filled* track surface).
- Fill: tone is an explicit caller choice. Critical/Warning tones are NOT offered — per skill: "Never use warning/critical colors on progress fill (those are for Attention states, not forward motion)."
- Height: comes from the `size` enum. Caller cannot override via `.height(...)` without violating intrinsic sizing.
- Radius: `radius.full` on both track and fill so the fill never appears as a sharp-edged bar inside a pill.

## Known gaps (parity with skill)

- No "Attention / Blocked" variant — yellow/red tones are intentionally excluded.
- No numeric %% label inside the bar. Compose the bar alongside a `Text(…%)` at the call site when needed.
- The upstream `wiom-progress-indicator` skill covers Linear · Completion · Milestones · Circular. Indeterminate variants for Linear and Circular are added here on top of the skill's determinate spec.
