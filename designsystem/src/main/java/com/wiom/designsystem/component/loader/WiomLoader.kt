package com.wiom.designsystem.component.loader

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wiom.designsystem.theme.WiomTheme

/**
 * Spinner tone — which surface the spinner lives on.
 *
 * - [Brand] : pink arc on a muted track. Default for light surfaces.
 * - [Neutral] : subtle arc for de-emphasised contexts.
 * - [OnColor] : white arc with no track — used on brand-filled fills and dark surfaces.
 */
enum class WiomSpinnerTone {
    Brand,
    Neutral,
    OnColor,
}

/** Spinner size. `Sm` fits inside buttons / input rows, `Md` fits in cards, `Lg` fills heroes. */
enum class WiomSpinnerSize(internal val diameter: Dp, internal val strokeWidth: Dp) {
    Sm(20.dp, 2.dp),
    Md(32.dp, 3.dp),
    Lg(48.dp, 4.dp),
}

/**
 * Circular spinner — a 3/4 arc rotating around a track.
 *
 * Use inline for waits where the layout is already on screen and progress is
 * unknown (button loading, inline validation, card-level refresh). For waits
 * where the layout isn't known yet (list / card arriving), prefer
 * [WiomSkeletonLine] / [WiomSkeletonCard] / [WiomSkeletonListItem] — they make
 * the wait feel faster because the user sees where content will appear.
 *
 * ```
 * WiomSpinner()                                    // default brand md
 * WiomSpinner(size = WiomSpinnerSize.Sm)           // inside a button
 * WiomSpinner(tone = WiomSpinnerTone.OnColor)      // on a brand-filled surface
 * ```
 *
 * @param modifier Modifier.
 * @param size One of [WiomSpinnerSize]. Default `Md` (32dp).
 * @param tone One of [WiomSpinnerTone]. Default `Brand`.
 */
@Composable
fun WiomSpinner(
    modifier: Modifier = Modifier,
    size: WiomSpinnerSize = WiomSpinnerSize.Md,
    tone: WiomSpinnerTone = WiomSpinnerTone.Brand,
) {
    val (arcColor, trackColor) = spinnerColors(tone)
    val transition = rememberInfiniteTransition(label = "wiom-spinner")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "rotation",
    )

    Canvas(modifier = modifier.size(size.diameter)) {
        val strokeWidthPx = size.strokeWidth.toPx()
        val inset = strokeWidthPx / 2f
        val arcSize = Size(this.size.width - strokeWidthPx, this.size.height - strokeWidthPx)
        val topLeft = Offset(inset, inset)

        if (trackColor != null) {
            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
            )
        }
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

// ---- Skeletons --------------------------------------------------------------

/**
 * Whether a skeleton block gets a shimmer sweep.
 *
 * - [Static] : plain `bg.muted` block. Good when the whole screen is a skeleton —
 *   moving shimmer across a busy skeleton grid can feel noisy.
 * - [Shimmer] : a soft `bg.default` highlight sweeps across at 1.2s. Default.
 */
enum class WiomSkeletonAnimation {
    Static,
    Shimmer,
}

/**
 * A skeleton line — a bar-shaped placeholder used to represent a single line of
 * text while the real content is loading.
 *
 * Width must come from the caller (`.fillMaxWidth()` or `.width(...)`). Height
 * defaults to 16dp to match body-line-height; override via [height] when the
 * line stands in for a heading.
 *
 * @param modifier Modifier; caller sets width.
 * @param height Bar height. Default 16dp (body line).
 * @param animation [WiomSkeletonAnimation]. Default `Shimmer`.
 */
@Composable
fun WiomSkeletonLine(
    modifier: Modifier = Modifier,
    height: Dp = 16.dp,
    animation: WiomSkeletonAnimation = WiomSkeletonAnimation.Shimmer,
) {
    val shape = RoundedCornerShape(WiomTheme.radius.tiny)
    val base = WiomTheme.color.bg.muted

    Box(
        modifier = modifier
            .height(height)
            .clip(shape)
            .background(base, shape),
    ) {
        if (animation == WiomSkeletonAnimation.Shimmer) {
            ShimmerSweep(highlight = WiomTheme.color.bg.default, shape = shape)
        }
    }
}

