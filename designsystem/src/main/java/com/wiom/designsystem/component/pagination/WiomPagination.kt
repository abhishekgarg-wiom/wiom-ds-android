package com.wiom.designsystem.component.pagination

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.foundation.icon.WiomIcons
import com.wiom.designsystem.theme.WiomTheme

enum class WiomPaginationDotStyle { Simple, Expanded }

/**
 * Discrete-page pagination — dots. For 2–6 slots. Onboarding / promo carousels.
 *
 * [Simple]: all dots 8dp, active = brand. [Expanded]: active slot stretches to 24dp pill.
 */
@Composable
fun WiomPaginationDots(
    total: Int,
    current: Int,
    modifier: Modifier = Modifier,
    style: WiomPaginationDotStyle = WiomPaginationDotStyle.Simple,
    counterLabel: String? = null,
) {
    require(total in 2..6) { "WiomPagination supports 2–6 slots; got $total" }
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
            repeat(total) { i ->
                val active = (i + 1) == current
                val width = if (active && style == WiomPaginationDotStyle.Expanded) 24.dp else 8.dp
                Box(
                    modifier = Modifier
                        .size(width = width, height = 8.dp)
                        .clip(RoundedCornerShape(WiomTheme.radius.full))
                        .background(if (active) WiomTheme.colors.brand.primary else WiomTheme.colors.border.default)
                )
            }
        }
        counterLabel?.let {
            Text(
                text = it,
                style = WiomTheme.type.labelMd,
                color = WiomTheme.colors.text.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/**
 * Discrete-step pagination — bars. For wizards (recharge, KYC, onboarding steps).
 * Slots 1..[current] are all filled to show accumulated progress.
 */
@Composable
fun WiomPaginationBars(
    total: Int,
    current: Int,
    modifier: Modifier = Modifier,
    counterLabel: String? = null,
) {
    require(total in 2..6) { "WiomPaginationBars supports 2–6 slots; got $total" }
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(4.dp),
            horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs),
        ) {
            repeat(total) { i ->
                val filled = (i + 1) <= current
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(WiomTheme.radius.full))
                        .background(if (filled) WiomTheme.colors.brand.primary else WiomTheme.colors.border.default)
                )
            }
        }
        counterLabel?.let {
            Text(
                text = it,
                style = WiomTheme.type.labelMd,
                color = WiomTheme.colors.text.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/**
 * Paginated-list counter — "1 / 10" with prev/next chevrons. For long lists without swipe.
 * Chevrons hide at bounds (don't disable visually — hide them).
 */
@Composable
fun WiomPaginationCounter(
    current: Int,
    total: Int,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
    ) {
        if (current > 1) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clickable(onClick = onPrev),
                contentAlignment = Alignment.Center,
            ) {
                WiomIcon(
                    id = WiomIcons.arrowBack,
                    contentDescription = "Previous",
                    size = WiomTheme.icon.md,
                    tint = WiomTheme.colors.text.secondary,
                )
            }
        } else {
            Box(modifier = Modifier.size(48.dp))
        }
        Text(
            text = "$current / $total",
            style = WiomTheme.type.labelMd,
            color = WiomTheme.colors.text.primary,
        )
        if (current < total) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clickable(onClick = onNext),
                contentAlignment = Alignment.Center,
            ) {
                Box(modifier = Modifier.graphicsLayer { rotationZ = 180f }) {
                    WiomIcon(
                        id = WiomIcons.arrowBack,
                        contentDescription = "Next",
                        size = WiomTheme.icon.md,
                        tint = WiomTheme.colors.text.secondary,
                    )
                }
            }
        } else {
            Box(modifier = Modifier.size(48.dp))
        }
    }
}

/**
 * Continuous horizontal scroll indicator — thin rail + thumb.
 * Drive [progress] (0f..1f) and [visibleFraction] (0f..1f) from LazyListState in real code.
 */
@Composable
fun WiomPaginationScrollIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    visibleFraction: Float = 0.3f,
) {
    val p = progress.coerceIn(0f, 1f)
    val vf = visibleFraction.coerceIn(0.1f, 1f)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(WiomTheme.radius.full))
            .background(WiomTheme.colors.surface.muted),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(vf)
                .graphicsLayer {
                    translationX = (size.width / vf) * p - (size.width * p)
                    // approximate: shift thumb along the track by (track - thumb) * progress
                }
                .clip(RoundedCornerShape(WiomTheme.radius.full))
                .background(WiomTheme.colors.brand.primary)
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun PaginationPreview() = WiomTheme {
    Column(
        modifier = Modifier
            .background(WiomTheme.colors.surface.base)
            .padding(WiomTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xl),
    ) {
        WiomPaginationDots(total = 4, current = 2, style = WiomPaginationDotStyle.Expanded)
        WiomPaginationBars(total = 5, current = 3, counterLabel = "Step 3 of 5")
        WiomPaginationCounter(current = 3, total = 10, onPrev = {}, onNext = {})
        WiomPaginationScrollIndicator(progress = 0.3f)
    }
}
