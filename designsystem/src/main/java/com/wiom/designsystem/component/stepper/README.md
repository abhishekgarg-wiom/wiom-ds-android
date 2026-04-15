# WiomStepper

Shows progress through a **known, finite sequence** of 2–6 steps. Two layouts, shared indicator atom.

## Layouts

| Layout | Use |
|---|---|
| `WiomStepperHorizontal` | Compact wizards with 1–2 word labels pinned to the top of recharge / payment / KYC / onboarding flows |
| `WiomStepperVertical` | Status flows (KYC, installation, refund, order tracking) — title + description + optional action per step |
| `WiomStepIndicator` (atom) | 32dp circle, 4 states — for custom layouts / documentation |

## When NOT to use

- Unknown / open-ended step count → progress bar or `WiomPaginationBars`
- Single step → hide entirely
- > 6 steps → restructure
- Unrelated tasks → list with checkboxes
- Paging through long content → `WiomPaginationCounter`

## States

Derived automatically from `currentStep`. Override per-step via `stateOverrides` (e.g., mark one step `Error`).

| State | Visual |
|---|---|
| `Completed` | Filled `brand.primary` circle + white check |
| `Active` | White fill + 2dp `brand.primary` ring + brand-colored number |
| `Upcoming` | White fill + 1dp `border.default` ring + secondary-text number |
| `Error` | Filled `negative.primary` circle + white `!` |

## API

```kotlin
// Horizontal — wizard top
WiomStepperHorizontal(
    steps = listOf(
        WiomHorizontalStep("Plan"),
        WiomHorizontalStep("Method"),
        WiomHorizontalStep("Confirm"),
        WiomHorizontalStep("Pay"),
        WiomHorizontalStep("Done"),
    ),
    currentStep = 3,
)

// Vertical — KYC with inline OTP on the active step
WiomStepperVertical(
    steps = listOf(
        WiomVerticalStep("Personal info", "Name, DOB, email verified"),
        WiomVerticalStep("Address", "Installation address confirmed"),
        WiomVerticalStep(
            title = "Aadhaar",
            description = "Enter the OTP sent to your mobile",
            action = {
                WiomInput(
                    value = otp,
                    onValueChange = { otp = it },
                    title = "Enter 6-digit OTP",
                    counter = timer,
                )
            },
        ),
        WiomVerticalStep("Selfie", "Clear photo in good lighting"),
    ),
    currentStep = 3,
)

// Mark step 2 as error
WiomStepperVertical(
    steps = steps,
    currentStep = 2,
    stateOverrides = mapOf(2 to WiomStepState.Error),
)
```

## Action slot patterns (Vertical)

Pass any composable. Typical choices:

- `WiomButton(type = Primary)` — "Schedule installation", "Continue", "Pay ₹500"
- `WiomButton(type = Tertiary)` — "View details →", "Track →"
- `WiomInput(...)` — inline pincode / UPI entry
- OTP variant of `WiomInput` — verify step

Don't stuff two interactive elements in an action slot. Wrap them into one composable first.

## Rules

1. **Exactly one Active step** — handled automatically by `currentStep`.
2. **Max 6 steps.** Beyond that, split the flow or merge micro-steps.
3. **Horizontal labels are 1–2 words.** Use nouns, not verbs. "Pay" the step, not "Pay now" the action.
4. **Use Error sparingly.** Only when the user must go back to fix that specific step. Not for warnings.
5. **Connectors are automatic.** Brand-filled after Completed steps; `border.default` otherwise.
6. **Same Stepper across all step screens of a flow.** Don't redesign between screens — only `currentStep` changes.
7. **Don't mix Horizontal + Vertical** within the same flow.

## Tokens

- Indicator: 32dp · `radius.full`
- Active ring: `stroke.medium` (2dp) · `brand.primary`
- Upcoming ring: `stroke.small` (1dp) · `border.default`
- Inner glyph: 20dp (check or priority_high)
- Number typography: `type.labelMd` (14sp SemiBold)
- Connector: 2dp · `radius.full` · brand.primary (filled) or border.default
- Horizontal step column gap (indicator → label): `space.sm`
- Horizontal label typography: `type.labelSm` (12sp SemiBold)
- Vertical rail width: 32dp
- Vertical rail → content gap: `space.md`
- Vertical title → description gap: `space.xs`
- Vertical description → action gap: `space.sm`
- Vertical step content bottom gap: `space.xl`
