package com.wiom.designsystem.component.listitem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.wiom.designsystem.component.iconbadge.WiomIconBadge
import com.wiom.designsystem.component.iconbadge.WiomIconBadgeSize
import com.wiom.designsystem.component.iconbadge.WiomIconBadgeTone
import com.wiom.designsystem.component.selectioncontrol.WiomCheckbox
import com.wiom.designsystem.component.selectioncontrol.WiomCheckboxSelection
import com.wiom.designsystem.component.selectioncontrol.WiomIndicatorState
import com.wiom.designsystem.component.selectioncontrol.WiomRadio
import com.wiom.designsystem.component.selectioncontrol.WiomSwitch
import com.wiom.designsystem.component.selectioncontrol.WiomSwitchState
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.theme.WiomTheme

/**
 * Type of a Wiom list-item row. One row primitive, five Type variants — pick by the leading-slot
 * archetype (per Figma `1655:487`).
 *
 * | Type | Leading | Trailing |
 * |---|---|---|
 * | [Default] | 24 dp icon (`icon.nonAction`) | 20 dp `chevron_right` (`icon.brand`) |
 * | [IconBg] | 48 dp `WiomIconBadge` (Md, tone-swappable) | 20 dp `chevron_right` (`icon.brand`) |
 * | [Checkbox] | 24 dp `WiomCheckbox` | (none) |
 * | [Radio] | 24 dp `WiomRadio` | (none) |
 * | [Switch] | 24 dp icon (optional) | 52 × 32 dp `WiomSwitch` |
 */
enum class WiomListItemType { Default, IconBg, Checkbox, Radio, Switch }

/**
 * Row state. Foundations Pattern A — `Disabled` swaps content tokens to the `*.disabled` family
 * (text → `text.disabled`, icon → `icon.disabled`, icon-bg fill → `bg.disabled`). Never opacity.
 */
enum class WiomListItemState { Default, Disabled }

/**
 * Slot-based list-item primitive. Use the typed helpers below for the five Figma archetypes;
 * reach for this only when you need a custom leading / trailing combo (avatars, custom badges).
 *
 * Owns: width (`fillMaxWidth`), 48 dp tap target, 16 dp inner gutter, ripple, Pressed / Selected
 * overlays, Disabled token swap. Hosts but does not own the indicator atoms — `WiomIconBadge`,
 * `WiomCheckbox`, `WiomRadio`, `WiomSwitch` keep their indicator anatomy.
 *
 * **Pressed XOR Selected.** Both are `bg.*` overlays — the row never renders both at once.
 * Selected wins when the row is the current item.
 */
@Composable
fun WiomListItemBase(
    primary: String,
    modifier: Modifier = Modifier,
    secondary: String? = null,
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    trailingMeta: String? = null,
    selected: Boolean = false,
    state: WiomListItemState = WiomListItemState.Default,
    onClick: (() -> Unit)? = null,
) {
    val isDisabled = state == WiomListItemState.Disabled
    val primaryColor: Color =
        if (isDisabled) WiomTheme.color.text.disabled else WiomTheme.color.text.default
    val secondaryColor: Color =
        if (isDisabled) WiomTheme.color.text.disabled else WiomTheme.color.text.subtle
    val metaColor: Color =
        if (isDisabled) WiomTheme.color.text.disabled else WiomTheme.color.text.subtle

    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    // Selected wins over Pressed (skill §6 rule 7). Disabled clears both.
    val overlay: Color = when {
        isDisabled -> Color.Transparent
        selected -> WiomTheme.color.bg.selected
        pressed -> WiomTheme.color.bg.subtle
        else -> Color.Transparent
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(overlay)
            .then(
                if (onClick != null && !isDisabled) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick,
                    )
                } else {
                    Modifier
                }
            )
            .defaultMinSize(minHeight = WiomTheme.spacing.huge)
            .padding(WiomTheme.spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md),
    ) {
        if (leading != null) {
            Box(contentAlignment = Alignment.Center) { leading() }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs),
        ) {
            Text(text = primary, style = WiomTheme.type.bodyLg, color = primaryColor)
            if (!secondary.isNullOrEmpty()) {
                Text(text = secondary, style = WiomTheme.type.bodyMd, color = secondaryColor)
            }
        }

        val hasMeta = !trailingMeta.isNullOrEmpty()
        if (hasMeta || trailing != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
            ) {
                if (hasMeta) {
                    Text(
                        text = trailingMeta!!,
                        style = WiomTheme.type.bodyMd,
                        color = metaColor,
                    )
                }
                if (trailing != null) {
                    Box(contentAlignment = Alignment.Center) { trailing() }
                }
            }
        }
    }
}