/**
 * A skeleton card — a standalone image + text-block placeholder used for plan
 * cards and dashboard tiles.
 *
 * ```
 * ┌────────────────────────────────────────────┐
 * │  ┌──────┐  ████████████████████████████     │
 * │  │ 72×72│  ██████████████████              │
 * │  └──────┘  ████████████████                │
 * └────────────────────────────────────────────┘
 * ```
 *
 * Wraps a thumbnail + 3 text lines in a `bg.default` card outlined with
 * `stroke.subtle`. Height is intrinsic.
 *
 * @param modifier Modifier; caller sets width.
 * @param animation [WiomSkeletonAnimation]. Default `Shimmer`.
 */
@Composable
fun WiomSkeletonCard(
    modifier: Modifier = Modifier,
    animation: WiomSkeletonAnimation = WiomSkeletonAnimation.Shimmer,
) {
    val cardShape = RoundedCornerShape(WiomTheme.radius.large)
    Row(
        modifier = modifier
            .clip(cardShape)
            .background(WiomTheme.color.bg.default, cardShape)
            .padding(WiomTheme.spacing.lg),
        horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Thumbnail
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(WiomTheme.radius.medium))
                .background(WiomTheme.color.bg.muted),
        ) {
            if (animation == WiomSkeletonAnimation.Shimmer) {
                ShimmerSweep(
                    highlight = WiomTheme.color.bg.default,
                    shape = RoundedCornerShape(WiomTheme.radius.medium),
                )
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
        ) {
            WiomSkeletonLine(modifier = Modifier.fillMaxWidth(), animation = animation)
            WiomSkeletonLine(modifier = Modifier.fillMaxWidth(0.75f), animation = animation)
            WiomSkeletonLine(modifier = Modifier.fillMaxWidth(0.5f), animation = animation)
        }
    }
}

/**
 * A skeleton list item — avatar + two lines + trailing meta.
 *
 * Use inside a scrolling list / transaction history while data is loading.
 * Stack several of these to simulate a full list.
 *
 * @param modifier Modifier; caller sets width.
 * @param animation [WiomSkeletonAnimation]. Default `Shimmer`.
 */
@Composable
fun WiomSkeletonListItem(
    modifier: Modifier = Modifier,
    animation: WiomSkeletonAnimation = WiomSkeletonAnimation.Shimmer,
) {
    Row(
        modifier = modifier.padding(
            horizontal = WiomTheme.spacing.lg,
            vertical = WiomTheme.spacing.md,
        ),
        horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.lg),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(WiomTheme.color.bg.muted),
        ) {
            if (animation == WiomSkeletonAnimation.Shimmer) {
                ShimmerSweep(highlight = WiomTheme.color.bg.default, shape = CircleShape)
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
        ) {
            WiomSkeletonLine(modifier = Modifier.fillMaxWidth(0.75f), animation = animation)
            WiomSkeletonLine(modifier = Modifier.fillMaxWidth(0.5f), animation = animation)
        }
        WiomSkeletonLine(
            modifier = Modifier.width(40.dp),
            animation = animation,
        )
    }
}

// ---- Full-screen blocking loader -------------------------------------------

/**
 * Variants for [WiomBrandLoader] — full-screen wait.
 *
 * - [Light] : light surface, for splash / cold start / post-login.
 * - [OnBrand] : brand-pink surface, for branded intro moments and marketing.
 * - [Overlay] : scrim over existing content, for blocking critical ops (payment / KYC upload).
 */
enum class WiomBrandLoaderStyle {
    Light,
    OnBrand,
    Overlay,
}

