# CLAUDE.md — Rules for Claude Code in `wiom-ds-android`

Loaded on every session in this repo. Breaking these rules breaks the design system.

---

## 0. Product context

Wiom serves Indian households (as of 2026-04-23). Design assumptions:

- **India-only userbase.** No country-code prefixes on phone fields — use a leading phone icon.
- **Currency:** INR only (`₹`).
- **Languages:** Hindi + English (budget 1.3× text expansion).
- **Devices:** Budget Android, min SDK 24. Outdoor-readable.
- **Consumers:** **Partner app** (active Flutter → Kotlin rewrite — new screens build daily with this library) and **Customer app** (live in Kotlin, new features only).

---

## 1. Element-first tokens — hard rule

All colors are element-first. Four namespaces map 1:1 to Compose slots:

- `Box(background=…)`, `Modifier.background(…)`, `Surface(color=…)` → **`WiomTheme.color.bg.*`**
- `Text(color=…)` → **`WiomTheme.color.text.*`**
- `BorderStroke(…)`, `Modifier.border(…)`, `Divider` → **`WiomTheme.color.stroke.*`**
- `Icon(tint=…)` → **`WiomTheme.color.icon.*`**

Don't reach for role-first names like `color.brand.primary`, `color.warning.primary`, `colors.positive.soft` — those don't exist here. Element-first only.

Never use raw hex / sp / dp literals in component code. Foundation files are the only place those are defined.

---

## 2. Icons — Material 3 Icons Rounded only

- **Use:** `Icons.Rounded.*` from `androidx.compose.material:material-icons-extended` (already a transitive dep via the library).
- **Directional icons:** Use `Icons.AutoMirrored.Rounded.*` for any icon whose meaning flips in RTL (`ArrowBack`, `ArrowForward`, `KeyboardArrowLeft`, `KeyboardArrowRight`, `Send`, `List`, `ReceiptLong`, `ExitToApp`, `Login`, etc.). The non-AutoMirrored versions of these are deprecated in Jetpack Compose. Same Rounded shape, just auto-mirrors when the layout direction is RTL.
- **Wrapper:** `WiomIcon(imageVector, contentDescription, size, tint)` applies Wiom token defaults.
- **Never use:** `Icons.Default.*`, `Icons.Filled.*`, `Icons.Outlined.*`, `Icons.Sharp.*`, `Icons.TwoTone.*`.
- **`painterResource(R.drawable.…)`** is reserved for `ic_wiom_*` and `ic_partner_*` brand assets only — never for standard Material icons.
- **Tint** uses `WiomTheme.color.icon.*` tokens (`icon.action` for tappable chrome, `icon.nonAction` for decorative, `icon.inverse` on colored CTA fills). Never `text.on*` as an icon tint.

---

## 3. Typography defaults

- **Default reading text:** `type.bodyLg` (16sp Regular Noto Sans).
- **Default interactive text:** `type.labelLg` (16sp SemiBold) — CTA, input label, radio/checkbox/switch, toast action.
- **Page / dialog / bottom-sheet header:** `type.headingLg` (24 Bold). `headingMd` (20) only for compact secondary sheets.
- **Chat bubble body:** `type.bodyMd` (14) — intentional exception for density.
- **Never** sub-14sp for Hindi body text. `meta.xs` (10sp) only for chat-bubble timestamps and badge counts.

Noto Sans is loaded via Google Fonts Compose provider. Play Services must be available on the device. Fallback is handled by the provider.

---

## 4. Intrinsic sizing

Components MUST NOT set fixed `height` or `width` on wrappers that contain text. Height emerges from content + padding + stroke. Rationale: system font scaling.

`defaultMinSize(minHeight = 48.dp)` is OK for touch targets. Fixed `height(…)` / `size(…)` is not.

Exception: icon-only square touch targets, drag handles, thumbs, and atoms whose dimensions are part of their spec (checkbox 20dp indicator, switch 52×32 track).

---

## 5. Filled state = no border

Checked checkbox, toggled switch, selected chip, filled button — fill IS the boundary. Remove the stroke.

---

## 6. Status color mixing

