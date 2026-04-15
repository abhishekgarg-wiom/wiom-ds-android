package com.wiom.designsystem.component.switch

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wiom.designsystem.theme.WiomTheme

private val TRACK_WIDTH = 52.dp
private val TRACK_HEIGHT = 32.dp
private val THUMB_SIZE = 24.dp
private val THUMB_INSET = 4.dp

/**
 * Wiom switch — binary on/off, instant-apply.
 *
 * V1: no hover/focus/error states. Use [WiomCheckbox] when you need form submit or validation.
 */
@Composable
fun WiomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    helper: String? = null,
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
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SwitchTrack(checked = checked, enabled = enabled)
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
private fun SwitchTrack(checked: Boolean, enabled: Boolean) {
    val fillColor: Color = when {
        checked && !enabled -> WiomTheme.colors.brand.primary.copy(alpha = 0.4f)
        checked -> WiomTheme.colors.brand.primary
        !enabled -> WiomTheme.colors.surface.subtle
        else -> WiomTheme.colors.surface.muted
    }
    val offsetX by animateDpAsState(
        targetValue = if (checked) TRACK_WIDTH - THUMB_SIZE - THUMB_INSET else THUMB_INSET,
        label = "switch-thumb-offset",
    )
    Box(
        modifier = Modifier
            .size(width = TRACK_WIDTH, height = TRACK_HEIGHT)
            .clip(RoundedCornerShape(WiomTheme.radius.full))
            .background(fillColor)
            .then(
                if (!checked) {
                    Modifier.border(
                        width = WiomTheme.stroke.medium,
                        color = if (enabled)
                            WiomTheme.colors.border.strong
                        else
                            WiomTheme.colors.border.strong.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(WiomTheme.radius.full),
                    )
                } else Modifier
            ),
    ) {
        Box(
            modifier = Modifier
                .offset(x = offsetX, y = THUMB_INSET)
                .size(THUMB_SIZE)
                .clip(CircleShape)
                .background(Color.White)
                .alpha(if (enabled) 1f else 0.8f),
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun SwitchPreview() = WiomTheme {
    Column(
        modifier = Modifier.background(WiomTheme.colors.surface.base),
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md),
    ) {
        WiomSwitch(checked = false, onCheckedChange = {}, label = "Push notifications")
        WiomSwitch(checked = true, onCheckedChange = {}, label = "Auto-renew plan", helper = "Your plan will renew on the 15th of each month")
        WiomSwitch(checked = false, onCheckedChange = {}, label = "Disabled off", enabled = false)
        WiomSwitch(checked = true, onCheckedChange = {}, label = "Disabled on", enabled = false)
    }
}
