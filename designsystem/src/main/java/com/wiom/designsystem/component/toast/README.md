# WiomToast

Transient bottom-of-screen feedback for the Wiom Android library. Built from `.skills-cache/wiom-toast.md`.

## Purpose

Tell the user *what just happened* after a tap, API response, or system event — and then get out of the way. Appears at the bottom, floats above content with `shadow.lg`, auto-dismisses (or requires a close tap).

If the user needs to *do* something about the feedback — fix a form field, acknowledge a persistent state, wait through a multi-step flow — **use something bigger** (inline error, banner, result screen). See the decision tree in the skill.

## Components

| Composable | Role |
|---|---|
| `WiomToast` | The visible surface itself. Exposed for static placement (previews, one-off screens). |
| `WiomToastHost` | Queued renderer with enter/exit animation. Place once at the bottom of a screen. |
| `WiomToastState` | State holder — call `showToast(WiomToastMessage(...))` from anywhere. |
| `rememberWiomToastState()` | Composition-rooted state factory. |

Typical usage:

```kotlin
val toastState = rememberWiomToastState()
val scope = rememberCoroutineScope()

Scaffold(
    snackbarHost = {
        WiomToastHost(
            state = toastState,
            modifier = Modifier.padding(horizontal = WiomTheme.spacing.sm, vertical = WiomTheme.spacing.sm),
        )
    },
) { /* content */ }

// Somewhere in a click handler:
toastState.showToast(
    WiomToastMessage(
        status = WiomToastStatus.Critical,
        message = "Server ne request reject kiya",
        durationMillis = WiomToastMessage.DURATION_SHORT,
    ),
)

// Actionable:
toastState.showToast(
    WiomToastMessage(
        status = WiomToastStatus.Info,
        message = "Ticket delete kiya",
        action = WiomToastAction("Undo") { restoreTicket() },
        durationMillis = WiomToastMessage.DURATION_LONG,
    ),
)

// Persistent:
toastState.showToast(
    WiomToastMessage(
        status = WiomToastStatus.Warning,
        message = "Offline — purana data dikh raha hai",
        showClose = true,
        durationMillis = null,
    ),
)
```

## Statuses (V2 — all 4 types share `bg.default`)

| `WiomToastStatus` | Fill | Body text | Icon tint | Leading icon |
|---|---|---|---|---|
| `Critical` | `bg.default` | `text.default` | `icon.critical` | `Icons.Rounded.Error` |
| `Warning` | `bg.default` | `text.onWarning` | `icon.warning` | `Icons.Rounded.Warning` |
| `Info` | `bg.default` | `text.default` | `icon.info` | `Icons.Rounded.Info` |
| `Positive` | `bg.default` | `text.default` | `icon.positive` | `Icons.Rounded.CheckCircle` |

**Warning** is the one-token family per `wiom-design-foundations` — body uses `text.onWarning` (the dark olive that pairs with `bg.warning` everywhere). The action label is **always `text.brand`** regardless of status. The close ✕ is always `icon.action`.

The V1 `Neutral` status (dark inverse surface) is gone — V2 has only the 4 status types.

## Container tokens

| Property | Token | Value |
|---|---|---|
| Corner radius | `radius.small` | 8dp |
| Horizontal padding | `space.lg` | 16dp |
| Vertical padding | `space.md` | 12dp |
| Gap icon ↔ body | `space.sm` | 8dp |
| Shadow | `shadow.lg` | 12dp elevation |
| Body text | `type.bodyLg` | 16sp Regular |
| Action label | `type.labelLg` | 16sp SemiBold |
| Leading icon | `iconSize.md` | 24dp |

## Animation

- **Enter** — 300ms slide from bottom + fade in.
- **Exit** — 300ms slide to bottom + fade out.
- **Auto-dismiss** — `DURATION_SHORT = 2000ms` (informational), `DURATION_LONG = 8000ms` (actionable), `null` (persistent).

## Rules enforced

- Max 1 toast visible at a time — new toasts queue inside the host.
- Message truncates at 2 lines (`maxLines = 2`). Longer copy is a signal to rewrite.
- Body of the toast is **not** tappable. Only the action label and close X are interactive — matches skill § 9 rule 10.
- No filled buttons in toasts — the action slot is text-only, per skill § 9.
- `shadow.lg` on a rounded container — and no border — honors CLAUDE.md § 8 (shadow XOR border).

## V1 → V2 changes

1. **Dropped `WiomToastStatus.Neutral`.** V2 ships only 4 status types — `Critical · Warning · Info · Positive`.
2. **All types share `bg.default`.** V1 used per-type tinted backgrounds (`bg.criticalSubtle`, `bg.warningSubtle`, etc.) — V2 makes the icon (and Warning's body text) the only carrier of the status colour.
3. **Radius `medium` (12) → `small` (8).**
4. **Padding `space.lg` all sides → `space.lg H × space.md V`** (16/12).
5. **Body style `bodyMd` (14sp) → `bodyLg` (16sp).**
6. **Action colour always `text.brand`.** V1 used per-type colours (e.g. `text.onWarning` for Warning).
7. **Close ✕ tint always `icon.action`.** V1 inherited the type's icon tint.
8. **Width 328 / 16dp gutter** (was 344 / 8dp). Toast aligns to the standard surface gutter used by every other inline component.

## Known gaps

- No explicit "heading + body" layout — toasts are single-line body only. Use a banner / dialog / result screen for two-tier copy.
- The host timer uses a simple `delay` loop. An actionable toast whose user tap causes a state change will dismiss via `state.dismiss()`; we don't yet support pause-on-press-and-hold semantics.
- No RTL-specific layout — Row arranges in start-to-end order, matching system locale.
