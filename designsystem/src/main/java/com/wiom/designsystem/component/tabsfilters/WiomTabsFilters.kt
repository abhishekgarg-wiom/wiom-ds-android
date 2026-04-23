package com.wiom.designsystem.component.tabsfilters

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.wiom.designsystem.theme.WiomTheme

// =================================================================================================
// Level 1 — Pill Tabs (context switch)
// =================================================================================================

/**
 * Pill-style segmented tabs. Use for **context switching** — switching between fundamentally
 * different datasets (my tickets vs team tickets, today / this week / this month).
 *
 * Max 4 tabs. Beyond that, rethink your IA. For status filtering inside a single dataset, use
 * [WiomUnderlineFilter] instead.
 *
 * Active pill: `bg.default` fill + `shadow.sm` sitting in a `bg.muted` track.
 *
 * @param tabs 2–4 labels.
 * @param selectedIndex current tab.
 * @param onTabSelect invoked with the tapped index.
 */
@Composable
fun WiomPillTabs(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    require(tabs.size in 2..4) { "WiomPillTabs supports 2–4 tabs, got ${tabs.size}." }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(WiomTheme.radius.medium))
            .background(WiomTheme.color.bg.muted)
            .padding(WiomTheme.spacing.xs),
        horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        tabs.forEachIndexed { index, label ->
            PillTabItem(
                label = label,
                selected = index == selectedIndex,
                onClick = { onTabSelect(index) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun PillTabItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pillModifier = if (selected) {
        modifier
            .shadow(
                elevation = WiomTheme.shadow.sm,
                shape = RoundedCornerShape(WiomTheme.radius.small),
            )
            .clip(RoundedCornerShape(WiomTheme.radius.small))
            .background(WiomTheme.color.bg.default)
    } else {
        modifier
            .clip(RoundedCornerShape(WiomTheme.radius.small))
    }
    Box(
        modifier = pillModifier
            .clickable(onClick = onClick)
            .padding(horizontal = WiomTheme.spacing.md, vertical = WiomTheme.spacing.sm),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = if (selected) WiomTheme.type.labelMd else WiomTheme.type.bodyMd,
            color = if (selected) WiomTheme.color.text.brand else WiomTheme.color.text.subtle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

// =================================================================================================
// Level 2 — Underline Filter (single-select)
// =================================================================================================

/**
 * Underline filter — single-select inside the same dataset (All / Pending / Closed / Expired).
 *
 * Active indicator: 2dp line underneath the label, hugging the text width. Bottom border: 1dp
 * `stroke.subtle` spanning the full bar.
 *
 * For 5+ filters pass `scrollable = true` — the bar becomes horizontally scrollable and each
 * filter hugs its content width. For 3–4 filters, leave `scrollable = false` and filters fill
 * equally.
 *
 * @param filters labels.
 * @param selectedIndex current filter.
 * @param onFilterSelect invoked with tapped index.
 * @param scrollable `true` when 5+ filters — enables horizontal scroll.
 */
@Composable
fun WiomUnderlineFilter(
    filters: List<String>,
    selectedIndex: Int,
    onFilterSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    scrollable: Boolean = false,
) {
    val borderColor = WiomTheme.color.stroke.subtle
    val borderPx = with(LocalDensity.current) { WiomTheme.stroke.small.toPx() }

    val barModifier = modifier
        .fillMaxWidth()
        .drawBehind {
            drawLine(
                color = borderColor,
                start = Offset(0f, size.height - borderPx / 2f),
                end = Offset(size.width, size.height - borderPx / 2f),
                strokeWidth = borderPx,
            )
        }

    if (scrollable) {
        Row(
            modifier = barModifier.horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            filters.forEachIndexed { index, label ->
                UnderlineFilterItem(
                    label = label,
                    selected = index == selectedIndex,
                    onClick = { onFilterSelect(index) },
                    hugContent = true,
                )
            }
        }
    } else {
        Row(
            modifier = barModifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            filters.forEachIndexed { index, label ->
                UnderlineFilterItem(
                    label = label,
                    selected = index == selectedIndex,
                    onClick = { onFilterSelect(index) },
                    hugContent = false,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun UnderlineFilterItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    hugContent: Boolean,
    modifier: Modifier = Modifier,
) {
    val indicatorColor = WiomTheme.color.stroke.brandFocus // = stroke.selected = bg.brand
    val indicatorPx = with(LocalDensity.current) { WiomTheme.stroke.medium.toPx() }

    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .defaultMinSize(minHeight = WiomTheme.spacing.huge)
            .padding(horizontal = if (hugContent) WiomTheme.spacing.lg else WiomTheme.spacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Label sits centered; underline = text width when hugContent=true, otherwise
        // = slot width via fillMaxWidth() inside the weight(1f) column.
        // Wrapper Box holds the label and — when selected — draws a 2dp underline at its bottom
        // edge. When hugContent=false the Box uses fillMaxWidth() inside the weight(1f) Column so
        // the underline spans the slot; when hugContent=true the Box wraps the text so the
        // underline hugs the label width.
        Box(
            modifier = Modifier
                .then(if (hugContent) Modifier else Modifier.fillMaxWidth())
                .drawBehind {
                    if (selected) {
                        val y = size.height - indicatorPx / 2f
                        drawLine(
                            color = indicatorColor,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = indicatorPx,
                            cap = androidx.compose.ui.graphics.StrokeCap.Round,
                        )
                    }
                }
                .padding(vertical = WiomTheme.spacing.md),
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = label,
                style = if (selected) WiomTheme.type.labelMd else WiomTheme.type.bodyMd,
                color = if (selected) WiomTheme.color.text.brand else WiomTheme.color.text.subtle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

// =================================================================================================
// Level 3 — Chips (multi-select toggle)
// =================================================================================================

/**
 * Chip — multi-select toggle used for combining attributes (Installation + Repair, WiFi + Fiber).
 *
 * Selected: `bg.selected` fill + `text.selected` text, **no border** (filled-state rule).
 * Unselected: 1dp `stroke.subtle` border + `text.default`.
 *
 * @param label chip text.
 * @param selected whether this chip is currently on.
 * @param onClick toggle callback.
 */
@Composable
fun WiomChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(WiomTheme.radius.small)

    // Selected: filled `bg.selected`, NO border (filled-state rule).
    // Unselected: 1dp `stroke.subtle` border, transparent fill.
    val selectionModifier = if (selected) {
        Modifier
            .clip(shape)
            .background(WiomTheme.color.bg.selected)
    } else {
        Modifier
            .clip(shape)
            .border(
                width = WiomTheme.stroke.small,
                color = WiomTheme.color.stroke.subtle,
                shape = shape,
            )
    }

    Box(
        modifier = modifier
            .then(selectionModifier)
            .clickable(onClick = onClick)
            .padding(horizontal = WiomTheme.spacing.md, vertical = WiomTheme.spacing.sm),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = if (selected) WiomTheme.type.labelMd else WiomTheme.type.bodyMd,
            color = if (selected) WiomTheme.color.text.selected else WiomTheme.color.text.default,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/**
 * Horizontal row of [WiomChip]s with an 8dp gap. Supports wrapping via the surrounding layout
 * (use a `FlowRow` parent if wrapping is needed). No horizontal padding — the consumer places the
 * row where it wants.
 */
@Composable
fun WiomChipRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

// =================================================================================================
// Previews
// =================================================================================================

@Preview(name = "PillTabs · 2", showBackground = true, widthDp = 360)
@Composable
private fun PreviewPillTabs2() {
    WiomTheme {
        Box(Modifier.padding(WiomTheme.spacing.lg)) {
            WiomPillTabs(
                tabs = listOf("मेरे टिकट", "टीम के टिकट"),
                selectedIndex = 0,
                onTabSelect = {},
            )
        }
    }
}

@Preview(name = "PillTabs · 3", showBackground = true, widthDp = 360)
@Composable
private fun PreviewPillTabs3() {
    WiomTheme {
        Box(Modifier.padding(WiomTheme.spacing.lg)) {
            WiomPillTabs(
                tabs = listOf("Today", "This week", "This month"),
                selectedIndex = 1,
                onTabSelect = {},
            )
        }
    }
}

@Preview(name = "PillTabs · 4", showBackground = true, widthDp = 360)
@Composable
private fun PreviewPillTabs4() {
    WiomTheme {
        Box(Modifier.padding(WiomTheme.spacing.lg)) {
            WiomPillTabs(
                tabs = listOf("Day", "Week", "Month", "Year"),
                selectedIndex = 3,
                onTabSelect = {},
            )
        }
    }
}

@Preview(name = "UnderlineFilter · fixed 3", showBackground = true, widthDp = 360)
@Composable
private fun PreviewUnderline3() {
    WiomTheme {
        WiomUnderlineFilter(
            filters = listOf("सभी", "पेंडिंग", "बंद"),
            selectedIndex = 1,
            onFilterSelect = {},
        )
    }
}

@Preview(name = "UnderlineFilter · fixed 4", showBackground = true, widthDp = 360)
@Composable
private fun PreviewUnderline4() {
    WiomTheme {
        WiomUnderlineFilter(
            filters = listOf("All", "Pending", "Closed", "Expired"),
            selectedIndex = 0,
            onFilterSelect = {},
        )
    }
}

@Preview(name = "UnderlineFilter · scrollable 6", showBackground = true, widthDp = 360)
@Composable
private fun PreviewUnderlineScrollable() {
    WiomTheme {
        WiomUnderlineFilter(
            filters = listOf("All tickets", "New", "Pending", "Processing", "Completed", "Cancelled"),
            selectedIndex = 2,
            onFilterSelect = {},
            scrollable = true,
        )
    }
}

@Preview(name = "Chip · selected + unselected", showBackground = true, widthDp = 360)
@Composable
private fun PreviewChipStates() {
    WiomTheme {
        Box(Modifier.padding(WiomTheme.spacing.lg)) {
            WiomChipRow {
                WiomChip(label = "Installation", selected = true, onClick = {})
                WiomChip(label = "Repair", selected = true, onClick = {})
                WiomChip(label = "Recharge", selected = false, onClick = {})
                WiomChip(label = "Upgrade", selected = false, onClick = {})
            }
        }
    }
}

@Preview(name = "Chip row · all unselected", showBackground = true, widthDp = 360)
@Composable
private fun PreviewChipRowUnselected() {
    WiomTheme {
        Box(Modifier.padding(WiomTheme.spacing.lg)) {
            WiomChipRow {
                WiomChip(label = "WiFi", selected = false, onClick = {})
                WiomChip(label = "Fiber", selected = false, onClick = {})
                WiomChip(label = "5G", selected = false, onClick = {})
            }
        }
    }
}
