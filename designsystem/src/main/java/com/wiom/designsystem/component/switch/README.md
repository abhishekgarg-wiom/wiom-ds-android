# WiomSwitch

Binary on/off toggle for instant-apply settings. No submit button needed.

## When to use

- Turn a setting on or off with immediate effect
- Binary preference (notifications, auto-renew, dark mode, offline)
- Feature flag / permission toggle

## When NOT to use

- Form field requiring submit → `WiomCheckbox`
- Mutually exclusive choice → `WiomRadio`
- Multiple selections → `WiomCheckbox`

## API

```kotlin
WiomSwitch(
    checked = autoRenew,
    onCheckedChange = { autoRenew = it },
    label = "Auto-renew plan",
    helper = "Your plan will renew on the 15th of each month",
    enabled = true,
)
```

## Wiom use cases

- **Notifications.** Push / SMS / Email — each independently toggleable in Settings.
- **Privacy.** Share usage data, location access, marketing comms. Off by default for privacy-sensitive.
- **Auto-pay.** Critical — affects billing. Consider a confirmation dialog before enabling.
- **Quick settings.** Dark mode, language, offline mode — no navigation needed.

## Rules

1. **Instant-apply only.** No error state in V1 — switches don't validate.
2. **Filled = no border.** On state removes the stroke.
3. **Thumb is always white.** Never change thumb color.
4. **Vertical centering.** Row uses `Alignment.CenterVertically` — the 32dp track is taller than 20dp text line height.
5. **V1: no hover/focus states** — mobile-first.

## Tokens

- Track: 52×32dp · `radius.full` · `stroke.medium` (2dp, Off only) · `shadow.none`
- Thumb: 24×24dp · `CircleShape` · white · 4dp inset
- Off fill: `surface.muted` → disabled: `surface.subtle`
- On fill: `brand.primary` → disabled: @ 40%
- Off stroke: `border.strong` → disabled: @ 40%
- Label: `type.bodyMd` · `text.primary` → disabled: `text.disabled`
- Helper: `type.bodySm` · `text.secondary`
- Gap track → content: `space.sm`
- Stack gap between switches: `space.md`