/**
 * Default list item — 24 dp leading icon (optional), primary (+ optional secondary), optional
 * trailing meta, trailing chevron in `icon.brand`. The most common row in the app: navigation,
 * settings, info display.
 *
 * @param leadingIcon optional leading glyph. Pass `null` for menu-row pattern (no leading).
 * @param showTrailingChevron set `false` for status / info rows where a [trailingMeta] is the
 *   trailing signal. Caller decides — the chevron isn't auto-hidden by [selected].
 * @param selected adopts `bg.selected` overlay. Pressed XOR Selected — selected wins.
 */
@Composable
fun WiomListItem(
    primary: String,
    modifier: Modifier = Modifier,
    secondary: String? = null,
    leadingIcon: ImageVector? = null,
    showTrailingChevron: Boolean = true,
    trailingMeta: String? = null,
    selected: Boolean = false,
    state: WiomListItemState = WiomListItemState.Default,
    onClick: (() -> Unit)? = null,
) {
    val isDisabled = state == WiomListItemState.Disabled
    val iconTint =
        if (isDisabled) WiomTheme.color.icon.disabled else WiomTheme.color.icon.nonAction
    val chevronTint =
        if (isDisabled) WiomTheme.color.icon.disabled else WiomTheme.color.icon.brand

    WiomListItemBase(
        primary = primary,
        modifier = modifier,
        secondary = secondary,
        trailingMeta = trailingMeta,
        selected = selected,
        state = state,
        onClick = onClick,
        leading = leadingIcon?.let {
            {
                WiomIcon(
                    imageVector = it,
                    contentDescription = null,
                    size = WiomTheme.iconSize.md,
                    tint = iconTint,
                )
            }
        },
        trailing = if (showTrailingChevron) {
            {
                WiomIcon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = null,
                    size = WiomTheme.iconSize.sm,
                    tint = chevronTint,
                )
            }
        } else {
            null
        },
    )
}

/**
 * IconBg list item — leading 48 dp `WiomIconBadge` (Md) + trailing chevron. Use for prominent
 * settings sections, feature entries, and status summaries.
 *
 * Disabled state swaps the icon-bg to `bg.disabled` + `icon.disabled` per foundations Pattern A
 * — `WiomIconBadge` ships no Disabled tone, so the row synthesises it.
 *
 * @param leadingTone the badge tone (Brand / Positive / Warning / Critical / Info / Neutral).
 *   Ignored when [state] is Disabled.
 */
@Composable
fun WiomListItemIconBg(
    primary: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    secondary: String? = null,
    leadingTone: WiomIconBadgeTone = WiomIconBadgeTone.Neutral,
    showTrailingChevron: Boolean = true,
    trailingMeta: String? = null,
    selected: Boolean = false,
    state: WiomListItemState = WiomListItemState.Default,
    onClick: (() -> Unit)? = null,
) {
    val isDisabled = state == WiomListItemState.Disabled
    val chevronTint =
        if (isDisabled) WiomTheme.color.icon.disabled else WiomTheme.color.icon.brand

    WiomListItemBase(
        primary = primary,
        modifier = modifier,
        secondary = secondary,
        trailingMeta = trailingMeta,
        selected = selected,
        state = state,
        onClick = onClick,
        leading = {
            if (isDisabled) {
                Box(
                    modifier = Modifier
                        .background(
                            color = WiomTheme.color.bg.disabled,
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    WiomIconBadge(
                        icon = leadingIcon,
                        size = WiomIconBadgeSize.Md,
                        tone = WiomIconBadgeTone.Neutral,
                    )
                }
            } else {
                WiomIconBadge(
                    icon = leadingIcon,
                    size = WiomIconBadgeSize.Md,
                    tone = leadingTone,
                )
            }
        },
        trailing = if (showTrailingChevron) {
            {
                WiomIcon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = null,
                    size = WiomTheme.iconSize.sm,
                    tint = chevronTint,
                )
            }
        } else {
            null
        },
    )
}

