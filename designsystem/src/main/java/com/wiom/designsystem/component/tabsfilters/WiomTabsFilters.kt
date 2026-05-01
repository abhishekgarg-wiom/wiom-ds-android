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

    // Skill §1.1: Track padding `space-4` all sides; items butt up against each other within
    // the track (no extra gap — the radius isolates them visually).
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(WiomTheme.radius.medium))
            .background(WiomTheme.color.bg.muted)
            .padding(WiomTheme.spacing.xs),
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
            // Skill §1.1: label is `type.label.md` for **both states** — pill tabs aren't body text.
            style = WiomTheme.type.labelMd,
            color = if (selected) WiomTheme.color.text.selected else WiomTheme.color.text.subtle,
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
    // Skill §2.2 indicator note: it's a **filled bar (`bg.*` slot)**, not a stroke. Element-first
    // discipline says use `bg.brand`, not `stroke.brandFocus`.
    val indicatorColor = WiomTheme.color.bg.brand
    val indicatorPx = with(LocalDensity.current) { WiomTheme.stroke.medium.toPx() }

    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .defaultMinSize(minHeight = WiomTheme.spacing.huge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Skill §2.1: Content padding `space-16` H × `space-12` V. Indicator width matches the
        // content row above (fills item width — never hugs label width even in scroll mode).
        Box(
            modifier = Modifier
                .then(if (hugContent) Modifier else Modifier.fillMaxWidth())
                .padding(horizontal = WiomTheme.spacing.lg, vertical = WiomTheme.spacing.md)
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
                },
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = label,
                // Skill §2.1: `type.label.md` for both states.
                style = WiomTheme.type.labelMd,
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
    enabled: Boolean = true,
    onClose: (() -> Unit)? = null,
) {
    val shape = RoundedCornerShape(WiomTheme.radius.small)

    // Skill §3.2 ship values:
    //   Unselected — transparent fill, 1 dp `stroke.subtle` border, label `text.default`
    //   Selected   — `bg.selected` fill, 1 dp `stroke.selected` border, label `text.selected`
    //   Disabled   — `bg.disabled` fill, **no border** (foundations Pattern A), label `text.disabled`
    val selectionModifier = when {
        !enabled -> Modifier
            .clip(shape)
            .background(WiomTheme.color.bg.disabled)
        selected -> Modifier
            .clip(shape)
            .background(WiomTheme.color.bg.selected)
            .border(
                width = WiomTheme.stroke.small,
                color = WiomTheme.color.stroke.selected,
                shape = shape,
            )
        else -> Modifier
            .clip(shape)
            .border(
                width = WiomTheme.stroke.small,
                color = WiomTheme.color.stroke.subtle,
                shape = shape,
            )
    }
    val labelColor = when {
        !enabled -> WiomTheme.color.text.disabled
        selected -> WiomTheme.color.text.selected
        else -> WiomTheme.color.text.default
    }

    Row(
        modifier = modifier
            .then(selectionModifier)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = WiomTheme.spacing.md, vertical = WiomTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs),
    ) {
        Text(
            text = label,
            // Skill §3.2 ship value: SemiBold (`label.md`) across all states. Description's claim
            // that Unselected uses Regular is wrong — Figma ships SemiBold everywhere.
            style = WiomTheme.type.labelMd,
            color = labelColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (selected && enabled) {
            // Skill §3.1: Selected ships with a trailing 16 dp close ✕ glyph in `icon.brand`. Tap
            // dispatches `onClose` if provided; otherwise falls back to `onClick` (deselect).
            // Hidden on Disabled — disabled chips aren't interactive.
            com.wiom.designsystem.foundation.icon.WiomIcon(
                imageVector = androidx.compose.material.icons.Icons.Rounded.Close,
                contentDescription = "Remove ${'$'}label",
                size = WiomTheme.iconSize.xs,
                tint = WiomTheme.color.icon.brand,
                modifier = Modifier.clickable { (onClose ?: onClick).invoke() },
            )
        }
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
