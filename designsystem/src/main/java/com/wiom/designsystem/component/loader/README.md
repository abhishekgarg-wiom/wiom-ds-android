# WiomLoader

Indeterminate loading affordances for the Wiom Android library. Built from `.skills-cache/wiom-loader.md`.

The library's **determinate** progress indicators live next door in `component/progressindicator/`. This package covers only *waits whose endpoint is unknown*.

## Purpose

Tell the user *that work is in progress* when we cannot tell them *how far along*. Three distinct moments:

| Moment | Use |
|---|---|
| Inline wait (button, input, card) | `WiomSpinner` |
| Layout-known content arriving (list, card) | `WiomSkeletonLine` / `WiomSkeletonCard` / `WiomSkeletonListItem` |
| Full-screen blocking wait (splash, payment) | `WiomBlockingLoader` |

For outcome-driven waits that must end in success or error (payment, KYC upload), the skill calls for a dedicated `Loader/Status` ring — not shipped in this package, will come with the Status loader work.

## Components

### `WiomSpinner`

A 3/4 arc rotating around a full-ring track.

```kotlin
WiomSpinner()                                              // md + brand (default)
WiomSpinner(size = WiomSpinnerSize.Sm)                     // inside a button
WiomSpinner(size = WiomSpinnerSize.Lg)                     // centered in a hero
WiomSpinner(tone = WiomSpinnerTone.OnColor)                // on a brand-filled surface
WiomSpinner(tone = WiomSpinnerTone.Neutral)                // de-emphasised
```

| `size` | Diameter | Stroke | Use |
|---|---|---|---|
| `Sm` | 20dp | 2dp | Button, field, compact row |
| `Md` | 32dp | 3dp | Card / dialog / section (default) |
| `Lg` | 48dp | 4dp | Hero, centered on screen |

| `tone` | Arc | Track | Use |
|---|---|---|---|
| `Brand` | `bg.brand` | `bg.brandSubtle` | Light surfaces |
| `Neutral` | `text.subtle` | `stroke.subtle` | De-emphasised |
| `OnColor` | `icon.inverse` | *(none)* | Brand-filled / dark surfaces |

### `WiomSkeletonLine` / `WiomSkeletonCard` / `WiomSkeletonListItem`

Content-shaped placeholders with optional shimmer sweep. Caller sets width; heights are intrinsic or explicit per the skill's anatomy.

```kotlin
WiomSkeletonLine(modifier = Modifier.fillMaxWidth())       // default 16dp, shimmer
WiomSkeletonLine(modifier = Modifier.fillMaxWidth(0.5f), height = 24.dp)   // heading
WiomSkeletonCard(modifier = Modifier.fillMaxWidth())                       // image + 3 lines
WiomSkeletonListItem(modifier = Modifier.fillMaxWidth())                   // avatar + 2 lines + trailing
```

Tokens:

| Element | Token |
|---|---|
| Placeholder base | `bg.muted` |
| Shimmer highlight | `bg.default` at 55% alpha |
| Line radius | `radius.tiny` (4dp) |
| Thumbnail radius | `radius.medium` (12dp) |
| Avatar shape | `CircleShape` |
| Card container | `bg.default`, `radius.large` |

Shimmer is a 1.2s horizontal gradient sweep. Pass `animation = WiomSkeletonAnimation.Static` to disable it for high-density skeleton grids.

### `WiomBlockingLoader`

Full-screen wait that covers the parent and swallows touch events.

```kotlin
WiomBlockingLoader(style = WiomBlockingLoaderStyle.Light, message = "Loading…")
WiomBlockingLoader(style = WiomBlockingLoaderStyle.OnBrand, message = "Getting things ready…")
WiomBlockingLoader(style = WiomBlockingLoaderStyle.Overlay, message = "Processing your payment…")
```

| `style` | Surface | Spinner tone | Message color |
|---|---|---|---|
| `Light` | `bg.default` | `Brand` | `text.subtle` |
| `OnBrand` | `bg.brand` | `OnColor` | `text.inverse` |
| `Overlay` | `overlay.scrim` | `OnColor` | `text.inverse` |

`Overlay` is the CLAUDE.md § 8 pairing: a dim scrim over existing content plus a spinner. Stack it **on top** of your screen inside the same `Box`.

## Rules enforced

- Only `Icons.Rounded.*` used in the loader brand slot (`Icons.Rounded.AllInclusive` as the canonical infinity stand-in). CLAUDE.md § 2.
- Spinner is pure Canvas — no Material3 `CircularProgressIndicator` under the hood — so the stroke width and cap follow Wiom tokens exactly.
- Full-screen loader uses `.pointerInput(Unit) {}` to consume touches; do not pair with a transparent click target behind it.
- Skeleton blocks use `bg.muted` for base and `bg.default` for the shimmer highlight, per the deliverable spec (shimmer highlight = `bg.default`).

## Skill-vs-foundation flags

- The skill calls the brand spinner track `bg.brand.subtle`; foundation token is `bg.brandSubtle`. Identical color, style-only path drift in the skill.
- The skill's **Brand loader** is built around a bespoke infinity vector at Figma node `2068:48`. We use `Icons.Rounded.AllInclusive` as the closest canonical stand-in so we don't import a one-off drawable into `res/drawable/` (which is reserved for `ic_wiom_*` / `ic_partner_*`). When the branded vector asset is added to the drawable set, swap the glyph in `WiomBlockingLoader` and surface a `WiomIcons.infinity` facade entry.
- The skill ships a **Status ring** (Wait → Success → Error) and a **Typing dots** variant. Neither is in this build — they're out of scope for the v1.0 loader deliverable. Add them alongside an outcome-aware API (e.g. `WiomStatusLoader(state: Wait/Success/Error)`) and chat-bubble composite in a later release.
- The skill quotes `bg.warning.subtle = #FFE9A1` under the Typing dots spec. Foundation is `#FFF2BF`. Skill drift; unused here but flagged for the skill author.

## Known gaps

- No Typing dots (chat).
- No Status ring (Wait → Success / Error). Deferred.
- Brand loader uses `Icons.Rounded.AllInclusive` until a branded infinity asset lands.
- Skeleton shimmer is a plain gradient (no elastic ease, no left/right selection). Matches the skill's "pending motion decision".
