package com.wiom.designsystem.component.listitem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.foundation.icon.WiomIcons
import com.wiom.designsystem.theme.WiomTheme

/**
 * Wiom list item — one row in a list. Unified component covering navigation,
 * settings, selection, toggles, and information display.
 *
 * Anatomy: [leadingIcon 40dp?] [primary + secondary?] [trailingMeta? + trailingIcon?]
 * Min height 48dp. Padding 16h × 12v.
 *
 * For checkbox/radio/switch rows, pass the Wiom control as [leadingIcon] or [trailingIcon]
 * instead of an icon.
 */
@Composable
fun WiomListItem(
    primary: String,
    modifier: Modifier = Modifier,
    secondary: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = {
        WiomIcon(id = WiomIcons.expandMore, contentDescription = null, size = 20.dp, tint = WiomTheme.colors.text.secondary)
    },
    trailingMeta: String? = null,
    enabled: Boolean = true,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val background: Color = when {
        selected -> WiomTheme.colors.surface.selected
        else -> Color.Transparent
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .background(background)
            .then(
                if (onClick != null && enabled) Modifier.clickable { onClick() } else Modifier
            )
            .padding(horizontal = WiomTheme.spacing.lg, vertical = WiomTheme.spacing.md)
            .alpha(if (enabled) 1f else 0.38f),
        horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leadingIcon?.let {
            Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) { it() }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs),
        ) {
            Text(
                text = primary,
                style = WiomTheme.type.bodyLg,
                color = WiomTheme.colors.text.primary,
                maxLines = 1,
            )
            secondary?.let {
                Text(text = it, style = WiomTheme.type.bodySm, color = WiomTheme.colors.text.secondary, maxLines = 1)
            }
        }
        if (selected) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(WiomTheme.colors.brand.primary)
            )
        }
        if (trailingMeta != null || trailingIcon != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
            ) {
                trailingMeta?.let {
                    Text(text = it, style = WiomTheme.type.bodySm, color = WiomTheme.colors.text.secondary)
                }
                trailingIcon?.invoke()
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun ListItemPreview() = WiomTheme {
    Column(modifier = Modifier.background(WiomTheme.colors.surface.base)) {
        WiomListItem(
            primary = "Account",
            leadingIcon = { WiomIcon(WiomIcons.phone, null, size = WiomTheme.icon.md, tint = WiomTheme.colors.text.secondary) },
            onClick = {},
        )
        WiomListItem(primary = "Language", trailingMeta = "English", onClick = {})
        WiomListItem(primary = "Build", secondary = "Up to date", trailingIcon = null)
        WiomListItem(primary = "English", trailingIcon = null, selected = true, onClick = {})
        WiomListItem(primary = "Disabled", enabled = false, onClick = {})
    }
}
