# WiomCheckbox

Binary or multi-select form control. Always needs a visible label.

## When to use

- User selects one or more options from a visible set
- User confirms or agrees to a statement (consent, T&C, privacy)
- User enables/disables a form-submit preference

## When NOT to use

- Exactly one from a set → `WiomRadio`
- Binary on/off with instant effect → `WiomSwitch`
- Filter tags → Chip (coming later)

## API

```kotlin
WiomCheckbox(
    checked = state,
    onCheckedChange = { state = it },
    label = "I agree to the Terms & Conditions",
    helper = "You can review the full terms before agreeing",
    isError = showError,
    errorText = "You must agree to continue",
    enabled = true,
)
```

## Wiom use cases

- **Consent (required).** Recharge confirmation, auto-debit authorization, plan activation. Never pre-check.
- **Multi-select.** Plan features, filter screens ("4G only", "Under ₹200", "OTT included").
- **Preferences.** Notification settings — e.g., "Send me offers via SMS".
- **Partner onboarding.** Checklist steps marked as complete programmatically.

## Rules

1. **Never pre-check consent.** User must actively opt in.
2. **Filled = no border.** When `checked`, stroke is removed (enforced internally).
3. **Label is always `type.bodyMd`** (Regular) — it's reading content, not control text.
4. **Error text replaces helper text** when `isError = true` and `errorText` is set.
5. **Whole row is the tap target** — parent applies vertical padding for 48dp touch spacing.

## Tokens

- Indicator: 20dp · `radius.tiny` (4dp) · `stroke.medium` (2dp) · `shadow.none`
- Check icon: 16dp · `text.inverse` · `WiomIcons.check` (Material Symbols Rounded filled)
- Fill (checked): `brand.primary` → error: `negative.primary`
- Stroke (unchecked): `border.strong` → error: `negative.primary` → disabled: @ 40%
- Label: `type.bodyMd` · `text.primary`
- Helper/Error: `type.bodySm` · `text.secondary` / `negative.primary`
- Gap indicator → label: `space.sm` · label → helper: `space.xs`
