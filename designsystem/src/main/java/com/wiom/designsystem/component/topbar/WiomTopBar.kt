package com.wiom.designsystem.component.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.foundation.icon.WiomIcons
import com.wiom.designsystem.theme.WiomTheme

enum class WiomTopBarSize { Small, Medium, Large }

/**
 * Wiom top bar. Pick [size] for visual weight:
 * - Small (default, 64dp) — 90% of screens. Detail, settings, forms, modals.
 * - Medium (112dp) — content-heavy screens needing prominent title.
 * - Large (152dp) — hero / landing only. One per flow max.
 *
 * Use [centered] = true for modal / bottom-sheet headers (Small only).
 * Set [scrolled] = true programmatically on scroll > 0 to show elevation.
 *
 * [leading] default is back arrow (provide `null` for root screens).
 * [actions] scope is `RowScope` — pack with [WiomTopBarIconAction] / [WiomTopBarTextAction].
 */
@Composable
fun WiomTopBar(
    title: String,
    modifier: Modifier = Modifier,
    size: WiomTopBarSize = WiomTopBarSize.Small,
    subtitle: String? = null,
    centered: Boolean = false,
    scrolled: Boolean = false,
    leading: (@Composable () -> Unit)? = {
        WiomIcon(WiomIcons.arrowBack, "Back", size = WiomTheme.icon.md, tint = WiomTheme.colors.text.secondary)
    },
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    val titleStyle = when (size) {
        WiomTopBarSize.Small -> WiomTheme.type.titleLg
        WiomTopBarSize.Medium -> WiomTheme.type.headingLg
        WiomTopBarSize.Large -> WiomTheme.type.headingXl
    }

    val elevation = if (scrolled) WiomTheme.shadow.sm else WiomTheme.shadow.none
    val container = Modifier
        .fillMaxWidth()
        .shadow(elevation = elevation, shape = RectangleShape, clip = false)
        .background(WiomTheme.colors.surface.base)

    when (size) {
        WiomTopBarSize.Small -> Row(
            modifier = modifier
                .then(container)
                .defaultMinSize(minHeight = 64.dp)
                .padding(horizontal = WiomTheme.spacing.xs, vertical = WiomTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs),
        ) {
            leading?.let { LeadingSlot(it) }
            Box(modifier = Modifier.weight(1f), contentAlignment = if (centered) Alignment.Center else Alignment.CenterStart) {
                Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs)) {
                    Text(
                        text = title,
                        style = titleStyle,
                        color = WiomTheme.colors.text.primary,
                        textAlign = if (centered) TextAlign.Center else TextAlign.Start,
                        maxLines = 1,
                    )
                    subtitle?.let {
                        Text(
                            text = it,
                            style = WiomTheme.type.bodySm,
                            color = WiomTheme.colors.text.secondary,
                            maxLines = 1,
                        )
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs)) {
                actions?.invoke(this)
            }
        }

        WiomTopBarSize.Medium, WiomTopBarSize.Large -> Column(
            modifier = modifier.then(container),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 64.dp)
                    .padding(horizontal = WiomTheme.spacing.xs, vertical = WiomTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs),
            ) {
                leading?.let { LeadingSlot(it) }
                Box(modifier = Modifier.weight(1f))
                Row(horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs)) {
                    actions?.invoke(this)
                }
            }
            Text(
                text = title,
                style = titleStyle,
                color = WiomTheme.colors.text.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = WiomTheme.spacing.lg,
                        end = WiomTheme.spacing.lg,
                        top = if (size == WiomTopBarSize.Large) WiomTheme.spacing.md else WiomTheme.spacing.sm,
                        bottom = if (size == WiomTopBarSize.Large) WiomTheme.spacing.xxl else WiomTheme.spacing.sm,
                    ),
            )
        }
    }
}

@Composable
private fun LeadingSlot(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.size(48.dp),
        contentAlignment = Alignment.Center,
    ) { content() }
}

/** Icon action inside the [WiomTopBar] actions slot. 48dp touch target. */
@Composable
fun WiomTopBarIconAction(
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(48.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .padding(WiomTheme.spacing.md),
            contentAlignment = Alignment.Center,
        ) { icon() }
    }
}

/** Text CTA inside the actions slot ("Save", "Done"). Never for destructive actions. */
@Composable
fun WiomTopBarTextAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Text(
        text = text,
        style = WiomTheme.type.labelMd,
        color = if (enabled) WiomTheme.colors.brand.primary else WiomTheme.colors.text.disabled,
        modifier = modifier.padding(horizontal = WiomTheme.spacing.md, vertical = WiomTheme.spacing.md),
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun TopBarSmallPreview() = WiomTheme {
    WiomTopBar(
        title = "Profile",
        actions = {
            WiomTopBarIconAction(icon = { WiomIcon(WiomIcons.search, "Search", tint = WiomTheme.colors.text.secondary) }, onClick = {})
        },
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun TopBarCenteredPreview() = WiomTheme {
    WiomTopBar(
        title = "Edit profile",
        centered = true,
        leading = { WiomIcon(WiomIcons.close, "Close", size = WiomTheme.icon.md, tint = WiomTheme.colors.text.secondary) },
        actions = { WiomTopBarTextAction(text = "Save", onClick = {}) },
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun TopBarMediumPreview() = WiomTheme {
    WiomTopBar(
        title = "Payment history",
        size = WiomTopBarSize.Medium,
        actions = {
            WiomTopBarIconAction(icon = { WiomIcon(WiomIcons.moreVert, "More", tint = WiomTheme.colors.text.secondary) }, onClick = {})
        },
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun TopBarLargePreview() = WiomTheme {
    WiomTopBar(
        title = "Good morning,\nAbhishek",
        size = WiomTopBarSize.Large,
        leading = { WiomIcon(WiomIcons.menu, "Menu", size = WiomTheme.icon.md, tint = WiomTheme.colors.text.secondary) },
    )
}
