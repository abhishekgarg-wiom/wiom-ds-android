package com.wiom.designsystem.foundation.icon

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.wiom.designsystem.theme.WiomTheme

/**
 * Wiom icon composable.
 *
 * Always provide a non-null [contentDescription] when the icon is actionable or conveys info.
 * Pass `null` only for purely decorative icons adjacent to a text label.
 *
 * Size defaults to [WiomTheme.icon.md] (24dp). Override with [WiomTheme.icon] tokens only —
 * never hardcode dp values.
 */
@Composable
fun WiomIcon(
    @DrawableRes id: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = WiomTheme.icon.md,
    tint: Color = LocalContentColor.current,
) {
    Icon(
        painter = painterResource(id),
        contentDescription = contentDescription,
        modifier = modifier.size(size),
        tint = tint,
    )
}
