package com.wiom.designsystem.foundation.spacing

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Wiom spacing primitives — all values on 4px grid.
 *
 * Hierarchy:
 *  - Tight (4–8)   → within one unit (icon + label)
 *  - Medium (12–16) → same section (form field groups)
 *  - Loose (24–32+) → between sections
 *
 * Use `Arrangement.spacedBy(Wiom.spacing.md)` on parents. Children own padding.
 */
@Immutable
data class WiomSpacing(
    val none: Dp = 0.dp,
    val xxs: Dp = 2.dp,
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
