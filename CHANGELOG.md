# Changelog

All notable changes to the Wiom Design System (Android) will be documented here.
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.0.1] — 2026-05-04

Patch release. No public API change.

### Fixed

- `WiomChip` (`WiomTabsFilters.kt`) — added missing `Icons.Rounded.Close` import; the trailing close glyph on a Selected chip now compiles.
- `WiomTopBarStatusBar` (`WiomTopBar.kt`) — added missing `androidx.compose.ui.graphics.toArgb` import; status-bar color assignment now compiles.
- `designsystem/build.gradle.kts` — added `androidx.core:core-ktx` so `androidx.core.view.WindowCompat` resolves inside `WiomTopBarStatusBar`.

### Docs

- Reframed README / ADOPTION / CHANGELOG / CLAUDE / CONTRIBUTING as first-time onboarding (V1 → V2 migration framing dropped — there were no V1 consumers).
- Recorded the upstream foundation PR that fixed the `Neutral_700` hex drift between primitive and core tokens (`UPSTREAM_SKILL_PRS.md`).

## [2.0.0] — 2026-05-01

First public release. **15 components** built end-to-end against the Wiom DS V2 skill set in [`wiom-design-system`](https://github.com/abhishekgarg-wiom/wiom-design-system) (locked SHA `ed110b6`). Element-first tokens throughout. Material 3 `Icons.Rounded.*` (incl. `AutoMirrored.Rounded.*` for directional glyphs). Min SDK 24.

### Foundations

- **Color** — element-first token API with four namespaces: `WiomTheme.color.bg.*` (containers), `text.*` (text), `stroke.*` (borders / dividers), `icon.*` (icon tints), plus `overlay.scrim` for modal backdrops. 67-token internal `WiomColorPrimitives` palette (Brand · Neutral · Gray · Positive · Critical · Warning · Info), exposed only through the semantic layer.
- **Typography** — Noto Sans via Google Fonts provider. 9 tokens (`headingXl` 32 / `headingLg` 24 / `headingMd` 20 / `titleLg` 20 / `titleSm` 16 / `bodyLg` 16 / `bodyMd` 14 / `bodySm` 12 / `labelLg` 16 / `labelMd` 14 / `labelSm` 12 / `metaXs` 10) with skill-spec letterSpacing.
- **Spacing** — 4-dp grid: `xs` 4 · `sm` 8 · `md` 12 · `lg` 16 · `xl` 24 · `xxl` 32 · `huge` 48.
- **Radius** — `tiny` 4 · `small` 8 · `medium` 12 · `large` 16 · `xlarge` 24 · `full`.
- **Stroke** — `small` 1 dp · `medium` 2 dp.
- **Shadow** — `none` · `sm` · `md` · `lg` · `xl` (elevation tokens).
- **Icons** — `WiomIconSize` (`xs` 16 / `sm` 20 / `md` 24 / `lg` 48) + `WiomIcon(imageVector, contentDescription, size, tint)` wrapper that defaults to Wiom token defaults.

### Components

| Component | What it is |
|---|---|
| `WiomButton` (`wiom-cta`) | 5 types — Primary · Secondary · Tertiary · Destructive · Pre-booking. Loading state with inline 24 dp spinner. `WiomAcknowledge` row + paired Primary CTA for 1st-person consent gates. |
| `WiomInput` / `WiomTextarea` (`wiom-input-fields`) | Single-line + multi-line text-entry. 8 states (Default · Focused · Filled · Error · Success · Warning · Disabled · ReadOnly). Configuration patterns for phone (`+91` prefix), currency (`₹` / `.00`), search, password, dropdown trigger. |
| `WiomOtp` (`wiom-input-fields` §6) | Boxed 4 / 6-digit verification field. Single `BasicTextField` + `decorationBox`; auto-advance focus; optional right-aligned timer (`"00:24"`). |
| `WiomSelectionControl` (`wiom-selection-controls`) | `WiomCheckbox` (No · Indeterminate · Selected) · `WiomRadio` · `WiomSwitch`. 24 × 24 dp bounding box; indicator-only — labels live on the parent row. |
| `WiomBadge` (`wiom-badge`) | Passive status indicators. 3 types — Dot · Count (default cap 99) · Label. 24 ship variants. |
| `WiomIconBadge` (`wiom-icon-badge`) | Single glyph in a filled circle. Sm 24 / Md 48 / Lg 96 dp × 6 tones (Neutral · Brand · Positive · Warning · Critical · Info). |
| `WiomListItem` (`wiom-list-item`) | Unified row — Default · IconBg · Checkbox · Radio · Switch types. `state: WiomListItemState` enum. Pressed XOR Selected overlays. Trailing chevron tinted `icon.brand` (the row's tap-affordance signal). |
| `WiomTopBar` (`wiom-top-bar`) | Edge-to-edge header. Small (64) · Medium (112) · Large (152). Default · Centered · Scrolled · Search states. `isDarkVariant` parameter for premium / partner headers (`bg.brand.bold`). `WiomTopBarStatusBar(isDarkVariant)` helper pairs the OS status bar lightness. |
| `WiomNavigationBar` (`wiom-navigation-bar`) | Edge-to-edge bottom nav. 2–5 destinations via `WiomNavItem(label, icon, iconSelected?, hasBadge)`. `bg.selected` pill on Selected, brand icon + `text.selected` label. Consumes `navigationBarsPadding()`. |
| `WiomDialog` (`wiom-dialog`) | 312 dp modal. 5 type helpers — `WiomAlertDialog` · `WiomInputDialog` · `WiomSelectionDialog` (≤ 4 options, uses `WiomListItemRadio`) · `WiomIllustrationDialog` · `WiomLoadingDialog`. `WiomDialogButtonsLayout { Stacked, SideBySide }`. Scrim-tap-to-dismiss disabled by default. |
| `WiomBottomSheet` (`wiom-bottomsheet`) | Material3 `ModalBottomSheet` + Wiom tokens. 8 sizes — Compact · Half · Expanded · Full · Illustration · IllustrationLeft · Share · Form. Helpers: `WiomBottomSheetHeader` (with optional leading icon + trailing action), `WiomBottomSheetIllustration` (uses `WiomIconBadge.Lg` 96 dp, tone-swappable, `leftAligned: Boolean`), `WiomBottomSheetActions`. |
| `WiomToast` (`wiom-toast`) | Transient bottom feedback. 4 statuses — `Critical · Warning · Info · Positive`. All share `bg.default`; the leading icon (and Warning's body) carries the status colour. `WiomToastHost` + `rememberWiomToastState()` queue manager (`DURATION_SHORT` 2 s · `DURATION_LONG` 8 s · persistent). |
| `WiomLoader` (`wiom-loader`) | `WiomSpinner` (Sm 20 / Md 32 / Lg 48 × Brand / Neutral / OnColor). `WiomLinearProgress` (4 dp track, indeterminate or determinate). `WiomDots` (chat typing indicator). `WiomBrandLoader` (full-screen branded wait). `WiomSkeletonLine` / `WiomSkeletonCard` / `WiomSkeletonListItem`. |
| `WiomPagination` (`wiom-pagination`) | `WiomPaginationDots` (Simple / Expanded, 2–6 slots) · `WiomPaginationBars` (accumulated fill, 2–6 slots) · `WiomPaginationCounter` (always renders both chevrons; non-tappable side tints `icon.disabled`) · `WiomPaginationScrollIndicator` (Float overload + `LazyListState` overload). |
| `WiomProgressIndicator` (`wiom-progress-indicator`) | `WiomLinearProgress` (Sm / Md, 3 tones) · `WiomCircularProgress` (Sm / Lg) + indeterminate variants. `WiomProgressCompletion` (4-state rich completion meter — JustStarted · InProgress · AlmostThere · Complete). `WiomProgressMilestones` (2–5 named stages with `WiomMilestoneStage(label, icon)`). |
| `WiomTabsFilters` (`wiom-tabs-filters`) | `WiomPillTabs` (2–4 pills, context switch). `WiomUnderlineFilter` (single-select within-dataset narrower; scrollable when overflowing). `WiomChip` + `WiomChipRow` (multi-select toggle with trailing close ✕ on Selected; Disabled state). |
| `WiomDialog` Acknowledge / `WiomBottomSheet` Form | Compound flows are documented in their respective READMEs. |

### Adoption kit

- `:designsystem-rules` Detekt module enforces the element-first token API and the `Icons.Rounded.*`-only policy.
- `ADOPTION.md` — 15-minute setup guide for app developers.
- `adoption-kit/` — drop-in `consumer-CLAUDE.md`, ADR template, PR template.

### Known limitations

- **`Loader/Status` ring** (Wait → Success → Error) — not yet shipped. Planned for v2.1.
- **`WiomStepper`** — not shipped; needs an upstream skill in `wiom-design-system` first. Workaround: use `WiomProgressMilestones` for status visuals or `Column` + `WiomListItem` for per-step navigation.
- **Dropdown** has no dedicated component — use `WiomInput(readOnly = true)` + chevron + `WiomBottomSheet` picker (canonical V2 picker pattern).

### Planned (v2.1+)

- `Loader/Status` ring
- Reinstate `WiomStepper` once an upstream skill exists in `wiom-design-system`
- Standard (non-modal, persistent) bottom sheet
- Paparazzi screenshot testing
- More Material Symbols Rounded drawables as components need them
