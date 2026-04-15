package com.wiom.designsystem.foundation.icon

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Wiom icon size tokens — 4 values.
 *
 * Library: Material Symbols Rounded, filled by default.
 * Never use Icons.Default / Icons.Filled / Icons.Outlined directly.
 * Use [com.wiom.designsystem.foundation.icon.WiomIcons] facade.
 */
@Immutable
data class WiomIconSize(
    /** 16dp — RESTRICTED: inline indicators, input trailing only. */
    val xs: Dp = 16.dp,
    /** 20dp — button icons, compact controls. */
    val sm: Dp = 20.dp,
    /** 24dp — DEFAULT. Toolbar, nav, list, chips, cards. */
    val md: Dp = 24.dp,
    /** 48dp — empty states, onboarding, hero. */
    val lg: Dp = 48.dp,
)

val LocalWiomIconSize = staticCompositionLocalOf { WiomIconSize() }
