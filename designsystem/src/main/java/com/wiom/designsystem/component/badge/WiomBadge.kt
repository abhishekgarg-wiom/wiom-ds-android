package com.wiom.designsystem.component.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wiom.designsystem.theme.WiomTheme

// -----------------------------------------------------------------------------
// Dot
// -----------------------------------------------------------------------------

/** Tone for [WiomBadgeDot]. */
enum class WiomBadgeDotTone { Brand, Critical, Neutral }

/**
 * 8 dp filled dot for unread / active indicators with no number or text.
 *
 * Overlay-only — position via `Modifier.align(Alignment.TopEnd)` on top of an
 * icon or avatar. Never inline as a standalone status indicator.
 */
@Composable
fun WiomBadgeDot(
    tone: WiomBadgeDotTone,
    modifier: Modifier = Modifier,
) {
    val fill = when (tone) {
        WiomBadgeDotTone.Brand -> WiomTheme.color.bg.brand
        WiomBadgeDotTone.Critical -> WiomTheme.color.bg.critical
        WiomBadgeDotTone.Neutral -> WiomTheme.color.bg.muted
    }
    Box(
        modifier = modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(color = fill, shape = CircleShape),
    )
}

// -----------------------------------------------------------------------------
// Count
// -----------------------------------------------------------------------------

/** Tone for [WiomBadgeCount]. Three ship tones per spec: Brand, Critical, Neutral. */
enum class WiomBadgeCountTone { Brand, Critical, Neutral }

/**
 * Numeric count badge.
 *
 * - Hidden at `count <= 0` (renders nothing). Never shows "0".
 * - Caps display per [cap] (default 99 → "99+"; pass 9 for single-digit "9+" caps).
 * - 20×20 dp container that grows horizontally for "9+" / "99+".
 * - `type.metaXs` (10 sp) — one of the few places sub-14sp is allowed (skill
 *   allowance for badge counts).
 *
 * Typical use: notification bell overlay. Position with `Modifier.align(Alignment.TopEnd)`
 * inside a parent `Box`.
 */
@Composable
fun WiomBadgeCount(
    count: Int,
    tone: WiomBadgeCountTone,
    modifier: Modifier = Modifier,
    cap: Int = 99,
) {
    if (count <= 0) return
    val label = if (count > cap) "$cap+" else count.toString()
    val fill = when (tone) {
        WiomBadgeCountTone.Brand -> WiomTheme.color.bg.brand
        WiomBadgeCountTone.Critical -> WiomTheme.color.bg.critical
        WiomBadgeCountTone.Neutral -> WiomTheme.color.bg.muted
    }
    val textColor = when (tone) {
        WiomBadgeCountTone.Brand -> WiomTheme.color.text.onBrand
        WiomBadgeCountTone.Critical -> WiomTheme.color.text.onCritical
        WiomBadgeCountTone.Neutral -> WiomTheme.color.text.subtle
    }
    val shape = RoundedCornerShape(WiomTheme.radius.full)
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 20.dp, minHeight = 20.dp)
            .clip(shape)
            .background(color = fill, shape = shape)
            .padding(horizontal = WiomTheme.spacing.xs),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = WiomTheme.type.metaXs,
            color = textColor,
            maxLines = 1,
        )
    }
}

// -----------------------------------------------------------------------------
// Label
// -----------------------------------------------------------------------------

/** Size of a [WiomBadgeLabel]. Small is ALWAYS Tinted — no Filled small exists. */
enum class WiomBadgeLabelSize { Default, Small }

/**
 * Style of a [WiomBadgeLabel].
 *
 * - `Filled` — terminal state that won't change (completed, failed, expired).
 *   Available only on `Default` size.
 * - `Tinted` — transitional state (processing, pending, active). Available on
 *   both sizes. The ONLY style for `Small` size.
 */
enum class WiomBadgeLabelStyle { Filled, Tinted }

/**
 * Tone of a [WiomBadgeLabel]. `Brand` is only valid on `Tinted` style.
 */
enum class WiomBadgeLabelTone {
    Positive, Warning, Critical, Info, Neutral, Brand,
}

