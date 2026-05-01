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

## [2.0.0] — 2026-05-01

Full rebuild against the Wiom DS V2 skill set (locked SHA `ed110b6` from
`github.com/abhishekgarg-wiom/wiom-design-system`). Every component re-derived from V2
ship values; many V1 drifts corrected. **15 components** (Stepper dropped — no V2
upstream skill yet). Element-first tokens throughout.

**Post-Phase-7 sweep — 4 gap closures:**
- `WiomChip` Disabled state (skill §3.2): `bg.disabled` fill, no border, `text.disabled` label, close glyph hidden, click suppressed.
- `WiomPaginationScrollIndicator` `LazyListState` overload (skill §8): derives thumb size + offset from `listState.layoutInfo`; thumb floor at 10 % so it stays readable on long rails.
- `WiomNavItem` adds optional `iconSelected: ImageVector? = null` (skill §7 outline → filled glyph swap on Selected). Falls back to `icon` so callers without a separate filled glyph see the same icon in both states.
- `WiomTopBar(isDarkVariant: Boolean = false)` parameter added (skill §6). Dark variant uses `bg.brand.bold` + `text.inverse` + `icon.inverse` for premium / partner headers. Subtitle and Search state are guarded out on dark variant (no dimmed-inverse text token, no inverse-translucent pill fill). New `WiomTopBarStatusBar(isDarkVariant)` Composable pairs the OS status bar with the bar (light icons on dark / dark icons on light) per skill §6 — call once per screen after `enableEdgeToEdge`.

**Breaking changes** — see per-phase notes below for the complete diff. A non-exhaustive
summary:
- **WiomToast**: dropped `Neutral` status; all 4 types share `bg.default`; padding 16/12;
  message `bodyLg`; action always `text.brand`.
- **WiomBottomSheet**: dropped `IllustrationCta` enum; illustration block uses
  `WiomIconBadge.Lg`; header typography swap (`headingMd` / `bodySm`); action bar drops
  the top divider.
- **WiomDialog**: `dismissOnClickOutside` default flipped to `false`; body text style
  unified at `bodyMd`; main → buttons gap `space.huge` for **all 5** variants;
  illustration is a 144 dp full-width rect with `radius.large` (was a 120 dp circle);
  Selection rows use `WiomListItemRadio`; Loading uses `WiomSpinner`; new
  `WiomDialogButtonsLayout { Stacked, SideBySide }`.
- **WiomListItem**: API switched to `state: WiomListItemState` (was `enabled: Boolean`);
  `WiomListItemIconBadge` renamed to `WiomListItemIconBg`; trailing chevron tint switched
  to `icon.brand`; selected dot dropped; pressed-overlay added (Pressed XOR Selected).
- **WiomBadge**: count tones include `Neutral`; default cap raised to 99.
- **WiomSelectionControl**: bounding box 24×24 (was 20×20); glyph `iconSize.md`.
- **WiomLoader**: `WiomBlockingLoader` → `WiomBrandLoader`; new `WiomLinearProgress`
  loader-sibling and `WiomDots` typing-indicator.
- **WiomNavigationBar / WiomTopBar**: edge-to-edge contract — both consume their OS
  insets via `navigationBarsPadding()` / `statusBarsPadding()`; top-bar heights locked
  per Size variant.
- **WiomTabsFilters**: typography unified to `labelMd` across states; underline indicator
  uses `bg.brand` (element-first); chip Selected has the 1 dp brand border (per Figma
  ship) and a trailing 16 dp close glyph.
- **WiomPagination**: inactive tint switched to `bg.muted`; counter renders both
  chevrons with availability-based tinting (no more hide-with-Spacer); counter label
  `text.default`.
- **NEW components**: `WiomOtp` (boxed verification-code field), `WiomLinearProgress`
  (loader-sibling, separate from `WiomProgressIndicator`'s linear), `WiomDots` (chat
  typing indicator).

**Dropped** — `WiomStepper` (no V2 upstream skill); `WiomDropdown` (deleted in v1.0.0,
stays gone — use `WiomInput(readOnly = true)` + chevron + `WiomBottomSheet` picker).

### v2.0.0 rebuild — Phase 6 (Indicators)

