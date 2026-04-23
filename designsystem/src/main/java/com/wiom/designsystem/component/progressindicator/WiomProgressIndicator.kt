package com.wiom.designsystem.component.progressindicator

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wiom.designsystem.theme.WiomTheme
import kotlin.math.max
import kotlin.math.min

/**
 * Tonal family for a progress indicator fill.
 *
 * Defaults to [Brand] (pink). Callers who want the skill's reversed-semantics ladder
 * (info → brand → positive as progress approaches 100%) can switch tones explicitly —
 * the library does NOT auto-ladder based on `progress` value, because the appropriate
 * mood depends on context (a long install vs a short upload).
 */
enum class WiomProgressTone {
    Brand,
    Info,
    Positive,
}

/**
 * Linear progress bar size.
 *
 * - [Small] 4dp — inline, compact cards, default.
 * - [Medium] 8dp — prominent standalone indicators.
 */
enum class WiomLinearProgressSize(internal val height: Dp) {
    Small(4.dp),
    Medium(8.dp),
}

/**
 * Linear determinate progress bar.
 *
 * Intrinsic width — caller sizes via `modifier` (`fillMaxWidth()`, `.width(...)`).
 * Track is `bg.muted`; fill is the tone color (default `bg.brand`). Both track and
 * fill use `radius.full`. No border, no shadow.
 *
 * @param progress 0f..1f clamped. Outside range is clamped with no exception.
 * @param modifier Modifier; width comes from parent. Do not set `.height(...)` — use [size].
 * @param tone Fill color family. Default [WiomProgressTone.Brand].
 * @param size Bar height. Default [WiomLinearProgressSize.Small] (4dp).
 */
@Composable
fun WiomLinearProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    tone: WiomProgressTone = WiomProgressTone.Brand,
    size: WiomLinearProgressSize = WiomLinearProgressSize.Small,
) {
    val clamped = progress.coerceIn(0f, 1f)
    val shape = RoundedCornerShape(WiomTheme.radius.full)
    val fillColor = progressToneColor(tone)
    val trackColor = WiomTheme.color.bg.muted

    Box(
        modifier = modifier
            .height(size.height)
            .clip(shape)
            .background(trackColor, shape),
    ) {
        if (clamped > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(clamped)
                    .height(size.height)
                    .clip(shape)
                    .background(fillColor, shape),
            )
        }
    }
}

/**
 * Linear indeterminate progress bar.
 *
 * A 25%-wide indicator sweeps across the track on a 1.4s linear loop. Use when
 * you have no completion estimate but want a non-spinning progress affordance
 * (upload in flight, streaming response).
 *
 * @param modifier Modifier; width comes from parent.
 * @param tone Fill color family. Default [WiomProgressTone.Brand].
 * @param size Bar height. Default [WiomLinearProgressSize.Small] (4dp).
 */
@Composable
fun WiomLinearProgressIndeterminate(
    modifier: Modifier = Modifier,
    tone: WiomProgressTone = WiomProgressTone.Brand,
    size: WiomLinearProgressSize = WiomLinearProgressSize.Small,
) {
    val shape = RoundedCornerShape(WiomTheme.radius.full)
    val fillColor = progressToneColor(tone)
    val trackColor = WiomTheme.color.bg.muted

    val transition = rememberInfiniteTransition(label = "wiom-linear-indeterminate")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "sweep",
    )

    // The 25%-wide indicator slides from -25% to 100% of the track width.
    val indicatorFraction = 0.25f
    val startFraction = -indicatorFraction + progress * (1f + indicatorFraction)

    Box(
        modifier = modifier
            .height(size.height)
            .clip(shape)
            .background(trackColor, shape),
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(size.height)) {
            val totalWidth = this.size.width
            val start = max(0f, startFraction) * totalWidth
            val end = min(1f, startFraction + indicatorFraction) * totalWidth
            val segmentWidth = end - start
            if (segmentWidth > 0f) {
                drawRect(
                    color = fillColor,
                    topLeft = Offset(start, 0f),
                    size = Size(segmentWidth, this.size.height),
                )
            }
        }
    }
}

/**
 * Circular progress size.
 *
 * - [Small] 24dp — default, fits in list rows and cards.
 * - [Large] 48dp — hero / empty states / full-screen centered.
 */
enum class WiomCircularProgressSize(internal val diameter: Dp, internal val strokeWidth: Dp) {
    Small(24.dp, 3.dp),
    Large(48.dp, 4.dp),
}

/**
 * Circular determinate progress.
 *
 * Full ring track in `bg.muted`; arc in tone color sweeps from 12 o'clock clockwise
 * proportional to `progress` (0..1). Round stroke caps. No track ring is drawn for
 * [WiomProgressTone.Brand] on inverse surfaces — caller switches tones for that case.
 *
 * @param progress 0f..1f clamped.
 * @param modifier Modifier.
 * @param tone Arc color family. Default [WiomProgressTone.Brand].
 * @param size Ring diameter. Default [WiomCircularProgressSize.Small] (24dp).
 */
@Composable
fun WiomCircularProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    tone: WiomProgressTone = WiomProgressTone.Brand,
    size: WiomCircularProgressSize = WiomCircularProgressSize.Small,
) {
    val clamped = progress.coerceIn(0f, 1f)
    val arcColor = progressToneColor(tone)
    val trackColor = WiomTheme.color.bg.muted
    val strokePx = size.strokeWidth

    Canvas(modifier = modifier.size(size.diameter)) {
        val strokeWidthPx = strokePx.toPx()
        val inset = strokeWidthPx / 2f
        val arcSize = Size(this.size.width - strokeWidthPx, this.size.height - strokeWidthPx)
        val topLeft = Offset(inset, inset)
        // Full track ring.
        drawArc(
            color = trackColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
        )
        if (clamped > 0f) {
            drawArc(
                color = arcColor,
                startAngle = -90f,
                sweepAngle = 360f * clamped,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
            )
        }
    }
}

