package com.wiom.designsystem.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Celebration
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.wiom.designsystem.component.button.WiomButton
import com.wiom.designsystem.component.button.WiomButtonType
import com.wiom.designsystem.component.listitem.WiomListItemRadio
import com.wiom.designsystem.component.loader.WiomSpinner
import com.wiom.designsystem.component.loader.WiomSpinnerSize
import com.wiom.designsystem.component.loader.WiomSpinnerTone
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.theme.WiomTheme

/**
 * A single action button inside a Wiom dialog.
 *
 * Maps 1:1 to `WiomButton(text = label, onClick = onClick, type = type)`. Helpers
 * pass `WiomDialogAction` instances to produce full-width or side-by-side buttons
 * inside the dialog's action area, depending on [WiomDialogButtonsLayout].
 */
data class WiomDialogAction(
    val label: String,
    val onClick: () -> Unit,
    val type: WiomButtonType = WiomButtonType.Primary,
)

/**
 * Layout for the dialog's action group.
 *
 * - [Stacked] — confirm on top, dismiss below; each CTA fills the row. Default and
 *   safer choice — works for any label length.
 * - [SideBySide] — dismiss left, confirm right; each CTA `Modifier.weight(1f)`.
 *   Only safe when both labels are short (≈ 14 chars Hindi / 18 chars English) —
 *   otherwise truncation. The 264 dp content width splits to ≈ 126 dp per cell
 *   with the 12 dp inter-CTA gap.
 */
enum class WiomDialogButtonsLayout { Stacked, SideBySide }

/**
 * Low-level Wiom dialog shell.
 *
 * Wraps Compose's [Dialog] with the Wiom dialog surface: `bg.default`,
 * `radius.xlarge`, `shadow.xl`, paired with `overlay.scrim` (Dialog supplies the
 * scrim automatically). Internal padding is `space.xl` on all sides.
 *
 * Prefer the typed helpers ([WiomAlertDialog], [WiomInputDialog],
 * [WiomSelectionDialog], [WiomIllustrationDialog], [WiomLoadingDialog]) over
 * calling this directly — they encode the per-variant anatomy from the V2 skill.
 */
@Composable
fun WiomDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    dismissOnBackPress: Boolean = true,
    // V2 §3 — scrim tap never dismisses, users always exit via an explicit CTA or system back.
    dismissOnClickOutside: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = WiomTheme.color
    val radius = WiomTheme.radius
    val spacing = WiomTheme.spacing
    val shadow = WiomTheme.shadow

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
            usePlatformDefaultWidth = false,
        ),
    ) {
        Surface(
            modifier = modifier
                .width(312.dp),
            shape = RoundedCornerShape(radius.xlarge),
            color = colors.bg.default,
            contentColor = colors.text.default,
            shadowElevation = shadow.xl,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.xl),
                content = content,
            )
        }
    }
}

/**
 * Alert dialog — single-thought confirmation with an optional leading icon-badge.
 *
 * Layout is left-aligned; title is `type.headingMd` · `text.default`; body is
 * `type.bodyLg` · `text.subtle`. Primary action sits on top; optional secondary
 * action sits below, stacked full-width with `space.md` gap.
 */
@Composable
fun WiomAlertDialog(
    title: String,
    body: String,
    primaryAction: WiomDialogAction,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    secondaryAction: WiomDialogAction? = null,
    icon: ImageVector? = null,
    buttonsLayout: WiomDialogButtonsLayout = WiomDialogButtonsLayout.Stacked,
) {
    val colors = WiomTheme.color
    val type = WiomTheme.type
    val spacing = WiomTheme.spacing
    val radius = WiomTheme.radius

    WiomDialog(onDismissRequest = onDismiss, modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(spacing.lg)) {
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = colors.bg.brandSubtle,
                            shape = RoundedCornerShape(radius.medium),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    WiomIcon(
                        imageVector = icon,
                        contentDescription = null,
                        size = WiomTheme.iconSize.md,
                        tint = colors.icon.brand,
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                Text(
                    text = title,
                    style = type.headingMd,
                    color = colors.text.default,
                )
                Text(
                    text = body,
                    style = type.bodyMd,
                    color = colors.text.subtle,
                )
            }
        }
        // V2 §2 — main → buttons gap is `space-48` for ALL 5 types.
        Spacer(modifier = Modifier.height(spacing.huge))
        DialogActions(primary = primaryAction, secondary = secondaryAction, layout = buttonsLayout)
    }
}

/**
 * Input dialog — capture a single value inline.
 *
 * Title is `type.headingMd`; body is `type.bodyLg` · `text.subtle`. The [content]
 * slot is where the caller places a `WiomInput` (or equivalent single-field
 * capture). Only ONE input field — multi-field flows belong on a dedicated screen
 * or a `WiomBottomSheet` Form.
 */
