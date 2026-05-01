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

// ---- Completion (rich completion meter) -------------------------------------

/**
 * Reversed-semantics state for [WiomProgressCompletion]. Drives fill % AND fill colour AND
 * status-pill copy in lockstep, per `wiom-progress-indicator` §1.2 ship table.
 *
 * | State | Fill % | Fill colour | Pill |
 * |---|---|---|---|
 * | [JustStarted] | 20% | `bg.info` (purple) | "Just started" |
 * | [InProgress]  | 60% | `bg.brand` (pink) | "In progress" |
 * | [AlmostThere] | 88% | `bg.positive` (green) | "Almost there" |
 * | [Complete]    | 100% | `bg.positive` (green) | "Complete" |
 */
enum class WiomProgressCompletionState { JustStarted, InProgress, AlmostThere, Complete }

/**
 * Rich completion meter — title + status pill + value + linear bar.
 *
 * Use for KYC / referral / earnings / profile-completion cards. The hosting card / row owns
 * the tap target; the meter itself is display-only.
 *
 * Skill anatomy (§1.2):
 *   Header row: title (`labelMd · text.default`) on the left, value (`bodySm · text.subtle`)
 *   right-aligned. ↓ space-4 ↓ status pill below the title (`metaXs`, pill bg + text per state).
 *   ↓ space-12 ↓ Linear Md bar (8 dp tall) coloured by state.
 *
 * @param title Top-left label, e.g. `"Profile completion"`. `type.labelMd`.
 * @param value Right-aligned value, e.g. `"3 of 5 steps"` or `"₹4,500 of ₹10,000"`. `type.bodySm`.
 * @param state Drives fill %, fill colour, and pill copy together — see [WiomProgressCompletionState].
 */
@Composable
fun WiomProgressCompletion(
    title: String,
    value: String,
    state: WiomProgressCompletionState,
    modifier: Modifier = Modifier,
) {
    val visual = when (state) {
        WiomProgressCompletionState.JustStarted -> CompletionVisual(
            tone = WiomProgressTone.Info,
            pillBg = WiomTheme.color.bg.infoSubtle,
            pillFg = WiomTheme.color.text.info,
            pillCopy = "Just started",
            fraction = 0.20f,
        )
        WiomProgressCompletionState.InProgress -> CompletionVisual(
            tone = WiomProgressTone.Brand,
            pillBg = WiomTheme.color.bg.brandSubtle,
            pillFg = WiomTheme.color.text.brand,
            pillCopy = "In progress",
            fraction = 0.60f,
        )
        WiomProgressCompletionState.AlmostThere -> CompletionVisual(
            tone = WiomProgressTone.Positive,
            pillBg = WiomTheme.color.bg.positiveSubtle,
            pillFg = WiomTheme.color.text.positive,
            pillCopy = "Almost there",
            fraction = 0.88f,
        )
        WiomProgressCompletionState.Complete -> CompletionVisual(
            tone = WiomProgressTone.Positive,
            pillBg = WiomTheme.color.bg.positiveSubtle,
            pillFg = WiomTheme.color.text.positive,
            pillCopy = "Complete",
            fraction = 1.00f,
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs),
            ) {
                androidx.compose.material3.Text(
                    text = title,
                    style = WiomTheme.type.labelMd,
                    color = WiomTheme.color.text.default,
                )
                CompletionPill(label = visual.pillCopy, bg = visual.pillBg, fg = visual.pillFg)
            }
            androidx.compose.material3.Text(
                text = value,
                style = WiomTheme.type.bodySm,
                color = WiomTheme.color.text.subtle,
            )
        }
        WiomLinearProgress(
            progress = visual.fraction,
            tone = visual.tone,
            size = WiomLinearProgressSize.Medium,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun CompletionPill(label: String, bg: Color, fg: Color) {
    Row(
        modifier = Modifier
            .background(bg, RoundedCornerShape(WiomTheme.radius.full))
            .padding(horizontal = WiomTheme.spacing.sm, vertical = 2.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs),
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(fg, androidx.compose.foundation.shape.CircleShape),
        )
        androidx.compose.material3.Text(
            text = label,
            style = WiomTheme.type.metaXs,
            color = fg,
        )
    }
}

