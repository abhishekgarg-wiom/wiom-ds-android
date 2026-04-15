package com.wiom.designsystem.component.navigationbar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wiom.designsystem.component.badge.WiomBadgeDot
import com.wiom.designsystem.component.badge.WiomBadgeColor
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.foundation.icon.WiomIcons
import com.wiom.designsystem.theme.WiomTheme

data class WiomNavItem(
    val label: String,
    @DrawableRes val icon: Int,
    val hasBadge: Boolean = false,
)

/**
 * Wiom bottom navigation bar. 2–5 items. Every item is always reachable.
 *
 * Hide this bar on non-root screens (detail, form, modal) — don't disable it.
 */
@Composable
fun WiomNavigationBar(
    items: List<WiomNavItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    require(items.size in 2..5) { "WiomNavigationBar requires 2–5 items; got ${items.size}" }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(WiomTheme.colors.surface.base),
    ) {
        HorizontalDivider(thickness = WiomTheme.stroke.small, color = WiomTheme.colors.border.default)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = WiomTheme.spacing.sm, horizontal = WiomTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEachIndexed { idx, item ->
                NavItem(
                    item = item,
                    selected = idx == selectedIndex,
                    onClick = { onSelect(idx) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun NavItem(
    item: WiomNavItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val iconTint = if (selected) WiomTheme.colors.brand.primary else WiomTheme.colors.text.secondary
    val labelColor = if (selected) WiomTheme.colors.brand.primary else WiomTheme.colors.text.secondary
    val pillColor = if (selected) WiomTheme.colors.brand.primaryTint else androidx.compose.ui.graphics.Color.Transparent

    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = WiomTheme.spacing.xs),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs),
    ) {
        Box {
            Box(
                modifier = Modifier
                    .background(pillColor, RoundedCornerShape(WiomTheme.radius.full))
                    .padding(horizontal = WiomTheme.spacing.lg, vertical = WiomTheme.spacing.xs),
                contentAlignment = Alignment.Center,
            ) {
                WiomIcon(
                    id = item.icon,
                    contentDescription = item.label,
                    size = WiomTheme.icon.md,
                    tint = iconTint,
                )
            }
            if (item.hasBadge) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 2.dp, end = 2.dp),
                ) {
                    WiomBadgeDot(color = WiomBadgeColor.Negative)
                }
            }
        }
        Text(
            text = item.label,
            style = WiomTheme.type.labelSm,
            color = labelColor,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(80.dp),
        )
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun NavBarPreview() = WiomTheme {
    val items = listOf(
        WiomNavItem("Home", WiomIcons.search),
        WiomNavItem("Plans", WiomIcons.expandMore),
        WiomNavItem("Bills", WiomIcons.phone, hasBadge = true),
        WiomNavItem("Profile", WiomIcons.menu),
    )
    WiomNavigationBar(items = items, selectedIndex = 0, onSelect = {})
}
