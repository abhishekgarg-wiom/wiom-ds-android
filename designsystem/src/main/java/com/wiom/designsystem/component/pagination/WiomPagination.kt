package com.wiom.designsystem.component.pagination

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.theme.WiomTheme

/**
 * Wiom Pagination — three semantics, four composables.
 *
 *  - [WiomPaginationDots] — 2–6 dots (Simple circle or Expanded pill active) for onboarding /
 *    promo carousels.
 *  - [WiomPaginationBars] — 2–6 filled bars for multi-step wizards (all bars up to `current`
 *    are filled to show accumulated progress).
 *  - [WiomPaginationCounter] — "N / Total" text + prev/next chevrons for long paginated lists.
 *  - [WiomPaginationScrollIndicator] — thin rail + thumb for continuous horizontal scroll.
 *
 * Never hand-draw pagination with manual rectangles — use one of these.
 */

/** Visual style for [WiomPaginationDots]. */
enum class WiomPaginationDotStyle { Simple, Expanded }

/**
 * Dot-style pagination for 2–6 discrete pages.
 *
 * Active dot is `bg.brand`; inactive dots are `stroke.subtle`. In [WiomPaginationDotStyle.Expanded]
 * the active slot becomes a 24×8 pill; in [WiomPaginationDotStyle.Simple] every slot is an 8×8
 * circle. Optional `counterLabel` renders below the row in `type.labelMd` / `text.subtle`.
 *
 * @param total number of slots (clamped to 2..6).
 * @param current 1-based index of the active slot (clamped to 1..total).
 */
@Composable
fun WiomPaginationDots(
    total: Int,
    current: Int,
    modifier: Modifier = Modifier,
    style: WiomPaginationDotStyle = WiomPaginationDotStyle.Simple,
    counterLabel: String? = null,
) {
    val clampedTotal = total.coerceIn(2, 6)
    val clampedCurrent = current.coerceIn(1, clampedTotal)
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(clampedTotal) { i ->
                val active = (i + 1) == clampedCurrent
                val width = if (active && style == WiomPaginationDotStyle.Expanded) 24.dp else 8.dp
                Box(
                    Modifier
                        .size(width = width, height = 8.dp)
                        .clip(RoundedCornerShape(WiomTheme.radius.full))
                        .background(
                            if (active) WiomTheme.color.bg.brand
                            else WiomTheme.color.stroke.subtle
                        )
                )
            }
        }
        if (counterLabel != null) {
            Text(
                text = counterLabel,
                style = WiomTheme.type.labelMd,
                color = WiomTheme.color.text.subtle,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/**
 * Bar-style pagination for 2–6 step wizards.
 *
 * Slots 1..`current` are all filled with `bg.brand` to show accumulated progress; remaining
 * slots are `stroke.subtle`. 4dp tall, `space.xs` gap between bars, bars share the full width
 * equally.
 *
 * @param total number of bars (clamped to 2..6).
 * @param current 1-based index of the current step (clamped to 1..total).
 */
@Composable
fun WiomPaginationBars(
    total: Int,
    current: Int,
    modifier: Modifier = Modifier,
    counterLabel: String? = null,
) {
    val clampedTotal = total.coerceIn(2, 6)
    val clampedCurrent = current.coerceIn(1, clampedTotal)
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs),
        ) {
            repeat(clampedTotal) { i ->
                val filled = (i + 1) <= clampedCurrent
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(WiomTheme.radius.full))
                        .background(
                            if (filled) WiomTheme.color.bg.brand
                            else WiomTheme.color.stroke.subtle
                        )
                )
            }
        }
        if (counterLabel != null) {
            Text(
                text = counterLabel,
                style = WiomTheme.type.labelMd,
                color = WiomTheme.color.text.subtle,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/**
 * Counter pagination — "current / total" label with chevron-left / chevron-right buttons.
 *
 * At bounds, the out-of-range chevron is **hidden** (not disabled). A spacer of equal width
 * occupies its place so the counter label doesn't shift horizontally. 48dp touch targets;
 * chevrons are `Icons.Rounded.ChevronLeft` / `.ChevronRight` at 24dp tinted `icon.nonAction`.
 * Label uses `type.labelMd` in `text.subtle`, centered.
 *
 * @param current 1-based current page.
 * @param total total number of pages.
 */
@Composable
fun WiomPaginationCounter(
    current: Int,
    total: Int,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val showPrev = current > 1
    val showNext = current < total
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showPrev) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clickable(onClick = onPrev),
                contentAlignment = Alignment.Center,
            ) {
                WiomIcon(
                    imageVector = Icons.Rounded.ChevronLeft,
                    contentDescription = "Previous",
                    size = WiomTheme.iconSize.md,
                    tint = WiomTheme.color.icon.nonAction,
                )
            }
        } else {
            Spacer(Modifier.size(48.dp))
        }
        Text(
            text = "$current / $total",
            style = WiomTheme.type.labelMd,
            color = WiomTheme.color.text.subtle,
            textAlign = TextAlign.Center,
        )
        if (showNext) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clickable(onClick = onNext),
                contentAlignment = Alignment.Center,
            ) {
                WiomIcon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = "Next",
                    size = WiomTheme.iconSize.md,
                    tint = WiomTheme.color.icon.nonAction,
                )
            }
        } else {
            Spacer(Modifier.size(48.dp))
        }
    }
}

