package com.wiom.designsystem.foundation.icon

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Wiom icon size tokens.
 *
 * Library: **Material 3 Icons Rounded** (`Icons.Rounded.*` from
 * `androidx.compose.material:material-icons-extended`). Filled is default.
 * Outlined only for toggle-off / nav-unselected.
 *
 * `painterResource` is reserved for brand / partner drawables (`ic_wiom_*`, `ic_partner_*`)
 * — never for a standard Material icon.
 *
 * Tint via `WiomTheme.color.icon.*` tokens. Never via `text.on*`.
 */
@Immutable
data class WiomIconSize(
    /** 16dp. RESTRICTED: inline indicators, input trailing only. Add hit-area padding if tappable. */
    val xs: Dp = 16.dp,
    /** 20dp. Button icons, compact controls. */
    val sm: Dp = 20.dp,
    /** 24dp. DEFAULT. Toolbar, nav, list, chips, cards. */
    val md: Dp = 24.dp,
    /** 48dp. Empty states, onboarding, hero. */
    val lg: Dp = 48.dp,
)

val LocalWiomIconSize = staticCompositionLocalOf { WiomIconSize() }
