# WiomInput & WiomTextarea

Single-line (`WiomInput`) and multi-line (`WiomTextarea`) form fields. Search is an `Input` configuration, not a separate component.

## Anatomy

```
[ Title (top, label.md) ]
╭─────────────────────────────────────────────────╮
│  [lead] [prefix] value/placeholder [suffix] [tr] │  ← container: radius.medium, stroke
╰─────────────────────────────────────────────────╯
[ Helper (body.sm, left) ]           [ Counter (right) ]
```

Height is **intrinsic** — padding + body line-height. Never set a fixed height.

## States

The component derives `Default / Focused / Filled` automatically from focus + value.
You explicitly set:

- `enabled = false` → Disabled
- `readOnly = true` → Read-only (text copyable, not editable)
- `status = WiomFieldStatus.Error | Success | Warning` → status border + status icon

Status icons auto-fill the trailing slot and override any `trailingIcon` you pass.

## Configuration patterns

### Phone number (Indian context — no country code)

```kotlin
WiomInput(
    value = phone,
    onValueChange = { phone = it },
    title = "Mobile number",
    leadingIcon = { WiomIcon(WiomIcons.phone, null, size = WiomTheme.icon.sm, tint = WiomTheme.colors.text.secondary) },
    helper = "We'll send an OTP",
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
)
```

> **Why leading icon, not `+91` prefix?** Wiom is India-only — every user dials an Indian number. `+91` adds friction and wastes visible characters. The phone icon communicates the field type without the redundant code.

### OTP with timer

```kotlin
WiomInput(
    value = otp,
    onValueChange = { otp = it },
    title = "Enter 6-digit OTP",
    counter = formatTimer(secondsLeft),          // e.g. "00:24"
    trailingIcon = {
        WiomIcon(WiomIcons.refresh, "Resend", size = WiomTheme.icon.sm,
                 modifier = Modifier.clickable(enabled = secondsLeft == 0) { resend() })
    },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
)
```

### Search

```kotlin
WiomInput(
    value = query,
    onValueChange = { query = it },
    placeholder = "Search plans, offers, bills",
    leadingIcon = { WiomIcon(WiomIcons.search, null, size = WiomTheme.icon.sm, tint = WiomTheme.colors.text.secondary) },
    trailingIcon = if (query.isNotEmpty()) {
        { WiomIcon(WiomIcons.cancel, "Clear", size = WiomTheme.icon.sm,
                   modifier = Modifier.clickable { query = "" }) }
    } else null,
)
```

### Currency

```kotlin
WiomInput(
    value = amount,
    onValueChange = { amount = it },
    title = "Recharge amount",
    prefix = "₹",
    suffix = ".00",
    helper = "Includes 18% GST",
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
)
```

### Password with visibility toggle

```kotlin
var visible by remember { mutableStateOf(false) }
WiomInput(
    value = password,
    onValueChange = { password = it },
    title = "Password",
    visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
    trailingIcon = {
        WiomIcon(
            id = if (visible) WiomIcons.visibilityOff else WiomIcons.visibility,
            contentDescription = if (visible) "Hide password" else "Show password",
            size = WiomTheme.icon.sm,
            modifier = Modifier.clickable { visible = !visible },
        )
    },
)
```

### Read-only (Customer ID, account number)

```kotlin
WiomInput(
    value = "CUST-2045",
    onValueChange = {},
    title = "Customer ID",
    readOnly = true,
)
```

Read-only ≠ disabled. Text stays `text.primary`, container uses `surface.subtle` + `border.subtle`. User can select/copy.

### Address (Textarea)

```kotlin
WiomTextarea(
    value = address,
    onValueChange = { address = it },
    title = "Installation address",
    counter = "${address.length} / 200",
    minLines = 3,
)
```

## Rules

1. **Label default color = `text.primary`.** Softens to `text.secondary` when the field is active (focused or filled) or read-only. Disabled → `text.disabled`.
2. **Error helper must be actionable.** Don't ship `helper = "Helper text"` in production — write what's wrong and how to fix it.
3. **Affixes go in `prefix` / `suffix`**, never in `value`. Typing `"+91 9876543210"` into value breaks validation and copy/paste.
4. **Read-only ≠ Disabled.** Use read-only for display values the user can read/copy (Customer ID, policy number). Use disabled only when the field is truly inactive.
5. **One trailing icon at a time.** Status icons (Error/Success/Warning) take over the trailing slot automatically.
6. **Title is visible by default.** Hide only when a parent section header already labels the field.
7. **Heights hug.** Wrap = V padding + line-height. Textarea grows with content. Never pin a fixed height.
8. **Counter only where it aids.** OTP timers, character limits on textareas. Don't show on short free-text fields.

## Tokens

- Container fill (rest): `surface.base` → readOnly/disabled: `surface.subtle`
- Border (rest): `stroke.small` + `border.default`
- Border (focused): `stroke.medium` + `brand.primary`
- Border (error/success/warning): `stroke.medium` + family primary
- Border (readOnly): `stroke.small` + `border.subtle`
- Radius: `radius.medium` (12dp)
- Padding: `space.lg` horizontal, `space.md` vertical
- Slot gap: `space.md`
- Label → container → helper gap: `space.sm`
- Label: `type.labelMd` · color per state above
- Value / Prefix / Suffix: `type.bodyLg` · `text.primary` (value) / `text.secondary` (affixes)
- Placeholder: `type.bodyLg` · `text.disabled`
- Helper: `type.bodySm` · `text.secondary` / status primary
- Counter: `type.bodySm` · `text.secondary`
- Icons: `icon.sm` (20dp)
- Shadow: `shadow.none`
