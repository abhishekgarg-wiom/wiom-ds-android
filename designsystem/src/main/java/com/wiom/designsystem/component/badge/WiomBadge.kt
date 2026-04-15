package com.wiom.designsystem.component.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wiom.designsystem.theme.WiomTheme

enum class WiomBadgeColor { Brand, Positive, Warning, Negative, Info, Neutral }
enum class WiomBadgeSize { Default, Small }
enum class WiomBadgeStyle { Filled, Tinted }

/**
 * Dot badge — 8dp filled circle. Overlay only; position absolutely top-end on an icon/avatar.
 * Allowed colors: Brand, Negative, Neutral.
 */
@Composable
fun WiomBadgeDot(
    modifier: Modifier = Modifier,
    color: WiomBadgeColor = WiomBadgeColor.Negative,
) {
    Box(
        modifier = modifier
            .size(8.dp)
            .background(dotFill(color), RoundedCornerShape(WiomTheme.radius.full))
    )
}

/**
 * Count badge — numeric indicator. Hides when [count] <= 0.
 * Caps at "9+" if [maxOneDigit] else "99+". Allowed colors: Brand, Negative.
 */
@Composable
fun WiomBadgeCount(
    count: Int,
    modifier: Modifier = Modifier,
    color: WiomBadgeColor = WiomBadgeColor.Negative,
    maxOneDigit: Boolean = true,
) {
    if (count <= 0) return
    val display = when {
        maxOneDigit && count > 9 -> "9+"
        !maxOneDigit && count > 99 -> "99+"
        else -> count.toString()
    }
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 18.dp, minHeight = 18.dp)
            .background(countFill(color), RoundedCornerShape(WiomTheme.radius.full))
            .padding(horizontal = 5.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = display,
            style = WiomTheme.type.metaXs,
            color = WiomTheme.colors.text.onColor,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * Label badge — text status.
 * Size: Default (28dp, type.label.md) or Small (24dp, type.label.sm).
 * Style: Filled (terminal states, Default only) or Tinted (transitional).
 */
@Composable
fun WiomBadgeLabel(
    text: String,
    modifier: Modifier = Modifier,
    size: WiomBadgeSize = WiomBadgeSize.Default,
    color: WiomBadgeColor = WiomBadgeColor.Neutral,
    style: WiomBadgeStyle = WiomBadgeStyle.Tinted,
) {
    val actualStyle = if (size == WiomBadgeSize.Small) WiomBadgeStyle.Tinted else style
    val (fill, textColor) = labelColors(color, actualStyle)
    val typo = when (size) {
        WiomBadgeSize.Default -> WiomTheme.type.labelMd
        WiomBadgeSize.Small -> WiomTheme.type.labelSm
    }
    val hPad = when (size) {
        WiomBadgeSize.Default -> WiomTheme.spacing.md
        WiomBadgeSize.Small -> WiomTheme.spacing.sm
    }
    Row(
        modifier = modifier
            .background(fill, RoundedCornerShape(WiomTheme.radius.tiny))
            .padding(horizontal = hPad, vertical = WiomTheme.spacing.xs),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = text, style = typo, color = textColor, maxLines = 1)
    }
}

@Composable
private fun dotFill(color: WiomBadgeColor): Color = when (color) {
    WiomBadgeColor.Brand -> WiomTheme.colors.brand.primary
    WiomBadgeColor.Negative -> WiomTheme.colors.negative.primary
    WiomBadgeColor.Neutral -> WiomTheme.colors.surface.muted
    else -> WiomTheme.colors.negative.primary
}

@Composable
private fun countFill(color: WiomBadgeColor): Color = when (color) {
    WiomBadgeColor.Brand -> WiomTheme.colors.brand.primary
    else -> WiomTheme.colors.negative.primary
}

@Composable
private fun labelColors(
    color: WiomBadgeColor,
    style: WiomBadgeStyle,
): Pair<Color, Color> {
    val c = WiomTheme.colors
    return when (style) {
        WiomBadgeStyle.Filled -> when (color) {
            WiomBadgeColor.Positive -> c.positive.primary to c.text.onColor
            WiomBadgeColor.Warning -> c.warning.primary to c.text.onColor
            WiomBadgeColor.Negative -> c.negative.primary to c.text.onColor
            WiomBadgeColor.Info -> c.info.primary to c.text.onColor
            WiomBadgeColor.Neutral -> c.surface.muted to c.text.secondary
            WiomBadgeColor.Brand -> c.brand.primary to c.text.onColor
        }
        WiomBadgeStyle.Tinted -> when (color) {
            WiomBadgeColor.Positive -> c.positive.tint to c.positive.primary
            WiomBadgeColor.Warning -> c.warning.subtle to c.warning.primary
            WiomBadgeColor.Negative -> c.negative.tint to c.negative.primary
            WiomBadgeColor.Info -> c.info.tint to c.info.primary
            WiomBadgeColor.Neutral -> c.surface.subtle to c.text.secondary
            WiomBadgeColor.Brand -> c.brand.primaryTint to c.brand.primary
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BadgeDotPreview() = WiomTheme {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        WiomBadgeDot(color = WiomBadgeColor.Brand)
        WiomBadgeDot(color = WiomBadgeColor.Negative)
        WiomBadgeDot(color = WiomBadgeColor.Neutral)
    }
}

@Preview(showBackground = true)
@Composable
private fun BadgeCountPreview() = WiomTheme {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        WiomBadgeCount(count = 1)
        WiomBadgeCount(count = 5, color = WiomBadgeColor.Brand)
        WiomBadgeCount(count = 15)
        WiomBadgeCount(count = 100, maxOneDigit = false)
    }
}

@Preview(showBackground = true)
@Composable
private fun BadgeLabelPreview() = WiomTheme {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        WiomBadgeLabel("Confirmed", color = WiomBadgeColor.Positive, style = WiomBadgeStyle.Filled)
        WiomBadgeLabel("Pending", color = WiomBadgeColor.Warning, style = WiomBadgeStyle.Tinted)
        WiomBadgeLabel("Failed", color = WiomBadgeColor.Negative, style = WiomBadgeStyle.Filled)
        WiomBadgeLabel("पक्का", size = WiomBadgeSize.Small, color = WiomBadgeColor.Positive)
    }
}