/**
 * Circular indeterminate progress.
 *
 * A 270° arc rotates continuously at 1.2s per turn. No track ring (matches the
 * Loader/Spinner pattern). For contexts where you also want a track, use
 * [WiomCircularProgress] with an animated progress value.
 *
 * @param modifier Modifier.
 * @param tone Arc color family. Default [WiomProgressTone.Brand].
 * @param size Ring diameter. Default [WiomCircularProgressSize.Small] (24dp).
 */
@Composable
fun WiomCircularProgressIndeterminate(
    modifier: Modifier = Modifier,
    tone: WiomProgressTone = WiomProgressTone.Brand,
    size: WiomCircularProgressSize = WiomCircularProgressSize.Small,
) {
    val arcColor = progressToneColor(tone)
    val strokePx = size.strokeWidth

    val transition = rememberInfiniteTransition(label = "wiom-circular-indeterminate")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "rotation",
    )

    Canvas(modifier = modifier.size(size.diameter)) {
        val strokeWidthPx = strokePx.toPx()
        val inset = strokeWidthPx / 2f
        val arcSize = Size(this.size.width - strokeWidthPx, this.size.height - strokeWidthPx)
        val topLeft = Offset(inset, inset)
        drawArc(
            color = arcColor,
            startAngle = -90f + rotation,
            sweepAngle = 270f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
        )
    }
}

// ---- internals --------------------------------------------------------------

@Composable
private fun progressToneColor(tone: WiomProgressTone): Color = when (tone) {
    WiomProgressTone.Brand -> WiomTheme.color.bg.brand
    WiomProgressTone.Info -> WiomTheme.color.bg.info
    WiomProgressTone.Positive -> WiomTheme.color.bg.positive
}

// ---- Previews ---------------------------------------------------------------

@Preview(name = "Linear — Determinate 40%", showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 328)
@Composable
private fun PreviewLinearDeterminate() {
    WiomTheme {
        Column(
            modifier = Modifier.padding(WiomTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
        ) {
            WiomLinearProgress(progress = 0.4f, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(name = "Linear — Determinate tones", showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 328)
@Composable
private fun PreviewLinearTones() {
    WiomTheme {
        Column(
            modifier = Modifier.padding(WiomTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
        ) {
            WiomLinearProgress(progress = 0.2f, tone = WiomProgressTone.Info, modifier = Modifier.fillMaxWidth())
            WiomLinearProgress(progress = 0.6f, tone = WiomProgressTone.Brand, modifier = Modifier.fillMaxWidth())
            WiomLinearProgress(progress = 0.9f, tone = WiomProgressTone.Positive, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(name = "Linear — Determinate Medium (8dp)", showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 328)
@Composable
private fun PreviewLinearMedium() {
    WiomTheme {
        Column(modifier = Modifier.padding(WiomTheme.spacing.lg)) {
            WiomLinearProgress(
                progress = 0.6f,
                size = WiomLinearProgressSize.Medium,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(name = "Linear — Indeterminate", showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 328)
@Composable
private fun PreviewLinearIndeterminate() {
    WiomTheme {
        Column(modifier = Modifier.padding(WiomTheme.spacing.lg)) {
            WiomLinearProgressIndeterminate(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(name = "Linear — 0% and 100%", showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 328)
@Composable
private fun PreviewLinearEdges() {
    WiomTheme {
        Column(
            modifier = Modifier.padding(WiomTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
        ) {
            WiomLinearProgress(progress = 0f, modifier = Modifier.fillMaxWidth())
            WiomLinearProgress(progress = 1f, tone = WiomProgressTone.Positive, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(name = "Circular — Determinate Small", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewCircularDeterminateSmall() {
    WiomTheme {
        Row(
            modifier = Modifier.padding(WiomTheme.spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.lg),
        ) {
            WiomCircularProgress(progress = 0.25f)
            WiomCircularProgress(progress = 0.5f)
            WiomCircularProgress(progress = 0.75f, tone = WiomProgressTone.Positive)
        }
    }
}

@Preview(name = "Circular — Determinate Large", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewCircularDeterminateLarge() {
    WiomTheme {
        Row(
            modifier = Modifier.padding(WiomTheme.spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.lg),
        ) {
            WiomCircularProgress(progress = 0.33f, size = WiomCircularProgressSize.Large)
            WiomCircularProgress(
                progress = 0.8f,
                size = WiomCircularProgressSize.Large,
                tone = WiomProgressTone.Positive,
            )
        }
    }
}

@Preview(name = "Circular — Indeterminate Small", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewCircularIndeterminateSmall() {
    WiomTheme {
        Row(
            modifier = Modifier.padding(WiomTheme.spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.lg),
        ) {
            WiomCircularProgressIndeterminate()
            WiomCircularProgressIndeterminate(tone = WiomProgressTone.Info)
        }
    }
}

@Preview(name = "Circular — Indeterminate Large", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewCircularIndeterminateLarge() {
    WiomTheme {
        Row(
            modifier = Modifier.padding(WiomTheme.spacing.lg),
        ) {
            WiomCircularProgressIndeterminate(size = WiomCircularProgressSize.Large)
        }
    }
}