**`WiomTabsFilters` (`wiom-tabs-filters` skill)**
- Pill Tab labels now use `type.label.md` for **both states** (skill §1.1 — pill tabs aren't body text). Was `labelMd` for selected, `bodyMd` for inactive.
- Pill Tab selected text colour switched to `text.selected` (semantic name) — same hex as `text.brand` but the canonical token for selected-state text.
- Pill Tab track no longer adds an extra inter-item gap on top of the `space-4` track padding (skill §1.1: items butt up against each other within the track; the radius isolates them visually).
- Underline Filter indicator now uses `bg.brand` (the element-first fill token) instead of `stroke.brandFocus` per skill §2.2 — "indicator is a 2 dp filled bar — a `bg.*` slot, not a `stroke.*` one".
- Underline Filter indicator now fills the item width even in scroll mode (skill §2.1 — "Indicator width: Fills item width"). The V1 `hugContent`-text-wrap branch is gone.
- Underline Filter labels use `type.label.md` for **both states** (skill §2.1).
- Underline Filter content padding corrected to `space.lg` H × `space.md` V (16/12) per skill §2.1.
- Chip Selected now ships with a 1 dp brand border per skill §3.2 ship value (the V1 "filled-state-rule, no border" was a Figma description error — Figma actually ships the border).
- Chip labels use `type.label.md` (SemiBold) for **all states** per skill §3.2 ship value (V1 used `bodyMd` Regular for unselected — Figma description was wrong).
- Chip Selected now renders a trailing 16 dp close ✕ in `icon.brand` per skill §3.1 (V1 had no close glyph).

**`WiomPagination` (`wiom-pagination` skill)**
- All inactive dot / bar tints switched from `stroke.subtle` to **`bg.muted`** per skill §5 (skill §10 explicitly corrects the V1 drift). Same change applies to `WiomPaginationDots`, `WiomPaginationBars`, and the existing scroll-indicator track which already used `bg.muted`.
- `WiomPaginationCounter` now **always renders both chevrons** per skill §6 rule 4 — at boundaries, the unavailable chevron tints to `icon.disabled` and consumes taps as a no-op (was hidden behind a 48 dp Spacer). The chevron footprint stays constant across page transitions.
- Tappable chevron tint switched to `icon.action` (was `icon.nonAction`) per skill §2 — chevrons in the counter are tappable chrome.
- Counter label colour switched to `text.default` per skill §5 — full contrast since it's the only text in the control (was `text.subtle`).

**`WiomProgressIndicator` (`wiom-progress-indicator` skill)**
- Existing helpers retained: `WiomLinearProgress` (Sm 4 dp / Md 8 dp · Brand / Info / Positive), `WiomLinearProgressIndeterminate`, `WiomCircularProgress`, `WiomCircularProgressIndeterminate`.
- **NEW** `WiomProgressCompletion` — rich completion meter per skill §1.2. Title (`labelMd`) + right-aligned value (`bodySm`) + status pill (`metaXs` with 6 dp brand-tinted dot) + Linear Md bar. State enum `WiomProgressCompletionState { JustStarted (20% · info · "Just started") · InProgress (60% · brand · "In progress") · AlmostThere (88% · positive · "Almost there") · Complete (100% · positive · "Complete") }` drives fill %, fill colour, and pill copy in lockstep.
- **NEW** `WiomProgressMilestones` — multi-stage tracker per skill §1.3. 2..5 named stages (`WiomMilestoneStage(label, icon)`); reached circles `bg.brand` + `icon.inverse`, unreached `bg.muted` + `icon.nonAction`; connector colour reflects whether the next stage was reached; per-stage label colour past=`text.default` / current=`text.brand` / future=`text.subtle`. Title `labelLg`, subtitle `bodySm`, label row `metaXs` centred under each circle.

### v2.0.0 rebuild — Phase 5 (Surfaces)

**`WiomNavigationBar` (`wiom-navigation-bar` skill)**
- Bar padding contract corrected per skill §2: top 0, bottom `space.md` (12 dp), no horizontal padding on the bar — items own their `space.xs H × space.sm V` padding.
- Bar now consumes the OS gesture / 3-button-bar inset via `navigationBarsPadding()`, so the container fill reads as continuous with the OS strip.
- Critical badge offset to `+2 dp` outside the icon's top-right (skill §2 anatomy).

**`WiomTopBar` (`wiom-top-bar` skill)**
- Bar consumes the status-bar inset via `statusBarsPadding()` so the surface reads as continuous with the OS strip.
- Heights locked per Size variant: Small 64 dp, Medium 112 dp, Large 152 dp. Action row inside Medium/Large is 48 dp.
- Search pill rebuilt: `space.lg` H × `space.sm` V padding, `space.md` gap between leading icon and field, 20 dp leading icon. The pill now wraps a real `BasicTextField` (was display-only with `@Suppress("UNUSED_EXPRESSION") onQueryChange`).
- Search state outer padding now asymmetric (`xs` start, `lg` end) per skill §2.
- Medium/Large title areas now use `weight(1f)` inside the fixed-height column with `Alignment.BottomStart` so the title sits where the skill anatomy diagrams place it.

**`WiomDialog` (`wiom-dialog` skill)**
- `dismissOnClickOutside` default flipped to `false` (skill §3 — scrim tap never dismisses).
- All dialog body / subtext text style switched from `bodyLg` (16/24) to `bodyMd` (14/20) — skill §2 specifies `type.body.md` for dialog body across every variant.
- Main → buttons gap is `space.huge` (48 dp) for **all 5 variants**. Input / Selection / Illustration helpers were on `space.xl` (24 dp); skill §2 explicitly corrects that drift.
- Illustration container is now a full-width 144 dp rectangle with `radius.large` (16 dp), per skill §2. Was a 120 dp circle (V1 anatomy that lives on the bottom sheet).
- Selection rows now render via `WiomListItemRadio` (Phase 4) instead of an inline `SelectionRow` — skill §2 says use `wiom-list-item` Type=Radio. Inline `SelectionRow` deleted.
- Loading dialog spinner now uses `WiomSpinner` (Phase 2) instead of Material's `CircularProgressIndicator` — skill §2 specifies `wiom-loader` Type=Spinner, size=md, color=brand.
- New `WiomDialogButtonsLayout { Stacked, SideBySide }` — every helper now exposes a `buttonsLayout` parameter. SideBySide places dismiss on the left and confirm on the right via `Modifier.weight(1f)` per Wiom button-group convention.

**`WiomBottomSheet` (`wiom-bottomsheet` skill)**
- `IllustrationCta` enum value dropped — V2 ships `Illustration` + `IllustrationLeft` (no separate `IllustrationCta`).
- Header rebuilt to skill §2: padding `8T · 16R · 16B · 16L`, slot gap `space.md`, title `headingMd` (was `headingLg`), subtitle `bodySm` (was `bodyMd`). New optional `leadingIcon` + `trailingActionLabel`/`onTrailingActionClick` slots.
- Action Bar drops the top divider and switches to `pb-16 · px-16 · pt-0` padding per skill §5 ("no top border on Action Bar — Content's pb-48 is the loose-gap separator").
- Illustration block rebuilt: now uses `WiomIconBadge.Lg` (96 dp, tone-swappable) instead of an inline 120 dp circle. Heading `headingMd` (was `headingLg`), subtext `bodyMd` (was `bodyLg`); padding `pt-16 · pb-48 · px-16`. New `tone` parameter resolves to `bg.{tone}.subtle` + `icon.{tone}` via `WiomIconBadgeTone`. `leftAligned = true` switches the layout to a Row (replaces the duplicate `IllustrationLeft` preview helper).

**`WiomToast` (`wiom-toast` skill)**
- Dropped `WiomToastStatus.Neutral` (the V1 inverse-surface variant). V2 ships only 4 status types — `Critical · Warning · Info · Positive`.
- All four types now share `bg.default` per skill §3 (was per-type tinted backgrounds — `bg.criticalSubtle`, `bg.warningSubtle`, etc.). Only the leading icon (and Warning's body text via `text.onWarning`) carries the type's status colour.
- Container radius `radius.medium` → `radius.small` (8 dp) per skill §2.
- Container padding `space.lg` all sides → `space.lg H × space.md V` (16/12) per skill §2.
- Message text style `bodyMd` (14 sp) → `bodyLg` (16 sp) per skill §2.
- Action label colour is now **always `text.brand`**, regardless of toast type. Was per-type — V1 used `text.onWarning` for Warning and `text.inverse` for the (now-deleted) Neutral.
- Close-icon tint always `icon.action` (was `visuals.iconTint`, which would render the close glyph in the type's status colour — wrong for Critical / Positive types).
- Preview gutters bumped from `space.sm` (8 dp) to `space.lg` (16 dp) — V2 toast aligns to the standard 16 dp gutter (skill §10 explicitly corrects the V1 "344 / 8 dp floating surface" drift).

### v2.0.0 rebuild — Phase 4 (List patterns)

**`WiomListItem` (`wiom-list-item` skill) — full rewrite**
- Trailing chevron tint switched from `icon.action` (neutral) to **`icon.brand`** (pink) per skill §5 — the chevron is the row's tap-affordance signal, not a generic accent. Disabled chevron stays `icon.disabled`.
- Removed the V1 selected dot (8 dp `bg.brand` circle in trailing slot). Selected = full-row `bg.selected` overlay only, per skill.
- Removed the auto-hide of the chevron when `selected = true` — the picker recipe passes `showTrailingChevron = false` explicitly.
- Added Pressed-state overlay via `MutableInteractionSource` + `collectIsPressedAsState`. **Pressed XOR Selected** — Selected wins per skill §6 rule 7.
- Outer padding corrected to 16 dp on all sides (was 16 H × 12 V).
- Vertical alignment is `Alignment.CenterVertically` always — no longer flips to `Top` when `secondary` is set.
- API: `enabled: Boolean` → `state: WiomListItemState` enum (`Default` / `Disabled`). Disabled is foundations Pattern A (token swap), never opacity.
- API: `WiomListItemIconBadge` renamed to **`WiomListItemIconBg`** to match skill enum value (`WiomListItemType.IconBg`). Now takes `leadingIcon: ImageVector` + `leadingTone: WiomIconBadgeTone` directly and instantiates `WiomIconBadge` internally — caller no longer needs to assemble the slot.
- API: `WiomListItemCheckbox` / `Radio` / `Switch` helpers now accept `selection` / `radioSelected` / `checked` + their callbacks directly; the helpers instantiate `WiomCheckbox` / `WiomRadio` / `WiomSwitch` (with `onToggle = null` so the row tap is the source of truth).
- IconBg disabled state synthesises Pattern A locally: `bg.disabled` circle + `icon.disabled` glyph (since `WiomIconBadge` ships no Disabled tone).
- `WiomListItemBase` slot API kept for custom rows (avatars, nested badges) — uses the same `state` enum.

### v2.0.0 rebuild — Phase 3 (Form atoms)

**`WiomButton` (`wiom-cta` skill)**
- Fixed CTA label centering bug — outer `Box` now uses `contentAlignment = Alignment.Center` so the hugging inner `Row` sits in the middle of the `fillMaxWidth()` container instead of the top-start corner.
- Loading spinner sized to `iconSize.md` (24 dp) per skill anatomy — was `iconSize.sm` (20 dp).
- `WiomAcknowledge` rebuilt on top of `WiomCheckbox` (24 dp from Phase 2) instead of the inline `AcknowledgeIndicator` (20 dp). Row alignment corrected to `Alignment.Top` per skill §9, label promoted from `bodyMd` → `labelLg`. Disabled state stops dimming the whole row — only the checkbox softens to `stroke.subtle`, label stays `text.default` per skill §4.6.

**`WiomInput` (`wiom-input-fields` skill)**
- Warning border now uses `stroke.warning` (gold) — Phase 1 added the soft `stroke.warning` token, so the v1.x `stroke.brandFocus` substitute is gone.
- Cursor uses `text.default` (skill §8) — was `stroke.brandFocus`.
- Search preview cancel icon corrected to `Icons.Rounded.Close` — was `Icons.Rounded.Error`.
- Removed the fake "OTP via Input + counter" preview — OTP now has its own component per skill §10 anti-pattern #2.

**`WiomOtp` (new — `wiom-input-fields` skill §6 / §8)**
- Boxed verification-code field. Single `BasicTextField` receives input; `decorationBox` renders 4 or 6 box children with `weight(1f)` so the row owns the width and the boxes scale with the parent.
- States: `Default` / `Focused` (active digit's box gets `stroke.medium` `stroke.brandFocus`; resting boxes `stroke.small` `stroke.subtle`) / `Filled` / `Error` / `Success` / `Disabled`.
- Helper row carries an optional right-aligned `timer` (e.g., `"00:24"`) per skill — Focused-variant timer is hardcoded in Figma but exposed in code as a string the caller drives from a countdown.
- Padding `space.md` H × `space.md` V, radius `radius.medium`, digit style `type.labelLg` (16/24 SemiBold) so the wrap lands at the skill's 48 dp height.

### Planned (v2.1+)
- Reinstate `WiomStepper` once an upstream skill exists in `wiom-design-system`
- `Loader/Status` ring (Wait → Success → Error) — skill §wiom-loader, deferred since V1
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
