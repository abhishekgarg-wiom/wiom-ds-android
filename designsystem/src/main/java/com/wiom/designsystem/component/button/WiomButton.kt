package com.wiom.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.theme.WiomTheme

/**
 * Wiom CTA types.
 *
 * See `.skills-cache/wiom-cta.md` for decision rules. Max one [Primary] and one
 * [Destructive] per screen; never pair Primary + Destructive (Destructive replaces
 * Primary in that group). [PreBooking] is customer-app only.
 */
enum class WiomButtonType {
    Primary,
    Secondary,
    Tertiary,
    Destructive,
    PreBooking,
}

/**
 * Wiom CTA states.
 *
 * [Default] / [Disabled] / [Loading] are driven by caller state. Pressed is NOT a
 * parameter here — it is derived automatically from `InteractionSource`. Keyboard
 * focus is surfaced via an outer `stroke.brandFocus` / `stroke.criticalFocus` ring,
 * not as a separate visual fill variant (per skill § "States deliberately NOT in V2").
 */
enum class WiomButtonState {
    Default,
    Disabled,
    Loading,
}

/**
 * Wiom CTA button.
 *
 * Intrinsic height — DO NOT set `.height(...)`. The button hugs to 48dp via
 * `space.md` (12dp) vertical padding + `labelLg` 24sp line-height.
 *
 * ```
 * WiomButton(
 *     text = "Recharge now",
 *     type = WiomButtonType.Primary,
 *     onClick = { },
 * )
 * ```
 *
 * @param text Label — must NOT wrap / truncate / shrink. Rewrite shorter if it overflows.
 * @param type One of [WiomButtonType].
 * @param modifier Modifier; callers set width via `.weight(1f)` inside a Row or
 *   `.fillMaxWidth()` inside a Column. Never `.height(...)`.
 * @param state Default / Disabled / Loading. Loading hides text + icon and shows a spinner.
 * @param leadingIcon Optional leading icon (20dp). Hidden in Loading.
 * @param trailingIcon Optional trailing icon (20dp). Hidden in Loading.
 * @param onClick Click handler. No-op when [state] is Disabled or Loading.
 */
@Composable
fun WiomButton(
    text: String,
    type: WiomButtonType,
    modifier: Modifier = Modifier,
    state: WiomButtonState = WiomButtonState.Default,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val enabled = state == WiomButtonState.Default

    val visuals = buttonVisuals(type = type, state = state, pressed = isPressed)
    val shape = RoundedCornerShape(WiomTheme.radius.large)

    // Shadow XOR border — all CTAs are flat (`shadow.none`). Only Secondary has a border.
    var container = modifier
        .defaultMinSize(minHeight = 48.dp) // touch target, not a fixed height
        .clip(shape)
        .background(color = visuals.fill, shape = shape)

    if (visuals.borderColor != null) {
        container = container.border(
            width = WiomTheme.stroke.medium,
            color = visuals.borderColor,
            shape = shape,
        )
    }

    container = container.clickable(
        interactionSource = interactionSource,
        indication = null,
        enabled = enabled,
        role = Role.Button,
        onClick = onClick,
    )

    Box(modifier = container) {
        Row(
            modifier = Modifier.padding(horizontal = WiomTheme.spacing.lg, vertical = WiomTheme.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val contentAlpha = if (state == WiomButtonState.Loading) 0f else 1f
            if (leadingIcon != null) {
                WiomIcon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = visuals.iconTint,
                    size = WiomTheme.iconSize.sm,
                    modifier = Modifier.alpha(contentAlpha),
                )
            }
            Text(
                text = text,
                style = WiomTheme.type.labelLg,
                color = visuals.labelColor,
                maxLines = 1,
                modifier = Modifier.alpha(contentAlpha),
            )
            if (trailingIcon != null) {
                WiomIcon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    tint = visuals.iconTint,
                    size = WiomTheme.iconSize.sm,
                    modifier = Modifier.alpha(contentAlpha),
                )
            }
        }

        if (state == WiomButtonState.Loading) {
            CircularProgressIndicator(
                color = visuals.iconTint,
                strokeWidth = WiomTheme.stroke.medium,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(WiomTheme.iconSize.sm),
            )
        }
    }
}

/**
 * 1st-person consent row paired with a Primary CTA.
 *
 * Per skill § 9. Acknowledge use cases — use 1st-person Hindi/English copy
 * ("मैंने…" / "I've…"), never 3rd person ("कस्टमर ने…"). Never pre-checked.
 *
 * The paired Primary [WiomButton] must stay disabled until [checked] is true;
 * callers wire that via shared state. This composable is only the checkbox +
 * label row; stack it above a `WiomButton(type = Primary, state = if (checked) Default else Disabled)`.
 *
 * @param text 1st-person verification copy.
 * @param checked Whether the user has acknowledged.
 * @param onCheckedChange Toggle handler.
 * @param modifier Modifier; callers typically `fillMaxWidth()`.
 * @param enabled When false the whole row is dimmed and non-interactive.
 */