/**
 * Full-screen blocking loader.
 *
 * Covers the parent (`fillMaxSize()`), swallows pointer events, and centers a
 * spinner + optional message. Always use this when the app cannot be interacted
 * with until the wait resolves (payment, plan activation, KYC upload).
 *
 * `Overlay` pairs with `overlay.scrim` per CLAUDE.md § 8 — the overlay darkens
 * whatever is underneath while the spinner runs. `Light` and `OnBrand` own the
 * whole surface (cold start / branded splash).
 *
 * For short waits (<3s) inline a [WiomSpinner] instead of this heavier variant.
 *
 * @param modifier Modifier.
 * @param style One of [WiomBrandLoaderStyle]. Default `Light`.
 * @param message Optional status copy shown below the spinner.
 */
@Composable
fun WiomBrandLoader(
    modifier: Modifier = Modifier,
    style: WiomBrandLoaderStyle = WiomBrandLoaderStyle.Light,
    message: String? = null,
) {
    val surfaceColor = when (style) {
        WiomBrandLoaderStyle.Light -> WiomTheme.color.bg.default
        WiomBrandLoaderStyle.OnBrand -> WiomTheme.color.bg.brand
        WiomBrandLoaderStyle.Overlay -> WiomTheme.color.overlay.scrim
    }
    val messageColor = when (style) {
        WiomBrandLoaderStyle.Light -> WiomTheme.color.text.subtle
        WiomBrandLoaderStyle.OnBrand,
        WiomBrandLoaderStyle.Overlay -> WiomTheme.color.text.onBrand    // #FFFFFF — pairs with bg.brand / scrim
    }
    val spinnerTone = when (style) {
        WiomBrandLoaderStyle.Light -> WiomSpinnerTone.Brand
        WiomBrandLoaderStyle.OnBrand,
        WiomBrandLoaderStyle.Overlay -> WiomSpinnerTone.OnColor
    }
    val wordmarkColor = when (style) {
        WiomBrandLoaderStyle.OnBrand -> WiomTheme.color.text.onBrand
        else -> WiomTheme.color.text.brand
    }
    val showWordmark = style != WiomBrandLoaderStyle.Overlay

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(surfaceColor)
            // Consume touch so the surface behind the overlay can't be tapped.
            .pointerInput(Unit) {},
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.lg),
        ) {
            // Wordmark "Wiom" — present on Light and OnBrand. Overlay omits it
            // (the underlying content carries the context).
            if (showWordmark) {
                Text(
                    text = "Wiom",
                    style = WiomTheme.type.headingXl,
                    color = wordmarkColor,
                )
            }
            // Infinity slot — until the Lottie stroke-dash chase ships, render
            // WiomSpinner.Lg as a structural stand-in (per skill §3 note).
            WiomSpinner(size = WiomSpinnerSize.Lg, tone = spinnerTone)
            if (!message.isNullOrEmpty()) {
                Text(
                    text = message,
                    style = WiomTheme.type.bodyMd,
                    color = messageColor,
                )
            }
        }
    }
}

// ---- Linear ----------------------------------------------------------------

/**
 * Loader linear progress bar — the 4dp track for short waits (≤ 5 s).
 *
 * - `progress = null` → indeterminate; a 40% fill segment travels left → right
 *   on a 1.5s loop.
 * - `progress = 0f..1f` → determinate; fill animates from previous to new value.
 *
 * The bar fills the parent's width; height is fixed at 4dp. Parent surface
 * (snackbar / inline strip) provides chrome — this composable is the bar only.
 *
 * For long-form determinate progress with milestones / step counters / KYC,
 * use `WiomProgressLinear` from the progress-indicator family instead.
 */
@Composable
fun WiomLinearProgress(
    progress: Float? = null,
    modifier: Modifier = Modifier,
) {
    val isIndeterminate = progress == null
    val targetFraction = progress?.coerceIn(0f, 1f) ?: 0f
    val animatedFraction by animateFloatAsState(
        targetValue = targetFraction,
        animationSpec = tween(durationMillis = 300, easing = LinearEasing),
        label = "WiomLinearDeterminate",
    )

    val transition = rememberInfiniteTransition(label = "WiomLinearIndeterminate")
    val travel by transition.animateFloat(
        initialValue = -0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "travel",
    )

    val track = WiomTheme.color.bg.brandSubtle
    val fill = WiomTheme.color.bg.brand
    val shape = RoundedCornerShape(WiomTheme.radius.full)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(shape)
            .background(color = track, shape = shape),
    ) {
        if (isIndeterminate) {
            val w = size.width * 0.4f
            val x = travel * size.width
            drawRect(color = fill, topLeft = Offset(x, 0f), size = Size(w, size.height))
        } else {
            val w = size.width * animatedFraction
            drawRect(color = fill, topLeft = Offset.Zero, size = Size(w, size.height))
        }
    }
}

