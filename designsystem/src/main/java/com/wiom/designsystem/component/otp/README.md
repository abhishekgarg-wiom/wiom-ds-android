# WiomOtp

Boxed verification-code field. The whole bar is one `BasicTextField`; the visual
N-box layout lives in `decorationBox`. Don't fake an OTP with a single `WiomInput`
and spaced digits — the dedicated component has correct per-digit boxing,
keyboard-type handling, and the Focused-state timer slot.

## Anatomy

```
Title                                       ← type.labelLg
┌──┐  ┌──┐  ┌──┐  ┌──┐                     ← length=4 (default), boxes weight(1f)
│  │  │  │  │  │  │  │                       radius.medium · stroke.small/medium per state
└──┘  └──┘  └──┘  └──┘
Helper text                          00:24  ← type.bodyMd · timer right-aligned
```

Title → boxes gap: `space.sm` (8 dp). Box → box gap: `space.sm`. Boxes → helper
gap: `space.sm`. Each box: `space.md` H × `space.md` V padding,
`radius.medium`, digit text `type.labelLg`.

## API

```kotlin
@Composable
fun WiomOtp(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = "Enter OTP",
    length: Int = 4,                                  // 4 (default) or 6
    helper: String? = null,
    timer: String? = null,                            // e.g., "00:24"
    status: WiomInputStatus = WiomInputStatus.None,
    enabled: Boolean = true,
)
```

## States

| State | Resting box border | Active box border | Helper color |
|---|---|---|---|
| Default / Filled | `stroke.small` `stroke.subtle` | `stroke.medium` `stroke.brandFocus` (focused only) | `text.subtle` |
| Error | `stroke.medium` `stroke.criticalFocus` | `stroke.medium` `stroke.brandFocus` (focused only) | `text.critical` |
| Success | `stroke.medium` `stroke.positiveFocus` | `stroke.medium` `stroke.brandFocus` (focused only) | `text.positive` |
| Disabled | `stroke.small` `stroke.subtle` over `bg.disabled` | n/a | `text.disabled` |

Per the skill, OTP has no `Warning` or `ReadOnly` Figma variants. Passing
`status = Warning` renders the same as `None`.

## Usage

```kotlin
var otp by remember { mutableStateOf("") }

WiomOtp(
    value = otp,
    onValueChange = { otp = it },                      // already filtered to digits + clamped
    length = 6,
    helper = "Sent to +91 98765 43210",
    timer = "00:${secondsRemaining.toString().padStart(2, '0')}",
    modifier = Modifier.fillMaxWidth(),
)
```

## Rules

- `fillMaxWidth()` at the call site — the host owns the gutter.
- Don't combine with a counter — the OTP timer is the OTP's counter.
- `length = 4` (default) or `6`. Other counts work but render off-spec.
- Caller drives the timer countdown — the component just renders the text.
- `onValueChange` always receives digits-only, clamped to `length`. Caller
  doesn't need to filter again.
