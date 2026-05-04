# WiomInput / WiomTextarea

Text-entry primitives for every form in the Wiom app. `WiomInput` is single-line; `WiomTextarea`
is multi-line. Search bars, OTP fields, currency fields, password fields and dropdown triggers
are all configurations of `WiomInput` — don't build custom rectangles.

## Anatomy

```
Title                                               ← type.labelLg
┌────────────────────────────────────────────────┐
│ [lead]  prefix  value / placeholder  suffix  [trail] │  ← bordered container, radius.medium
└────────────────────────────────────────────────┘
Helper text                                  Counter ← type.bodyMd · text.subtle
```

Padding: `space.lg` H × `space.md` V · Internal gap: `space.md` · Title → container → helper
gap: `space.sm`.

## API

```kotlin
@Composable
fun WiomInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    placeholder: String? = null,
    helper: String? = null,
    counter: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    prefix: String? = null,
    suffix: String? = null,
    status: WiomInputStatus = WiomInputStatus.None,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onClick: (() -> Unit)? = null,
)

@Composable
fun WiomTextarea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    placeholder: String? = null,
    helper: String? = null,
    counter: String? = null,
    status: WiomInputStatus = WiomInputStatus.None,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    minLines: Int = 3,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
)

enum class WiomInputStatus { None, Error, Success, Warning }
```

### `status` vs `enabled` vs `readOnly`

These are three independent axes. You can have a `readOnly` field that is also in `Error`.

| Axis      | Values                           | What it does                                                        |
|-----------|----------------------------------|---------------------------------------------------------------------|
| `status`  | None / Error / Success / Warning | Border colour + helper colour + auto trailing status icon           |
| `enabled` | `true` / `false`                 | Disabled fades content to `text.disabled` and fill to `bg.disabled` |
| `readOnly`| `true` / `false`                 | Value is visible and copyable but not editable. Fill `bg.subtle`    |

## Statuses

| Status | Border (when active / focused) | Helper text colour    | Auto trailing icon              |
|--------|--------------------------------|-----------------------|---------------------------------|
| None   | `stroke.brandFocus` 2dp (focus) / `stroke.subtle` 1dp (rest) | `text.subtle` | (caller-supplied, optional)    |
| Error  | `stroke.criticalFocus` 2dp     | `text.critical`       | `Icons.Rounded.Error`           |
| Success| `stroke.positiveFocus` 2dp     | `text.positive`       | `Icons.Rounded.CheckCircle`     |
| Warning| `stroke.brandFocus` 2dp        | `text.onWarning`      | `Icons.Rounded.Warning`         |

> Status icons override any caller-supplied `trailingIcon`. One trailing icon at a time.

## Tokens used

| Part                 | Token                                                        |
|----------------------|--------------------------------------------------------------|
| Container fill (rest)| `bg.default`                                                 |
| Container fill (read-only) | `bg.subtle`                                            |
| Container fill (disabled)  | `bg.disabled`                                          |
| Border rest          | `stroke.small` + `stroke.subtle`                             |
| Border focus         | `stroke.medium` + `stroke.brandFocus`                        |
| Border error         | `stroke.medium` + `stroke.criticalFocus`                     |
| Border success       | `stroke.medium` + `stroke.positiveFocus`                     |
| Container radius     | `radius.medium`                                              |
| Padding              | `space.lg` H × `space.md` V                                  |
| Internal gap         | `space.md`                                                   |
| Vertical gap         | `space.sm`                                                   |
| Title                | `type.labelLg` · `text.default` (rest) / `text.subtle` (filled, focused, readOnly) / `text.disabled` |
| Value / prefix / suffix | `type.bodyLg` · `text.default` (value) / `text.subtle` (affixes) |
| Placeholder          | `type.bodyLg` · `text.disabled`                              |
| Helper               | `type.bodyMd` · `text.subtle` / `text.critical` / `text.positive` / `text.onWarning` |
| Counter              | `type.bodyMd` · `text.subtle`                                |
| Leading icon         | `iconSize.sm` · `icon.nonAction`                             |
| Trailing icon (user) | `iconSize.sm` · `icon.action`                                |
| Trailing icon (error/success/warning) | `icon.critical` / `icon.positive` / `icon.warning` |

