# WiomTopBar

Screen header. One component covers every top-of-screen chrome via **Size × State**.

## Variants

| Size | Height | When |
|---|---|---|
| `Small` | 64dp | Default for ~90% of screens — detail, settings, forms, modals (with `Centered`), search. |
| `Medium` | 112dp | Content-heavy screens where the title deserves weight (`Payment history`, `All invoices`). |
| `Large` | 152dp | Hero / landing surfaces only — one per flow (home root, dashboard greeting). |

| State | Where | Notes |
|---|---|---|
| `Default` | Small · Medium · Large | Standard header. |
| `Centered` | Small only | Modal / bottom-sheet headers. Max 1 trailing action. |
| `Scrolled` | Small · Medium · Large | Programmatic — apply when scroll offset > 0. Adds `shadow.sm`. |
| `Search` | Small only | Search pill replaces the title. |

## API

```kotlin
WiomTopBar(
    title = "Profile",
    size = WiomTopBarSize.Small,
    state = WiomTopBarState.Default,
    subtitle = null,
    leading = { WiomTopBarIconAction(Icons.Rounded.ArrowBack, onClick = { ... }) },
    actions = {
        WiomTopBarIconAction(Icons.Rounded.Search, onClick = { ... })
        WiomTopBarIconAction(Icons.Rounded.MoreVert, onClick = { ... })
    },
)
```

Helpers:
- `WiomTopBarIconAction(icon, onClick, contentDescription)` — 48dp touch target, `icon.action` tint.
- `WiomTopBarTextAction(text, onClick)` — 48dp min-height, `type.labelMd` · `text.brand`.

## Wiom use cases

- **Secondary screen:** Small · Default · `ArrowBack` leading + 1–3 icon actions.
- **Modal / sheet header:** Small · Centered · `Close` leading + optional text CTA (Save / Done).
- **List root (major):** Medium · Default — e.g. "Payment history".
- **Home tab:** Large · Default · `Menu` leading (no back on root).
- **On scroll:** switch `state = Scrolled` when offset > 0. Ship `Default` at rest.
- **Search active:** Small · Search — back arrow exits, pill replaces title, optional "Cancel" text action.

## Rules

1. `Small` is the default. Only use `Medium` / `Large` with a concrete reason.
2. Only one `Large` per flow. Never on list / form / detail / modal / settings.
3. `Centered` is modal-only — never on full-screen nav destinations.
4. Cap trailing icons at 3. Never stack Text CTA + 3 icons.
5. `Scrolled` shadow is programmatic, not decorative — don't ship it at rest.
6. Height is intrinsic — never override with a fixed `height(...)`.
7. Text CTA is never destructive (use a real Button inside the screen).
8. Title ≤ 24 chars on 360dp — long titles → move length into the subtitle.

## Tokens

| Part | Token |
|---|---|
| Container fill | `color.bg.default` |
| Scrolled elevation | `shadow.sm` |
| Search pill fill | `color.bg.subtle` |
| Search pill radius | `radius.full` |
| Search pill placeholder | `type.bodyLg` · `text.disabled` |
| Title — Small | `type.titleLg` · `text.default` |
| Title — Medium | `type.headingLg` · `text.default` |
| Title — Large | `type.headingXl` · `text.default` |
| Subtitle | `type.bodySm` · `text.subtle` |
| Text CTA | `type.labelMd` · `text.brand` · 48dp min-height |
| Icon action | `icon.action` (`Icons.Rounded.*`) · 48dp touch target via `space.md` padding |
| Medium title padding | `space.sm` top + 32lh + `space.sm` bottom |
| Large title padding | `space.md` top + 44lh + `space.xxl` bottom |
| Small vertical padding | `space.sm` |
| Small outer horizontal padding | `space.xs` (→ 16dp from glyph edge) |

All heights hug from padding + line-height — nothing is pinned.
