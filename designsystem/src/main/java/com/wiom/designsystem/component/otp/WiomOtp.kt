package com.wiom.designsystem.component.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.wiom.designsystem.component.input.WiomInputStatus
import com.wiom.designsystem.theme.WiomTheme

/**
 * Wiom OTP — boxed verification-code field.
 *
 * One BasicTextField receives input; the visual is N (4 or 6) boxes laid out via
 * `weight(1f)` so the row owns its width and the boxes scale with the parent.
 *
 * Anatomy (per skill §2):
 *  1. `title` — `type.labelLg`, optional.
 *  2. Boxes row — `space.sm` (8 dp) gap, each box `radius.medium` + `space.md` H/V padding.
 *  3. Helper row — helper on the left (weight 1f), optional `timer` on the right.
 *
 * The active digit's box uses `stroke.medium` `stroke.brandFocus`; resting boxes use
 * `stroke.small` `stroke.subtle`. Status (Error / Success) overrides the resting border.
 *
 * Use this for any verification-code entry. Do **not** fake an OTP with a single
 * [com.wiom.designsystem.component.input.WiomInput] — per skill §10 anti-pattern.
 *
 * @param value Current digits — caller is responsible for clamping to digits-only.
 * @param onValueChange Receives the new value already filtered to digits and
 *   clamped to [length].
 * @param title Field label. Pass `null` to hide when a parent section already labels it.
 * @param length 4 (default) or 6. Other counts work but render off-spec.
 * @param helper Helper / resend prompt (e.g., `"Sent to +91 98765 43210"`).
 * @param timer Right-aligned timer text (e.g., `"00:24"`). Skill notes the Focused
 *   variant ships with a hardcoded timer — pass it from the caller's countdown state.
 * @param status [WiomInputStatus.None] (rest) / `Error` / `Success`. Skill omits
 *   `Warning` and `ReadOnly` for OTP — passing them renders the same as `None`.
 * @param enabled Disabled fields use `bg.disabled` + `text.disabled`.
 * @param modifier Modifier; callers typically `fillMaxWidth()`.
 */