@Composable
fun WiomAcknowledge(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val rowAlpha = if (enabled) 1f else 0.4f
    Row(
        modifier = modifier
            .alpha(rowAlpha)
            .clickable(enabled = enabled, role = Role.Checkbox) { onCheckedChange(!checked) }
            .padding(vertical = WiomTheme.spacing.xs),
        horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AcknowledgeIndicator(checked = checked)
        Text(
            text = text,
            style = WiomTheme.type.bodyMd,
            color = if (enabled) WiomTheme.color.text.default else WiomTheme.color.text.disabled,
        )
    }
}

// ---- internals --------------------------------------------------------------

private data class ButtonVisuals(
    val fill: Color,
    val labelColor: Color,
    val iconTint: Color,
    val borderColor: Color?,
)

@Composable
private fun buttonVisuals(
    type: WiomButtonType,
    state: WiomButtonState,
    pressed: Boolean,
): ButtonVisuals {
    val c = WiomTheme.color
    return when (type) {
        WiomButtonType.Primary -> when (state) {
            WiomButtonState.Default, WiomButtonState.Loading -> ButtonVisuals(
                fill = if (pressed && state == WiomButtonState.Default) c.bg.brandPressed else c.bg.brand,
                labelColor = c.text.onBrand,
                iconTint = c.icon.inverse,
                borderColor = null,
            )
            WiomButtonState.Disabled -> ButtonVisuals(
                fill = c.bg.disabled,
                labelColor = c.text.disabled,
                iconTint = c.icon.disabled,
                borderColor = null,
            )
        }
        WiomButtonType.Secondary -> when (state) {
            WiomButtonState.Default, WiomButtonState.Loading -> ButtonVisuals(
                fill = if (pressed && state == WiomButtonState.Default) c.bg.brandSubtle else Color.Transparent,
                labelColor = c.text.brand,
                iconTint = c.icon.brand,
                borderColor = c.stroke.brandFocus,
            )
            WiomButtonState.Disabled -> ButtonVisuals(
                fill = Color.Transparent,
                labelColor = c.text.disabled,
                iconTint = c.icon.disabled,
                borderColor = c.stroke.subtle,
            )
        }
        WiomButtonType.Tertiary -> when (state) {
            WiomButtonState.Default, WiomButtonState.Loading -> ButtonVisuals(
                fill = if (pressed && state == WiomButtonState.Default) c.bg.brandSubtle else Color.Transparent,
                labelColor = c.text.brand,
                iconTint = c.icon.brand,
                borderColor = null,
            )
            WiomButtonState.Disabled -> ButtonVisuals(
                fill = Color.Transparent,
                labelColor = c.text.disabled,
                iconTint = c.icon.disabled,
                borderColor = null,
            )
        }
        WiomButtonType.Destructive -> when (state) {
            WiomButtonState.Default, WiomButtonState.Loading -> ButtonVisuals(
                fill = if (pressed && state == WiomButtonState.Default) c.bg.criticalPressed else c.bg.critical,
                labelColor = c.text.onCritical,
                iconTint = c.icon.inverse,
                borderColor = null,
            )
            WiomButtonState.Disabled -> ButtonVisuals(
                fill = c.bg.disabled,
                labelColor = c.text.disabled,
                iconTint = c.icon.disabled,
                borderColor = null,
            )
        }
        WiomButtonType.PreBooking -> when (state) {
            WiomButtonState.Default, WiomButtonState.Loading -> ButtonVisuals(
                fill = if (pressed && state == WiomButtonState.Default) c.bg.brandAccentPressed else c.bg.brandAccent,
                labelColor = c.text.onBrandAccent,
                iconTint = c.icon.action,
                borderColor = null,
            )
            WiomButtonState.Disabled -> ButtonVisuals(
                fill = c.bg.disabled,
                labelColor = c.text.disabled,
                iconTint = c.icon.disabled,
                borderColor = null,
            )
        }
    }
}

@Composable
private fun AcknowledgeIndicator(checked: Boolean) {
    val shape = RoundedCornerShape(WiomTheme.radius.tiny)
    val size = 20.dp
    if (checked) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(shape)
                .background(WiomTheme.color.bg.brand, shape),
            contentAlignment = Alignment.Center,
        ) {
            WiomIcon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = WiomTheme.color.icon.inverse,
                size = 14.dp, // ~65% of 20dp indicator per skill
            )
        }
    } else {
        Box(
            modifier = Modifier
                .size(size)
                .clip(shape)
                .border(WiomTheme.stroke.medium, WiomTheme.color.stroke.strong, shape),
        )
    }
}

// ---- Previews ---------------------------------------------------------------