@Composable
fun WiomInputDialog(
    title: String,
    primaryAction: WiomDialogAction,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    body: String? = null,
    secondaryAction: WiomDialogAction? = null,
    buttonsLayout: WiomDialogButtonsLayout = WiomDialogButtonsLayout.Stacked,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = WiomTheme.color
    val type = WiomTheme.type
    val spacing = WiomTheme.spacing

    WiomDialog(onDismissRequest = onDismiss, modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(spacing.lg)) {
            Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                Text(
                    text = title,
                    style = type.headingMd,
                    color = colors.text.default,
                )
                if (body != null) {
                    Text(
                        text = body,
                        style = type.bodyMd,
                        color = colors.text.subtle,
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacing.md),
                content = content,
            )
        }
        Spacer(modifier = Modifier.height(spacing.huge))
        DialogActions(primary = primaryAction, secondary = secondaryAction, layout = buttonsLayout)
    }
}

/**
 * Selection dialog — pick one of up to 4 short options with a radio control.
 *
 * For > 4 options or any search / scroll requirement, use `WiomBottomSheet`
 * picker patterns instead — a scrollable area inside a dialog is an anti-pattern.
 */
@Composable
fun WiomSelectionDialog(
    title: String,
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    primaryAction: WiomDialogAction,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    body: String? = null,
    secondaryAction: WiomDialogAction? = null,
    buttonsLayout: WiomDialogButtonsLayout = WiomDialogButtonsLayout.Stacked,
) {
    require(options.size in 1..4) {
        "Selection dialog supports 1..4 options; for longer lists use a wiom-bottomsheet picker"
    }
    val colors = WiomTheme.color
    val type = WiomTheme.type
    val spacing = WiomTheme.spacing

    WiomDialog(onDismissRequest = onDismiss, modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(spacing.lg)) {
            Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                Text(
                    text = title,
                    style = type.headingMd,
                    color = colors.text.default,
                )
                if (body != null) {
                    Text(
                        text = body,
                        style = type.bodyMd,
                        color = colors.text.subtle,
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
                options.forEachIndexed { index, option ->
                    WiomListItemRadio(
                        primary = option,
                        radioSelected = index == selectedIndex,
                        onRadioSelect = { onSelect(index) },
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(spacing.huge))
        DialogActions(primary = primaryAction, secondary = secondaryAction, layout = buttonsLayout)
    }
}

/**
 * Illustration dialog — celebratory or emphasized single-screen moment.
 *
 * Uses the same illustration anatomy as the bottom sheet's illustration block —
 * a 120dp `bg.brandSubtle` circle with a 48dp `icon.brand` glyph — but centered
 * inside the dialog's 312dp surface. Heading and subtext are centered.
 */
@Composable
fun WiomIllustrationDialog(
    icon: ImageVector,
    heading: String,
    subtext: String,
    primaryAction: WiomDialogAction,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    secondaryAction: WiomDialogAction? = null,
    buttonsLayout: WiomDialogButtonsLayout = WiomDialogButtonsLayout.Stacked,
) {
    val colors = WiomTheme.color
    val type = WiomTheme.type
    val spacing = WiomTheme.spacing
    val radius = WiomTheme.radius

    WiomDialog(onDismissRequest = onDismiss, modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.lg),
        ) {
            // V2 §2: full-width 144 dp container with `radius.large` (16 dp), `bg.brand.subtle`,
            // 48 dp icon centered. NOT a 120 dp circle — that anatomy lives on the bottom sheet.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(144.dp)
                    .background(
                        color = colors.bg.brandSubtle,
                        shape = RoundedCornerShape(radius.large),
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
                    style = type.headingMd,
                    color = colors.text.default,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = subtext,
                    style = type.bodyMd,
                    color = colors.text.subtle,
                    textAlign = TextAlign.Center,
                )
            }
        }
        Spacer(modifier = Modifier.height(spacing.huge))
        DialogActions(primary = primaryAction, secondary = secondaryAction, layout = buttonsLayout)
    }
}

/**
 * Loading dialog — blocking foreground loading. Non-dismissible by design:
 * tap-outside and back-press are disabled so the user can't trap the app in an
 * in-flight state accidentally. Error handling must route to an Alert dialog.
 *
 * Spinner tinted `bg.brand`; optional message below is centered
 * `type.bodyLg` · `text.subtle`.
 */
