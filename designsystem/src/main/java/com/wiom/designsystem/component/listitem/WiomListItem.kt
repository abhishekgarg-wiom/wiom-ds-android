package com.wiom.designsystem.component.listitem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.theme.WiomTheme

/**
 * Unified list-row primitive. One API — five Types mapped 1:1 to the Figma component set.
 *
 * Pick the Type by the leading-slot archetype:
 *
 * | Type           | Leading                                                           |
 * |----------------|-------------------------------------------------------------------|
 * | [Default]      | 24dp icon, optional                                               |
 * | [IconWithBg]   | `WiomIconBadge` (48dp tinted container)                           |
 * | [Checkbox]     | `WiomCheckbox` — pushes its label into this row's Primary text    |
 * | [Radio]        | `WiomRadio` — same                                                |
 * | [Switch]       | 24dp icon + trailing `WiomSwitch`                                 |
 *
 * The `Default` Type is the only one that renders a trailing chevron. The chevron is hidden
 * when [hasTrailingIcon] is false (menu rows, status rows) or when a non-empty `trailingMeta`
 * is set (the meta is the trailing signal). `Checkbox` / `Radio` Types never render trailing
 * chrome — the row is tappable as a whole. `Switch` always renders its trailing toggle.
 *
 * When [selected] is true, the row adopts `bg.selected` and renders an 8dp `bg.brand` dot on
 * the trailing side — used by picker bottom-sheets for the currently-chosen option.
 */
enum class WiomListItemType { Default, IconWithBg, Checkbox, Radio, Switch }

/**
 * Slot-based list item — universal primitive used by every `WiomListItem*` convenience wrapper.
 *
 * Prefer the typed wrappers below for common cases:
 * - [WiomListItem] — Default Type (icon + chevron)
 * - [WiomListItemIconBadge] — IconWithBg Type (badge leading + chevron)
 * - [WiomListItemCheckbox] / [WiomListItemRadio] / [WiomListItemSwitch]
 *
 * Use this raw slot API only when you need to compose a custom leading/trailing combo that
 * the wrappers don't expose (e.g. avatars, nested badges).
 */
