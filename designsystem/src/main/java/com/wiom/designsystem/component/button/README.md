# WiomButton · WiomAcknowledge

Primary CTA primitive. 4 types + a checkbox-gated acknowledge row for irreversible actions.

## Pick the type

```
Is this the main action on the screen?              → Primary
Is this an alternate/cancel next to a Primary?      → Secondary
Low-emphasis dismiss or soft action?                → Tertiary
Deletes, removes, or can't be undone?               → Destructive
User must confirm they've verified something?       → Acknowledge (then Primary)
```

## API

```kotlin
WiomButton(
    text = "भुगतान करें",
    onClick = { pay() },
    type = WiomButtonType.Primary,    // Primary | Secondary | Tertiary | Destructive
    enabled = true,
    loading = false,
    icon = null,                       // or WiomButtonIcon.Leading(WiomIcons.phone) / .Trailing(...)
)

// Acknowledge flow
var ack by remember { mutableStateOf(false) }
WiomAcknowledge(
    text = "मैंने सभी जानकारी सही दी है",
    checked = ack,
    onCheckedChange = { ack = it },
)
WiomButton(
    text = "Submit",
    onClick = { submit() },
    type = WiomButtonType.Primary,
    enabled = ack,                     // Primary disabled until acknowledge is checked
)
```

## Wiom use cases

- **Primary** — "भुगतान करें" (Pay), "Recharge Now", "Verify OTP"
- **Secondary** — "Cancel", "वापस जाएं", "Review"
- **Tertiary** — "ठीक है" (OK), "Maybe Later", "Skip"
- **Destructive** — "Delete plan", "Remove account", "Cancel subscription"
- **Acknowledge + Primary pair** — "मैंने Terms & Conditions पढ़ लिए हैं" + "Submit"

## Pairing rules (2 CTAs)

| Pair | Layout | Example |
|---|---|---|
| Secondary + Primary | side-by-side (right = primary) | "Cancel"  "Confirm" |
| Primary + Tertiary | vertical stack (top = primary) | "Recharge now" / "Maybe later" |
| Secondary + Secondary (short text) | side-by-side | Equal-weight choices |
| Secondary + Secondary (long text) | vertical stack | When shortening loses context |
| Secondary + Destructive | side-by-side (right = destructive) | "Cancel"  "Delete" |
| Acknowledge + Primary | vertical, Acknowledge on top | Irreversible flows |

**Never:** Primary + Primary · Destructive alone · 3+ buttons at one level · Tertiary side-by-side with Primary.

## Rules

1. **Max 2 CTAs per screen/sheet/dialog.**
2. **Primary appears only once** per screen.
3. **Right / top = preferred action.**
4. **Destructive never alone** — always paired with a Secondary escape route.
5. **Copy reflects the next screen's context.** "भुगतान करें" not "आगे बढ़ें".
6. **Acknowledge uses 1st person.** "मैंने..." / "I have..." — never "कस्टमर ने...".
7. **Text never wraps, truncates, or shrinks.** Rewrite shorter copy. No ellipsis. No font-size hack.
8. **Minimum 48dp tall** (via `defaultMinSize`) — grows with font scale. Never pin a fixed height.
9. **No shadow on the button.** Flat.
10. **Loading hides text and icon**, shows a spinner in the text color.

## Tokens

- Min height: 48dp (via `defaultMinSize` — grows with font scale)
- Padding: `space.lg` H (16dp) · `space.md` V (12dp)
- Radius: `radius.large` (16dp)
- Typography: `type.labelLg` (16sp SemiBold)
- Icon size: 20dp · icon-text gap: `space.sm` (8dp)
- Secondary border: `stroke.medium` (2dp)
- Side-by-side CTA gap: `space.md` (12dp)
- Stacked CTA gap: `space.sm` (8dp)
- Shadow: `shadow.none` always

### Color per type × state

| Type | Default fill / text | Pressed | Disabled |
|---|---|---|---|
| Primary | `brand.primary` / `text.onColor` | `brand.primaryPressed` | `surface.muted` / `text.disabled` |
| Secondary | transparent / `brand.primary` + 2dp border | `brand.primarySubtle` fill | transparent / `text.disabled` + `border.default` |
| Tertiary | transparent / `brand.primary` | `brand.primarySubtle` fill | transparent / `text.disabled` |
| Destructive | `negative.primary` / `text.onColor` | `negative.primaryPressed` | `surface.muted` / `text.disabled` |
