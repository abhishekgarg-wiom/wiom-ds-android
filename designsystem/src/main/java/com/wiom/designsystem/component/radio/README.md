# WiomRadio

Single-choice selection within a group of 2–5 options. Pre-select one by default.

## When to use

- Pick exactly one from a short visible set (plan, payment method, language)
- Single-choice question in onboarding or filters

## When NOT to use

- Multiple selections → `WiomCheckbox`
- Binary on/off instant-apply → `WiomSwitch`
- 6+ options → `WiomDropdown`
- Single radio alone → invalid; always 2+

## API

```kotlin
var selected by remember { mutableStateOf("upi") }

Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md)) {
    WiomRadio(selected = selected == "upi",  onClick = { selected = "upi" },  label = "Pay via UPI", helper = "Instant transfer from your bank")
    WiomRadio(selected = selected == "card", onClick = { selected = "card" }, label = "Debit / Credit Card")
    WiomRadio(selected = selected == "cod",  onClick = { selected = "cod" },  label = "Cash on delivery")
}
```

## Wiom use cases

- **Plan selection.** Recharge/upgrade screens — pre-select recommended plan.
- **Payment method.** UPI, card, COD — pre-select most popular.
- **Language.** "English" / "हिन्दी". Component must render equally at both scripts.
- **Notification frequency.** Immediately / Daily digest / Weekly / Never.

## Rules

1. **Always in groups of 2+.** Single radio is an anti-pattern.
2. **Pre-select one option by default** unless there's a product reason not to.
3. **Indicator is circular** (`CircleShape` = `radius.full`) — this is what distinguishes it visually from Checkbox.
4. **V1: no hover/focus states** — mobile-first.
5. **Error usually lives at the group level**, below all items ("Please select an option to continue"). Use per-item `isError` for the red border only when the whole group is invalid.

## Tokens

- Indicator: 20dp · `CircleShape` · `stroke.medium` (2dp) · `shadow.none`
- Inner dot: 10dp · `CircleShape` · `text.inverse`
- Fill (selected): `brand.primary` → error: `negative.primary`
- Stroke (unselected): `border.strong` → error: `negative.primary` → disabled @ 40%
- Label: `type.bodyMd` · `text.primary` → disabled: `text.disabled`
- Helper: `type.bodySm` · `text.secondary`
- Gap indicator → label: `space.sm` · label → helper: `space.xs`
- Stack gap between radios: `space.md`
