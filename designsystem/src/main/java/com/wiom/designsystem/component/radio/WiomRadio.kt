package com.wiom.designsystem.component.radio

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.wiom.designsystem.theme.WiomTheme

/**
 * Wiom radio — single-choice within a group. Always use 2+ in a group.
 *
 * V1: no hover/focus states (mobile-first). Error shown at group level,
 * typically — pass isError per-item for the red border treatment.
 */
@Composable
fun WiomRadio(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    helper: String? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = modifier.clickable(
            enabled = enabled,
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick,
        ),
        horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
        verticalAlignment = Alignment.Top,
    ) {
        RadioIndicator(selected = selected, enabled = enabled, isError = isError)
        Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs)) {
            Text(
                text = label,
                style = WiomTheme.type.bodyMd,
                color = if (enabled) WiomTheme.colors.text.primary else WiomTheme.colors.text.disabled,
            )
            helper?.let {
                Text(
                    text = it,
                    style = WiomTheme.type.bodySm,
                    color = if (enabled) WiomTheme.colors.text.secondary else WiomTheme.colors.text.disabled,
                )
            }
        }
    }
}

@Composable
private fun RadioIndicator(selected: Boolean, enabled: Boolean, isError: Boolean) {
    val borderColor = when {
        isError -> WiomTheme.colors.negative.primary
        !enabled -> WiomTheme.colors.border.strong.copy(alpha = 0.4f)
        else -> WiomTheme.colors.border.strong
    }
    val fillColor: Color = when {
        selected && isError -> WiomTheme.colors.negative.primary
        selected && !enabled -> WiomTheme.colors.brand.primary.copy(alpha = 0.4f)
        selected -> WiomTheme.colors.brand.primary
        !enabled -> WiomTheme.colors.surface.subtle
        else -> Color.Transparent
    }
    Box(
        modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(fillColor)
            .then(
                if (!selected) {
                    Modifier.border(width = WiomTheme.stroke.medium, color = borderColor, shape = CircleShape)
                } else Modifier
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(WiomTheme.colors.text.inverse)
                    .alpha(if (enabled) 1f else 0.4f),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun RadioPreview() = WiomTheme {
    Column(
        modifier = Modifier.background(WiomTheme.colors.surface.base),
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md),
    ) {
        WiomRadio(selected = true, onClick = {}, label = "Plan A: ₹299/month — 1.5GB/day")
        WiomRadio(selected = false, onClick = {}, label = "Plan B: ₹449/month — 2GB/day + Hotstar", helper = "Billed monthly. Cancel anytime.")
        WiomRadio(selected = false, onClick = {}, label = "With error", isError = true)
        WiomRadio(selected = true, onClick = {}, label = "Disabled selected", enabled = false)
        WiomRadio(selected = false, onClick = {}, label = "Disabled unselected", enabled = false)
    }
}
