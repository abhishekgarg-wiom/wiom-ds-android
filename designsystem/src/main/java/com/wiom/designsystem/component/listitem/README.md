# WiomListItem

Unified list-row primitive. One component covers every row in the app — navigation,
settings, selection (checkbox / radio), toggles (switch), prominent feature entries
(icon-with-bg), and info-display rows with trailing meta.

**1 component · 5 Types × 2 States = 10 ship variants** (Figma `1655:487`).

## 5 Types

| Type      | Leading                                       | Trailing                          | Use case                                       |
|-----------|-----------------------------------------------|-----------------------------------|------------------------------------------------|
| `Default` | 24 dp `WiomIcon` (optional)                   | `chevron_right` (`icon.brand`)    | Navigation / settings / status / info rows     |
| `IconBg`  | 48 dp `WiomIconBadge` (Md, tone-swappable)    | `chevron_right` (`icon.brand`)    | Prominent settings sections, feature entries   |
| `Checkbox`| 24 dp `WiomCheckbox`                          | —                                 | Multi-select lists                             |
| `Radio`   | 24 dp `WiomRadio`                             | —                                 | Single-select lists                            |
| `Switch`  | 24 dp `WiomIcon` (optional)                   | 52 × 32 dp `WiomSwitch`           | Settings toggles (notifications, dark mode)    |

## API

```kotlin
enum class WiomListItemType  { Default, IconBg, Checkbox, Radio, Switch }
enum class WiomListItemState { Default, Disabled }

@Composable
fun WiomListItem(                                       // Default Type
    primary: String,
    modifier: Modifier = Modifier,
    secondary: String? = null,
    leadingIcon: ImageVector? = null,
    showTrailingChevron: Boolean = true,
    trailingMeta: String? = null,
    selected: Boolean = false,
    state: WiomListItemState = WiomListItemState.Default,
    onClick: (() -> Unit)? = null,
)

@Composable
fun WiomListItemIconBg(                                 // IconBg Type
    primary: String,
    leadingIcon: ImageVector,
    leadingTone: WiomIconBadgeTone = WiomIconBadgeTone.Neutral,
    showTrailingChevron: Boolean = true,
    ...
)

@Composable
fun WiomListItemCheckbox(                               // Checkbox Type
    primary: String,
    selection: WiomCheckboxSelection,
    onSelectionChange: (WiomCheckboxSelection) -> Unit,
    ...
    selected: Boolean = false,                          // independent of the checkbox value
)

@Composable
fun WiomListItemRadio(                                  // Radio Type
    primary: String,
    radioSelected: Boolean,
    onRadioSelect: () -> Unit,
    ...
    selected: Boolean = false,
)

@Composable
fun WiomListItemSwitch(                                 // Switch Type — never carries selected
    primary: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    leadingIcon: ImageVector? = null,
    ...
)

// Raw slot API for custom leading/trailing (avatars, nested badges, etc.)
@Composable
fun WiomListItemBase(
    primary: String,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    ...
)
```

The Checkbox / Radio / Switch helpers instantiate the indicator atoms internally
(`WiomCheckbox` / `WiomRadio` / `WiomSwitch` from `selectioncontrol`). Their `onToggle` /
`onSelect` / `onCheckedChange` is set to `null` so the indicator is decorative in the row's
a11y tree — the **row tap** is the source of truth.

## Tokens

| Slot                       | Token                                       |
|----------------------------|---------------------------------------------|
| Outer padding              | `space.lg` (16 dp) — all sides              |
| Leading → content gap      | `space.md` (12 dp)                          |
| Primary → secondary gap    | `space.xs` (4 dp)                           |
| Meta → trailing icon gap   | `space.sm` (8 dp)                           |
| Min height                 | `space.huge` (48 dp) via `defaultMinSize`   |
| Primary text               | `type.bodyLg` · `text.default`              |
| Secondary text             | `type.bodyMd` · `text.subtle`               |
| Trailing meta              | `type.bodyMd` · `text.subtle`               |
| Leading icon tint          | `icon.nonAction`                            |
| **Trailing chevron tint**  | **`icon.brand`** — the row's tap-affordance signal |
| Disabled text / icon       | `text.disabled` / `icon.disabled`           |
| Pressed overlay            | `bg.subtle`                                 |
| Selected overlay           | `bg.selected`                               |

