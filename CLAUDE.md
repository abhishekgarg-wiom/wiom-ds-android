# CLAUDE.md — Rules for Claude Code in the Wiom Design System repo

This file is loaded automatically whenever Claude Code operates in this repository. Every session must follow these rules. Violations will be caught by review.

---

## 0. Product context

Wiom serves Indian households (as of 2026-04-15). Every design decision should assume:

- **India-only userbase.** No country code prefixes in phone fields — use leading phone icon.
- **Currency:** INR only (`₹` prefix).
- **Languages:** Hindi + English. Budget for 1.3x text expansion.
- **Devices:** Budget Android (min SDK 24). Small screens, outdoor use, variable lighting.
- **Tone:** Trust-heavy (payments, recharges, plans). Low cognitive load. Clear and scannable.

---

## 1. Token-only rule (hard block)

**No raw hex, sp, dp, or font family literals outside `designsystem/foundation/`.**

Every visual value must come from `WiomTheme.colors`, `WiomTheme.type`, `WiomTheme.spacing`, `WiomTheme.radius`, `WiomTheme.stroke`, `WiomTheme.shadow`, or `WiomTheme.icon`.

**Allowed:**
```kotlin
Modifier.padding(WiomTheme.spacing.lg)
color = WiomTheme.colors.text.primary
```

**Forbidden:**
```kotlin
Modifier.padding(16.dp)                    // use WiomTheme.spacing.lg
color = Color(0xFF161021)                  // use WiomTheme.colors.text.primary
fontSize = 14.sp                            // use WiomTheme.type.bodyMd
```

Exception: inside foundation files themselves, where the canonical values are defined.

---

## 2. Intrinsic sizing rule (accessibility-critical)

**Components MUST NOT set fixed `height` or `width` on wrappers.**

Size must emerge from content + padding + stroke. Rationale: when users increase system font size, fixed-height components clip or overflow.

**Allowed:**
```kotlin
// Height emerges from text line-height + vertical padding
Row(modifier = Modifier.padding(
    horizontal = WiomTheme.spacing.lg,
    vertical = WiomTheme.spacing.md,
)) { Text(...) }
```

**Forbidden:**
```kotlin
Modifier.height(48.dp)          // hardcoded height breaks font scaling
Modifier.size(56.dp)            // fixed size on a text-bearing component
```

**Exception:** icon-only square touch targets (e.g., a 48dp icon button for a11y). Must be documented in the component README with justification.

---

## 3. Icon sourcing rule

**Consumers must use `WiomIcons.<name>` only.** Never import `Icons.Default`, `Icons.Filled`, `Icons.Outlined`, `Icons.Sharp`, or `Icons.TwoTone`.

To add a new icon:
1. Download from https://fonts.google.com/icons?icon.style=Rounded (filled=1, weight=400)
2. Convert SVG to Android Vector Drawable
3. Place in `designsystem/src/main/res/drawable/wiom_ic_<name>.xml`
4. Add `@DrawableRes val <name>` to `WiomIcons.kt`

Use a custom drawable (outside `WiomIcons`) only when the glyph is visually unique to a product surface and cannot be represented by a standard Material Symbols icon.

---

## 4. Use-case documentation rule

Every component README must include a **"When to use which variant"** section with concrete Wiom examples — not generic ones.

Example (good):
> **Phone number → leading `phone` icon, no prefix.** Reason: Wiom users are Indian-only as of 2026. `+91` prefix is redundant and adds friction.

Example (bad — generic):
> "Use leading icon to show the type of input."

---

## 5. Component authoring checklist

When adding or modifying a component, every change must:

- [ ] Use only `WiomTheme.*` tokens (no hex/dp/sp literals)
- [ ] Follow intrinsic sizing rule (no fixed height/width)
- [ ] Expose `modifier: Modifier = Modifier` as first optional param
- [ ] Include `@Preview` for every variant × state combination
- [ ] Include a README.md in the component folder with: purpose, when to use / not to use, variants, states, all params (typed), usage examples, do/don't, accessibility notes, changelog
- [ ] Have entries in the sample app (`:sample`) showing real usage
- [ ] Pass `./gradlew :designsystem:assembleRelease` cleanly