/**
 * Checkbox list item — leading `WiomCheckbox` indicator. Row tap toggles the selection;
 * the checkbox itself is decorative in the row's a11y tree.
 *
 * Primary text **is** the label — never put a label inside the checkbox.
 *
 * @param selection current state (No · Indeterminate · Selected). Caller holds the value.
 * @param onSelectionChange invoked on row tap with the next selection. Indeterminate counts as
 *   "not selected" — tapping flips it to Selected.
 * @param selected the row's selection-overlay flag (independent of the checkbox value). Use for
 *   picker patterns where the currently-chosen row also gets `bg.selected`.
 */
@Composable
fun WiomListItemCheckbox(
    primary: String,
    selection: WiomCheckboxSelection,
    onSelectionChange: (WiomCheckboxSelection) -> Unit,
    modifier: Modifier = Modifier,
    secondary: String? = null,
    state: WiomListItemState = WiomListItemState.Default,
    selected: Boolean = false,
) {
    val cbState = if (state == WiomListItemState.Disabled) {
        WiomIndicatorState.Disabled
    } else {
        WiomIndicatorState.Default
    }
    val toggle: () -> Unit = {
        val next = when (selection) {
            WiomCheckboxSelection.Selected -> WiomCheckboxSelection.No
            WiomCheckboxSelection.No, WiomCheckboxSelection.Indeterminate ->
                WiomCheckboxSelection.Selected
        }
        onSelectionChange(next)
    }
    WiomListItemBase(
        primary = primary,
        modifier = modifier,
        secondary = secondary,
        selected = selected,
        state = state,
        onClick = toggle,
        leading = {
            WiomCheckbox(
                selection = selection,
                state = cbState,
                onToggle = null,
            )
        },
        trailing = null,
    )
}

/**
 * Radio list item — leading `WiomRadio` indicator. Row tap selects this option; the radio is
 * decorative in the row's a11y tree.
 *
 * @param radioSelected the radio's Yes / No state — named to disambiguate from row.[selected].
 * @param selected row-level overlay (use sparingly; the radio dot usually carries the signal
 *   inside a bottom-sheet picker).
 */
@Composable
fun WiomListItemRadio(
    primary: String,
    radioSelected: Boolean,
    onRadioSelect: () -> Unit,
    modifier: Modifier = Modifier,
    secondary: String? = null,
    state: WiomListItemState = WiomListItemState.Default,
    selected: Boolean = false,
) {
    val rState = if (state == WiomListItemState.Disabled) {
        WiomIndicatorState.Disabled
    } else {
        WiomIndicatorState.Default
    }
    WiomListItemBase(
        primary = primary,
        modifier = modifier,
        secondary = secondary,
        selected = selected,
        state = state,
        onClick = onRadioSelect,
        leading = {
            WiomRadio(
                selected = radioSelected,
                state = rState,
                onSelect = null,
            )
        },
        trailing = null,
    )
}

/**
 * Switch list item — optional 24 dp leading icon + primary (+ optional secondary) + trailing
 * `WiomSwitch`. The primary instant-apply pattern for binary settings.
 *
 * Row tap flips the switch. Switch type **never** carries a [selected] overlay — the switch
 * position IS the selection signal.
 */
@Composable
fun WiomListItemSwitch(
    primary: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    secondary: String? = null,
    leadingIcon: ImageVector? = null,
    state: WiomListItemState = WiomListItemState.Default,
) {
    val isDisabled = state == WiomListItemState.Disabled
    val iconTint =
        if (isDisabled) WiomTheme.color.icon.disabled else WiomTheme.color.icon.nonAction
    val swState = if (isDisabled) WiomSwitchState.Disabled else WiomSwitchState.Default

    WiomListItemBase(
        primary = primary,
        modifier = modifier,
        secondary = secondary,
        selected = false,
        state = state,
        onClick = { onCheckedChange(!checked) },
        leading = leadingIcon?.let {
            {
                WiomIcon(
                    imageVector = it,
                    contentDescription = null,
                    size = WiomTheme.iconSize.md,
                    tint = iconTint,
                )
            }
        },
        trailing = {
            WiomSwitch(
                checked = checked,
                state = swState,
                onCheckedChange = null,
            )
        },
    )
}