## States

| State    | Visual                                              | When                                     |
|----------|-----------------------------------------------------|------------------------------------------|
| Pressed  | `bg.subtle` overlay (runtime via `InteractionSource`) | Tap feedback                             |
| Selected | `bg.selected` overlay                               | Currently-chosen menu item / picker row  |
| Disabled | All content tokens swap to `*.disabled`             | Row gated, prerequisite missing          |

**Pressed XOR Selected** — Selected wins. **Disabled overrides both** — disabled rows show
no press feedback or selection. Disabled is foundations Pattern A — never `Modifier.alpha`.

## Composition rules

1. **Stack rows with 0 gap and no dividers.** The 16 dp vertical padding on each row creates
   natural separation.
2. **Card hosting the stack uses 0 internal padding** — rows supply their own.
3. **Min 48 dp height** via `defaultMinSize`. Switch rows compute to ~64 dp because of the
   trailing track height; let them hug.
4. **Don't put a CTA in a row.** Rows route navigation / selection / display. Action goes
   on the destination page.
5. **Primary text is the label** for Checkbox / Radio / Switch types — never inside the
   indicator.

## Examples

### Settings card

```kotlin
Column(
    modifier = Modifier
        .clip(RoundedCornerShape(WiomTheme.radius.large))
        .background(WiomTheme.color.bg.default)
) {
    WiomListItem("Account", leadingIcon = Icons.Rounded.Person, onClick = onAccount)
    WiomListItem("Notifications", leadingIcon = Icons.Rounded.Notifications, onClick = onNotifs)
    WiomListItem(
        "Language",
        leadingIcon = Icons.Rounded.Language,
        trailingMeta = "English",
        onClick = onLanguage,
    )
    WiomListItem("Privacy", leadingIcon = Icons.Rounded.Lock, onClick = onPrivacy)
}
```

### Picker sheet (single-select via row tap)

```kotlin
options.forEach { opt ->
    WiomListItem(
        primary = opt,
        selected = opt == picked,
        showTrailingChevron = false,
        onClick = { picked = opt; dismiss() },
    )
}
```

### Multi-select (Checkbox)

```kotlin
val picked = remember { mutableStateMapOf<String, Boolean>() }
plans.forEach { plan ->
    WiomListItemCheckbox(
        primary = plan,
        selection = if (picked[plan] == true) WiomCheckboxSelection.Selected
                    else WiomCheckboxSelection.No,
        onSelectionChange = { next ->
            picked[plan] = (next == WiomCheckboxSelection.Selected)
        },
    )
}
```

### Toggle settings (Switch)

```kotlin
WiomListItemSwitch(
    primary = "Push notifications",
    secondary = "Plan renewals and offers",
    leadingIcon = Icons.Rounded.Notifications,
    checked = pushEnabled,
    onCheckedChange = { pushEnabled = it },
)
```

### Prominent settings entry (IconBg)

```kotlin
WiomListItemIconBg(
    primary = "Wallet",
    secondary = "Recharges, refunds, pending payments",
    leadingIcon = Icons.Rounded.AccountBalanceWallet,
    leadingTone = WiomIconBadgeTone.Brand,
    onClick = onWallet,
)
```

## Don'ts

- ❌ Pressed + Selected together — Selected wins.
- ❌ Dividers between stacked rows — the padding handles it.
- ❌ `Modifier.alpha(...)` for Disabled — use `*.disabled` tokens.
- ❌ 3+ lines of text — that's a card, not a row.
- ❌ Fixed `height(...)` — use `defaultMinSize` only.
- ❌ Adding a label inside `WiomCheckbox` / `WiomRadio` / `WiomSwitch` — those are indicator-only.