@Composable
fun WiomLoadingDialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String? = null,
) {
    val colors = WiomTheme.color
    val type = WiomTheme.type
    val spacing = WiomTheme.spacing

    WiomDialog(
        onDismissRequest = { /* no-op: loading dialog is non-dismissible */ },
        modifier = modifier,
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.lg),
        ) {
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center,
            ) {
                WiomSpinner(
                    size = WiomSpinnerSize.Sm,
                    tone = WiomSpinnerTone.Brand,
                )
            }
            if (title != null || message != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(spacing.sm),
                ) {
                    if (title != null) {
                        Text(
                            text = title,
                            style = type.headingMd,
                            color = colors.text.default,
                            textAlign = TextAlign.Center,
                        )
                    }
                    if (message != null) {
                        Text(
                            text = message,
                            style = type.bodyMd,
                            color = colors.text.subtle,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}

// region Internals

/**
 * Action group for a Wiom dialog. Stacked (default) or SideBySide per [layout].
 * 12 dp inter-CTA gap either way; max 2 CTAs.
 */
@Composable
private fun DialogActions(
    primary: WiomDialogAction,
    secondary: WiomDialogAction?,
    layout: WiomDialogButtonsLayout,
) {
    val spacing = WiomTheme.spacing
    when (layout) {
        WiomDialogButtonsLayout.Stacked -> Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(spacing.md),
        ) {
            WiomButton(
                text = primary.label,
                onClick = primary.onClick,
                modifier = Modifier.fillMaxWidth(),
                type = primary.type,
            )
            if (secondary != null) {
                WiomButton(
                    text = secondary.label,
                    onClick = secondary.onClick,
                    modifier = Modifier.fillMaxWidth(),
                    type = secondary.type,
                )
            }
        }
        WiomDialogButtonsLayout.SideBySide -> Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Wiom button-group convention: dismiss on the left, confirm on the right.
            if (secondary != null) {
                WiomButton(
                    text = secondary.label,
                    onClick = secondary.onClick,
                    modifier = Modifier.weight(1f),
                    type = secondary.type,
                )
            }
            WiomButton(
                text = primary.label,
                onClick = primary.onClick,
                modifier = Modifier.weight(1f),
                type = primary.type,
            )
        }
    }
}

// endregion

// region Previews

@Preview(name = "Alert", showBackground = true)
@Composable
private fun PreviewAlert() {
    WiomTheme {
        Column(modifier = Modifier.padding(WiomTheme.spacing.xl)) {
            WiomAlertDialog(
                title = "Payment successful",
                body = "Your recharge of Rs 499 has been credited to your account.",
                primaryAction = WiomDialogAction("Done", onClick = {}, type = WiomButtonType.Primary),
                secondaryAction = WiomDialogAction("View receipt", onClick = {}, type = WiomButtonType.Tertiary),
                onDismiss = {},
                icon = Icons.Rounded.CheckCircle,
            )
        }
    }
}

@Preview(name = "Alert · destructive", showBackground = true)
@Composable
private fun PreviewAlertDestructive() {
    WiomTheme {
        Column(modifier = Modifier.padding(WiomTheme.spacing.xl)) {
            WiomAlertDialog(
                title = "Delete this plan?",
                body = "This will permanently remove your plan. This action can't be undone.",
                primaryAction = WiomDialogAction("Delete", onClick = {}, type = WiomButtonType.Destructive),
                secondaryAction = WiomDialogAction("Cancel", onClick = {}, type = WiomButtonType.Tertiary),
                onDismiss = {},
            )
        }
    }
}

@Preview(name = "Input", showBackground = true)
@Composable
private fun PreviewInput() {
    WiomTheme {
        Column(modifier = Modifier.padding(WiomTheme.spacing.xl)) {
            WiomInputDialog(
                title = "Enter mobile number",
                body = "We'll send a verification code to this number.",
                primaryAction = WiomDialogAction("Continue", onClick = {}, type = WiomButtonType.Primary),
                secondaryAction = WiomDialogAction("Cancel", onClick = {}, type = WiomButtonType.Tertiary),
                onDismiss = {},
            ) {
                // In real usage callers insert a WiomInput here.
                Text(
                    text = "[ WiomInput slot ]",
                    style = WiomTheme.type.bodyLg,
                    color = WiomTheme.color.text.subtle,
                )
            }
        }
    }
}

@Preview(name = "Selection", showBackground = true)
@Composable
private fun PreviewSelection() {
    WiomTheme {
        val selected = remember { mutableStateOf(1) }
        Column(modifier = Modifier.padding(WiomTheme.spacing.xl)) {
            WiomSelectionDialog(
                title = "Choose your language",
                body = "You can change this anytime from Settings.",
                options = listOf("English", "हिन्दी", "मराठी", "தமிழ்"),
                selectedIndex = selected.value,
                onSelect = { selected.value = it },
                primaryAction = WiomDialogAction("Confirm", onClick = {}, type = WiomButtonType.Primary),
                secondaryAction = WiomDialogAction("Cancel", onClick = {}, type = WiomButtonType.Tertiary),
                onDismiss = {},
            )
        }
    }
}

@Preview(name = "Illustration", showBackground = true)
@Composable
private fun PreviewIllustration() {
    WiomTheme {
        Column(modifier = Modifier.padding(WiomTheme.spacing.xl)) {
            WiomIllustrationDialog(
                icon = Icons.Rounded.Celebration,
                heading = "You saved Rs 150 this month!",
                subtext = "Keep recharging on time to unlock more savings.",
                primaryAction = WiomDialogAction("View savings", onClick = {}, type = WiomButtonType.Primary),
                secondaryAction = WiomDialogAction("Dismiss", onClick = {}, type = WiomButtonType.Tertiary),
                onDismiss = {},
            )
        }
    }
}

@Preview(name = "Loading", showBackground = true)
@Composable
private fun PreviewLoading() {
    WiomTheme {
        Column(modifier = Modifier.padding(WiomTheme.spacing.xl)) {
            WiomLoadingDialog(
                title = "Processing payment",
                message = "Please wait while we confirm your transaction.",
            )
        }
    }
}

// endregion