@Preview(name = "Primary — Default", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewPrimaryDefault() {
    WiomTheme {
        WiomButton(
            text = "Recharge now",
            type = WiomButtonType.Primary,
            leadingIcon = Icons.Rounded.Check,
            onClick = { },
        )
    }
}

@Preview(name = "Primary — Disabled", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewPrimaryDisabled() {
    WiomTheme {
        WiomButton(
            text = "Recharge now",
            type = WiomButtonType.Primary,
            state = WiomButtonState.Disabled,
            leadingIcon = Icons.Rounded.Check,
            onClick = { },
        )
    }
}

@Preview(name = "Primary — Loading", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewPrimaryLoading() {
    WiomTheme {
        WiomButton(
            text = "Recharge now",
            type = WiomButtonType.Primary,
            state = WiomButtonState.Loading,
            onClick = { },
        )
    }
}

@Preview(name = "Secondary — Default", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewSecondaryDefault() {
    WiomTheme {
        WiomButton(
            text = "Change plan",
            type = WiomButtonType.Secondary,
            trailingIcon = Icons.Rounded.ArrowForward,
            onClick = { },
        )
    }
}

@Preview(name = "Secondary — Disabled", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewSecondaryDisabled() {
    WiomTheme {
        WiomButton(
            text = "Change plan",
            type = WiomButtonType.Secondary,
            state = WiomButtonState.Disabled,
            onClick = { },
        )
    }
}

@Preview(name = "Secondary — Loading", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewSecondaryLoading() {
    WiomTheme {
        WiomButton(
            text = "Change plan",
            type = WiomButtonType.Secondary,
            state = WiomButtonState.Loading,
            onClick = { },
        )
    }
}

@Preview(name = "Tertiary — Default", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewTertiaryDefault() {
    WiomTheme {
        WiomButton(
            text = "Maybe later",
            type = WiomButtonType.Tertiary,
            onClick = { },
        )
    }
}

@Preview(name = "Tertiary — Disabled", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewTertiaryDisabled() {
    WiomTheme {
        WiomButton(
            text = "Maybe later",
            type = WiomButtonType.Tertiary,
            state = WiomButtonState.Disabled,
            onClick = { },
        )
    }
}

@Preview(name = "Tertiary — Loading", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewTertiaryLoading() {
    WiomTheme {
        WiomButton(
            text = "Maybe later",
            type = WiomButtonType.Tertiary,
            state = WiomButtonState.Loading,
            onClick = { },
        )
    }
}

@Preview(name = "Destructive — Default", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewDestructiveDefault() {
    WiomTheme {
        WiomButton(
            text = "Delete plan",
            type = WiomButtonType.Destructive,
            leadingIcon = Icons.Rounded.Delete,
            onClick = { },
        )
    }
}

@Preview(name = "Destructive — Disabled", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewDestructiveDisabled() {
    WiomTheme {
        WiomButton(
            text = "Delete plan",
            type = WiomButtonType.Destructive,
            state = WiomButtonState.Disabled,
            onClick = { },
        )
    }
}

@Preview(name = "Destructive — Loading", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewDestructiveLoading() {
    WiomTheme {
        WiomButton(
            text = "Delete plan",
            type = WiomButtonType.Destructive,
            state = WiomButtonState.Loading,
            onClick = { },
        )
    }
}

@Preview(name = "PreBooking — Default", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewPreBookingDefault() {
    WiomTheme {
        WiomButton(
            text = "Pre-book now",
            type = WiomButtonType.PreBooking,
            leadingIcon = Icons.Rounded.Phone,
            onClick = { },
        )
    }
}

@Preview(name = "PreBooking — Disabled", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewPreBookingDisabled() {
    WiomTheme {
        WiomButton(
            text = "Pre-book now",
            type = WiomButtonType.PreBooking,
            state = WiomButtonState.Disabled,
            onClick = { },
        )
    }
}

@Preview(name = "PreBooking — Loading", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewPreBookingLoading() {
    WiomTheme {
        WiomButton(
            text = "Pre-book now",
            type = WiomButtonType.PreBooking,
            state = WiomButtonState.Loading,
            onClick = { },
        )
    }
}

@Preview(name = "Acknowledge — Unchecked + Primary disabled", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewAcknowledgeUnchecked() {
    WiomTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
            modifier = Modifier.padding(WiomTheme.spacing.lg),
        ) {
            WiomAcknowledge(
                text = "मैंने सभी जानकारी सही दी है",
                checked = false,
                onCheckedChange = { },
                modifier = Modifier.fillMaxWidth(),
            )
            WiomButton(
                text = "रिचार्ज करें",
                type = WiomButtonType.Primary,
                state = WiomButtonState.Disabled,
                modifier = Modifier.fillMaxWidth(),
                onClick = { },
            )
        }
    }
}

@Preview(name = "Acknowledge — Checked + Primary enabled", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewAcknowledgeChecked() {
    WiomTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
            modifier = Modifier.padding(WiomTheme.spacing.lg),
        ) {
            WiomAcknowledge(
                text = "मैंने सभी जानकारी सही दी है",
                checked = true,
                onCheckedChange = { },
                modifier = Modifier.fillMaxWidth(),
            )
            WiomButton(
                text = "रिचार्ज करें",
                type = WiomButtonType.Primary,
                modifier = Modifier.fillMaxWidth(),
                onClick = { },
            )
        }
    }
}

@Preview(name = "Acknowledge — Disabled", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewAcknowledgeDisabled() {
    WiomTheme {
        WiomAcknowledge(
            text = "मैंने सभी जानकारी सही दी है",
            checked = false,
            onCheckedChange = { },
            enabled = false,
            modifier = Modifier.fillMaxWidth().padding(WiomTheme.spacing.lg),
        )
    }
}
