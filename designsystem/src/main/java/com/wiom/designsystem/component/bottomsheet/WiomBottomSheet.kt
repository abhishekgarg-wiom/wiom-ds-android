package com.wiom.designsystem.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.theme.WiomTheme
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Payment
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.SupportAgent

/**
 * Wiom Bottom Sheet — Material3 [ModalBottomSheet] wrapped with Wiom tokens.
 *
 * Container: `bg.default`, top corners `radius.xlarge`, bottom `radius.none`.
 * Scrim: `overlay.scrim`. Drag handle: 32×4dp pill, `stroke.strong`, `radius.full`.
 *
 * The 8 content variants from the V2 skill (`Compact`, `Half`, `Expanded`, `Full`,
 * `Illustration`, `IllustrationCta`, `Share`, `IllustrationLeft`, `Form`) are modelled
 * as a [WiomBottomSheetSize] enum driving the sheet's height behavior. Content is
 * slot-based: callers compose the appropriate helpers ([WiomBottomSheetHeader],
 * [WiomBottomSheetListItem], [WiomBottomSheetIllustration], [WiomBottomSheetActions])
 * inside the `content` lambda to match the size's anatomy.
 *
 * Callers should pass `skipPartiallyExpanded = true` for [WiomBottomSheetSize.Full] or
 * when the sheet should always open expanded; default [SheetState] is fine for list
 * variants.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WiomBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    size: WiomBottomSheetSize = WiomBottomSheetSize.Compact,
    showHandle: Boolean = true,
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = size == WiomBottomSheetSize.Full,
    ),
    content: @Composable ColumnScope.() -> Unit,
) {
    val radius = WiomTheme.radius
    val colors = WiomTheme.color

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        containerColor = colors.bg.default,
        contentColor = colors.text.default,
        scrimColor = colors.overlay.scrim,
        shape = RoundedCornerShape(
            topStart = radius.xlarge,
            topEnd = radius.xlarge,
            bottomStart = radius.none,
            bottomEnd = radius.none,
        ),
        dragHandle = if (showHandle) {
            { WiomBottomSheetDragHandle() }
        } else {
            null
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(size.heightModifier())
                .navigationBarsPadding(),
            content = content,
        )
    }
}

/**
 * Size variant for [WiomBottomSheet]. Maps to the V2 skill's 8 size presets. The
 * enum controls the sheet's height behavior:
 *
 * - [Compact] / [Half] / [Expanded] / [Full] — list content; suggested min heights.
 * - [Illustration] / [IllustrationCta] / [IllustrationLeft] / [Share] / [Form] —
 *   wrap content (hug height).
 */
enum class WiomBottomSheetSize {
    Compact,
    Half,
    Expanded,
    Full,
    Illustration,
    IllustrationCta,
    IllustrationLeft,
    Share,
    Form;

    internal fun heightModifier(): Modifier = when (this) {
        // Heuristic min-heights from the V2 skill's Kotlin mapping. The sheet
        // can still expand beyond — ModalBottomSheet's SheetState drives actual
        // expansion — these just seed the minimum at open.
        Compact -> Modifier.heightIn(min = 240.dp)
        Half -> Modifier.heightIn(min = 400.dp)
        Expanded -> Modifier.heightIn(min = 600.dp)
        Full -> Modifier.fillMaxWidth()
        Illustration,
        IllustrationCta,
        IllustrationLeft,
        Share,
        Form,
        -> Modifier
    }
}

/** 32×4dp pill drag handle — `stroke.strong` fill, `radius.full`, `space.sm` vertical padding. */
@Composable
private fun WiomBottomSheetDragHandle(
    modifier: Modifier = Modifier,
) {
    val colors = WiomTheme.color
    val radius = WiomTheme.radius
    val spacing = WiomTheme.spacing
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = spacing.sm),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .width(32.dp)
                .height(4.dp)
                .background(
                    color = colors.stroke.strong,
                    shape = RoundedCornerShape(radius.full),
                ),
        )
    }
}

/**
 * Header row for a list-style bottom sheet.
 *
 * Title `type.headingLg` left-aligned; optional subtitle `type.bodyMd` · `text.subtle`
 * directly below. Padding: `space.xl` left, `space.lg` right, `space.xs` top,
 * `space.md` bottom. Divider below is `stroke.small` thick, `stroke.subtle` fill.
 */
