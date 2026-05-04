# Upstream skill PRs — queue against `wiom-design-system`

Findings collected during the v2.0.0 Android rebuild. Each item is a clarification or
fix to the corresponding `SKILL.md` in
[`github.com/abhishekgarg-wiom/wiom-design-system`](https://github.com/abhishekgarg-wiom/wiom-design-system)
that — if applied — would let the next consumer derive the right Android code on the
first try.

These are not bugs in V2 (which is what the Android rebuild now matches). They're
edge cases the skill text doesn't currently spell out.

| # | Skill | Section | Finding |
|---|---|---|---|
| 1 | `wiom-cta` | §9 (Compose API + code) | The example wraps the inner `Row` inside a `Surface`, which auto-sizes to its content. Real-world callers wrap the `Row` inside a `Box(modifier.fillMaxWidth())` instead — the `Arrangement.spacedBy(_, Alignment.CenterHorizontally)` only centers when the Row is wider than its content. The `Box` therefore needs `contentAlignment = Alignment.Center` (or the Row needs `fillMaxWidth()`). Phase 3 surfaced this as a real CTA-label-centering bug in the v1.x Android code. **Fix:** add a one-line note to §9 / §7 universal rules calling out the constraint. |
| 2 | `wiom-input-fields` | §6 (OTP anatomy) | The boxed-OTP variant doesn't specify a typography token for the digit text. Implementations have to guess. Skill anatomy says "wrap = 12 V + 24 LH + 12 V = 48 dp" so any 16/24 token works (`type.label.lg` or `type.body.lg`). **Fix:** name the token explicitly. The Android impl picked `type.label.lg`. |
| 3 | `wiom-input-fields` | §6 (OTP) | Recommended Compose pattern (single `BasicTextField` + `decorationBox` rendering N visual cells) isn't called out — designers / implementers reach for `N` separate fields with manual focus chaining, which is the wrong pattern. **Fix:** add the canonical Compose snippet to §8 like the other helpers have. |
| 4 | `wiom-loader` | Typing dots | 10 dp vertical padding inside the dots cell is off the 4-dp grid. **Fix:** specify `space-12` (or document the intentional off-grid choice). |
| ~~5~~ | ~~`wiom-tabs-filters`~~ | ~~§3~~ | ~~Chip Disabled state missing in code~~ — **closed in v2.0.0 post-Phase-7 sweep**: `WiomChip(..., enabled = false)` ships per skill §3.2 (Pattern A — `bg.disabled` fill, no border, `text.disabled` label). Skill is correct as-is; no PR needed. |
| 6 | `wiom-tabs-filters` | §1.5 (4+ pill tabs) | The "horizontally scrollable" fallback for 4+ pill tabs is described in prose but no example. The Android code allows up to 4 hard-capped (no scroll fallback). **Fix:** either add the scroll-fallback example to the skill or tighten the cap to 3 in code (matching Figma `Count = 1/2/3`). |
| 7 | `wiom-progress-indicator` | §1.3 (Milestones glyphs) | Stage glyphs are direct-edit per instance in Figma (no `INSTANCE_SWAP` property). The Compose API takes them as `WiomMilestoneStage(label, icon)`, but the skill doesn't say what icon family they should come from. **Fix:** name `Icons.Rounded.*` (or `painterResource(R.drawable.ic_*_rounded)` for brand-specific glyphs) explicitly. |
| 8 | `wiom-bottomsheet` | §2 (Header) | Skill ships title `headingMd` + subtitle `bodySm` for the Header — but the previous V1 doc said `headingLg` + `bodyMd`. Drift was caught in this rebuild. **Status:** V2 skill is correct; flagged here for traceability. No PR needed unless older docs in the repo still cite the V1 sizes. |
| **9** | `wiom-design-foundations` | `references/color/primitives.md` (Neutral_700) | `primitives.md` had `Neutral_700 = #665E75`; `core-tokens.md`, `chat.md`, `contrast.md` all use `#5C5570`. The `contrast.md` 6.70 AA ratio is computed against `#5C5570`, so that's the canonical hex. 11 component SKILLs propagated the bad `#665E75` value. **PR filed:** [`wiom-design-system#14`](https://github.com/abhishekgarg-wiom/wiom-design-system/pull/14) — fixes `primitives.md` + 8 component SKILLs (13 swap sites). Android already uses `#5C5570` (matches core-tokens.md). |

## Filed PRs
- [`wiom-design-system#13`](https://github.com/abhishekgarg-wiom/wiom-design-system/pull/13) — items 1, 2, 3, 4, 6, 7 queued as a `clarifications-from-android-v2-rebuild.md` file at the upstream root for the maintainer to action at their pace.
- [`wiom-design-system#14`](https://github.com/abhishekgarg-wiom/wiom-design-system/pull/14) — item 9, mechanical hex fix across primitives.md + 8 component SKILLs.

## Workflow

1. Open one PR per item (smaller diffs, easier review).
2. Each PR amends the relevant skill's `SKILL.md` with the clarification + a `references/...` snippet if a code example is needed.
3. After merge, re-run the V2 sweep on the affected component in this repo (`wiom-ds-android`) — most fixes here will not require code changes since the Android impl already followed the implicit V2 ship value, but the skill text should match the code.