// ---- Dots ------------------------------------------------------------------

/** Tone for [WiomDots] — 3 ship colors. */
enum class WiomDotsColor { Brand, Neutral, OnColor }

/**
 * Chat typing indicator — 3 dots in a chat bubble that bounce in sequence.
 *
 * - `Brand`   → bot/assistant composing (default — soft brand bubble + brand dots)
 * - `Neutral` → human-agent typing (de-emphasised)
 * - `OnColor` → inside a brand-filled chat bubble (white dots on brand)
 *
 * Bubble shape is the chat-bubble convention: 16dp on three corners,
 * 4dp on the bottom-left tail.
 */
@Composable
fun WiomDots(
    color: WiomDotsColor = WiomDotsColor.Brand,
    modifier: Modifier = Modifier,
) {
    val c = WiomTheme.color
    val (bubble, dot) = when (color) {
        WiomDotsColor.Brand -> c.bg.subtle to c.bg.brand
        WiomDotsColor.Neutral -> c.bg.subtle to c.text.subtle
        WiomDotsColor.OnColor -> c.bg.brand to c.icon.inverse
    }
    val shape = RoundedCornerShape(
        topStart = WiomTheme.radius.large,
        topEnd = WiomTheme.radius.large,
        bottomEnd = WiomTheme.radius.large,
        bottomStart = WiomTheme.radius.tiny,
    )

    Box(
        modifier = modifier
            .clip(shape)
            .background(color = bubble, shape = shape)
            .padding(horizontal = WiomTheme.spacing.md, vertical = 10.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs)) {
            repeat(3) { i ->
                BouncingDot(color = dot, phaseMillis = i * 150)
            }
        }
    }
}

@Composable
private fun BouncingDot(color: Color, phaseMillis: Int) {
    val transition = rememberInfiniteTransition(label = "WiomDotsBounce")
    val bounce by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                delayMillis = phaseMillis
                0f at 0
                -3f at 200
                0f at 400
            },
            repeatMode = RepeatMode.Restart,
        ),
        label = "offset",
    )
    Box(
        modifier = Modifier
            .offset(y = bounce.dp)
            .size(8.dp)
            .background(color, CircleShape),
    )
}

// ---- internals --------------------------------------------------------------

@Composable
private fun spinnerColors(tone: WiomSpinnerTone): Pair<Color, Color?> = when (tone) {
    WiomSpinnerTone.Brand -> WiomTheme.color.bg.brand to WiomTheme.color.bg.brandSubtle
    WiomSpinnerTone.Neutral -> WiomTheme.color.text.subtle to WiomTheme.color.stroke.subtle
    WiomSpinnerTone.OnColor -> WiomTheme.color.icon.inverse to null
}

/**
 * Diagonal shimmer sweep that fills the parent Box. The parent must clip to
 * the same [shape].
 */
@Composable
private fun ShimmerSweep(
    highlight: Color,
    shape: androidx.compose.ui.graphics.Shape,
) {
    val transition = rememberInfiniteTransition(label = "wiom-shimmer")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmer",
    )
    // Gradient of transparent → highlight → transparent sliding left-to-right.
    // Stops must be strictly monotonic; nudge by 0.001 when clamping collapses them.
    val center = progress.coerceIn(0f, 1f)
    val leftStop = (center - 0.3f).coerceAtLeast(0f)
    val rightStop = (center + 0.3f).coerceAtMost(1f)
    val safeCenter = center.coerceIn(leftStop + 0.001f, rightStop - 0.001f)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(shape)
            .background(
                Brush.horizontalGradient(
                    colorStops = arrayOf(
                        leftStop to Color.Transparent,
                        safeCenter to highlight.copy(alpha = 0.55f),
                        rightStop to Color.Transparent,
                    ),
                ),
            ),
    )
}