## Configuration patterns

### Phone (India-only — no `+91` prefix)

```kotlin
WiomInput(
    value = phone,
    onValueChange = { phone = it.filter(Char::isDigit).take(10) },
    title = "Mobile number",
    leadingIcon = Icons.Rounded.Phone,
    helper = "We'll send you an OTP",
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
)
```

The Wiom customer base is India-only — do NOT prepend `+91`. A leading phone icon is the
country-code affordance.

### OTP with resend-timer counter

```kotlin
WiomInput(
    value = otp,
    onValueChange = { otp = it.filter(Char::isDigit).take(6) },
    title = "Enter OTP",
    helper = "Didn't get the code?",
    counter = "Resend in 00:24",           // put the timer in the counter slot
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
)
```

This is the "single box" OTP. For the boxed 4/6-digit entry with per-digit focus, a dedicated
`WiomOtp` component is planned.

### Search

```kotlin
WiomInput(
    value = query,
    onValueChange = { query = it },
    placeholder = "Search plans, routers…",
    leadingIcon = Icons.Rounded.Search,
    trailingIcon = if (query.isNotEmpty()) Icons.Rounded.Cancel else null,
    onTrailingIconClick = { query = "" },
)
```

### Currency (₹ prefix, `.00` suffix)

```kotlin
WiomInput(
    value = amount,
    onValueChange = { amount = it.filter(Char::isDigit) },
    title = "Recharge amount",
    prefix = "₹",
    suffix = ".00",
    helper = "Includes 18% GST",
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
)
```

Never type `₹` into the value — it breaks validation and copy-paste. Always use the `prefix`
slot.

### Password (visibility toggle)

```kotlin
var revealed by remember { mutableStateOf(false) }
WiomInput(
    value = password,
    onValueChange = { password = it },
    title = "Password",
    leadingIcon = Icons.Rounded.Lock,
    trailingIcon = if (revealed) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
    onTrailingIconClick = { revealed = !revealed },
    visualTransformation = if (revealed) VisualTransformation.None else PasswordVisualTransformation(),
)
```

### Dropdown-replacement pattern

`WiomDropdown` is not a separate component. Use `WiomInput(readOnly = true)` + chevron trailing +
`onClick` to open a `WiomBottomSheet` picker.

```kotlin
var language by remember { mutableStateOf<Language?>(null) }
var showSheet by remember { mutableStateOf(false) }

WiomInput(
    value = language?.label ?: "",
    onValueChange = {},
    title = "Language",
    placeholder = "Select language",
    trailingIcon = Icons.Rounded.KeyboardArrowDown,
    readOnly = true,
    onClick = { showSheet = true },
)
if (showSheet) {
    WiomBottomSheet(onDismiss = { showSheet = false }) {
        // WiomListItem(type = Radio, selected = …, primary = …)
    }
}
```

Notes:
- `readOnly = true` — the user cannot type into a dropdown field.
- `onClick` fires only when `readOnly = true` and `enabled = true`. In any other state the
  container swallows clicks so the text field receives focus.
- `text.default` for the value (not `text.subtle`) — the user can read / copy the selection.

## Rules

1. Intrinsic height — never set `height(...)`. Container height = vertical padding +
   `bodyLg` line-height. Textarea grows with content.
2. Title is visible by default. Hide only when a parent section header already labels the
   field. Placeholder-only disappears the moment the user types.
3. Error helper must be actionable — explain what's wrong and how to fix it.
4. Affixes live in `prefix` / `suffix`. Do not concatenate `+91 ` or `₹` into the value.
5. One trailing icon at a time. Status icons override the caller's choice.
6. Read-only ≠ Disabled. Read-only keeps content readable and copyable; Disabled dims it.

## Known gaps vs skill

- **No orange stroke token for Warning.** The foundation does not have a warning stroke; this
  component uses `stroke.brandFocus` for the warning border so the status is still signalled
  via helper colour + trailing warning icon. A `stroke.warningFocus` token is tracked for a
  future foundation update.
- **Boxed OTP is not built here.** Skill names this as a separate `WiomOtp` component.