@Composable
fun WiomBottomSheetHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    val colors = WiomTheme.color
    val type = WiomTheme.type
    val spacing = WiomTheme.spacing
    val stroke = WiomTheme.stroke

    Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = spacing.xl,
                    end = spacing.lg,
                    top = spacing.xs,
                    bottom = spacing.md,
                ),
            verticalArrangement = Arrangement.spacedBy(spacing.xs),
        ) {
            Text(
                text = title,
                style = type.headingLg,
                color = colors.text.default,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = type.bodyMd,
                    color = colors.text.subtle,
                )
            }
        }
        HorizontalDivider(
            thickness = stroke.small,
            color = colors.stroke.subtle,
        )
    }
}

/**
 * List row inside a bottom sheet.
 *
 * Built inline (NOT via `WiomListItem`) so it can honor the skill's 24dp
 * (`space.xl`) horizontal padding — sheet content lines up under the 24dp header
 * left inset. Icon lives in a 40dp `bg.brandSubtle` circle; label is
 * `type.labelLg`; chevron is `Icons.Rounded.ChevronRight` 20dp `icon.disabled`.
 */
@Composable
fun WiomBottomSheetListItem(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: ImageVector? = null,
) {
    val colors = WiomTheme.color
    val type = WiomTheme.type
    val spacing = WiomTheme.spacing
    val radius = WiomTheme.radius

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .defaultMinSize(minHeight = 48.dp)
            .padding(horizontal = spacing.xl, vertical = spacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        if (icon != null) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = colors.bg.brandSubtle,
                        shape = RoundedCornerShape(radius.full),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                WiomIcon(
                    imageVector = icon,
                    contentDescription = null,
                    size = WiomTheme.iconSize.sm,
                    tint = colors.icon.brand,
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(spacing.xxs),
        ) {
            Text(
                text = label,
                style = type.labelLg,
                color = colors.text.default,
            )
            if (description != null) {
                Text(
                    text = description,
                    style = type.bodyMd,
                    color = colors.text.subtle,
                )
            }
        }
        WiomIcon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            size = WiomTheme.iconSize.sm,
            tint = colors.icon.disabled,
        )
    }
}

/**
 * Centered illustration block for Illustration / IllustrationCta / Share variants.
 *
 * 120dp `bg.brandSubtle` circle with a 48dp `icon.brand` glyph centered. Heading is
 * `type.headingLg` centered; subtext is `type.bodyLg` · `text.subtle` centered.
 * Padding: `space.xl` horizontal, `space.lg` top, `space.xl` bottom.
 */
@Composable
fun WiomBottomSheetIllustration(
    icon: ImageVector,
    heading: String,
    subtext: String,
    modifier: Modifier = Modifier,
) {
    val colors = WiomTheme.color
    val type = WiomTheme.type
    val spacing = WiomTheme.spacing
    val radius = WiomTheme.radius

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = spacing.xl,
                end = spacing.xl,
                top = spacing.lg,
                bottom = spacing.xl,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.lg),
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    color = colors.bg.brandSubtle,
                    shape = RoundedCornerShape(radius.full),
                ),
            contentAlignment = Alignment.Center,
        ) {
            WiomIcon(
                imageVector = icon,
                contentDescription = null,
                size = WiomTheme.iconSize.lg,
                tint = colors.icon.brand,
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.sm),
        ) {
            Text(
                text = heading,
                style = type.headingLg,
                color = colors.text.default,
                textAlign = TextAlign.Center,
            )
            Text(
                text = subtext,
                style = type.bodyLg,
                color = colors.text.subtle,
                textAlign = TextAlign.Center,
            )
        }
    }
}

/**
 * Bottom action bar for a bottom sheet.
 *
 * Divider above (`stroke.small`, `stroke.subtle`). Row has `space.xl` horizontal,
 * `space.lg` vertical padding and a `space.md` gap between children. Callers place
 * 1–3 `WiomButton` instances via the [RowScope].
 */
@Composable
fun WiomBottomSheetActions(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = WiomTheme.color
    val spacing = WiomTheme.spacing
    val stroke = WiomTheme.stroke

    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider(
            thickness = stroke.small,
            color = colors.stroke.subtle,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.xl, vertical = spacing.lg),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing.md),
            content = content,
        )
    }
}

// region Previews — one per size variant. No real sheet state; we render the
// inner column so the spec (header + content anatomy) is verifiable.

@Composable
private fun PreviewFrame(content: @Composable ColumnScope.() -> Unit) {
    WiomTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(WiomTheme.color.bg.default),
            content = content,
        )
    }
}

@Preview(name = "Compact", showBackground = true)
@Composable
private fun PreviewCompact() {
    PreviewFrame {
        WiomBottomSheetHeader(title = "Policy options")
        WiomBottomSheetListItem(
            label = "View policy details",
            icon = Icons.Rounded.Description,
            onClick = {},
        )
        WiomBottomSheetListItem(
            label = "Contact support",
            icon = Icons.Rounded.SupportAgent,
            onClick = {},
        )
    }
}