// ---- Previews ---------------------------------------------------------------

@Preview(name = "Spinner — sizes (brand)", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewSpinnerBrand() {
    WiomTheme {
        Row(
            modifier = Modifier.padding(WiomTheme.spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.lg),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WiomSpinner(size = WiomSpinnerSize.Sm)
            WiomSpinner(size = WiomSpinnerSize.Md)
            WiomSpinner(size = WiomSpinnerSize.Lg)
        }
    }
}

@Preview(name = "Spinner — neutral", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewSpinnerNeutral() {
    WiomTheme {
        Row(modifier = Modifier.padding(WiomTheme.spacing.lg)) {
            WiomSpinner(size = WiomSpinnerSize.Md, tone = WiomSpinnerTone.Neutral)
        }
    }
}

@Preview(name = "Spinner — onColor on brand fill", showBackground = true, backgroundColor = 0xFFD9008D)
@Composable
private fun PreviewSpinnerOnColor() {
    WiomTheme {
        Row(modifier = Modifier.padding(WiomTheme.spacing.lg)) {
            WiomSpinner(size = WiomSpinnerSize.Md, tone = WiomSpinnerTone.OnColor)
        }
    }
}

@Preview(name = "Skeleton — Line", showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 328)
@Composable
private fun PreviewSkeletonLine() {
    WiomTheme {
        Column(
            modifier = Modifier.padding(WiomTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
        ) {
            WiomSkeletonLine(modifier = Modifier.fillMaxWidth())
            WiomSkeletonLine(modifier = Modifier.fillMaxWidth(0.75f))
            WiomSkeletonLine(modifier = Modifier.fillMaxWidth(0.5f))
        }
    }
}

@Preview(name = "Skeleton — Card", showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 360)
@Composable
private fun PreviewSkeletonCard() {
    WiomTheme {
        Column(modifier = Modifier.padding(WiomTheme.spacing.lg)) {
            WiomSkeletonCard(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(name = "Skeleton — ListItem × 3", showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 360)
@Composable
private fun PreviewSkeletonListItem() {
    WiomTheme {
        Column {
            WiomSkeletonListItem(modifier = Modifier.fillMaxWidth())
            WiomSkeletonListItem(modifier = Modifier.fillMaxWidth())
            WiomSkeletonListItem(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(name = "Skeleton — Static (no shimmer)", showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 328)
@Composable
private fun PreviewSkeletonStatic() {
    WiomTheme {
        Column(
            modifier = Modifier.padding(WiomTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
        ) {
            WiomSkeletonLine(
                modifier = Modifier.fillMaxWidth(),
                animation = WiomSkeletonAnimation.Static,
            )
            WiomSkeletonLine(
                modifier = Modifier.fillMaxWidth(0.75f),
                animation = WiomSkeletonAnimation.Static,
            )
        }
    }
}

@Preview(name = "BlockingLoader — Light", showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 360, heightDp = 640)
@Composable
private fun PreviewBlockingLoaderLight() {
    WiomTheme {
        WiomBrandLoader(
            style = WiomBrandLoaderStyle.Light,
            message = "Loading your dashboard…",
        )
    }
}

@Preview(name = "BlockingLoader — OnBrand", showBackground = true, backgroundColor = 0xFFD9008D, widthDp = 360, heightDp = 640)
@Composable
private fun PreviewBlockingLoaderOnBrand() {
    WiomTheme {
        WiomBrandLoader(
            style = WiomBrandLoaderStyle.OnBrand,
            message = "Getting things ready…",
        )
    }
}

@Preview(name = "BlockingLoader — Overlay", showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 360, heightDp = 640)
@Composable
private fun PreviewBlockingLoaderOverlay() {
    WiomTheme {
        WiomBrandLoader(
            style = WiomBrandLoaderStyle.Overlay,
            message = "Processing your payment…",
        )
    }
}
