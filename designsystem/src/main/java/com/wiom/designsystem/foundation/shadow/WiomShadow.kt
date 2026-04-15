package com.wiom.designsystem.foundation.shadow

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Wiom shadow/elevation tokens.
 *
 * Rule: A component uses EITHER a border OR a shadow, never both.
 * Default to [md]. Max 1–2 [lg] per screen. [xl] always pairs with overlay.
 *
 * Compose uses elevation (Dp) to approximate Figma shadow tokens.
 */
@Immutable
data class WiomShadow(
    val none: Dp = 0.dp,
    val sm: Dp = 1.dp,
    val md: Dp = 3.dp,
    val lg: Dp = 6.dp,
    val xl: Dp = 12.dp,
)

val LocalWiomShadow = staticCompositionLocalOf { WiomShadow() }
