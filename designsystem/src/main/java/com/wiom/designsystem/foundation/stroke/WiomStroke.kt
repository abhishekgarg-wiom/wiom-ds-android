package com.wiom.designsystem.foundation.stroke

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Wiom stroke widths. Position: always inside. Focus rings: outside, offset 2dp,
 * radius = component radius + 2dp. Filled state = no border (fill is the boundary).
 */
@Immutable
data class WiomStroke(
    val small: Dp = 1.dp,   // dividers, card outlines, input rest border
    val medium: Dp = 2.dp,  // outlined buttons, checkbox/radio, selected/active/error, focus rings
)

val LocalWiomStroke = staticCompositionLocalOf { WiomStroke() }
