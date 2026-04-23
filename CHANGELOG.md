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

### Added (v1.0.0 work-in-progress)
- **`WiomPagination`** rewritten against the v1.0.0 element-first tokens and `Icons.Rounded.*` pipeline. Four composables — `WiomPaginationDots` (Simple 8×8 circle / Expanded 24×8 pill active slot, 2–6 slots), `WiomPaginationBars` (2–6 slots, accumulated fill, 4dp tall, `space.xs` gap), `WiomPaginationCounter` ("N / Total" with `Icons.Rounded.ChevronLeft` / `ChevronRight`; chevrons **hide** at bounds behind a 48dp spacer so the label doesn't jump), and `WiomPaginationScrollIndicator` (4dp rail in `bg.muted`, thumb in `bg.brand`, `visibleFraction` default 0.3). Counter labels use `type.labelMd` / `text.subtle` centered.
- **`WiomStepper`** (v1.0.0) — `WiomStepIndicator` atom (32dp, 4 states: Completed brand-fill + `Icons.Rounded.Check`, Active `bg.default` + 2dp `stroke.brandFocus` ring + brand number, Upcoming `bg.default` + 1dp `stroke.subtle` ring + `text.subtle` number, Error `bg.critical` + `Icons.Rounded.PriorityHigh`); `WiomStepperHorizontal` uses `fillMaxWidth()` (no pinned 360dp) with 2dp connector brand-filled after Completed steps; `WiomStepperVertical` with 32dp rail + 2dp connector, `space.md` rail→content gap, title (`type.labelMd`) + optional description (`type.bodyMd` / `text.subtle`) + optional `action: @Composable () -> Unit` slot that accepts any composable (WiomButton, WiomInput, OTP, tertiary CTA). State derivation (step<current→Completed, ==→Active, >→Upcoming) + optional `stateOverrides: Map<Int, WiomStepState>` for Error.
- **`WiomInput`** + **`WiomTextarea`** (v1.0.0 rebuild). New `status` axis (`None / Error / Success / Warning`) separated from `enabled` and `readOnly` booleans — a field can be readOnly AND Error at the same time. Status icons auto-populate the trailing slot and override any caller-supplied `trailingIcon`. Border: rest `stroke.small + stroke.subtle`, focus `stroke.medium + stroke.brandFocus`, error `stroke.medium + stroke.criticalFocus`, success `stroke.medium + stroke.positiveFocus`. Container `bg.default` → `bg.subtle` (readOnly) → `bg.disabled`. Padding `space.lg` H × `space.md` V, radius `radius.medium`. Label `type.labelLg`, value `type.bodyLg`, helper + counter `type.bodyMd`. Configuration patterns in README for Phone (India-only — leading phone icon, no `+91` prefix), OTP + timer counter, Search, Currency (`₹` prefix / `.00` suffix), Password (visibility toggle), and the **dropdown-replacement pattern** (`readOnly=true` + `trailingIcon=KeyboardArrowDown` + `onClick` → bottom-sheet picker — when acting as a dropdown trigger the internal BasicTextField is swapped for a plain Text so the row's click handler receives the tap).
- **`WiomListItem`** rebuilt on the 5-Type hybrid model — `Default` (24dp leading icon + chevron) · `IconWithBg` (new — leading is a `WiomIconBadge`) · `Checkbox` · `Radio` · `Switch`. Exposes a typed wrapper per Type (`WiomListItem`, `WiomListItemIconBadge`, `WiomListItemCheckbox`, `WiomListItemRadio`, `WiomListItemSwitch`) plus a raw `WiomListItemBase` slot API. The selection-control sub-components (`WiomCheckbox` / `WiomRadio` / `WiomSwitch`) and `WiomIconBadge` are consumed via `@Composable () -> Unit` slots — the caller holds `checked` / `tone` state and passes a configured instance, keeping list-item decoupled from the sibling modules. `selected = true` adopts `bg.selected` fill + 8dp `bg.brand` dot on the trailing side (picker-sheet pattern). Padding `space.lg` H × `space.md` V, min height 48dp via `defaultMinSize`. Primary `type.bodyLg` · `text.default`, secondary `type.bodyMd` · `text.subtle`, trailing meta `type.bodyMd` · `text.subtle`.
- **`WiomBottomSheet`** (v1.0.0 rebuild) — Material3 `ModalBottomSheet` wrapped with Wiom tokens. `bg.default` surface, top `radius.xlarge`, bottom `radius.none`, 32×4dp `stroke.strong` drag handle with `radius.full` + `space.sm` vertical padding, `overlay.scrim`, `navigationBarsPadding()` on content. 8 sizes modelled via `WiomBottomSheetSize` enum (Compact / Half / Expanded / Full / Illustration / IllustrationCta / IllustrationLeft / Share / Form) — list variants set a `heightIn(min)` heuristic, hug-height variants let content drive the height. Helpers: `WiomBottomSheetHeader` (title `type.headingLg` + optional subtitle `type.bodyMd` · `text.subtle` + divider `stroke.small` · `stroke.subtle`), `WiomBottomSheetListItem` (built inline, NOT via `WiomListItem`, so it honors `space.xl` (24dp) horizontal padding — 40dp `bg.brandSubtle` icon circle with `icon.brand` glyph, `type.labelLg` label, `Icons.Rounded.ChevronRight` 20dp `icon.disabled`), `WiomBottomSheetIllustration` (120dp `bg.brandSubtle` circle with 48dp `icon.brand` glyph centered, `type.headingLg` heading + `type.bodyLg`/`text.subtle` subtext, all centered), `WiomBottomSheetActions` (top divider `stroke.small`/`stroke.subtle` + row with `space.xl` H × `space.lg` V padding, `space.md` gap between children). `@Preview` for each of the 9 enum values.
- **`WiomDialog`** (v1.0.0 rebuild) — Material3 `Dialog` wrapped with Wiom tokens. 312 dp fixed width, `bg.default`, `radius.xlarge`, `shadow.xl` paired with `overlay.scrim` (auto via `Dialog`), `space.xl` padding. 5 typed helpers covering every variant from the V2 skill: `WiomAlertDialog` (optional 48dp `bg.brandSubtle` icon badge, left-aligned `type.headingMd` title + `type.bodyLg`/`text.subtle` body, `space.huge` (48dp) main-to-buttons gap per Alert-only rule), `WiomInputDialog` (single-field content slot, `space.xl` main-to-buttons gap), `WiomSelectionDialog` (inline radio rows honoring filled-state rule — selected = `bg.brand` fill + `bg.default` inner dot, unselected = transparent + `stroke.medium` `stroke.strong` border; intended for ≤4 options), `WiomIllustrationDialog` (120dp `bg.brandSubtle` circle + 48dp `icon.brand` glyph centered, same illustration anatomy as the bottom sheet), `WiomLoadingDialog` (non-dismissible — `dismissOnBackPress = false`, `dismissOnClickOutside = false` — 20dp `CircularProgressIndicator` tinted `bg.brand` inside a 40dp frame, optional title + message centered). `WiomDialogAction(label, onClick, type: WiomButtonType = Primary)` data class maps to `WiomButton` types; actions are always rendered stacked full-width with `space.md` gap, max 2 CTAs per group.
- **`WiomLoader`** (v1.0.0 from `wiom-loader` skill) — `WiomSpinner` (Sm 20dp · Md 32dp · Lg 48dp × Brand/Neutral/OnColor tones; Canvas-drawn 270° arc rotating at 900ms with round caps); `WiomSkeletonLine` (16dp body-height bar, `bg.muted` + optional `bg.default` shimmer sweep on a 1.2s linear loop), `WiomSkeletonCard` (72dp thumbnail + 3 lines inside a `bg.default` card, `radius.large`), `WiomSkeletonListItem` (40dp circular avatar + 2 lines + trailing meta); `WiomBlockingLoader` (Light = `bg.default`, OnBrand = `bg.brand`, Overlay = `overlay.scrim` — all swallow pointer events, pair `Icons.Rounded.AllInclusive` as the canonical infinity stand-in, Lg spinner, optional `type.bodyMd` message). Status (Wait → Success/Error) ring and typing dots deferred.
- **`WiomToast`** (v1.0.0 from `wiom-toast` skill) — 5 statuses: `Neutral` (`bg.inverse` + `text.inverse`), `Positive` (`bg.positiveSubtle` + `icon.positive`, body `text.default`), `Critical` (`bg.criticalSubtle` + `icon.critical`, body `text.default`), `Warning` (`bg.warningSubtle` + `icon.warning`, body and action share `text.onWarning`), `Info` (`bg.infoSubtle` + `icon.info`, body `text.default`). `radius.medium`, `shadow.lg`, `space.lg` padding, `space.md` icon→body gap, body `type.bodyMd` max 2 lines, action label `type.labelLg`. Ships with `WiomToastHost` + `WiomToastState` + `rememberWiomToastState()` pattern: unbounded queue, max 1 visible, 300ms slide+fade enter/exit, `DURATION_SHORT = 2000ms` / `DURATION_LONG = 8000ms` / `null` = persistent. Close X appears when `onClose` (direct call) or `showClose = true` (via `WiomToastMessage`). Action tap auto-dismisses the current toast before invoking the callback.
- **`WiomProgressIndicator`** (v1.0.0 from `wiom-progress-indicator` skill) — `WiomLinearProgress` determinate bar with `WiomLinearProgressSize` (Small 4dp default / Medium 8dp); `WiomLinearProgressIndeterminate` with a 25% head sweeping across the track on a 1.4s linear loop; `WiomCircularProgress` determinate arc with `WiomCircularProgressSize` (Small 24dp / Large 48dp) and round stroke caps; `WiomCircularProgressIndeterminate` 270° rotating arc at 1.2s per turn. All four accept `WiomProgressTone` (Brand / Info / Positive). Track = `bg.muted`; fill/arc = `bg.brand` / `bg.info` / `bg.positive`. `radius.full` on linear. No border or shadow. Warning/Critical tones intentionally excluded — those colors are reserved for Attention states, not forward motion.

### Flagged — foundation gaps
- No `stroke.warningFocus` token. `WiomInput(status = Warning)` currently borrows
  `stroke.brandFocus` for the border; helper text uses `text.onWarning` and the trailing
  icon uses `icon.warning`, so the warning state is still identifiable. Tracked for a
  foundation update.

### Flagged — skill drift (Loader / Toast / Progress Indicator build)
- `wiom-toast` says Info uses `bg.muted` + `icon.nonAction`. Foundation-correct is
  `bg.infoSubtle` + `icon.info` (deliverable spec). Foundation wins.
- `wiom-toast` quotes Warning subtle hex as `#FFE9A1`; foundation
  `bg.warningSubtle = #FFF2BF`. Token used; skill hex discarded.
- `wiom-toast` radius = 8dp (`radius-small`); deliverable = 12dp (`radius.medium`). Deliverable wins.
- `wiom-toast` body color on Critical: skill says `text.default` (matches CLAUDE.md § 6);
  deliverable spec restates this explicitly. No drift, documented for clarity.
- `wiom-loader` uses legacy dotted `bg.brand.subtle` path; foundation exposes camelCase
  `bg.brandSubtle`. Same color.
- `wiom-loader` Brand loader wants a bespoke Figma infinity vector (node `2068:48`).
  Substituted `Icons.Rounded.AllInclusive` until a branded drawable is added to `res/drawable/`.
- `wiom-progress-indicator` does not cover circular progress (circular loading lives
  in Loader/Spinner and Loader/Status). Circular determinate + indeterminate added here
  to satisfy the v1.0 deliverable brief; skill authors should absorb in next refresh.
- `wiom-progress-indicator` recommends a reversed-semantics color ladder (info <35% →
  brand 35–80% → positive >80%). Library exposes `WiomProgressTone` as an explicit
  caller choice rather than auto-laddering on `progress` — appropriate tone depends on
  context (a long install vs a short upload).

### Planned
- Standard (non-modal, persistent) bottom sheet
- Google Fonts provider for Noto Sans
- Paparazzi screenshot testing
- Detekt lint rule enforcing token-only values
- More Material Symbols Rounded drawables as components need them

## [0.3.0] — 2026-04-15

Spacing audit + new component. Authoritative design source clarified.

### Added

- **`WiomStepper`** (from new `wiom-stepper` skill) — `WiomStepperHorizontal` for 2–6 step wizards with 1–2 word labels, `WiomStepperVertical` for status flows with per-step title + description + optional action slot (accepts any composable — a button, tertiary CTA, inline `WiomInput`, OTP field). `WiomStepIndicator` exposed as the shared atom. 4 states: Completed / Active / Upcoming / Error.
- New icon: `WiomIcons.priorityHigh` (for Error state indicator). Material Symbols Rounded drawable added.
- Sample app includes a Stepper section showing horizontal and vertical layouts.

### Fixed — spacing audit

- **`WiomDropdown`:** field slot gap was `space.md` (12dp); should be `space.sm` (8dp) per wiom-dropdown skill.
- **`WiomTopBar` (Medium):** title was missing top padding, clipping close to the action row. Added `space.sm` top.
- **`WiomTopBar` (Large):** title was missing top padding. Added `space.md` top to match the skill's `space/12 top + 44lh + space/32 bottom = 88dp` title area.
- **`WiomUnderlineFilter`:** active indicator was a fixed 64dp width; now uses `fillMaxWidth()` inside a center-aligned column, so the underline hugs the label instead of overshooting/undershooting.
- **`WiomBottomSheetListItem`:** was wrapping `WiomListItem` and inheriting its 16dp horizontal padding; skill calls for 24dp inside bottom sheets. Rebuilt inline with `space.xl` padding so sheet content aligns under the 24dp header left inset.
- **`WiomBottomSheetIllustration`:** bottom padding was `space.lg` (16dp); skill specifies `space/24` bottom. Changed to `space.xl`.

### Changed — guidance

- `CLAUDE.md` updated: **`wiom-design-foundations` is the single source of truth** for tokens; `wiom-visual-skill` is explicitly noted as a principles document, not a token source. Rule of precedence added: when a component skill disagrees with the foundation, flag it — don't encode the drift.

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