/**
 * Text status / state / label chip. Passive — never tappable.
 *
 * ```
 * WiomBadgeLabel(
 *     text = "असफल",
 *     size = WiomBadgeLabelSize.Default,
 *     tone = WiomBadgeLabelTone.Critical,
 *     style = WiomBadgeLabelStyle.Filled,
 * )
 * ```
 *
 * Rules:
 * - Small size is ALWAYS Tinted — passing [WiomBadgeLabelStyle.Filled] with
 *   [WiomBadgeLabelSize.Small] silently falls back to Tinted.
 * - Single line only. Rewrite shorter if the text overflows.
 * - Brand tone exists on Tinted only; Filled + Brand falls back to Brand + Tinted.
 * - Warning filled uses the gold `bg.warning` — the one family where the on-surface
 *   label is DARK (`text.onWarning` olive) because white on gold fails AA.
 *
 * @param text Status text. Never generic ("Status"/"State") — use specific copy.
 * @param tone Color family. See [WiomBadgeLabelTone].
 * @param modifier Modifier.
 * @param size Default (28 dp) or Small (24 dp).
 * @param style Filled (terminal) or Tinted (transitional). Ignored when [size] is Small.
 */
@Composable
fun WiomBadgeLabel(
    text: String,
    tone: WiomBadgeLabelTone,
    modifier: Modifier = Modifier,
    size: WiomBadgeLabelSize = WiomBadgeLabelSize.Default,
    style: WiomBadgeLabelStyle = WiomBadgeLabelStyle.Tinted,
) {
    // Small is always Tinted per skill Rule 3 + foundations: no Filled Small variant exists.
    val effectiveStyle = if (size == WiomBadgeLabelSize.Small) WiomBadgeLabelStyle.Tinted else style

    val palette = labelPalette(tone = tone, style = effectiveStyle)
    val shape = RoundedCornerShape(WiomTheme.radius.tiny)

    val horizontal = when (size) {
        WiomBadgeLabelSize.Default -> WiomTheme.spacing.md   // 12dp
        WiomBadgeLabelSize.Small -> WiomTheme.spacing.sm      // 8dp
    }
    val vertical = WiomTheme.spacing.xs                       // 4dp

    val textStyle = when (size) {
        WiomBadgeLabelSize.Default -> WiomTheme.type.labelMd  // 14 sp SemiBold / 20 LH → 28 dp total
        WiomBadgeLabelSize.Small -> WiomTheme.type.labelSm    // 12 sp SemiBold / 16 LH → 24 dp total
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(color = palette.fill, shape = shape)
            .padding(horizontal = horizontal, vertical = vertical),
    ) {
        Text(
            text = text,
            style = textStyle,
            color = palette.text,
            maxLines = 1,
        )
    }
}

// ---- internals --------------------------------------------------------------

private data class LabelPalette(val fill: Color, val text: Color)

@Composable
private fun labelPalette(tone: WiomBadgeLabelTone, style: WiomBadgeLabelStyle): LabelPalette {
    val c = WiomTheme.color
    return when (style) {
        WiomBadgeLabelStyle.Filled -> when (tone) {
            WiomBadgeLabelTone.Positive -> LabelPalette(c.bg.positive, c.text.onPositive)
            WiomBadgeLabelTone.Warning -> LabelPalette(c.bg.warning, c.text.onWarning)
            WiomBadgeLabelTone.Critical -> LabelPalette(c.bg.critical, c.text.onCritical)
            WiomBadgeLabelTone.Info -> LabelPalette(c.bg.info, c.text.onInfo)
            WiomBadgeLabelTone.Neutral -> LabelPalette(c.bg.muted, c.text.subtle)
            // Brand is a Tinted-only tone per skill. Fall back to Brand + Tinted.
            WiomBadgeLabelTone.Brand -> LabelPalette(c.bg.brandSubtle, c.text.brand)
        }
        WiomBadgeLabelStyle.Tinted -> when (tone) {
            WiomBadgeLabelTone.Positive -> LabelPalette(c.bg.positiveSubtle, c.text.positive)
            WiomBadgeLabelTone.Warning -> LabelPalette(c.bg.warningSubtle, c.text.onWarning)
            WiomBadgeLabelTone.Critical -> LabelPalette(c.bg.criticalSubtle, c.text.critical)
            WiomBadgeLabelTone.Info -> LabelPalette(c.bg.infoSubtle, c.text.info)
            WiomBadgeLabelTone.Neutral -> LabelPalette(c.bg.subtle, c.text.subtle)
            WiomBadgeLabelTone.Brand -> LabelPalette(c.bg.brandSubtle, c.text.brand)
        }
    }
}

