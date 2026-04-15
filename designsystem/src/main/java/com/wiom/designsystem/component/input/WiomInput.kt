package com.wiom.designsystem.component.input

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.foundation.icon.WiomIcons
import com.wiom.designsystem.theme.WiomTheme

enum class WiomFieldStatus { None, Error, Success, Warning }

/**
 * Wiom input — single-line form field.
 *
 * Anatomy: Title (above) → bordered container (leading? prefix? value/placeholder suffix? trailing?) → Helper + Counter (below).
 * Height is intrinsic (padding + body line-height). Never set a fixed height.
 *
 * Status icons (Error/Success/Warning) auto-populate the trailing slot and override [trailingIcon].
 * Prefix/Suffix live in their own slots — never type "+91" into [value].
 *
 * @param value current text
 * @param title label above the field; null hides the label row
 * @param status overrides default/focused borders with red/green/orange
 * @param helper text below field; replaced by status-color text when [status] is set
 * @param readOnly display mode (text copyable, not editable) — keeps text.primary color
 */
@Composable
fun WiomInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    placeholder: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    status: WiomFieldStatus = WiomFieldStatus.None,
    helper: String? = null,
    counter: String? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    prefix: String? = null,
    suffix: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    minLines: Int = 1,
) {
    var focused by remember { mutableStateOf(false) }
    val isActive = focused || value.isNotEmpty()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
    ) {
        title?.let {
            Text(
                text = it,
                style = WiomTheme.type.labelMd,
                color = labelColor(enabled = enabled, readOnly = readOnly, isActive = isActive),
            )
        }

        FieldContainer(
            enabled = enabled,
            readOnly = readOnly,
            focused = focused,
            status = status,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = WiomTheme.spacing.lg, vertical = WiomTheme.spacing.md),
                horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leadingIcon?.invoke()
                prefix?.let {
                    Text(
                        text = it,
                        style = WiomTheme.type.bodyLg,
                        color = WiomTheme.colors.text.secondary,
                    )
                }

                val textColor = when {
                    !enabled -> WiomTheme.colors.text.disabled
                    else -> WiomTheme.colors.text.primary
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty() && placeholder.isNotEmpty()) {
                        Text(
                            text = placeholder,
                            style = WiomTheme.type.bodyLg,
                            color = WiomTheme.colors.text.disabled,
                        )
                    }
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focused = it.isFocused },
                        textStyle = LocalTextStyle.current.merge(
                            TextStyle(
                                fontFamily = WiomTheme.type.bodyLg.fontFamily,
                                fontSize = WiomTheme.type.bodyLg.fontSize,
                                fontWeight = WiomTheme.type.bodyLg.fontWeight,
                                lineHeight = WiomTheme.type.bodyLg.lineHeight,
                                color = textColor,
                            )
                        ),
                        cursorBrush = SolidColor(WiomTheme.colors.brand.primary),
                        enabled = enabled,
                        readOnly = readOnly,
                        visualTransformation = visualTransformation,
                        keyboardOptions = keyboardOptions,
                        singleLine = singleLine,
                        minLines = minLines,
                        interactionSource = remember { MutableInteractionSource() },
                    )
                }

                suffix?.let {
                    Text(
                        text = it,
                        style = WiomTheme.type.bodyLg,
                        color = WiomTheme.colors.text.secondary,
                    )
                }

                val statusTrailing: (@Composable () -> Unit)? = when (status) {
                    WiomFieldStatus.Error -> {
                        { WiomIcon(id = WiomIcons.error, contentDescription = "Error", size = WiomTheme.icon.sm, tint = WiomTheme.colors.negative.primary) }
                    }
                    WiomFieldStatus.Success -> {
                        { WiomIcon(id = WiomIcons.checkCircle, contentDescription = "Success", size = WiomTheme.icon.sm, tint = WiomTheme.colors.positive.primary) }
                    }
                    WiomFieldStatus.Warning -> {
                        { WiomIcon(id = WiomIcons.warning, contentDescription = "Warning", size = WiomTheme.icon.sm, tint = WiomTheme.colors.warning.primary) }
                    }
                    WiomFieldStatus.None -> trailingIcon
                }
                statusTrailing?.invoke()
            }
        }

        if (helper != null || counter != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                helper?.let {
                    Text(
                        text = it,
                        style = WiomTheme.type.bodySm,
                        color = helperColor(enabled = enabled, status = status),
                        modifier = Modifier.weight(1f, fill = false),
                    )
                } ?: Box(Modifier.width(0.dp))
                counter?.let {
                    Text(
                        text = it,
                        style = WiomTheme.type.bodySm,
                        color = WiomTheme.colors.text.secondary,
                    )
                }
            }
        }
    }
}

