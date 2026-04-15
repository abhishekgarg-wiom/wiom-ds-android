package com.wiom.designsystem.component.tabsfilters

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.wiom.designsystem.theme.WiomTheme

/**
 * Level 1 — Pill Tabs. Context switch ("whose data? which mode?").
 * Max 4 options. Exactly one active; active pill sits on white with shadow.sm.
 */
@Composable
fun WiomPillTabs(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    require(tabs.size in 2..4) { "WiomPillTabs supports 2–4 options; got ${tabs.size}" }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(WiomTheme.radius.medium))
            .background(WiomTheme.colors.surface.muted)
            .padding(WiomTheme.spacing.xs),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        tabs.forEachIndexed { idx, label ->
            val selected = idx == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(WiomTheme.radius.small))
                    .then(
                        if (selected) Modifier
                            .shadow(WiomTheme.shadow.sm, RoundedCornerShape(WiomTheme.radius.small))
                            .background(WiomTheme.colors.surface.base)
                        else Modifier
                    )
                    .clickable { onTabSelect(idx) }
                    .padding(vertical = WiomTheme.spacing.sm, horizontal = WiomTheme.spacing.md),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    style = if (selected) WiomTheme.type.labelMd else WiomTheme.type.bodyMd,
                    color = if (selected) WiomTheme.colors.brand.primary else WiomTheme.colors.text.secondary,
                    maxLines = 1,
                )
            }
        }
    }
}

/**
 * Level 2 — Underline Filter. Single-select filter with 2dp brand underline.
 * Pass [scrollable] = true for 5–6 filters (hug content, horizontal scroll).
 */
@Composable
fun WiomUnderlineFilter(
    filters: List<String>,
    selectedIndex: Int,
    onFilterSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    scrollable: Boolean = false,
) {
    val rowMod = if (scrollable) {
        Modifier.horizontalScroll(rememberScrollState())
    } else {
        Modifier.fillMaxWidth()
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = WiomTheme.stroke.small,
                color = WiomTheme.colors.border.default,
                shape = RoundedCornerShape(0.dp),
            ),
    ) {
        Row(modifier = rowMod) {
            filters.forEachIndexed { idx, label ->
                val selected = idx == selectedIndex
                val itemMod = if (scrollable) Modifier else Modifier.weight(1f)
                Column(
                    modifier = itemMod
                        .clickable { onFilterSelect(idx) }
                        .defaultMinSize(minHeight = 48.dp)
                        .padding(horizontal = WiomTheme.spacing.lg),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = label,
                        style = if (selected) WiomTheme.type.labelMd else WiomTheme.type.bodyMd,
                        color = if (selected) WiomTheme.colors.brand.primary else WiomTheme.colors.text.secondary,
                        maxLines = 1,
                    )
                    Box(
                        modifier = Modifier
                            .padding(top = WiomTheme.spacing.sm)
                            .width(if (selected) 64.dp else 0.dp)
                            .clip(RoundedCornerShape(WiomTheme.radius.full))
                            .background(WiomTheme.colors.brand.primary)
                            .defaultMinSize(minHeight = WiomTheme.stroke.medium),
                    )
                }
            }
        }
    }
}

/**
 * Level 3 — Chip. Multi-select additive filter. Filled when selected (no border).
 */
@Composable
fun WiomChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val bg: Color = if (selected) WiomTheme.colors.surface.selected else Color.Transparent
    val textColor = when {
        !enabled -> WiomTheme.colors.text.disabled
        selected -> WiomTheme.colors.brand.primary
        else -> WiomTheme.colors.text.primary
    }
    val shape = RoundedCornerShape(WiomTheme.radius.small)
    Box(
        modifier = modifier
            .clip(shape)
            .background(bg)
            .then(
                if (!selected) Modifier.border(WiomTheme.stroke.small, WiomTheme.colors.border.subtle, shape)
                else Modifier
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = WiomTheme.spacing.md, vertical = WiomTheme.spacing.sm),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = if (selected) WiomTheme.type.labelMd else WiomTheme.type.bodyMd,
            color = textColor,
            maxLines = 1,
        )
    }
}

/** Row of chips with 8dp gap. */
@Composable
fun WiomChipRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = WiomTheme.spacing.lg),
        horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun TabsFiltersPreview() = WiomTheme {
    Column(
        modifier = Modifier
            .background(WiomTheme.colors.surface.base)
            .padding(WiomTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xl),
    ) {
        WiomPillTabs(tabs = listOf("मेरे टिकट", "टीम के टिकट"), selectedIndex = 0, onTabSelect = {})
        WiomUnderlineFilter(filters = listOf("सभी", "पेंडिंग", "बंद", "एक्सपायर्ड"), selectedIndex = 1, onFilterSelect = {})
        WiomChipRow {
            WiomChip(label = "इंस्टॉलेशन", selected = true, onClick = {})
            WiomChip(label = "रिपेयर", selected = true, onClick = {})
            WiomChip(label = "रिचार्ज", selected = false, onClick = {})
            WiomChip(label = "अपग्रेड", selected = false, onClick = {})
        }
    }
}
