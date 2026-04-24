package com.wiom.designsystem.component.navigationbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.SupportAgent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.wiom.designsystem.component.badge.WiomBadgeDot
import com.wiom.designsystem.component.badge.WiomBadgeDotTone
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.theme.WiomTheme

/**
 * Descriptor for a single item in the [WiomNavigationBar].
 *
 * @param label one word — keep it short (e.g. "Home", "Plans"). `type.labelSm`.
 * @param icon `Icons.Rounded.*` image vector. Filled automatically via Material Rounded.
 * @param hasBadge whether to show a critical dot at the top-end of the icon.
 */
data class WiomNavItem(
    val label: String,
    val icon: ImageVector,
    val hasBadge: Boolean = false,
)

/**
 * Wiom bottom navigation bar — 2–5 top-level destinations.
 *
 * Exactly one item is selected at any time. The active item wears a `bg.selected` pill,
 * an `icon.brand` tint, and a `text.selected` label. Inactive items use `icon.nonAction` +
 * `text.subtle`.
 *
 * Hide the bar (don't just disable it) on non-root screens, flows, and media / keyboard screens.
 *
 * @param items 2–5 destinations. Put the highest-traffic one first.
 * @param selectedIndex current route index.
 * @param onItemSelect invoked with the tapped index.
 */
@Composable
fun WiomNavigationBar(
    items: List<WiomNavItem>,
    selectedIndex: Int,
    onItemSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    require(items.size in 2..5) { "WiomNavigationBar supports 2–5 items, got ${items.size}." }

    val topBorderColor = WiomTheme.color.stroke.subtle
    val topBorderPx = with(LocalDensity.current) { WiomTheme.stroke.small.toPx() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(WiomTheme.color.bg.default)
            .drawBehind {
                drawLine(
                    color = topBorderColor,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = topBorderPx,
                )
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = WiomTheme.spacing.xs,
                    vertical = WiomTheme.spacing.sm,
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEachIndexed { index, item ->
                NavItem(
                    item = item,
                    selected = index == selectedIndex,
                    onClick = { onItemSelect(index) },
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
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(WiomTheme.radius.small))
            .clickable(onClick = onClick)
            .padding(vertical = WiomTheme.spacing.xs),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs),
    ) {
        // Pill wraps the icon. In selected state the pill has a brand-tint fill;
        // in default state the pill is transparent so the icon sits flush.
        Box(
            modifier = Modifier
                .background(
                    color = if (selected) WiomTheme.color.bg.selected else Color.Transparent,
                    shape = RoundedCornerShape(WiomTheme.radius.full),
                )
                .padding(
                    horizontal = WiomTheme.spacing.lg,
                    vertical = WiomTheme.spacing.xs,
                ),
            contentAlignment = Alignment.Center,
        ) {
            // Icon with optional badge dot in the top-end corner.
            Box {
                WiomIcon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = if (selected) WiomTheme.color.icon.brand else WiomTheme.color.icon.nonAction,
                )
                if (item.hasBadge) {
                    Box(
                        modifier = Modifier.align(Alignment.TopEnd),
                    ) {
                        WiomBadgeDot(tone = WiomBadgeDotTone.Critical)
                    }
                }
            }
        }
        Text(
            text = item.label,
            style = WiomTheme.type.labelSm,
            color = if (selected) WiomTheme.color.text.selected else WiomTheme.color.text.subtle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

private val sampleItems = listOf(
    WiomNavItem("Home", Icons.Rounded.Home),
    WiomNavItem("Plans", Icons.Rounded.Payments),
    WiomNavItem("Bills", Icons.AutoMirrored.Rounded.ReceiptLong, hasBadge = true),
    WiomNavItem("Support", Icons.Rounded.SupportAgent),
    WiomNavItem("Profile", Icons.Rounded.Person),
)

@Preview(name = "5 tabs · Home selected", showBackground = true, widthDp = 360)
@Composable
private fun PreviewFiveHomeSelected() {
    WiomTheme {
        WiomNavigationBar(
            items = sampleItems,
            selectedIndex = 0,
            onItemSelect = {},
        )
    }
}

@Preview(name = "5 tabs · Bills selected (badge on same item)", showBackground = true, widthDp = 360)
@Composable
private fun PreviewFiveBillsSelected() {
    WiomTheme {
        WiomNavigationBar(
            items = sampleItems,
            selectedIndex = 2,
            onItemSelect = {},
        )
    }
}

@Preview(name = "4 tabs", showBackground = true, widthDp = 360)
@Composable
private fun PreviewFour() {
    WiomTheme {
        WiomNavigationBar(
            items = sampleItems.take(4),
            selectedIndex = 1,
            onItemSelect = {},
        )
    }
}

@Preview(name = "3 tabs", showBackground = true, widthDp = 360)
@Composable
private fun PreviewThree() {
    WiomTheme {
        WiomNavigationBar(
            items = listOf(
                WiomNavItem("Home", Icons.Rounded.Home),
                WiomNavItem("Plans", Icons.Rounded.Payments),
                WiomNavItem("Profile", Icons.Rounded.Person),
            ),
            selectedIndex = 0,
            onItemSelect = {},
        )
    }
}

@Preview(name = "2 tabs (minimum)", showBackground = true, widthDp = 360)
@Composable
private fun PreviewTwo() {
    WiomTheme {
        WiomNavigationBar(
            items = listOf(
                WiomNavItem("Home", Icons.Rounded.Home),
                WiomNavItem("Profile", Icons.Rounded.Person),
            ),
            selectedIndex = 1,
            onItemSelect = {},
        )
    }
}

@Preview(name = "Badge on inactive tab", showBackground = true, widthDp = 360)
@Composable
private fun PreviewInactiveBadge() {
    WiomTheme {
        WiomNavigationBar(
            items = sampleItems,
            selectedIndex = 0,
            onItemSelect = {},
        )
    }
}