// region Previews ------------------------------------------------------------------------------

@Preview(name = "ListItem · Default", showBackground = true)
@Composable
private fun PreviewListItemDefault() {
    WiomTheme {
        WiomListItem(
            primary = "Account",
            leadingIcon = Icons.Rounded.Settings,
            onClick = {},
        )
    }
}

@Preview(name = "ListItem · Default with secondary", showBackground = true)
@Composable
private fun PreviewListItemDefaultTwoLine() {
    WiomTheme {
        WiomListItem(
            primary = "Notifications",
            secondary = "Push, SMS, WhatsApp",
            leadingIcon = Icons.Rounded.Notifications,
            onClick = {},
        )
    }
}

@Preview(name = "ListItem · Default with meta", showBackground = true)
@Composable
private fun PreviewListItemDefaultMeta() {
    WiomTheme {
        WiomListItem(
            primary = "Language",
            trailingMeta = "English",
            leadingIcon = Icons.Rounded.Settings,
            onClick = {},
        )
    }
}

@Preview(name = "ListItem · Status row (no chevron)", showBackground = true)
@Composable
private fun PreviewListItemStatusRow() {
    WiomTheme {
        WiomListItem(
            primary = "Build",
            trailingMeta = "v3.4.1",
            showTrailingChevron = false,
        )
    }
}

@Preview(name = "ListItem · Selected (picker row)", showBackground = true)
@Composable
private fun PreviewListItemSelected() {
    WiomTheme {
        WiomListItem(
            primary = "English",
            selected = true,
            showTrailingChevron = false,
            onClick = {},
        )
    }
}

@Preview(name = "ListItem · Disabled", showBackground = true)
@Composable
private fun PreviewListItemDisabled() {
    WiomTheme {
        WiomListItem(
            primary = "Linked Aadhaar",
            trailingMeta = "Locked",
            leadingIcon = Icons.Rounded.Bolt,
            state = WiomListItemState.Disabled,
        )
    }
}

@Preview(name = "ListItem · IconBg Brand", showBackground = true)
@Composable
private fun PreviewListItemIconBgBrand() {
    WiomTheme {
        WiomListItemIconBg(
            primary = "Wallet",
            secondary = "Recharges, refunds, pending payments",
            leadingIcon = Icons.Rounded.AccountBalanceWallet,
            leadingTone = WiomIconBadgeTone.Brand,
            onClick = {},
        )
    }
}

@Preview(name = "ListItem · IconBg Disabled", showBackground = true)
@Composable
private fun PreviewListItemIconBgDisabled() {
    WiomTheme {
        WiomListItemIconBg(
            primary = "Wallet",
            secondary = "Locked until KYC complete",
            leadingIcon = Icons.Rounded.AccountBalanceWallet,
            leadingTone = WiomIconBadgeTone.Brand,
            state = WiomListItemState.Disabled,
        )
    }
}

@Preview(name = "ListItem · Checkbox", showBackground = true)
@Composable
private fun PreviewListItemCheckbox() {
    WiomTheme {
        WiomListItemCheckbox(
            primary = "Quarterly plan",
            selection = WiomCheckboxSelection.Selected,
            onSelectionChange = {},
        )
    }
}

@Preview(name = "ListItem · Radio", showBackground = true)
@Composable
private fun PreviewListItemRadio() {
    WiomTheme {
        WiomListItemRadio(
            primary = "हिन्दी",
            radioSelected = true,
            onRadioSelect = {},
        )
    }
}

@Preview(name = "ListItem · Switch", showBackground = true)
@Composable
private fun PreviewListItemSwitch() {
    WiomTheme {
        WiomListItemSwitch(
            primary = "Push notifications",
            secondary = "Plan renewals and offers",
            leadingIcon = Icons.Rounded.Notifications,
            checked = true,
            onCheckedChange = {},
        )
    }
}

@Preview(name = "ListItem · Switch Disabled (dark mode)", showBackground = true)
@Composable
private fun PreviewListItemSwitchDisabled() {
    WiomTheme {
        WiomListItemSwitch(
            primary = "Dark mode",
            leadingIcon = Icons.Rounded.DarkMode,
            checked = false,
            onCheckedChange = {},
            state = WiomListItemState.Disabled,
        )
    }
}

// endregion