@Composable
fun WiomListItemBase(
    primary: String,
    modifier: Modifier = Modifier,
    secondary: String? = null,
    trailingMeta: String? = null,
    selected: Boolean = false,
    enabled: Boolean = true,
    @Suppress("UNUSED_PARAMETER") type: WiomListItemType = WiomListItemType.Default,
    onClick: (() -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    // `type` is a semantic marker — the actual rendering is driven by the leading/trailing
    // slots. Kept in the API so callers (and future tooling: semantics, analytics, lint)
    // know which Figma archetype a custom row maps to.
    val bg: Color = when {
        selected -> WiomTheme.color.bg.selected
        else -> Color.Transparent
    }
    val primaryColor: Color =
        if (enabled) WiomTheme.color.text.default else WiomTheme.color.text.disabled
    val secondaryColor: Color =
        if (enabled) WiomTheme.color.text.subtle else WiomTheme.color.text.disabled
    val metaColor: Color =
        if (enabled) WiomTheme.color.text.subtle else WiomTheme.color.text.disabled

    val verticalAlignment =
        if (secondary != null) Alignment.Top else Alignment.CenterVertically

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(bg)
            .then(
                if (onClick != null && enabled) Modifier.clickable(onClick = onClick)
                else Modifier
            )
            .defaultMinSize(minHeight = WiomTheme.spacing.huge)
            .padding(
                horizontal = WiomTheme.spacing.lg,
                vertical = WiomTheme.spacing.md,
            ),
        verticalAlignment = verticalAlignment,
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

        // Trailing composition: optional meta text → selected dot (if selected) → trailing slot.
        val hasMeta = !trailingMeta.isNullOrEmpty()
        if (hasMeta || selected || trailing != null) {
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
                if (selected) {
                    Box(
                        modifier = Modifier
                            .size(WiomTheme.spacing.sm)
                            .background(
                                color = WiomTheme.color.bg.brand,
                                shape = CircleShape,
                            ),
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
 * Default List Item — 24dp leading icon + primary (+ optional secondary) + optional trailing
 * meta + chevron. The most common row in the app.
 *
 * @param leadingIcon optional. Pass `null` to hide the leading slot entirely (menu-row pattern).
 * @param hasTrailingIcon when false, hides the chevron. Use for status rows (where a trailing
 *                       meta replaces the chevron) and menu rows.
 * @param selected picker-row selection. Adds `bg.selected` fill and a brand dot on the trailing
 *                side.
 */
@Composable
fun WiomListItem(
    primary: String,
    modifier: Modifier = Modifier,
    secondary: String? = null,
    trailingMeta: String? = null,
    leadingIcon: ImageVector? = null,
    hasTrailingIcon: Boolean = true,
    selected: Boolean = false,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
) {
    val iconTint = if (enabled) WiomTheme.color.icon.nonAction else WiomTheme.color.icon.disabled
    val chevronTint = if (enabled) WiomTheme.color.icon.action else WiomTheme.color.icon.disabled

    WiomListItemBase(
        primary = primary,
        modifier = modifier,
        secondary = secondary,
        trailingMeta = trailingMeta,
        selected = selected,
        enabled = enabled,
        type = WiomListItemType.Default,
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
        trailing = if (hasTrailingIcon && !selected) {
            {
                WiomIcon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = null,
                    size = WiomTheme.iconSize.sm,
                    tint = chevronTint,
                )
            }
        } else null,
    )
}

/**
 * IconWithBg List Item — leading slot hosts a `WiomIconBadge` (md size). Use for prominent
 * settings-section or feature-entry rows.
 *
 * The `leadingBadge` slot is intentionally a composable — the caller supplies a
 * `com.wiom.designsystem.component.iconbadge.WiomIconBadge(...)` instance (built by Agent A).
 * This keeps the list-item module decoupled from the iconbadge module.
 */
@Composable
fun WiomListItemIconBadge(
    primary: String,
    leadingBadge: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    secondary: String? = null,
    trailingMeta: String? = null,
    hasTrailingIcon: Boolean = true,
    selected: Boolean = false,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
) {
    val chevronTint = if (enabled) WiomTheme.color.icon.action else WiomTheme.color.icon.disabled
    WiomListItemBase(
        primary = primary,
        modifier = modifier,
        secondary = secondary,
        trailingMeta = trailingMeta,
        selected = selected,
        enabled = enabled,
        type = WiomListItemType.IconWithBg,
        onClick = onClick,
        leading = leadingBadge,
        trailing = if (hasTrailingIcon && !selected) {
            {
                WiomIcon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = null,
                    size = WiomTheme.iconSize.sm,
                    tint = chevronTint,
                )
            }
        } else null,
    )
}

/**
 * Checkbox List Item — leading is a `com.wiom.designsystem.component.selectioncontrol.WiomCheckbox`
 * instance supplied by the caller. The checkbox's own label is suppressed; this row's
 * [primary] is THE label.
 *
 * Whole-row tap toggles the checkbox — wire `onClick` to invert the `checked` state that
 * backs the [leadingCheckbox] slot.
 */
@Composable
fun WiomListItemCheckbox(
    primary: String,
    leadingCheckbox: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    secondary: String? = null,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
) {
    WiomListItemBase(
        primary = primary,
        modifier = modifier,
        secondary = secondary,
        selected = false,
        enabled = enabled,
        type = WiomListItemType.Checkbox,
        onClick = onClick,
        leading = leadingCheckbox,
        trailing = null,
    )
}

/**
 * Radio List Item — leading is a `com.wiom.designsystem.component.selectioncontrol.WiomRadio`
 * instance supplied by the caller. Whole-row tap selects the radio.
 *
 * Use `selected = true` ONLY for picker-sheet rows where you want the row highlight
 * (`bg.selected`) AND a brand dot — the radio indicator alone is usually enough inside a
 * bottom sheet.
 */
@Composable
fun WiomListItemRadio(
    primary: String,
    leadingRadio: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    secondary: String? = null,
    selected: Boolean = false,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
) {
    WiomListItemBase(
        primary = primary,
        modifier = modifier,
        secondary = secondary,
        selected = selected,
        enabled = enabled,
        type = WiomListItemType.Radio,
        onClick = onClick,
        leading = leadingRadio,
        trailing = null,
    )
}

/**
 * Switch List Item — 24dp leading icon + primary (+ optional secondary) + trailing
 * `com.wiom.designsystem.component.selectioncontrol.WiomSwitch`. Used for settings toggles
 * (notifications, dark mode, auto-pay).
 *
 * The row is tappable and flips the switch via [onClick]. Switch's trailing slot stays
 * visible regardless of `selected`.
 */
@Composable
fun WiomListItemSwitch(
    primary: String,
    trailingSwitch: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    secondary: String? = null,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
) {
    val iconTint = if (enabled) WiomTheme.color.icon.nonAction else WiomTheme.color.icon.disabled
    WiomListItemBase(
        primary = primary,
        modifier = modifier,
        secondary = secondary,
        selected = false,
        enabled = enabled,
        type = WiomListItemType.Switch,
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
        trailing = trailingSwitch,
    )
}

// region Previews ------------------------------------------------------------------------------

@Preview(name = "ListItem · Default", showBackground = true)
@Composable
private fun PreviewListItemDefault() {
    com.wiom.designsystem.theme.WiomTheme {
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
    com.wiom.designsystem.theme.WiomTheme {
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
    com.wiom.designsystem.theme.WiomTheme {
        WiomListItem(
            primary = "Language",
            trailingMeta = "English",
            onClick = {},
        )
    }
}

@Preview(name = "ListItem · Status row (no chevron)", showBackground = true)
@Composable
private fun PreviewListItemStatusRow() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomListItem(
            primary = "Build",
            trailingMeta = "v3.4.1",
            hasTrailingIcon = false,
        )
    }
}

@Preview(name = "ListItem · Selected (picker row)", showBackground = true)
@Composable
private fun PreviewListItemSelected() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomListItem(
            primary = "English",
            selected = true,
            onClick = {},
        )
    }
}

@Preview(name = "ListItem · Disabled", showBackground = true)
@Composable
private fun PreviewListItemDisabled() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomListItem(
            primary = "Linked Aadhaar",
            trailingMeta = "Locked",
            leadingIcon = Icons.Rounded.Bolt,
            enabled = false,
        )
    }
}