/**
 * Thin rail + thumb indicator for continuous horizontal scroll (e.g. plan rails, recommendation
 * strips). Not for discrete pages.
 *
 * 4dp tall rail in `bg.muted`; thumb in `bg.brand`. The thumb width = `visibleFraction * rail`
 * and its offset = `progress * (rail - thumb)`. Both `progress` and `visibleFraction` are clamped
 * to `0f..1f`.
 *
 * @param progress 0f = at the start, 1f = at the end of the scrollable content.
 * @param visibleFraction 0f..1f — share of content currently on screen (thumb width). Default 0.3.
 */
@Composable
fun WiomPaginationScrollIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    visibleFraction: Float = 0.3f,
) {
    val clampedProgress = progress.coerceIn(0f, 1f)
    val clampedVisible = visibleFraction.coerceIn(0f, 1f)
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(WiomTheme.radius.full))
            .background(WiomTheme.color.bg.muted),
    ) {
        val railWidth = maxWidth
        val thumbWidth = railWidth * clampedVisible
        val thumbOffset = (railWidth - thumbWidth) * clampedProgress
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .width(thumbWidth)
                .fillMaxHeight()
                .clip(RoundedCornerShape(WiomTheme.radius.full))
                .background(WiomTheme.color.bg.brand)
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewDotsSimple() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomPaginationDots(total = 4, current = 2, style = WiomPaginationDotStyle.Simple)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewDotsExpanded() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomPaginationDots(total = 4, current = 2, style = WiomPaginationDotStyle.Expanded)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewDotsWithCounter() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomPaginationDots(
            total = 3,
            current = 1,
            style = WiomPaginationDotStyle.Simple,
            counterLabel = "1 / 3",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 360)
@Composable
private fun PreviewBars() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomPaginationBars(total = 5, current = 3)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 360)
@Composable
private fun PreviewBarsWithCounter() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomPaginationBars(total = 5, current = 3, counterLabel = "Step 3 of 5")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewCounterMiddle() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomPaginationCounter(current = 4, total = 10, onPrev = {}, onNext = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewCounterFirst() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomPaginationCounter(current = 1, total = 10, onPrev = {}, onNext = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewCounterLast() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomPaginationCounter(current = 10, total = 10, onPrev = {}, onNext = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 240)
@Composable
private fun PreviewScrollIndicator() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomPaginationScrollIndicator(progress = 0.4f, visibleFraction = 0.3f)
    }
}