// -----------------------------------------------------------------------------
// Previews
// -----------------------------------------------------------------------------

@Preview(name = "Dot — tones", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewBadgeDots() {
    WiomTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md),
            modifier = Modifier.padding(WiomTheme.spacing.lg),
        ) {
            WiomBadgeDot(WiomBadgeDotTone.Brand)
            WiomBadgeDot(WiomBadgeDotTone.Critical)
            WiomBadgeDot(WiomBadgeDotTone.Neutral)
        }
    }
}

@Preview(name = "Count — Brand / Critical / overflow / zero", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewBadgeCount() {
    WiomTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(WiomTheme.spacing.lg),
        ) {
            WiomBadgeCount(count = 3, tone = WiomBadgeCountTone.Brand)
            WiomBadgeCount(count = 5, tone = WiomBadgeCountTone.Critical)
            WiomBadgeCount(count = 42, tone = WiomBadgeCountTone.Critical, cap = 9)
            WiomBadgeCount(count = 125, tone = WiomBadgeCountTone.Critical, cap = 99)
            WiomBadgeCount(count = 0, tone = WiomBadgeCountTone.Critical) // renders nothing
        }
    }
}

@Preview(name = "Label — Default Filled", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewBadgeLabelDefaultFilled() {
    WiomTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
            modifier = Modifier.padding(WiomTheme.spacing.lg),
        ) {
            WiomBadgeLabel("पूरा हुआ", tone = WiomBadgeLabelTone.Positive, style = WiomBadgeLabelStyle.Filled)
            WiomBadgeLabel("रुका हुआ", tone = WiomBadgeLabelTone.Warning, style = WiomBadgeLabelStyle.Filled)
            WiomBadgeLabel("असफल", tone = WiomBadgeLabelTone.Critical, style = WiomBadgeLabelStyle.Filled)
            WiomBadgeLabel("चालू", tone = WiomBadgeLabelTone.Info, style = WiomBadgeLabelStyle.Filled)
            WiomBadgeLabel("बाकी", tone = WiomBadgeLabelTone.Neutral, style = WiomBadgeLabelStyle.Filled)
        }
    }
}

@Preview(name = "Label — Default Tinted", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewBadgeLabelDefaultTinted() {
    WiomTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
            modifier = Modifier.padding(WiomTheme.spacing.lg),
        ) {
            WiomBadgeLabel("पक्का", tone = WiomBadgeLabelTone.Positive, style = WiomBadgeLabelStyle.Tinted)
            WiomBadgeLabel("अपडेट", tone = WiomBadgeLabelTone.Warning, style = WiomBadgeLabelStyle.Tinted)
            WiomBadgeLabel("समय खत्म", tone = WiomBadgeLabelTone.Critical, style = WiomBadgeLabelStyle.Tinted)
            WiomBadgeLabel("प्रोसेसिंग", tone = WiomBadgeLabelTone.Info, style = WiomBadgeLabelStyle.Tinted)
            WiomBadgeLabel("प्रस्तावित", tone = WiomBadgeLabelTone.Neutral, style = WiomBadgeLabelStyle.Tinted)
            WiomBadgeLabel("फ़ीचर्ड", tone = WiomBadgeLabelTone.Brand, style = WiomBadgeLabelStyle.Tinted)
        }
    }
}

@Preview(name = "Label — Small (always Tinted)", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewBadgeLabelSmall() {
    WiomTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
            modifier = Modifier.padding(WiomTheme.spacing.lg),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
                WiomBadgeLabel("पक्का", tone = WiomBadgeLabelTone.Positive, size = WiomBadgeLabelSize.Small)
                WiomBadgeLabel("पेंडिंग", tone = WiomBadgeLabelTone.Warning, size = WiomBadgeLabelSize.Small)
                WiomBadgeLabel("एक्सपायर्ड", tone = WiomBadgeLabelTone.Critical, size = WiomBadgeLabelSize.Small)
                WiomBadgeLabel("एडमिन", tone = WiomBadgeLabelTone.Info, size = WiomBadgeLabelSize.Small)
                WiomBadgeLabel("न्यूट्रल", tone = WiomBadgeLabelTone.Neutral, size = WiomBadgeLabelSize.Small)
                WiomBadgeLabel("ब्रांड", tone = WiomBadgeLabelTone.Brand, size = WiomBadgeLabelSize.Small)
            }
        }
    }
}