@Preview(name = "ListItem · IconWithBg (placeholder badge)", showBackground = true)
@Composable
private fun PreviewListItemIconBadge() {
    com.wiom.designsystem.theme.WiomTheme {
        // Agent A's WiomIconBadge is pending — use a simple bordered box as a visual placeholder.
        WiomListItemIconBadge(
            primary = "Payments",
            secondary = "Auto-pay, history, refunds",
            leadingBadge = {
                Box(
                    modifier = Modifier
                        .size(WiomTheme.iconSize.lg)
                        .background(
                            color = WiomTheme.color.bg.brandSubtle,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                WiomTheme.radius.medium
                            ),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    WiomIcon(
                        imageVector = Icons.Rounded.Bolt,
                        contentDescription = null,
                        tint = WiomTheme.color.icon.brand,
                    )
                }
            },
            onClick = {},
        )
    }
}

@Preview(name = "ListItem · Checkbox (placeholder)", showBackground = true)
@Composable
private fun PreviewListItemCheckbox() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomListItemCheckbox(
            primary = "Save my login",
            leadingCheckbox = {
                // Agent A's WiomCheckbox pending — placeholder indicator.
                Box(
                    modifier = Modifier
                        .size(WiomTheme.iconSize.sm)
                        .background(
                            color = WiomTheme.color.bg.brand,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                WiomTheme.radius.tiny
                            ),
                        ),
                )
            },
            onClick = {},
        )
    }
}

@Preview(name = "ListItem · Radio (placeholder)", showBackground = true)
@Composable
private fun PreviewListItemRadio() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomListItemRadio(
            primary = "हिन्दी",
            leadingRadio = {
                Box(
                    modifier = Modifier
                        .size(WiomTheme.iconSize.sm)
                        .background(color = WiomTheme.color.bg.brand, shape = CircleShape),
                )
            },
            selected = true,
            onClick = {},
        )
    }
}

@Preview(name = "ListItem · Switch (placeholder)", showBackground = true)
@Composable
private fun PreviewListItemSwitch() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomListItemSwitch(
            primary = "Notifications",
            leadingIcon = Icons.Rounded.Notifications,
            trailingSwitch = {
                // Placeholder — Agent A's WiomSwitch pending. Uses iconSize tokens as a
                // visual stand-in; final component owns its own 52x32dp track dimensions.
                Box(
                    modifier = Modifier
                        .size(width = WiomTheme.iconSize.lg, height = WiomTheme.spacing.xxl)
                        .background(
                            color = WiomTheme.color.bg.brand,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                WiomTheme.radius.full
                            ),
                        ),
                )
            },
            onClick = {},
        )
    }
}

// endregion
