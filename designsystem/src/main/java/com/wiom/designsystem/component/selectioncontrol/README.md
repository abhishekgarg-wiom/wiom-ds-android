# WiomSelectionControl — Checkbox · Radio · Switch

Indicator-only selection primitives. Labels + helper text + row padding live on
the parent row (`WiomListItem`) — not inside these controls.

Source of truth: `.skills-cache/wiom-selection-controls.md`. Exposed as three
public composables that share internal tint / border / filled-state logic:
`WiomCheckbox`, `WiomRadio`, `WiomSwitch`.

---

## Variants

| Control     | Shape                | Selection values                       | States                      |
| ----------- | -------------------- | -------------------------------------- | --------------------------- |
| `Checkbox`  | 20×20 dp · rounded-4 | `No` · `Indeterminate` · `Selected`    | `Default` · `Disabled` · `Error` |
| `Radio`     | 20×20 dp · circle    | `false` · `true`                       | `Default` · `Disabled` · `Error` |
| `Switch`    | 52×32 dp · pill      | `false` · `true`                       | `Default` · `Disabled` (Pressed derived automatically) |

Switches have NO Error state — they are instant-apply and have no form
validation layer. Switches DO have Pressed (new in V2), derived from
`InteractionSource`.

## Filled-state rule

When a checkbox is Selected/Indeterminate or a radio is selected or a switch is
on, the fill IS the boundary — NO stroke. When unselected, a 2 dp
`stroke.medium` outlines the indicator in:

- `stroke.strong` (#473F55) — Default
- `stroke.subtle` (#D7D3E0) — Disabled
- `stroke.criticalFocus` (#D92130) — Error (checkbox + radio only)

On `Error + Selected` the fill is `bg.critical` (not `bg.brand`).

## API

```kotlin
@Composable
fun WiomCheckbox(
    selection: WiomCheckboxSelection,
    state: WiomIndicatorState,
    onToggle: (() -> Unit)?,
    modifier: Modifier = Modifier,
)

@Composable
fun WiomRadio(
    selected: Boolean,
    state: WiomIndicatorState,
    onSelect: (() -> Unit)?,
    modifier: Modifier = Modifier,
)

@Composable
fun WiomSwitch(
    checked: Boolean,
    state: WiomSwitchState = WiomSwitchState.Default,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
)
```

All three composables have fixed dimensions — callers must NOT pass `.size(...)`
/ `.width(...)` / `.height(...)` via `modifier`. `modifier` accepts alignment
and parent-driven padding only.

## Wiom use cases

- **Checkbox** — T&C consent (never pre-checked), multi-select plan comparison,
  partner onboarding checklists, Indeterminate parent in bulk-select trees.
- **Radio** — plan-selection critical path (monthly/quarterly/annual), payment
  method, language preference (English / हिन्दी), notification frequency.
- **Switch** — notification toggles, privacy settings, auto-pay, dark mode.
  Confirm-before-enable for billing-impacting switches.

## Rules

1. No label / helper / supporting-text baked in. Pair with `WiomListItem`.
2. No size parameter — 20×20 (checkbox/radio) and 52×32 (switch) are fixed.
3. Filled state = no border. Unselected state = `stroke.medium` border only.
4. No shadow, ever. All three are flat inline controls.
5. Checkbox `Error + Selected` uses `bg.critical` fill, not `bg.brand`.
6. Switches have NO Error state. If you think you need one, you want a checkbox.
7. Radio always in a group of 2+. Standalone radio is invalid.
8. Never pre-check a consent checkbox.

## Tokens

- Indicator stroke (unselected): `stroke.medium` (2 dp) + `stroke.strong` / `stroke.subtle` / `stroke.criticalFocus`
- Selected fill: `bg.brand` (Default) · `bg.disabled` (Disabled) · `bg.critical` (Error)
- Checkbox glyph: `Icons.Rounded.Check` (Selected) · `Icons.Rounded.Remove` (Indeterminate), tinted `icon.inverse`
- Radio dot: 10×10 dp, tinted `icon.inverse` / `icon.disabled`
- Switch track radius: `radius.full`
- Switch thumb: 24×24 dp `#FFFFFF` with 4 dp inset
- Shadow: none