/** Multi-line textarea. Same anatomy as [WiomInput] but height grows with content. */
@Composable
fun WiomTextarea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    placeholder: String = "",
    enabled: Boolean = true,
    status: WiomFieldStatus = WiomFieldStatus.None,
    helper: String? = null,
    counter: String? = null,
    minLines: Int = 3,
    maxLines: Int = 8,
) {
    WiomInput(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        title = title,
        placeholder = placeholder,
        enabled = enabled,
        status = status,
        helper = helper,
        counter = counter,
        singleLine = false,
        minLines = minLines,
        keyboardOptions = KeyboardOptions.Default,
    )
}

@Composable
private fun FieldContainer(
    enabled: Boolean,
    readOnly: Boolean,
    focused: Boolean,
    status: WiomFieldStatus,
    content: @Composable () -> Unit,
) {
    val fill = when {
        !enabled || readOnly -> WiomTheme.colors.surface.subtle
        else -> WiomTheme.colors.surface.base
    }
    val (strokeWidth, borderColor) = when {
        status == WiomFieldStatus.Error -> WiomTheme.stroke.medium to WiomTheme.colors.negative.primary
        status == WiomFieldStatus.Success -> WiomTheme.stroke.medium to WiomTheme.colors.positive.primary
        status == WiomFieldStatus.Warning -> WiomTheme.stroke.medium to WiomTheme.colors.warning.primary
        readOnly -> WiomTheme.stroke.small to WiomTheme.colors.border.subtle
        focused -> WiomTheme.stroke.medium to WiomTheme.colors.brand.primary
        !enabled -> WiomTheme.stroke.small to WiomTheme.colors.border.default.copy(alpha = 0.4f)
        else -> WiomTheme.stroke.small to WiomTheme.colors.border.default
    }
    val shape = RoundedCornerShape(WiomTheme.radius.medium)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(fill)
            .border(width = strokeWidth, color = borderColor, shape = shape)
    ) {
        content()
    }
}

@Composable
private fun labelColor(enabled: Boolean, readOnly: Boolean, isActive: Boolean): Color = when {
    !enabled -> WiomTheme.colors.text.disabled
    readOnly -> WiomTheme.colors.text.secondary
    isActive -> WiomTheme.colors.text.secondary
    else -> WiomTheme.colors.text.primary
}

@Composable
private fun helperColor(enabled: Boolean, status: WiomFieldStatus): Color = when {
    !enabled -> WiomTheme.colors.text.disabled
    status == WiomFieldStatus.Error -> WiomTheme.colors.negative.primary
    status == WiomFieldStatus.Success -> WiomTheme.colors.positive.primary
    status == WiomFieldStatus.Warning -> WiomTheme.colors.warning.primary
    else -> WiomTheme.colors.text.secondary
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun InputsPreview() = WiomTheme {
    Column(
        modifier = Modifier
            .background(WiomTheme.colors.surface.base)
            .padding(WiomTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.lg),
    ) {
        WiomInput(value = "", onValueChange = {}, title = "Full name", placeholder = "Enter your name", helper = "As per Aadhaar")
        WiomInput(value = "9876543210", onValueChange = {}, title = "Mobile number", leadingIcon = {
            WiomIcon(WiomIcons.phone, contentDescription = null, size = WiomTheme.icon.sm, tint = WiomTheme.colors.text.secondary)
        }, helper = "We'll send an OTP", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
        WiomInput(value = "500", onValueChange = {}, title = "Recharge amount", prefix = "₹", suffix = ".00", helper = "Includes 18% GST")
        WiomInput(value = "abhishek@wiom", onValueChange = {}, title = "Email", status = WiomFieldStatus.Error, helper = "Enter a valid email address")
        WiomInput(value = "4 3 8 1 2 9", onValueChange = {}, title = "Enter 6-digit OTP", counter = "00:24", status = WiomFieldStatus.Success)
        WiomInput(value = "••••••••", onValueChange = {}, title = "Password", visualTransformation = PasswordVisualTransformation(), trailingIcon = {
            WiomIcon(WiomIcons.visibility, contentDescription = "Show password", size = WiomTheme.icon.sm, tint = WiomTheme.colors.text.secondary)
        })
        WiomInput(value = "CUST-2045", onValueChange = {}, title = "Customer ID", readOnly = true)
        WiomInput(value = "Locked", onValueChange = {}, title = "Disabled", enabled = false)
    }
}
