# WiomStepper

Progress through a **known, finite sequence** of 2–6 steps. Two layouts; one shared indicator atom.

| Composable | Use for | Avoid for |
|---|---|---|
| `WiomStepperHorizontal` | 2–5 step wizards with 1–2 word labels (recharge, payment, KYC top). | Steps that need explanation. >6 steps. |
| `WiomStepperVertical` | Status flows (KYC, installation, refund, order tracking). Steps that carry a title + description + maybe an action. | Compact UIs without vertical room. |
| `WiomStepIndicator` (atom) | Documentation / custom layouts only. | Real screens — use the layouts. |

If steps are unknown or open-ended, use `WiomPaginationBars` or a progress bar instead.

## API

```kotlin
enum class WiomStepState { Completed, Active, Upcoming, Error }

data class WiomHorizontalStep(val label: String)

data class WiomVerticalStep(
    val title: String,
    val description: String? = null,
    val action: (@Composable () -> Unit)? = null,
)

WiomStepIndicator(
    state: WiomStepState,
    number: Int,
    modifier: Modifier = Modifier,
)

WiomStepperHorizontal(
    steps: List<WiomHorizontalStep>,       // 2..6
    currentStep: Int,                       // 1..steps.size
    modifier: Modifier = Modifier,
    stateOverrides: Map<Int, WiomStepState> = emptyMap(),
)

WiomStepperVertical(
    steps: List<WiomVerticalStep>,         // 2..6
    currentStep: Int,
    modifier: Modifier = Modifier,
    stateOverrides: Map<Int, WiomStepState> = emptyMap(),
)
```

## States

| State | Visual |
|---|---|
| `Completed` | `bg.brand` fill + white `Icons.Rounded.Check` (20dp) |
| `Active` | `bg.default` fill + 2dp `stroke.brandFocus` ring + brand-colored number |
| `Upcoming` | `bg.default` fill + 1dp `stroke.subtle` ring + `text.subtle` number |
| `Error` | `bg.critical` fill + white `Icons.Rounded.PriorityHigh` (20dp) |

State derivation: `step < current` → Completed; `step == current` → Active; `step > current` → Upcoming. Use `stateOverrides` (1-based index → `WiomStepState.Error`) to mark a failed step.

## Wiom use cases

- **Recharge / payment wizard** (Horizontal): 5 steps "Plan · Method · Confirm · Pay · Done"; pinned at top of every step screen.
- **KYC** (Vertical): "Personal info · Address · Aadhaar · Selfie"; Aadhaar step exposes an OTP field via the action slot.
- **Installation tracking** (Vertical): "Order placed · Approved · Dispatched · Installed · Activated"; the current step might have a tertiary "Reschedule →" action.
- **Order error** (Vertical): override one step to `Error`, use description to explain ("Aadhaar blurred — tap to retry"), attach a Primary CTA via the action slot.

## Action slot (vertical only)

`WiomVerticalStep.action` is a `@Composable () -> Unit`. Pass anything: `WiomButton`, a tertiary CTA, `WiomInput`, OTP field, a custom Input+Button group. One component per slot — if a step needs two controls, wrap them in your own composable first.

## Rules

1. **One Active step at a time.** Don't override two steps to Active.
2. **Steppers are for known, finite sequences.** Not for open-ended checklists or unknown totals.
3. **Max 6 steps.** Beyond that, split the flow.
4. **Horizontal labels are 1–2 words** (nouns: "Plan", "Pay", not verbs like "Pay now").
5. **Connector color matches what came before** — brand-filled when previous is Completed, `stroke.subtle` otherwise. Error does not fill the connector.
6. **Use `Error` sparingly** — only when the user must return to fix the step.
7. **Same stepper across all screens of a flow** — only `currentStep` changes between screens.
8. **Token-only values.** Colors, sizes, spacing, radius, stroke come from `WiomTheme.*`.

## Tokens

### StepIndicator
| Part | Token |
|---|---|
| Size | 32 × 32 dp · `CircleShape` |
| Completed fill | `bg.brand` |
| Completed icon | `Icons.Rounded.Check` · 20dp · `icon.inverse` |
| Active fill | `bg.default` |
| Active ring | 2dp · `stroke.brandFocus` |
| Active number | `bg.brand` · `type.labelMd` |
| Upcoming fill | `bg.default` |
| Upcoming ring | 1dp · `stroke.subtle` |
| Upcoming number | `text.subtle` · `type.labelMd` |
| Error fill | `bg.critical` |
| Error icon | `Icons.Rounded.PriorityHigh` · 20dp · `icon.inverse` |

### Stepper / Horizontal
| Part | Token |
|---|---|
| Container width | `fillMaxWidth()` (no pinned 360dp) |
| Indicator → label gap | `space.sm` (8dp) |
| Label typography | `type.labelSm` |
| Label color (Active / Completed / Error) | `text.default` |
| Label color (Upcoming) | `text.subtle` |
| Connector | 2dp · `radius.full` · `bg.brand` when Completed, else `stroke.subtle` |

### Stepper / Vertical
| Part | Token |
|---|---|
| Rail width | 32 dp |
| Rail → content gap | `space.md` (12dp) |
| Indicator → connector gap | `space.xs` (4dp) |
| Connector width | 2 dp · `radius.full` |
| Title typography | `type.labelMd` |
| Title color (Active / Completed / Error) | `text.default` |
| Title color (Upcoming) | `text.subtle` |
| Title → description gap | `space.xxs` |
| Description typography | `type.bodyMd` · `text.subtle` |
| Description → action gap | `space.md` |
| Between-step bottom padding | `space.xl` (24dp) |

No shadows. No hardcoded hex, sp, or dp literals outside the foundation tokens.
