# Prompt — Refresh wiom-ds-android to V2 skills (v1.0.0)

Load project memory **Wiom DS Android**. Work in `C:\Users\Abhishek\wiom-ds-android`.
Source of truth: the `wiom-*` skills on `github.com/abhishekgarg-wiom/wiom-design-system`
**after** the V2 refresh (PR #11, merged 2026-04-23). Fetch fresh skill content via `gh api`
before coding.

Rule of precedence: `wiom-design-foundations` wins when any component skill disagrees.

**You have permission to rebuild from scratch if that produces a cleaner v1.0.0.** No one is
on the current v0.3.0 JitPack artifact, so backward compatibility is a non-goal.

---

## Context — why this matters now

- **Partner app** is being actively rewritten from Flutter to Kotlin. New Compose code is
  landing **daily**. Those new screens must use `wiom-ds-android` from day one — the
  whole point of this release is to arm that team before they build another hardcoded
  screen.
- **Customer app** is already live in Kotlin. New features there also adopt Wiom going
  forward. Existing screens stay untouched — no migration.

---

## Goal

Two things in one release (**v1.0.0**):

1. **Align the library to V2 skills** (breaking rename + component reshape).
2. **Ship an Adoption Kit** so new feature work in Partner (primary) and Customer
   (secondary) apps uses Wiom by default. Old code stays untouched. Opting out for a
   new feature must cost more than opting in.

---

## Part 1 — Library refresh

### Tokens
- **Element-first rename:** `brandPrimary → bgBrand`, `negativePrimary → bgCritical`,
  `textPrimary → textDefault`, `surfaceBase → bgDefault`, plus new `strokeBrandFocus`,
  `strokeCriticalFocus`, `iconAction`, `iconNonAction`.
- **Warning family:** add `text.onWarning` (#372902 olive); drop brown `warning.primary`
  if skill says so.

### Typography — Noto Sans via Google Fonts Compose provider
- Replace `FontFamily.Default` with a `GoogleFont.Provider`-backed `FontFamily` for
  **Noto Sans** (Regular 400, Medium 500, SemiBold 600, Bold 700).
- Ship the standard Google Fonts cert array resource. Gate on Play Services Fonts
  availability; fall back to `FontFamily.Default` if unavailable.

### Icons — Material Symbols Rounded, properly imported (not hand-crafted)
- The `wiom-design-foundations` icon skill mandates Material Symbols Rounded **filled**,
  weight 400. Current library ships ~16 hand-drawn drawables — replace with a first-class
  Material Symbols pipeline.
- **Preferred approach:** bundle Material Symbols Rounded as a font asset and expose
  icons via a typography-ligature `Icon` composable, **or** pre-generate Android Vector
  Drawables from the official
  [`@material-symbols`](https://github.com/marella/material-symbols) / Google Fonts
  [`material-design-icons`](https://github.com/google/material-design-icons) repo's
  Rounded **filled** set.
- Whichever approach: the public API stays `WiomIcons.search`, `WiomIcons.phone`, etc.
  Existing consumers don't care how it's resolved underneath.
- Ship **every glyph referenced by any V2 skill** (audit the cached skill files; likely
  40–60 icons). Document how to add new ones in `WiomIcons.kt`.

### Components
- **Merge** `WiomCheckbox` + `WiomRadio` + `WiomSwitch` → `WiomSelectionControl`
  (indicator-only). Labels move to `WiomListItem` params. Checkbox gains **Indeterminate**;
  Switch gains **Pressed**; remove Hover/Focused.
- **NEW `WiomIconBadge`** — 3 sizes × 6 tones.
- **NEW `WiomDialog`** — Alert / Input / Selection / Illustration / Loading variants.
- **`WiomButton`:** add **Pre-booking** type; remove Focus as a visual variant (focus
  ring uses `strokeBrandFocus` / `strokeCriticalFocus`).
- **`WiomBottomSheet`:** add Share / Illustration Left / Form sizes (8 total).
- **`WiomListItem`:** add `icon-with-bg` 5th Type (leading = `WiomIconBadge` instance).
- **DELETE `WiomDropdown`** outright. No deprecated stub. Document the replacement
  pattern in `MIGRATION.md` and in the `WiomInput` README: dropdown field =
  `WiomInput(readOnly = true)` + chevron + `onClick` → `WiomBottomSheet` picker.

---

## Part 2 — Adoption Kit

Produce inside this repo:

1. **`designsystem-rules/` module** — Detekt rule set published as
   `com.github.abhishekgarg-wiom.wiom-ds-android:designsystem-rules:v1.0.0`, consumed
   via `detektPlugins(...)`.
   - `WiomTokenRule` — flags raw `Color(0xFF…)` / `.sp` / `.dp` literals in `@Composable`
     bodies. **Severity: error.**
   - `WiomIconImportRule` — flags `androidx.compose.material.icons.*` imports.
     **Severity: error.**
   - `WiomMaterialComponentRule` — flags Material3 `Button`, `OutlinedTextField`,
     `Switch`, `Checkbox`, `RadioButton`, `TopAppBar`, `NavigationBar`, `ModalBottomSheet`
     and suggests the Wiom equivalent in the message. **Severity: warning.**
   - **Opt-out mechanism:** file-level `@file:Suppress("WiomNotUsed")` **+** inline
     `// non-wiom: <short reason>` comment **+** ADR link in the PR body.

2. **`adoption-kit/pr-template.md`** — consumer apps paste into
   `.github/pull_request_template.md`:
   ```
   ### Wiom Design System
   - [ ] New UI uses Wiom components and tokens
   - [ ] If not: link the ADR explaining why
   ```

3. **`adoption-kit/adr-template.md`** — for "we're NOT using Wiom for feature X".
   Fields: what we built instead, why Wiom didn't fit, what would make it fit, owner to
   revisit by when.

4. **`adoption-kit/consumer-CLAUDE.md`** — drop-in for consumer app repos. Tells future
   Claude sessions: "new feature code MUST use Wiom; to bypass, open an ADR first".

5. **`ADOPTION.md`** in repo root — one-pager:
   - **Partner app (active migration)** — adopt library immediately; every new Kotlin
     screen in the Flutter → Kotlin rewrite uses Wiom components.
   - **Customer app (live)** — adopt for new features only; existing screens untouched.
   - Steps: install library → enable Detekt rules → paste PR template → done.

---

## Part 3 — Release

- Bump to **v1.0.0**. Keep v0.3.0 tag live on JitPack for any stray consumer.
- Update CHANGELOG with the V2 diff (tokens, merged components, deleted Dropdown,
  new components, Material Symbols, Noto Sans).
- Update per-component READMEs.
- Rewrite `MIGRATION.md` → replace "migrate every screen" framing with
  "new = Wiom, old stays, opt out via ADR". Include the WiomDropdown → WiomInput
  replacement pattern here, since there's no deprecated stub to fall back on.
- Sample app demos every component, including new WiomIconBadge, WiomDialog, and
  3 new bottom-sheet sizes.
- CI green, APK uploaded as artifact before tagging.

---

## Non-goals

- Do not migrate existing app code. Do not touch Customer app or Partner app UI.
- Do not deprecate v0.3.0 on JitPack (just let it age).
- Do not edit consumer app repos this session — only deliver drop-in files.
- Do not preserve `WiomDropdown`. It's gone. No stub, no alias.

---

## Exit criteria

- **v1.0.0** tagged, CI green, sample APK attached.
- `designsystem-rules` artifact resolvable via JitPack and usable via `detektPlugins(...)`.
- `adoption-kit/` has PR template, ADR template, consumer CLAUDE.md.
- `ADOPTION.md` answers "how do we start using this today?" in under 2 minutes of
  reading, with a named call-out to the Partner Flutter → Kotlin migration.
- Every V2-skill-referenced icon ships in `WiomIcons`; Noto Sans renders via Google
  Fonts provider.
- `WiomDropdown` no longer exists in the codebase — only documented replacement
  pattern remains.
