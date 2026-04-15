package com.wiom.designsystem.component.checkbox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.foundation.icon.WiomIcons
import com.wiom.designsystem.theme.WiomTheme

/**
 * Wiom checkbox — binary or multi-select form control.
 *
 * The whole row (indicator + label) is tappable. Label is required.
 * Filled state removes the stroke (fill IS the boundary).
 *
 * DO NOT pre-check consent checkboxes — user must actively opt in.
 */
@Composable
fun WiomCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    helper: String? = null,
    errorText: String? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = modifier.clickable(
            enabled = enabled,
            interactionSource = interactionSource,
            indication = null,
            onClick = { onCheckedChange(!checked) },
        ),
        horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
        verticalAlignment = Alignment.Top,
    ) {
        CheckboxIndicator(checked = checked, enabled = enabled, isError = isError)
        Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs)) {
            Text(
                text = label,
                style = WiomTheme.type.bodyMd,
                color = when {
                    !enabled -> WiomTheme.colors.text.disabled
                    else -> WiomTheme.colors.text.primary
                },
            )
            val sub = when {
                isError && errorText != null -> errorText to WiomTheme.colors.negative.primary
                helper != null -> helper to if (enabled) WiomTheme.colors.text.secondary else WiomTheme.colors.text.disabled
                else -> null
            }
            sub?.let { (text, color) ->
                Text(text = text, style = WiomTheme.type.bodySm, color = color)
            }
        }
    }
}

@Composable
private fun CheckboxIndicator(checked: Boolean, enabled: Boolean, isError: Boolean) {
    val borderColor = when {
        isError -> WiomTheme.colors.negative.primary
        !enabled -> WiomTheme.colors.border.strong.copy(alpha = 0.4f)
        else -> WiomTheme.colors.border.strong
    }
    val fillColor: Color = when {
        checked && isError -> WiomTheme.colors.negative.primary
        checked && !enabled -> WiomTheme.colors.brand.primary.copy(alpha = 0.4f)
        checked -> WiomTheme.colors.brand.primary
        !enabled -> WiomTheme.colors.surface.subtle
        else -> Color.Transparent
    }
    Box(
        modifier = Modifier
            .size(20.dp)
            .clip(RoundedCornerShape(WiomTheme.radius.tiny))
            .background(fillColor)
            .then(
                if (!checked) {
                    Modifier.border(
                        width = WiomTheme.stroke.medium,
                        color = borderColor,
                        shape = RoundedCornerShape(WiomTheme.radius.tiny),
                    )
                } else Modifier
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (checked) {
            WiomIcon(
                id = WiomIcons.check,
                contentDescription = null,
                size = 16.dp,
                tint = WiomTheme.colors.text.inverse,
                modifier = Modifier.alpha(if (enabled) 1f else 0.4f),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun CheckboxPreview() = WiomTheme {
    Column(
        modifier = Modifier.background(WiomTheme.colors.surface.base),
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md),
    ) {
        WiomCheckbox(checked = false, onCheckedChange = {}, label = "Unchecked")
        WiomCheckbox(checked = true, onCheckedChange = {}, label = "Checked")
        WiomCheckbox(checked = false, onCheckedChange = {}, label = "With helper", helper = "Supporting text")
        WiomCheckbox(checked = false, onCheckedChange = {}, label = "I agree to the Terms & Conditions", isError = true, errorText = "You must agree to continue")
        WiomCheckbox(checked = true, onCheckedChange = {}, label = "Disabled checked", enabled = false)
        WiomCheckbox(checked = false, onCheckedChange = {}, label = "Disabled unchecked", enabled = false)
    }
}