---

## 6. Versioning rules

Semantic versioning. Tag format: `v<major>.<minor>.<patch>`.

- **Major** (1.0.0 → 2.0.0) — breaking public API change (removed/renamed params, removed components)
- **Minor** (0.1.0 → 0.2.0) — new component, new param with default, new variant
- **Patch** (0.1.0 → 0.1.1) — bug fix, visual tweak that doesn't change API

Pre-1.0 rules are relaxed — minor bumps may include breaking changes. Document in CHANGELOG.md.

---

## 7. Commit style

Conventional Commits. First line under 72 chars.

```
feat(input): add OTP variant with resend countdown
fix(button): correct disabled token to text.disabled
breaking(topbar): rename `subtitle` to `description`
chore(ci): bump AGP to 8.8.0
docs(input): add phone number use-case example
```

---

## 8. Repo layout (authoritative)

```
designsystem/src/main/
├── java/com/wiom/designsystem/
│   ├── foundation/
│   │   ├── color/       ← 43 semantic color tokens
│   │   ├── typography/  ← 13 type tokens (Noto Sans)
│   │   ├── spacing/     ← 13 spacing tokens (4px grid)
│   │   ├── radius/      ← 7 radius tokens
│   │   ├── stroke/      ← 2 stroke tokens
│   │   ├── shadow/      ← 5 elevation tokens
│   │   └── icon/        ← WiomIconSize, WiomIcon composable, WiomIcons facade
│   ├── theme/
│   │   └── WiomTheme.kt ← root theme composable + token accessor
│   └── component/
│       └── <name>/
│           ├── Wiom<Name>.kt
│           ├── Wiom<Name>Previews.kt
│           └── README.md
└── res/drawable/
    └── wiom_ic_*.xml    ← Material Symbols Rounded icon set
```

---

## 9. What to do when asked to build a new component

1. **Load `wiom-design-foundations`** — this is the **single source of truth** for colors, typography, spacing, radius, stroke, shadow, iconography. Every design decision cross-checks against it. (Note: `wiom-visual-skill` is a separate philosophy / principles document and is **not** used to drive component code — don't derive tokens or spacings from it.)
2. **Load the component-specific skill** (e.g., `wiom-input-fields`, `wiom-top-bar`, `wiom-stepper`). These skills are the **per-component spec** and cite tokens that live in `wiom-design-foundations`.
3. **Ask for the Figma link** if not provided. Do not invent visual details.
4. **Get Figma context** via the `mcp__figma__get_design_context` tool (user must select the node in Figma desktop first).
5. **Check if Material Symbols Rounded has the required icons.** If yes, add to `WiomIcons`. If no, ask the user whether to create a custom drawable.
6. **Follow checklist in Rule 5.**
7. **Propose the version bump** when done (patch/minor/major).

**Rule of precedence when skills disagree:** `wiom-design-foundations` wins. If a component-specific skill cites a token value that differs from the foundation, treat it as a bug in the component skill and flag it — don't encode the drift.

---

## 10. What NOT to do

- Do not modify `WiomColors` hex values without explicit approval. These come from the `wiom-design-foundations` skill and must stay in sync with Figma.
- Do not add new tokens. If a gap exists, surface it — don't paper over it with a raw value.
- Do not add a component without loading the design foundations skill first.
- Do not commit `local.properties`, `*.keystore`, or any file containing secrets.
- Do not use `Icons.Default.*`. Period.
- Do not set fixed heights on components that contain text.
- Do not mix feedback color families (e.g., red border on a positive card).
- Do not publish new versions without updating `CHANGELOG.md`.

---

## Open TODOs for the next Claude session

- Wire Google Fonts provider for Noto Sans (v0.1 uses `FontFamily.Default`).
- Add Paparazzi screenshot testing.
- Add Detekt lint rule that fails on raw hex/dp/sp outside `foundation/`.
- Build `WiomInput` component from Figma (`node-id=1329:120`).
- Grow `WiomIcons` drawable set as components demand.

Ask the user before starting any of these unless explicitly requested.
