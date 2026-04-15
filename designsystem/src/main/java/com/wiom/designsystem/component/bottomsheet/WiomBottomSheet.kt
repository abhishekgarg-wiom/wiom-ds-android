package com.wiom.designsystem.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.foundation.icon.WiomIcons
import com.wiom.designsystem.theme.WiomTheme

/**
 * Wiom modal bottom sheet. Surface anchored to screen bottom with scrim.
 *
 * Wrap Material3's [ModalBottomSheet] with Wiom tokens:
 *  - `surface.base` container, `radius.xlarge` top corners only
 *  - `color.overlay.default` scrim
 *  - 32×4dp `border.strong` drag handle (primary dismiss affordance)
 *
 * Compose content with [WiomBottomSheetHeader], [WiomBottomSheetListItem],
 * [WiomBottomSheetIllustration], and [WiomBottomSheetActions].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WiomBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = WiomTheme.colors.surface.base,
        scrimColor = WiomTheme.colors.overlay.default,
        shape = RoundedCornerShape(
            topStart = WiomTheme.radius.xlarge,
            topEnd = WiomTheme.radius.xlarge,
            bottomStart = WiomTheme.radius.none,
            bottomEnd = WiomTheme.radius.none,
        ),
        dragHandle = { WiomDragHandle() },
        modifier = modifier,
    ) {
        Column(modifier = Modifier.fillMaxWidth(), content = content)
    }
}

@Composable
private fun WiomDragHandle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = WiomTheme.spacing.sm),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(width = 32.dp, height = 4.dp)
                .clip(RoundedCornerShape(WiomTheme.radius.full))
                .background(WiomTheme.colors.border.strong)
        )
    }
}

/** Header row with left-aligned title + optional subtitle. Divider below. */
@Composable
fun WiomBottomSheetHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = WiomTheme.spacing.xl,
                end = WiomTheme.spacing.lg,
                top = WiomTheme.spacing.xs,
                bottom = WiomTheme.spacing.md,
            ),
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs),
    ) {
        Text(text = title, style = WiomTheme.type.headingMd, color = WiomTheme.colors.text.primary)
        subtitle?.let {
            Text(text = it, style = WiomTheme.type.bodySm, color = WiomTheme.colors.text.secondary)
        }
    }
    HorizontalDivider(
        thickness = WiomTheme.stroke.small,
        color = WiomTheme.colors.border.subtle,
        modifier = Modifier.fillMaxWidth(),
    )
}

/**
 * List item row tailored for a bottom sheet — icon in brand-tint circle + label + description + chevron.
 *
 * Built inline (not via [WiomListItem]) because bottom-sheet rows use **space.xl (24dp)** horizontal
 * padding per the wiom-bottomsheet skill, whereas normal list items use space.lg (16dp).
 */
@Composable
fun WiomBottomSheetListItem(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: Int? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = WiomTheme.spacing.xl, vertical = WiomTheme.spacing.md),
        horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon?.let { iconId ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(WiomTheme.colors.brand.primaryTint),
                contentAlignment = Alignment.Center,
            ) {
                WiomIcon(id = iconId, contentDescription = null, size = 20.dp, tint = WiomTheme.colors.brand.primary)
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs),
        ) {
            Text(text = label, style = WiomTheme.type.labelMd, color = WiomTheme.colors.text.primary)
            description?.let {
                Text(text = it, style = WiomTheme.type.bodySm, color = WiomTheme.colors.text.secondary)
            }
        }
        WiomIcon(
            id = WiomIcons.expandMore,
            contentDescription = null,
            size = 20.dp,
            tint = WiomTheme.colors.text.disabled,
        )
    }
}

/** Illustration layout — 120dp brand-tint circle with 48dp icon + centered heading + subtext. */
@Composable
fun WiomBottomSheetIllustration(
    icon: Int,
    heading: String,
    subtext: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = WiomTheme.spacing.xl,
                end = WiomTheme.spacing.xl,
                top = WiomTheme.spacing.lg,
                bottom = WiomTheme.spacing.xl,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(WiomTheme.colors.brand.primaryTint),
            contentAlignment = Alignment.Center,
        ) {
            WiomIcon(id = icon, contentDescription = null, size = WiomTheme.icon.lg, tint = WiomTheme.colors.brand.primary)
        }
        Text(
            text = heading,
            style = WiomTheme.type.headingMd,
            color = WiomTheme.colors.text.primary,
            textAlign = TextAlign.Center,
        )
        Text(
            text = subtext,
            style = WiomTheme.type.bodyMd,
            color = WiomTheme.colors.text.secondary,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * Action bar — row of primary/secondary buttons at the bottom of the sheet.
 * Divider above. Pass your buttons (or `WiomCta` when available) via [content].
 */
@Composable
fun WiomBottomSheetActions(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    HorizontalDivider(thickness = WiomTheme.stroke.small, color = WiomTheme.colors.border.subtle)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = WiomTheme.spacing.xl, vertical = WiomTheme.spacing.lg),
        horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
    }
}