Each status family (**positive / critical / warning / info**) is self-contained. A success icon never sits on a warning surface; a critical border never wraps a positive card.

**Warning is the one-token family** — only `text.onWarning` (#372902 olive) for body text on warning surfaces AND for warning-tinted inline text on `bg.default`. No `text.warning` exists.

**Text on banners:**
- Critical / Positive → `text.default` for body, `text.critical` / `text.positive` for heading + icon.
- Warning → `text.onWarning` everywhere in the warning family.
- Info → `text.default` or `text.info`.

---

## 7. Touch targets (three tiers)

- **Action** 48×48dp — high-consequence icons (delete, edit, share, close, menu, back)
- **Navigation** 32×32dp — frequent-use (expand, sort, arrows, pagination, filter)
- **Info** 24×24dp — low-stakes (help, tooltip triggers, status indicators)

Buttons reach 48dp naturally via `space.md` vertical padding + 24sp line-height.

---

## 8. Shadow vs border — never both

Flat → 1px border. Elevated → shadow. Exception: critical payment cards may pair `shadow.md` + `stroke.small` + `stroke.subtle` for budget-device robustness.

`shadow.xl` always pairs with `overlay.scrim` (modal backdrop).

---

## 9. Repo layout

```
designsystem/src/main/
├── java/com/wiom/designsystem/
│   ├── foundation/
│   │   ├── color/       ← WiomColors (bg/text/stroke/icon/overlay)
│   │   ├── typography/  ← WiomTypography (Noto Sans via Google Fonts)
│   │   ├── spacing/     ← WiomSpacing (4px grid)
│   │   ├── radius/      ← WiomRadius
│   │   ├── stroke/      ← WiomStroke + focus-ring tokens
│   │   ├── shadow/      ← WiomShadow
│   │   └── icon/        ← WiomIconSize, WiomIcon (Material 3 Rounded wrapper)
│   ├── theme/
│   │   └── WiomTheme.kt ← root theme + WiomTheme.color / .type / .spacing / .radius / .stroke / .shadow / .iconSize
│   └── component/
│       └── <name>/
│           ├── Wiom<Name>.kt
│           └── README.md
└── res/
    ├── drawable/        ← only ic_wiom_* / ic_partner_* brand assets
    └── values/
        └── font_certs.xml ← Google Fonts provider certs
```

---

## 10. Skill precedence

`wiom-design-foundations` is the **single source of truth** for tokens. When a component skill cites a value that conflicts with the foundation, **treat it as a skill bug and flag** — don't encode the drift.

Current cached skills (post–V2 refresh): `.skills-cache/` in the repo (gitignored).

---

## 11. When building or modifying a component

1. Read the component's skill in `.skills-cache/`.
2. Cross-check every token citation against `wiom-design-foundations`.
3. Use **only** `WiomTheme.color.*` / `type.*` / `spacing.*` / `radius.*` / `stroke.*` / `shadow.*` / `iconSize.*`.
4. Use **only** `Icons.Rounded.*` for icons.
5. Follow intrinsic-sizing rule.
6. Provide `modifier: Modifier = Modifier` as first optional param.
7. Add `@Preview` covering every variant × state.
8. Write a short README.md in the component folder: purpose, variants, API, Wiom use cases, rules, tokens.
9. Update `CHANGELOG.md` under `[Unreleased]`.
10. Propose the version bump when done.

---

## 12. Hard prohibitions

- Raw hex / sp / dp literals outside `foundation/`
- `Icons.Default` / `Icons.Filled` / `Icons.Outlined` / `Icons.Sharp` / `Icons.TwoTone`
- `painterResource` for a non-brand icon
- Fixed heights on text-bearing components
- Border + shadow on the same component
- Border on a filled/checked/toggled component
- Legacy role-first tokens (`color.brand.primary`, `warning.soft`, etc.)
- Pre-checked consent checkboxes
- Red button for non-destructive actions
- Titles + subtitles concatenated into one string
- `WiomDropdown` — not exposed; use `WiomInput(readOnly = true)` + trailing chevron + `WiomBottomSheet` picker (canonical V2 picker pattern)