private data class CompletionVisual(
    val tone: WiomProgressTone,
    val pillBg: Color,
    val pillFg: Color,
    val pillCopy: String,
    val fraction: Float,
)

// ---- Milestones (named-stage tracker) ---------------------------------------

/**
 * One stage of a [WiomProgressMilestones] tracker. The icon shows inside the 24 dp stage
 * circle; the label sits centred below the circle when `showLabels = true`.
 */
data class WiomMilestoneStage(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
)

/**
 * Multi-stage compact tracker — 2 / 3 / 4 / 5 named stages with a connector bar between
 * each. Stage colour reflects whether the stage has been reached:
 *
 *   - Reached stage circle: `bg.brand` fill + `icon.inverse` glyph.
 *   - Unreached stage circle: `bg.muted` fill + `icon.nonAction` glyph.
 *   - Connector to a fully-reached neighbour: `bg.brand`. Otherwise `bg.muted`.
 *
 * Stage label colour:
 *   - Past (i < currentStage): `text.default`.
 *   - Current (i == currentStage): `text.brand` — the current-stage emphasis.
 *   - Future (i > currentStage): `text.subtle`.
 *
 * Use for installation / delivery / verification trackers. The hosting card owns the tap;
 * the milestones visual is display-only.
 *
 * @param title Top label, `type.labelLg`.
 * @param subtitle Supporting line, e.g. `"Step 3 of 5 · Router"`. `type.bodySm`.
 * @param stages 2..5 stages — caller names them explicitly.
 * @param currentStage 1-based index of the current stage; must be in `1..stages.size`.
 * @param showLabels Toggle the per-stage label row below the bar.
 */
@Composable
fun WiomProgressMilestones(
    title: String,
    subtitle: String,
    stages: List<WiomMilestoneStage>,
    currentStage: Int,
    modifier: Modifier = Modifier,
    showLabels: Boolean = true,
) {
    require(stages.size in 2..5) { "Milestones supports 2..5 stages; got ${'$'}{stages.size}" }
    require(currentStage in 1..stages.size) {
        "currentStage must be in 1..${'$'}{stages.size}; got ${'$'}currentStage"
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs)) {
            androidx.compose.material3.Text(
                text = title,
                style = WiomTheme.type.labelLg,
                color = WiomTheme.color.text.default,
            )
            androidx.compose.material3.Text(
                text = subtitle,
                style = WiomTheme.type.bodySm,
                color = WiomTheme.color.text.subtle,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        ) {
            stages.forEachIndexed { i, stage ->
                val stageNumber = i + 1
                val reached = stageNumber <= currentStage
                MilestoneCircle(stage = stage, reached = reached)
                if (i != stages.lastIndex) {
                    val connectorReached = stageNumber < currentStage
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .background(
                                color = if (connectorReached) WiomTheme.color.bg.brand
                                else WiomTheme.color.bg.muted,
                                shape = RoundedCornerShape(WiomTheme.radius.full),
                            ),
                    )
                }
            }
        }

        if (showLabels) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            ) {
                stages.forEachIndexed { i, stage ->
                    val stageNumber = i + 1
                    val labelColor = when {
                        stageNumber < currentStage -> WiomTheme.color.text.default
                        stageNumber == currentStage -> WiomTheme.color.text.brand
                        else -> WiomTheme.color.text.subtle
                    }
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = androidx.compose.ui.Alignment.Center,
                    ) {
                        androidx.compose.material3.Text(
                            text = stage.label,
                            style = WiomTheme.type.metaXs,
                            color = labelColor,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MilestoneCircle(stage: WiomMilestoneStage, reached: Boolean) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .background(
                color = if (reached) WiomTheme.color.bg.brand else WiomTheme.color.bg.muted,
                shape = androidx.compose.foundation.shape.CircleShape,
            )
            .padding(4.dp),
        contentAlignment = androidx.compose.ui.Alignment.Center,
    ) {
        com.wiom.designsystem.foundation.icon.WiomIcon(
            imageVector = stage.icon,
            contentDescription = null,
            size = 16.dp,
            tint = if (reached) WiomTheme.color.icon.inverse
            else WiomTheme.color.icon.nonAction,
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

