package com.wiom.designsystem.component.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wiom.designsystem.component.checkbox.WiomCheckbox
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.foundation.icon.WiomIcons
import com.wiom.designsystem.theme.WiomTheme

enum class WiomButtonType { Primary, Secondary, Tertiary, Destructive }

sealed class WiomButtonIcon {
    data class Leading(@DrawableRes val id: Int) : WiomButtonIcon()
    data class Trailing(@DrawableRes val id: Int) : WiomButtonIcon()
}

/**
 * Wiom button (CTA).
 *
 * Height is 48dp at the default font size (12dp padding + 24sp line-height).
 * `defaultMinSize` keeps the touch target at 48dp minimum while allowing growth
 * when the user scales up the system font — text never wraps, truncates, or shrinks.
 *
 * Loading state hides text and icon; shows a spinner in the right tint for the type.
 *
 * @param type Primary (main action), Secondary (alternate, outlined), Tertiary (low-emphasis dismiss), Destructive (irreversible)
 * @param icon null for text-only, [WiomButtonIcon.Leading] or [WiomButtonIcon.Trailing] to add a 20dp icon
 * @param loading hides content and shows a spinner. Keep `enabled = true` — don't double-disable.
 */
@Composable
fun WiomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: WiomButtonType = WiomButtonType.Primary,
    enabled: Boolean = true,
    loading: Boolean = false,
    icon: WiomButtonIcon? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val style = resolveStyle(type = type, enabled = enabled, pressed = pressed)
    val shape = RoundedCornerShape(WiomTheme.radius.large)

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .clip(shape)
            .background(style.fill)
            .then(
                style.borderColor?.let {
                    Modifier.border(width = WiomTheme.stroke.medium, color = it, shape = shape)
                } ?: Modifier
            )
            .clickable(
                enabled = enabled && !loading,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = WiomTheme.spacing.lg, vertical = WiomTheme.spacing.md),
        contentAlignment = Alignment.Center,
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = style.spinnerColor,
                strokeWidth = 2.dp,
            )
        } else {
            androidx.compose.foundation.layout.Row(
                horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                (icon as? WiomButtonIcon.Leading)?.let {
                    WiomIcon(id = it.id, contentDescription = null, size = 20.dp, tint = style.textColor)
                }
                Text(
                    text = text,
                    style = WiomTheme.type.labelLg,
                    color = style.textColor,
                    maxLines = 1,
                )
                (icon as? WiomButtonIcon.Trailing)?.let {
                    WiomIcon(id = it.id, contentDescription = null, size = 20.dp, tint = style.textColor)
                }
            }
        }
    }
}

/**
 * Wiom Acknowledge — a checkbox-gated confirmation row that must be checked
 * before its paired [WiomButton] becomes enabled.
 *
 * Use for: irreversible actions, terms/policy acceptance, verified-data submission,
 * partner task completion, credential handoff.
 *
 * Copy rule: always 1st person ("मैंने...", "I have...") — never 3rd person.
 */
@Composable
fun WiomAcknowledge(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    WiomCheckbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        label = text,
        modifier = modifier,
        enabled = enabled,
    )
}

private data class ButtonStyle(
    val fill: Color,
    val textColor: Color,
    val borderColor: Color?,
    val spinnerColor: Color,
)

@Composable
private fun resolveStyle(
    type: WiomButtonType,
    enabled: Boolean,
    pressed: Boolean,
): ButtonStyle {
    val c = WiomTheme.colors
    return when (type) {
        WiomButtonType.Primary -> when {
            !enabled -> ButtonStyle(c.surface.muted, c.text.disabled, null, c.text.disabled)
            pressed -> ButtonStyle(c.brand.primaryPressed, c.text.onColor, null, c.text.onColor)
            else -> ButtonStyle(c.brand.primary, c.text.onColor, null, c.text.onColor)
        }
        WiomButtonType.Secondary -> when {
            !enabled -> ButtonStyle(Color.Transparent, c.text.disabled, c.border.default, c.text.disabled)
            pressed -> ButtonStyle(c.brand.primarySubtle, c.brand.primary, c.brand.primary, c.brand.primary)
            else -> ButtonStyle(Color.Transparent, c.brand.primary, c.brand.primary, c.brand.primary)
        }
        WiomButtonType.Tertiary -> when {
            !enabled -> ButtonStyle(Color.Transparent, c.text.disabled, null, c.text.disabled)
            pressed -> ButtonStyle(c.brand.primarySubtle, c.brand.primary, null, c.brand.primary)
            else -> ButtonStyle(Color.Transparent, c.brand.primary, null, c.brand.primary)
        }
        WiomButtonType.Destructive -> when {
            !enabled -> ButtonStyle(c.surface.muted, c.text.disabled, null, c.text.disabled)
            pressed -> ButtonStyle(c.negative.primaryPressed, c.text.onColor, null, c.text.onColor)
            else -> ButtonStyle(c.negative.primary, c.text.onColor, null, c.text.onColor)
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun ButtonPreview() = WiomTheme {
    androidx.compose.foundation.layout.Column(
        modifier = Modifier
            .background(WiomTheme.colors.surface.base)
            .padding(WiomTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md),
    ) {
        WiomButton(text = "भुगतान करें", onClick = {}, type = WiomButtonType.Primary)
        WiomButton(text = "वापस जाएं", onClick = {}, type = WiomButtonType.Secondary)
        WiomButton(text = "Maybe later", onClick = {}, type = WiomButtonType.Tertiary)
        WiomButton(text = "Delete plan", onClick = {}, type = WiomButtonType.Destructive, icon = WiomButtonIcon.Leading(WiomIcons.cancel))
        WiomButton(text = "Processing…", onClick = {}, type = WiomButtonType.Primary, loading = true)
        WiomButton(text = "Disabled primary", onClick = {}, type = WiomButtonType.Primary, enabled = false)
        WiomAcknowledge(text = "मैंने सभी जानकारी सही दी है", checked = true, onCheckedChange = {})
    }
}
