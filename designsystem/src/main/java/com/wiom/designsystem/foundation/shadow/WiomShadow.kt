package com.wiom.designsystem.foundation.shadow

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Wiom elevation tokens. Shadow XOR border — never both. shadow.xl always pairs with overlay.scrim. */
@Immutable
data class WiomShadow(
    val none: Dp = 0.dp,
    val sm: Dp = 1.dp,
    val md: Dp = 3.dp,
    val lg: Dp = 6.dp,
    val xl: Dp = 12.dp,
)

val LocalWiomShadow = staticCompositionLocalOf { WiomShadow() }