@Preview(name = "Half", showBackground = true)
@Composable
private fun PreviewHalf() {
    PreviewFrame {
        WiomBottomSheetHeader(
            title = "Filter plans",
            subtitle = "3 filters applied",
        )
        WiomBottomSheetListItem(
            label = "Unlimited",
            icon = Icons.Rounded.Check,
            onClick = {},
        )
        WiomBottomSheetListItem(
            label = "Cheapest",
            icon = Icons.Rounded.Check,
            onClick = {},
        )
        WiomBottomSheetListItem(
            label = "Newest",
            icon = Icons.Rounded.Check,
            onClick = {},
        )
        WiomBottomSheetListItem(
            label = "Recommended",
            icon = Icons.Rounded.Check,
            onClick = {},
        )
    }
}

@Preview(name = "Expanded", showBackground = true)
@Composable
private fun PreviewExpanded() {
    PreviewFrame {
        WiomBottomSheetHeader(title = "Manage account")
        repeat(6) { index ->
            WiomBottomSheetListItem(
                label = "Action $index",
                description = "Short description",
                icon = Icons.Rounded.Edit,
                onClick = {},
            )
        }
    }
}

@Preview(name = "Full", showBackground = true)
@Composable
private fun PreviewFull() {
    PreviewFrame {
        WiomBottomSheetHeader(title = "Terms & conditions")
        repeat(8) { index ->
            WiomBottomSheetListItem(
                label = "Section $index",
                icon = Icons.Rounded.Description,
                onClick = {},
            )
        }
    }
}

@Preview(name = "Illustration", showBackground = true)
@Composable
private fun PreviewIllustration() {
    PreviewFrame {
        WiomBottomSheetIllustration(
            icon = Icons.Rounded.Check,
            heading = "Payment successful",
            subtext = "Rs 299 has been charged. Your plan is active until 15 May 2026.",
        )
    }
}

@Preview(name = "IllustrationCta", showBackground = true)
@Composable
private fun PreviewIllustrationCta() {
    PreviewFrame {
        WiomBottomSheetIllustration(
            icon = Icons.Rounded.Payment,
            heading = "Pay Rs 299?",
            subtext = "Monthly recharge for Plan A. Amount will be debited from your UPI account.",
        )
        WiomBottomSheetActions {
            // Caller places WiomButton instances here. Preview shows the frame.
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(name = "IllustrationLeft", showBackground = true)
@Composable
private fun PreviewIllustrationLeft() {
    PreviewFrame {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = WiomTheme.spacing.xl,
                    end = WiomTheme.spacing.xl,
                    top = WiomTheme.spacing.lg,
                    bottom = WiomTheme.spacing.xl,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.lg),
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = WiomTheme.color.bg.brandSubtle,
                        shape = RoundedCornerShape(WiomTheme.radius.full),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                WiomIcon(
                    imageVector = Icons.Rounded.Share,
                    contentDescription = null,
                    size = WiomTheme.iconSize.lg,
                    tint = WiomTheme.color.icon.brand,
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
            ) {
                Text(
                    text = "Share your plan",
                    style = WiomTheme.type.headingLg,
                    color = WiomTheme.color.text.default,
                )
                Text(
                    text = "Invite friends to Wiom with a one-tap share link.",
                    style = WiomTheme.type.bodyLg,
                    color = WiomTheme.color.text.subtle,
                )
            }
        }
    }
}

@Preview(name = "Share", showBackground = true)
@Composable
private fun PreviewShare() {
    PreviewFrame {
        WiomBottomSheetHeader(title = "Share your plan")
        WiomBottomSheetListItem(
            label = "Share via message",
            icon = Icons.Rounded.Share,
            onClick = {},
        )
        WiomBottomSheetListItem(
            label = "Download as PDF",
            icon = Icons.Rounded.Download,
            onClick = {},
        )
    }
}

@Preview(name = "Form", showBackground = true)
@Composable
private fun PreviewForm() {
    PreviewFrame {
        WiomBottomSheetHeader(
            title = "Edit display name",
            subtitle = "Visible on your Wiom profile",
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = WiomTheme.spacing.xl, vertical = WiomTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md),
        ) {
            // Caller inserts WiomInput here in real usage.
            Text(
                text = "[ WiomInput slot ]",
                style = WiomTheme.type.bodyLg,
                color = WiomTheme.color.text.subtle,
            )
        }
        WiomBottomSheetActions {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

// endregion
