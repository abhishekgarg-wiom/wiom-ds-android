# WiomListItem

Unified list-row primitive. One component covers every row in the app — navigation, settings,
selection (checkbox / radio), toggles (switch), prominent feature entries (icon with bg), and
info-display rows with trailing meta.

Replaces the previous 288-variant library by moving from structural variants to a 5-Type
hybrid: one variant axis (Type) + booleans/slots for state.

## 5 Types

| Type         | Leading                                                    | Trailing                          | Use case                                       |
|--------------|------------------------------------------------------------|-----------------------------------|------------------------------------------------|
| `Default`    | 24dp `WiomIcon` (optional)                                 | `chevron_right` (can be hidden)   | Navigation / settings / status / info rows     |
| `IconWithBg` | `WiomIconBadge` (48dp tinted container)                    | `chevron_right` (can be hidden)   | Prominent settings sections, feature entries   |
| `Checkbox`   | `WiomCheckbox` (label is this row's `primary`)             | —                                 | Multi-select lists                             |
| `Radio`      | `WiomRadio` (label is this row's `primary`)                | —                                 | Single-select lists                            |
| `Switch`     | 24dp `WiomIcon` (optional)                                 | `WiomSwitch`                      | Settings toggles (notifications, dark mode)    |

## API

```kotlin
@Composable
fun WiomListItem(                               // Type = Default
    primary: String,
    modifier: Modifier = Modifier,
    secondary: String? = null,
    trailingMeta: String? = null,
    leadingIcon: ImageVector? = null,
    hasTrailingIcon: Boolean = true,            // false = hide chevron (menu row, status row)
    selected: Boolean = false,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
)

@Composable
fun WiomListItemIconBadge(                      // Type = IconWithBg
    primary: String,
    leadingBadge: @Composable () -> Unit,       // caller supplies a WiomIconBadge instance
    ...
)

@Composable
fun WiomListItemCheckbox(                       // Type = Checkbox
    primary: String,
    leadingCheckbox: @Composable () -> Unit,    // caller supplies a WiomCheckbox instance
    ...
    onClick: (() -> Unit)? = null,              // tap-the-row-to-toggle
)

@Composable
fun WiomListItemRadio(                          // Type = Radio
    primary: String,
    leadingRadio: @Composable () -> Unit,       // caller supplies a WiomRadio instance
    selected: Boolean = false,                  // picker-row highlight
    ...
)

@Composable
fun WiomListItemSwitch(                         // Type = Switch
    primary: String,
    trailingSwitch: @Composable () -> Unit,     // caller supplies a WiomSwitch instance
    leadingIcon: ImageVector? = null,
    ...
)

// Raw slot API for custom leading/trailing compositions (avatars, nested badges, etc.)
@Composable
fun WiomListItemBase(
    primary: String,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    ...
)
```

### Why `leadingBadge` / `leadingCheckbox` / `trailingSwitch` are slots

These sub-components (`WiomIconBadge`, `WiomCheckbox`, `WiomRadio`, `WiomSwitch`) live in
sibling modules (`component/iconbadge`, `component/selectioncontrol`). Passing them as
`@Composable () -> Unit` slots decouples this module from theirs — you hold the
`checked`/`tone`/`state` state in your screen and pass a configured instance in. This mirrors
the Figma model where the nested component's properties bubble up to the List Item instance.

## Tokens used

| Part                  | Token                                             |
|-----------------------|---------------------------------------------------|
| Horizontal padding    | `space.lg` (16dp)                                 |
| Vertical padding      | `space.md` (12dp)                                 |
| Leading → content gap | `space.md` (12dp)                                 |
| Primary → secondary   | `space.xs` (4dp)                                  |
| Meta → icon gap       | `space.sm` (8dp)                                  |
| Min height            | 48dp via `defaultMinSize`                         |
| Primary text          | `type.bodyLg` · `text.default`                    |
| Secondary text        | `type.bodyMd` · `text.subtle`                     |
| Trailing meta         | `type.bodyMd` · `text.subtle`                     |
| Leading icon tint     | `icon.nonAction`                                  |
| Chevron tint          | `icon.action`                                     |
| Disabled text         | `text.disabled`                                   |
| Disabled icon         | `icon.disabled`                                   |
| Selected row fill     | `bg.selected` (Brand_200 #FFCCED)                 |
| Selected dot          | 8dp circle · `bg.brand`                           |

## Selected state

`selected = true` is reserved for picker-sheet rows where you want to show the current choice:

- Fills the whole row with `bg.selected`.
- Renders an 8dp `bg.brand` dot at the trailing side.
- On the Default Type, the chevron is suppressed when selected (the dot + highlight are the
  trailing signal). Restore the chevron by keeping `selected = false` and using a different
  mechanism.
- `Switch` Type ignores `selected` — the toggle is the signal.

## Composition rules

1. Stack list items with **0 gap** — the 12dp vertical padding on each row creates natural
   separation. Do not add dividers between them.
2. Place a stack inside a card (`radius.large` corners, `bg.default` or `bg.subtle`). The card
   takes zero padding — rows supply their own.
3. Minimum 48dp height via `defaultMinSize`. Rows grow taller when secondary text is set.
4. Wrap pressed-state visuals in the standard Compose ripple via `Modifier.clickable` (already
   applied when `onClick` is non-null and `enabled = true`).

## Don'ts

- **Don't** use Pressed + Disabled together.
- **Don't** add dividers between stacked rows.
- **Don't** ship 3+ lines of text — that's a card, not a row.
- **Don't** set a fixed height. Use `defaultMinSize` only.

## Example: settings card

```kotlin
Column(
    modifier = Modifier
        .clip(RoundedCornerShape(WiomTheme.radius.large))
        .background(WiomTheme.color.bg.default)
) {
    WiomListItem("Account", leadingIcon = Icons.Rounded.Person, onClick = { /* ... */ })
    WiomListItem("Notifications", leadingIcon = Icons.Rounded.Notifications, onClick = { /* ... */ })
    WiomListItem(
        "Language",
        leadingIcon = Icons.Rounded.Language,
        trailingMeta = "English",
        onClick = { /* ... */ },
    )
    WiomListItem("Privacy", leadingIcon = Icons.Rounded.Lock, onClick = { /* ... */ })
}
```

## Example: picker sheet

```kotlin
WiomBottomSheet(onDismiss = { ... }) {
    languages.forEach { lang ->
        WiomListItem(
            primary = lang.label,
            selected = lang == selectedLanguage,
            onClick = { selectedLanguage = lang; dismiss() },
        )
    }
}
```

## Example: multi-select (Checkbox Type)

```kotlin
WiomListItemCheckbox(
    primary = "Save my login",
    leadingCheckbox = {
        WiomCheckbox(checked = saveLogin, onCheckedChange = null)  // null — row handles tap
    },
    onClick = { saveLogin = !saveLogin },
)
```