@Composable
fun WiomOtp(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = "Enter OTP",
    length: Int = 4,
    helper: String? = null,
    timer: String? = null,
    status: WiomInputStatus = WiomInputStatus.None,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val clamped = value.filter(Char::isDigit).take(length)

    val titleColor: Color = when {
        !enabled -> WiomTheme.color.text.disabled
        clamped.isNotEmpty() || isFocused -> WiomTheme.color.text.subtle
        else -> WiomTheme.color.text.default
    }
    val digitColor: Color = if (enabled) WiomTheme.color.text.default else WiomTheme.color.text.disabled
    val helperColor: Color = when (status) {
        WiomInputStatus.Error -> WiomTheme.color.text.critical
        WiomInputStatus.Success -> WiomTheme.color.text.positive
        WiomInputStatus.Warning -> WiomTheme.color.text.onWarning
        WiomInputStatus.None -> if (enabled) WiomTheme.color.text.subtle else WiomTheme.color.text.disabled
    }

    val containerFill: Color = if (enabled) WiomTheme.color.bg.default else WiomTheme.color.bg.disabled

    val restingWidth: Dp
    val restingColor: Color
    when {
        !enabled -> {
            restingWidth = WiomTheme.stroke.small
            restingColor = WiomTheme.color.stroke.subtle
        }
        status == WiomInputStatus.Error -> {
            restingWidth = WiomTheme.stroke.medium
            restingColor = WiomTheme.color.stroke.criticalFocus
        }
        status == WiomInputStatus.Success -> {
            restingWidth = WiomTheme.stroke.medium
            restingColor = WiomTheme.color.stroke.positiveFocus
        }
        else -> {
            restingWidth = WiomTheme.stroke.small
            restingColor = WiomTheme.color.stroke.subtle
        }
    }
    val activeColor: Color = WiomTheme.color.stroke.brandFocus
    val boxShape = RoundedCornerShape(WiomTheme.radius.medium)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
    ) {
        if (!title.isNullOrEmpty()) {
            Text(text = title, style = WiomTheme.type.labelLg, color = titleColor)
        }

        BasicTextField(
            value = clamped,
            onValueChange = { onValueChange(it.filter(Char::isDigit).take(length)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            interactionSource = interactionSource,
            cursorBrush = SolidColor(Color.Transparent),
            textStyle = WiomTheme.type.labelLg.copy(color = Color.Transparent),
            decorationBox = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
                ) {
                    repeat(length) { index ->
                        val digit = clamped.getOrNull(index)?.toString().orEmpty()
                        val isActive = enabled && isFocused &&
                            index == clamped.length.coerceAtMost(length - 1)
                        val borderWidth = if (isActive) WiomTheme.stroke.medium else restingWidth
                        val borderColor = if (isActive) activeColor else restingColor
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(containerFill, boxShape)
                                .border(borderWidth, borderColor, boxShape)
                                .padding(
                                    horizontal = WiomTheme.spacing.md,
                                    vertical = WiomTheme.spacing.md,
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = digit,
                                style = WiomTheme.type.labelLg,
                                color = digitColor,
                            )
                        }
                    }
                }
            },
        )

        if (!helper.isNullOrEmpty() || !timer.isNullOrEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md),
            ) {
                if (!helper.isNullOrEmpty()) {
                    Text(
                        text = helper,
                        style = WiomTheme.type.bodyMd,
                        color = helperColor,
                        modifier = Modifier.weight(1f),
                    )
                } else {
                    Box(modifier = Modifier.weight(1f))
                }
                if (!timer.isNullOrEmpty()) {
                    Text(
                        text = timer,
                        style = WiomTheme.type.bodyMd,
                        color = if (enabled) WiomTheme.color.text.subtle else WiomTheme.color.text.disabled,
                    )
                }
            }
        }
    }
}

// region Previews ------------------------------------------------------------------------------

@Preview(name = "OTP · 4-digit Default", showBackground = true)
@Composable
private fun PreviewOtp4Default() {
    WiomTheme {
        WiomOtp(
            value = "",
            onValueChange = {},
            helper = "Sent to +91 98765 43210",
        )
    }
}

@Preview(name = "OTP · 6-digit Filled", showBackground = true)
@Composable
private fun PreviewOtp6Filled() {
    WiomTheme {
        WiomOtp(
            value = "483921",
            onValueChange = {},
            length = 6,
            helper = "Sent to +91 98765 43210",
        )
    }
}

@Preview(name = "OTP · Error", showBackground = true)
@Composable
private fun PreviewOtpError() {
    WiomTheme {
        WiomOtp(
            value = "9999",
            onValueChange = {},
            helper = "Code didn't match. Try again.",
            status = WiomInputStatus.Error,
        )
    }
}

@Preview(name = "OTP · Success", showBackground = true)
@Composable
private fun PreviewOtpSuccess() {
    WiomTheme {
        WiomOtp(
            value = "1234",
            onValueChange = {},
            helper = "Verified",
            status = WiomInputStatus.Success,
        )
    }
}

@Preview(name = "OTP · Disabled", showBackground = true)
@Composable
private fun PreviewOtpDisabled() {
    WiomTheme {
        WiomOtp(
            value = "1234",
            onValueChange = {},
            helper = "Resend disabled",
            enabled = false,
        )
    }
}

@Preview(name = "OTP · Focused with timer", showBackground = true)
@Composable
private fun PreviewOtpFocusedTimer() {
    WiomTheme {
        var v by remember { mutableStateOf("12") }
        WiomOtp(
            value = v,
            onValueChange = { v = it },
            length = 6,
            helper = "Sent to +91 98765 43210",
            timer = "00:24",
        )
    }
}

// endregion
