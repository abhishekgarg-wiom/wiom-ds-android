# Changelog

All notable changes to the Wiom Design System (Android) will be documented here.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.1] — 2026-04-15

First working JitPack release. v0.1.0 shipped with three build-blocking issues; this is the same 12-component set with those fixed.

### Fixed
- `gradlew` script lost its executable bit when committed from Windows — Linux CI couldn't run it. Restored via `git update-index --chmod=+x`.
- AGP's `android.publishing { singleVariant("release") }` block conflicted with the `com.vanniktech.maven.publish` plugin (double registration of the `release` component). Removed the AGP block; vanniktech owns publishing.
- `WiomSwitch` missing `import androidx.compose.runtime.getValue` needed for `by animateDpAsState(...)` delegated state. Compile error: *"Type 'State<Dp>' has no method 'getValue'"*. Import added.

Added **MIGRATION.md** — step-by-step adoption guide for app developers: install, wrap in `WiomTheme`, migration playbook (screen order, per-screen checklist, PR template), mapping table (old primitive → Wiom component), common gotchas, versioning, bug-reporting workflow.

Use this version. **v0.1.0 does not build**; v0.1.1 is the first usable release.

## [Unreleased]

### Planned
- Standard (non-modal, persistent) bottom sheet
- Google Fonts provider for Noto Sans
- Paparazzi screenshot testing
- Detekt lint rule enforcing token-only values
- More Material Symbols Rounded drawables as components need them

## [0.2.0] — 2026-04-15

All 13 components from the Wiom skill set are now in the library.

### Added

- **`WiomButton`** — the CTA primitive, from `wiom-cta` skill. 4 types (Primary / Secondary / Tertiary / Destructive) × 3 interaction states (default / pressed / disabled) + loading state with inline spinner. Optional leading or trailing icon (via `WiomButtonIcon.Leading` / `.Trailing`). Intrinsic sizing (`defaultMinSize(minHeight = 48dp)`) so touch target is preserved but text never wraps/truncates/shrinks when the user scales system font.
- **`WiomAcknowledge`** — 1st-person checkbox row that gates its paired `WiomButton`. Use for irreversible actions, terms/policy acceptance, verified-data submission.
- Sample app now includes a Buttons section demonstrating all 4 types, loading state, and the Acknowledge → Primary flow.

### Rules enforced by the component

- Max 2 CTAs per screen/sheet/dialog (library doesn't enforce — reviewer/designer responsibility).
- Text never wraps: `maxLines = 1`.
- No shadow — flat.
- Min 48dp touch target via `defaultMinSize` (grows with font scale, never pinned).
- Pressed state uses `primary-pressed` / `primary-subtle` / `negative-primary-pressed` per type.
- Secondary border is always `stroke.medium` (2dp).

## [0.1.0] — 2026-04-15

First component-rich release. 12 components built from 13 Wiom design skills (button `WiomCta` deferred pending updated skill).

### Added — Components

- **WiomBadge** — Dot / Count / Label (Default 28dp + Small 24dp, Filled + Tinted styles, 6 colors)
- **WiomCheckbox** — 2×5 variants with filled-state rule and error state
- **WiomRadio** — 2×3 variants, single-select, circular indicator
- **WiomSwitch** — instant-apply toggle, 52×32dp track with animated thumb
- **WiomInput** + **WiomTextarea** — all 8 states (Default/Focused/Filled/Error/Success/Warning/Disabled/ReadOnly), prefix/suffix, leading/trailing icon slots, helper, counter. Includes configuration patterns for Phone (no country code — India-only), OTP with timer, Search, Currency, Password
- **WiomListItem** — unified row for navigation / settings / selection / toggle / info. `selected` flag for dropdown rows
- **WiomTopBar** — Small / Medium / Large sizes × Default / Centered / Scrolled states. `WiomTopBarIconAction`, `WiomTopBarTextAction` helpers
- **WiomNavigationBar** — 2–5 tabs with brand-tint pill on selected, `WiomBadgeDot` overlay support
- **WiomDropdown** — single-select field with expanded menu. Menu rows reuse `WiomListItem`
- **WiomPillTabs** / **WiomUnderlineFilter** / **WiomChip** + **WiomChipRow** — 3-level filtering system (context switch / single-select / multi-select)
- **WiomPagination** — Dots (Simple / Expanded), Bars, Counter, ScrollIndicator — four components, three semantics
- **WiomBottomSheet** (modal) — `WiomBottomSheetHeader`, `WiomBottomSheetListItem`, `WiomBottomSheetIllustration`, `WiomBottomSheetActions` helpers

### Added — Icons

- `wiom_ic_check` (for checkbox glyph)
- `wiom_ic_arrow_back`, `wiom_ic_menu`, `wiom_ic_more_vert` (for top bar / nav patterns)

Total `WiomIcons` facade: 15 Material Symbols Rounded drawables.

### Changed

- Sample app rewritten to showcase every component as a scrollable catalogue with `WiomTopBar` header and `WiomNavigationBar` footer.
- `CHANGELOG.md`, root `README.md`, per-component `README.md` files updated.

### Known gaps (shipping anyway, tracked for v0.2.0)

- **Typography uses `FontFamily.Default`** — Noto Sans via Google Fonts provider coming in v0.2.0.
- **`WiomCta` (button) deferred** pending updated skill from design.
- **Only modal `WiomBottomSheet` shipped** — Standard (persistent, no scrim) for later.
- **No Paparazzi screenshot tests** — follow-up.
- **No Detekt lint rule** yet blocking raw hex / dp / sp — follow-up.
- **Pagination scroll indicator** thumb-position math is approximate — refine with real-world usage.

## [0.0.1] — 2026-04-15

First scaffold — foundations only.

### Added
- `WiomTheme` composable + `WiomTheme.*` token accessor
- `WiomColors` — 43 semantic color tokens
- `WiomTypography` — 13 type tokens
- `WiomSpacing` — 13 tokens on 4px grid
- `WiomRadius` — 7 radius tokens
- `WiomStroke` — 2 stroke widths
- `WiomShadow` — 5 elevation tokens
- `WiomIconSize` — 4 icon size tokens
- `WiomIcon` composable + `WiomIcons` facade
- 10 Material Symbols Rounded drawables
- Material3 bridge
- Sample app showing tokens
- CLAUDE.md with 5 hard rules
- GitHub Actions CI + JitPack config
