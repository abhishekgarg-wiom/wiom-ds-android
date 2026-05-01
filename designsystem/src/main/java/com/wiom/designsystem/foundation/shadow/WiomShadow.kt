package com.wiom.designsystem.foundation.shadow

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Wiom elevation tokens — 5 shadow levels.
 *
 * Pair with `Modifier.shadow(elevation = WiomTheme.shadow.md, shape = ...)`.
 *
 * Rules (from skill `shadow.md`):
 *   - shadow XOR border — never both. Flat (0dp) → 1px border. Elevated → no border.
 *     Exception: critical interactive surfaces (payment cards) may pair shadow.md
 *     with stroke.small for budget-device robustness.
 *   - shadow.xl ALWAYS pairs with overlay.scrim (modal backdrop).
 *   - Default to shadow.md when adding elevation. Max 1–2 shadow.lg per screen.
 *
 * Spec → Compose mapping (note: spec is web-CSS-centric):
 *   The skill defines shadows as `0 Y blur 0 rgba(0,0,0,0.15)` with offset-y / blur
 *   in pixels (sm=1/3, md=2/6, lg=4/12, xl=8/24). Compose's `Modifier.shadow(elevation)`
 *   doesn't expose offset/blur separately — it draws a Material elevation shadow at the
 *   given dp. The values below are dp elevations chosen to visually approximate the
 *   spec's CSS shadows on Compose's elevation algorithm.
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
