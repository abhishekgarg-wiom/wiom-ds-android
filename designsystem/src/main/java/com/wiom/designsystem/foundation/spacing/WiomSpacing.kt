package com.wiom.designsystem.foundation.spacing

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Wiom spacing — 4px grid · 11 semantic tokens.
 *
 * Parents own gap between children (Arrangement.spacedBy); children own internal padding.
 * No margins — ever.
 *
 * Spec reference: `wiom-design-foundations/references/spacing.md` lists tokens as
 * `space-4 … space-84`. Kotlin/Compose access uses t-shirt aliases for idiom; the
 * mapping is 1:1:
 *
 *   xs       = space-4   (4dp)   — chip icon→label, hairline-ish gaps
 *   sm       = space-8   (8dp)   — inline pairings, related items
 *   md       = space-12  (12dp)  — between list items, label→input
 *   lg       = space-16  (16dp)  — card padding, screen gutter (default)
 *   xl       = space-24  (24dp)  — within-card sections, dialog padding
 *   xxl      = space-32  (32dp)  — content→CTA, between sections
 *   xxxl     = space-40  (40dp)  — section breaks
 *   huge     = space-48  (48dp)  — touch-target tier (Action), page margins
 *   xxxHuge  = space-64  (64dp)  — hero padding, large layouts
 *   section  = space-72  (72dp)  — extra-large layouts
 *   hero     = space-84  (84dp)  — full-width hero sections
 *
 * `none` (0dp) is a Kotlin convenience — semantically "no padding", not a primitive.
 *
 * Per spec the utility primitives `space/1` (1dp) and `space/2` (2dp) intentionally have
 * no semantic alias — they're for hairlines / optical adjustments only. If a component
 * genuinely needs them it must annotate the call site (rare).
 */
@Immutable
data class WiomSpacing(
    val none: Dp = 0.dp,
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 12.dp,
    val lg: Dp = 16.dp,
    val xl: Dp = 24.dp,
    val xxl: Dp = 32.dp,
    val xxxl: Dp = 40.dp,
    val huge: Dp = 48.dp,
    val xxxHuge: Dp = 64.dp,
    val section: Dp = 72.dp,
    val hero: Dp = 84.dp,
)

val LocalWiomSpacing = staticCompositionLocalOf { WiomSpacing() }
