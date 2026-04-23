package com.wiom.designsystem.foundation.radius

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Wiom corner radius. Nested rule: inner = outer − padding. Standard circular (no squircle). */
@Immutable
data class WiomRadius(
    val none: Dp = 0.dp,
    val tiny: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 12.dp,
    val large: Dp = 16.dp,
    val xlarge: Dp = 24.dp,
    val full: Dp = 9999.dp,
)

val LocalWiomRadius = staticCompositionLocalOf { WiomRadius() }
