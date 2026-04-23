# WiomDialog

The modal, blocking, centered surface. Material3 `Dialog` wrapped with Wiom tokens. Five variants cover every confirmation, capture, and foreground-loading moment in the Wiom apps.

For transient, bottom-anchored, swipe-to-dismiss surfaces, use `WiomBottomSheet` instead. Pickers with more than 4 options also belong in a bottom sheet.

---

## Shared structure

| Property | Value | Token |
|---|---|---|
| Width | 312 dp (fixed) | — |
| Radius | 24 dp | `radius.xlarge` |
| Background | #FAF9FC | `bg.default` |
| Shadow | 12 dp | `shadow.xl` |
| Scrim | #161021 @ 50% | `overlay.scrim` (supplied by `Dialog`) |
| Padding | 24 dp all sides | `space.xl` |
| Title | 20sp Bold | `type.headingMd` · `text.default` |
| Body | 16sp Regular | `type.bodyLg` · `text.subtle` |

The dialog has **one width, one radius, one padding**. `WiomDialog` does not expose those as parameters — the helpers encode the whole anatomy. Shadow + scrim, no border.

---

## Variants

### 1. `WiomAlertDialog`

Single-thought confirmation with an optional leading icon-badge. 48dp `bg.brandSubtle` square with `icon.brand` glyph. Left-aligned text. `space.huge` (48 dp) gap between content and buttons — the Alert-only gap per skill.

```kotlin
WiomAlertDialog(
    title = "Delete this plan?",
    body = "This will permanently remove your plan. This action can't be undone.",
    primaryAction = WiomDialogAction("Delete", onClick = { /* … */ }, type = WiomButtonType.Destructive),
    secondaryAction = WiomDialogAction("Cancel", onClick = onDismiss, type = WiomButtonType.Tertiary),
    onDismiss = onDismiss,
)
```

### 2. `WiomInputDialog`

Capture ONE input value. The `content` slot is for a `WiomInput` call. Use a new screen or a `WiomBottomSheet` Form for two or more fields.

```kotlin
WiomInputDialog(
    title = "Enter mobile number",
    body = "We'll send a verification code to this number.",
    primaryAction = WiomDialogAction("Continue", onClick = { /* … */ }),
    secondaryAction = WiomDialogAction("Cancel", onClick = onDismiss, type = WiomButtonType.Tertiary),
    onDismiss = onDismiss,
) {
    WiomInput(
        value = phone,
        onValueChange = { phone = it },
        label = "Mobile number",
    )
}
```

### 3. `WiomSelectionDialog`

Pick one option from up to 4. Each row is a radio. For > 4 options or any search/scroll requirement, use a bottom-sheet picker.

```kotlin
WiomSelectionDialog(
    title = "Choose your language",
    body = "You can change this anytime from Settings.",
    options = listOf("English", "हिन्दी", "मराठी", "தமிழ்"),
    selectedIndex = selected,
    onSelect = { selected = it },
    primaryAction = WiomDialogAction("Confirm", onClick = { /* … */ }),
    secondaryAction = WiomDialogAction("Cancel", onClick = onDismiss, type = WiomButtonType.Tertiary),
    onDismiss = onDismiss,
)
```

### 4. `WiomIllustrationDialog`

Celebratory or emphasized single-screen moment. Same illustration anatomy as the bottom sheet — 120dp `bg.brandSubtle` circle with 48dp `icon.brand` glyph — but centered inside the dialog surface. Heading and subtext centered.

```kotlin
WiomIllustrationDialog(
    icon = Icons.Rounded.Celebration,
    heading = "You saved Rs 150 this month!",
    subtext = "Keep recharging on time to unlock more savings.",
    primaryAction = WiomDialogAction("View savings", onClick = { /* … */ }),
    secondaryAction = WiomDialogAction("Dismiss", onClick = onDismiss, type = WiomButtonType.Tertiary),
    onDismiss = onDismiss,
)
```

### 5. `WiomLoadingDialog`

Blocking foreground loading. **Non-dismissible by design** — back-press and tap-outside are disabled. Spinner tinted with `bg.brand`, optional title + message below, both centered. Error handling must route to an Alert dialog.

```kotlin
WiomLoadingDialog(
    title = "Processing payment",
    message = "Please wait while we confirm your transaction.",
)
```

---

## `WiomDialogAction`

```kotlin
data class WiomDialogAction(
    val label: String,
    val onClick: () -> Unit,
    val type: WiomButtonType = WiomButtonType.Primary,
)
```

Maps directly to a `WiomButton`. All helpers render actions **stacked full-width** with a `space.md` (12 dp) gap — side-by-side layouts are excluded at 312 dp width.

### Valid action pairings

| Intent | Primary | Secondary |
|---|---|---|
| Neutral confirm ("Save" / "OK") | Primary | — |
| Confirm + offer cancel | Primary | Tertiary |
| Delete / destructive | Destructive | Tertiary |
| Celebratory acknowledge | Primary | Tertiary |
| Loading (cancellable) | Tertiary "Cancel" only | — (use `WiomLoadingDialog`, no button group for non-cancellable) |

**Never pair Primary + Destructive** — Destructive replaces Primary as the primary of the group.

---

## Low-level `WiomDialog`

`WiomDialog(onDismissRequest, dismissOnBackPress, dismissOnClickOutside, content)` is exposed for edge cases, but prefer the typed helpers above — they encode the per-variant anatomy (icon vs illustration vs spinner, alignment, gaps) so it stays consistent across callers.

---

## Rules

- Exactly one width (312 dp) and one radius (`radius.xlarge`). Do not expose those as parameters.
- Shadow XOR border — dialog is always `shadow.xl` with scrim; never add a stroke.
- Stacked action group only, max 2 CTAs. No side-by-side.
- No Primary or Destructive in a Loading dialog. Tertiary "Cancel" only, or no buttons.
- No scrollable content inside `WiomSelectionDialog`. Longer lists → bottom-sheet picker.
- Single input field inside `WiomInputDialog`. Multi-field flows → new screen or `WiomBottomSheet` Form.
- Alignment is baked in per variant: left for Alert / Input / Selection; centered for Illustration / Loading.

---

## Tokens used

| Slot | Token |
|---|---|
| Container fill | `bg.default` |
| Container radius | `radius.xlarge` |
| Container shadow | `shadow.xl` (auto-paired with `overlay.scrim` by `Dialog`) |
| Internal padding | `space.xl` |
| Title | `type.headingMd` · `text.default` |
| Body | `type.bodyLg` · `text.subtle` |
| Main-to-buttons gap (Alert) | `space.huge` |
| Main-to-buttons gap (other) | `space.xl` |
| Button group gap | `space.md` |
| Illustration circle | `bg.brandSubtle` · 120dp · `radius.full` |
| Illustration icon | `icon.brand` · 48dp |
| Loading spinner | `bg.brand` tint · 20dp in 40dp frame |
| Selection row label | `type.labelLg` · `text.default` |
| Selected radio | `bg.brand` fill + `bg.default` inner dot (filled state = no border) |
| Unselected radio | `Color.Transparent` fill + `stroke.medium` · `stroke.strong` border |
