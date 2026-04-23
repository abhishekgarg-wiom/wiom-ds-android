package com.wiom.designsystem.component.iconbadge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.theme.WiomTheme

/** Icon Badge size. Container / glyph / padding dimensions are per `wiom-icon-badge` skill. */
enum class WiomIconBadgeSize { Sm, Md, Lg }

/**
 * Icon Badge tone.
 *
 * Each tone pairs a `bg.*.subtle` container with the matching `icon.*` glyph
 * tint — never mix families. `Warning` glyph is legible only on a warning-tinted
 * surface (per foundations rule).
 */
enum class WiomIconBadgeTone { Neutral, Brand, Positive, Warning, Critical, Info }

/**
 * Wiom Icon Badge — a single Material Rounded glyph inside a filled, always-round container.
 *
 * Leading affordance for list rows, empty-state heros, settings entries, status
 * summaries. Display-only — the tap target lives on the parent row / CTA.
 *
 * Not the same as [WiomBadge][com.wiom.designsystem.component.badge]. Badge is a
 * count / dot / label chip (text-bearing). Icon Badge is a single glyph in a
 * colored circle (visual-only).
 *
 * Size comes from the enum — DO NOT pass `.size(...)` / `.width(...)` /
 * `.height(...)` via [modifier].
 *
 * @param icon Material Rounded icon to display. NEVER `Icons.Default/Filled/Outlined/Sharp`.
 * @param size Container size. `Sm` 24dp · `Md` 48dp · `Lg` 96dp.
 * @param tone Color family — container bg + glyph tint are paired.
 * @param contentDescription Accessibility description; null if the badge is purely decorative
 *   and the parent row's headline already names it.
 * @param modifier Accepts alignment + parent-driven padding only.
 */
@Composable
fun WiomIconBadge(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    size: WiomIconBadgeSize = WiomIconBadgeSize.Md,
    tone: WiomIconBadgeTone = WiomIconBadgeTone.Neutral,
    contentDescription: String? = null,
) {
    val dims = dimensionsFor(size)
    val palette = paletteFor(tone)
    Box(
        modifier = modifier
            .size(dims.container)
            .clip(CircleShape)
            .background(color = palette.containerBg, shape = CircleShape)
            .padding(dims.padding),
        contentAlignment = Alignment.Center,
    ) {
        WiomIcon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = palette.glyphTint,
            size = dims.glyph,
        )
    }
}

// ---- internals --------------------------------------------------------------

private data class IconBadgeDimensions(val container: Dp, val glyph: Dp, val padding: Dp)

/**
 * Container sizes (24 / 48 / 96 dp) are atom-spec dimensions per the
 * `wiom-icon-badge` skill — same exemption as switch (52×32) and checkbox (20×20)
 * indicator sizes. Glyph sizes + padding come from tokens.
 */
@Composable
private fun dimensionsFor(size: WiomIconBadgeSize): IconBadgeDimensions = when (size) {
    WiomIconBadgeSize.Sm -> IconBadgeDimensions(
        container = 24.dp,
        glyph = WiomTheme.iconSize.xs,      // 16dp
        padding = WiomTheme.spacing.xs,      // 4dp
    )
    WiomIconBadgeSize.Md -> IconBadgeDimensions(
        container = 48.dp,
        glyph = WiomTheme.iconSize.md,      // 24dp
        padding = WiomTheme.spacing.md,      // 12dp
    )
    WiomIconBadgeSize.Lg -> IconBadgeDimensions(
        container = 96.dp,
        glyph = WiomTheme.iconSize.lg,      // 48dp
        padding = WiomTheme.spacing.xl,      // 24dp
    )
}

private data class IconBadgePalette(val containerBg: Color, val glyphTint: Color)

@Composable
private fun paletteFor(tone: WiomIconBadgeTone): IconBadgePalette {
    val c = WiomTheme.color
    return when (tone) {
        WiomIconBadgeTone.Neutral -> IconBadgePalette(c.bg.subtle, c.icon.nonAction)
        WiomIconBadgeTone.Brand -> IconBadgePalette(c.bg.brandSubtle, c.icon.brand)
        WiomIconBadgeTone.Positive -> IconBadgePalette(c.bg.positiveSubtle, c.icon.positive)
        WiomIconBadgeTone.Warning -> IconBadgePalette(c.bg.warningSubtle, c.icon.warning)
        WiomIconBadgeTone.Critical -> IconBadgePalette(c.bg.criticalSubtle, c.icon.critical)
        WiomIconBadgeTone.Info -> IconBadgePalette(c.bg.infoSubtle, c.icon.info)
    }
}

// ---- Previews ---------------------------------------------------------------

@Preview(name = "IconBadge — all sizes × tones", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewIconBadgeGrid() {
    WiomTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.lg),
            modifier = Modifier.padding(WiomTheme.spacing.lg),
        ) {
            listOf(WiomIconBadgeSize.Sm, WiomIconBadgeSize.Md, WiomIconBadgeSize.Lg).forEach { s ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    WiomIconBadgeTone.values().forEach { t ->
                        WiomIconBadge(
                            icon = when (t) {
                                WiomIconBadgeTone.Positive -> Icons.Rounded.Check
                                WiomIconBadgeTone.Warning -> Icons.Rounded.Warning
                                WiomIconBadgeTone.Critical -> Icons.Rounded.Warning
                                WiomIconBadgeTone.Info -> Icons.Rounded.Info
                                else -> Icons.Rounded.Settings
                            },
                            size = s,
                            tone = t,
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}
