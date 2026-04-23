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
        status = WiomToastStatus.Neutral,
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

## Statuses

| `WiomToastStatus` | Fill | Body text | Icon tint | Leading icon |
|---|---|---|---|---|
| `Neutral` | `bg.inverse` | `text.inverse` | `icon.inverse` | `Icons.Rounded.Info` |
| `Positive` | `bg.positiveSubtle` | `text.default` | `icon.positive` | `Icons.Rounded.CheckCircle` |
| `Critical` | `bg.criticalSubtle` | `text.default` | `icon.critical` | `Icons.Rounded.Error` |
| `Warning` | `bg.warningSubtle` | `text.onWarning` | `icon.warning` | `Icons.Rounded.Warning` |
| `Info` | `bg.infoSubtle` | `text.default` | `icon.info` | `Icons.Rounded.Info` |

**Warning is the exception** per CLAUDE.md § 6 — body AND action share `text.onWarning` (the one-token warning family). All other statuses render the action label in `text.brand`; `Neutral` uses `text.inverse` on its dark surface.

## Container tokens

| Property | Token | Value |
|---|---|---|
| Corner radius | `radius.medium` | 12dp |
| Padding | `space.lg` | 16dp |
| Gap icon ↔ body | `space.md` | 12dp |
| Shadow | `shadow.lg` | 6dp elevation |
| Body text | `type.bodyMd` | 14sp Regular |
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

## Skill-vs-foundation flags

1. **Info fill mismatch.** The skill calls Info `bg.muted` with icon tint `icon.nonAction`. The deliverable spec is `bg.infoSubtle` + `text.info` (with `icon.info` tint). Foundation-correct tokens are used here — Info status should live in the info family, not look neutral.
2. **Warning fill hex drift.** Skill writes `bg.warning.subtle = #FFE9A1`. Foundation token `bg.warningSubtle = #FFF2BF`. Token used; hex discarded.
3. **Type name Error → Critical.** Foundation / CLAUDE.md name the family `critical`. Enum value is `WiomToastStatus.Critical` to align.
4. **Neutral variant added.** Not in the skill's 4-type table; deliverable required 5 statuses including `Neutral` (`bg.inverse` + `text.inverse`). Added as the default dark snackbar for contexts without a status implication.
5. **Radius `radius-small` in skill vs `radius.medium` in deliverable.** Skill quotes 8dp radius. Deliverable specifies `radius.medium` (12dp). Deliverable wins — the 12dp matches Wiom's card-radius scale and reads as a floating pill, not a cramped chip.
6. **Body color on Critical.** Skill says `text.default`, CLAUDE.md § 6 confirms: body text on critical/positive banners is `text.default`; only heading is `text.critical`. Toast has only a body, so `text.default` is correct — implemented exactly that way.

## Known gaps

- No explicit "heading + body" layout — toasts are single-line body only. Use a banner / dialog / result screen for two-tier copy.
- The host timer uses a simple `delay` loop. An actionable toast whose user tap causes a state change will dismiss via `state.dismiss()`; we don't yet support pause-on-press-and-hold semantics.
- No RTL-specific layout — Row arranges in start-to-end order, matching system locale.
