package com.wiom.designsystem.foundation.icon

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.wiom.designsystem.theme.WiomTheme

/**
 * Wiom icon — Material 3 Icons Rounded wrapper.
 *
 * Usage:
 * ```
 * WiomIcon(Icons.Rounded.Search, contentDescription = "Search")
 * WiomIcon(Icons.Rounded.Phone, contentDescription = null, tint = WiomTheme.color.icon.brand)
 * ```
 *
 * Size defaults to [WiomTheme.iconSize.md] (24dp). Tint defaults to [WiomTheme.color.icon.nonAction]
 * — the safe default for decorative icons. For tappable chrome icons, pass
 * `tint = WiomTheme.color.icon.action`.
 */
@Composable
fun WiomIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = WiomTheme.iconSize.md,
    tint: Color = WiomTheme.color.icon.nonAction,
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier.size(size),
        tint = tint,
    )
}
