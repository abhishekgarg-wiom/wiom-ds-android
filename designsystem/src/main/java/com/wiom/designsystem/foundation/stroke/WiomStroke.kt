package com.wiom.designsystem.foundation.stroke

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Wiom border/stroke width tokens.
 *
 * Position: always inside. Focus rings outside (a11y only).
 * Filled state removes the stroke — fill IS the boundary.
 */
@Immutable
data class WiomStroke(
    val small: Dp = 1.dp,
    val medium: Dp = 2.dp,
)

val LocalWiomStroke = staticCompositionLocalOf { WiomStroke() }
