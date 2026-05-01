package com.wiom.designsystem.component.input

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.theme.WiomTheme

/**
 * Status semantics for [WiomInput] / [WiomTextarea].
 *
 * Separate from [enabled] and [readOnly] — a field can be ReadOnly and Error at the same time.
 *
 * - [None]     Rest / focused / filled. No status chrome.
 * - [Error]    Validation failed. Red 2dp border + red helper + auto trailing error icon.
 * - [Success]  Validated OK. Green 2dp border + green helper + auto trailing check icon.
 * - [Warning]  Risky but valid. Gold border + olive (`text.onWarning`) helper + auto trailing
 *              warning icon. Note: uses `stroke.brandFocus` as an orange substitute is not in
 *              the foundation — see README for the judgment call.
 */
enum class WiomInputStatus { None, Error, Success, Warning }

/**
 * Single-line Wiom input field.
 *
 * Anatomy (top → bottom):
 *  1. `title` above the container — `type.labelLg`.
 *  2. Bordered container: optional leading icon → prefix → value/placeholder → suffix → optional
 *     trailing icon.
 *  3. Helper + counter row below (only rendered when helper or counter is non-null).
 *
 * Padding is `space.lg` × `space.md`; radius is `radius.medium`; rest border is
 * `stroke.small` + `stroke.subtle`, focus is `stroke.medium` + `stroke.brandFocus`, error is
 * `stroke.medium` + `stroke.criticalFocus`.
 *
 * Status icons (Error/Success/Warning) auto-populate the trailing slot and override any
 * caller-supplied `trailingIcon`.
 *
 * `enabled` and `readOnly` are independent. Read-only keeps text at `text.default` and fill at
 * `bg.subtle`; Disabled dims text to `text.disabled` and fills `bg.disabled`.
 *
 * When [onClick] is set AND `readOnly = true` AND `enabled = true`, the entire container becomes
 * tappable — this is the dropdown-replacement pattern (the `BasicTextField` below still renders
 * the value but is non-focusable because of readOnly).
 */
@Composable
fun WiomInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    placeholder: String? = null,
    helper: String? = null,
    counter: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    prefix: String? = null,
    suffix: String? = null,
    status: WiomInputStatus = WiomInputStatus.None,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onClick: (() -> Unit)? = null,
) {
    WiomInputInternal(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        title = title,
        placeholder = placeholder,
        helper = helper,
        counter = counter,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onTrailingIconClick = onTrailingIconClick,
        prefix = prefix,
        suffix = suffix,
        status = status,
        enabled = enabled,
        readOnly = readOnly,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        onClick = onClick,
        singleLine = true,
        minLines = 1,
    )
}

/**
 * Multi-line Wiom textarea.
 *
 * Same anatomy as [WiomInput] minus the icon/prefix/suffix slots. Grows with content — no fixed
 * height. The inner row aligns to `Alignment.Top` so the placeholder and any icons sit on the
 * first line, not the vertical middle of a tall field.
 */
@Composable
fun WiomTextarea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    placeholder: String? = null,
    helper: String? = null,
    counter: String? = null,
    status: WiomInputStatus = WiomInputStatus.None,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    minLines: Int = 3,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    WiomInputInternal(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        title = title,
        placeholder = placeholder,
        helper = helper,
        counter = counter,
        leadingIcon = null,
        trailingIcon = null,
        onTrailingIconClick = null,
        prefix = null,
        suffix = null,
        status = status,
        enabled = enabled,
        readOnly = readOnly,
        visualTransformation = VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        onClick = null,
        singleLine = false,
        minLines = minLines,
    )
}

@Composable
private fun WiomInputInternal(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    title: String?,
    placeholder: String?,
    helper: String?,
    counter: String?,
    leadingIcon: ImageVector?,
    trailingIcon: ImageVector?,
    onTrailingIconClick: (() -> Unit)?,
    prefix: String?,
    suffix: String?,
    status: WiomInputStatus,
    enabled: Boolean,
    readOnly: Boolean,
    visualTransformation: VisualTransformation,
    keyboardOptions: KeyboardOptions,
    onClick: (() -> Unit)?,
    singleLine: Boolean,
    minLines: Int,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val containerFill: Color = when {
        !enabled -> WiomTheme.color.bg.disabled
        readOnly -> WiomTheme.color.bg.subtle
        else -> WiomTheme.color.bg.default
    }

    // Border priority: disabled > error > success > warning > focused > rest.
    val borderWidth = when {
        !enabled -> WiomTheme.stroke.small
        status == WiomInputStatus.Error -> WiomTheme.stroke.medium
        status == WiomInputStatus.Success -> WiomTheme.stroke.medium
        status == WiomInputStatus.Warning -> WiomTheme.stroke.medium
        isFocused && !readOnly -> WiomTheme.stroke.medium
        else -> WiomTheme.stroke.small
    }
    val borderColor: Color = when {
        !enabled -> WiomTheme.color.stroke.subtle
        status == WiomInputStatus.Error -> WiomTheme.color.stroke.criticalFocus
        status == WiomInputStatus.Success -> WiomTheme.color.stroke.positiveFocus
        status == WiomInputStatus.Warning -> WiomTheme.color.stroke.warning
        isFocused && !readOnly -> WiomTheme.color.stroke.brandFocus
        else -> WiomTheme.color.stroke.subtle
    }

    val labelColor: Color = when {
        !enabled -> WiomTheme.color.text.disabled
        isFocused || value.isNotEmpty() || readOnly -> WiomTheme.color.text.subtle
        else -> WiomTheme.color.text.default
    }

    val valueColor: Color = if (enabled) WiomTheme.color.text.default else WiomTheme.color.text.disabled
    val placeholderColor: Color = WiomTheme.color.text.disabled
    val affixColor: Color = if (enabled) WiomTheme.color.text.subtle else WiomTheme.color.text.disabled

    val helperColor: Color = when (status) {
        WiomInputStatus.Error -> WiomTheme.color.text.critical
        WiomInputStatus.Success -> WiomTheme.color.text.positive
        WiomInputStatus.Warning -> WiomTheme.color.text.onWarning
        WiomInputStatus.None -> if (enabled) WiomTheme.color.text.subtle else WiomTheme.color.text.disabled
    }

    // Status icon overrides any caller-supplied trailing icon.
    val resolvedTrailingIcon: ImageVector? = when (status) {
        WiomInputStatus.Error -> Icons.Rounded.Error
        WiomInputStatus.Success -> Icons.Rounded.CheckCircle
        WiomInputStatus.Warning -> Icons.Rounded.Warning
        WiomInputStatus.None -> trailingIcon
    }
    val trailingTint: Color = when (status) {
        WiomInputStatus.Error -> WiomTheme.color.icon.critical
        WiomInputStatus.Success -> WiomTheme.color.icon.positive
        WiomInputStatus.Warning -> WiomTheme.color.icon.warning
        WiomInputStatus.None -> if (enabled) WiomTheme.color.icon.action else WiomTheme.color.icon.disabled
    }
    val leadingTint: Color = if (enabled) WiomTheme.color.icon.nonAction else WiomTheme.color.icon.disabled

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
    ) {
        if (!title.isNullOrEmpty()) {
            Text(text = title, style = WiomTheme.type.labelLg, color = labelColor)
        }

        val containerShape = RoundedCornerShape(WiomTheme.radius.medium)
        val rowVerticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top

        val containerModifier = Modifier
            .fillMaxWidth()
            .background(color = containerFill, shape = containerShape)
            .border(width = borderWidth, color = borderColor, shape = containerShape)
            .then(
                if (onClick != null && enabled && readOnly) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .padding(horizontal = WiomTheme.spacing.lg, vertical = WiomTheme.spacing.md)

        Row(
            modifier = containerModifier,
            verticalAlignment = rowVerticalAlignment,
            horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md),
        ) {
            if (leadingIcon != null) {
                WiomIcon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    size = WiomTheme.iconSize.sm,
                    tint = leadingTint,
                )
            }

            if (!prefix.isNullOrEmpty()) {
                Text(text = prefix, style = WiomTheme.type.bodyLg, color = affixColor)
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = WiomTheme.spacing.xl),
                contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart,
            ) {
                // Dropdown-trigger pattern: when the field is readOnly + has onClick, render a
                // plain Text so the whole row's Modifier.clickable handles the tap. Otherwise
                // render an editable BasicTextField.
                val actingAsDropdownTrigger = readOnly && onClick != null && enabled
                if (actingAsDropdownTrigger) {
                    Text(
                        text = if (value.isEmpty()) placeholder.orEmpty() else value,
                        style = WiomTheme.type.bodyLg,
                        color = if (value.isEmpty()) placeholderColor else valueColor,
                    )
                } else {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = enabled,
                        readOnly = readOnly,
                        singleLine = singleLine,
                        minLines = minLines,
                        textStyle = WiomTheme.type.bodyLg.copy(color = valueColor),
                        cursorBrush = SolidColor(WiomTheme.color.text.default),
                        visualTransformation = visualTransformation,
                        keyboardOptions = keyboardOptions,
                        interactionSource = interactionSource,
                    )
                    if (value.isEmpty() && !placeholder.isNullOrEmpty()) {
                        Text(
                            text = placeholder,
                            style = WiomTheme.type.bodyLg,
                            color = placeholderColor,
                        )
                    }
                }
            }

            if (!suffix.isNullOrEmpty()) {
                Text(text = suffix, style = WiomTheme.type.bodyLg, color = affixColor)
            }

            if (resolvedTrailingIcon != null) {
                val trailingModifier = if (
                    status == WiomInputStatus.None &&
                    onTrailingIconClick != null &&
                    enabled
                ) {
                    Modifier.clickable(onClick = onTrailingIconClick)
                } else {
                    Modifier
                }
                WiomIcon(
                    imageVector = resolvedTrailingIcon,
                    contentDescription = null,
                    modifier = trailingModifier,
                    size = WiomTheme.iconSize.sm,
                    tint = trailingTint,
                )
            }
        }

        if (!helper.isNullOrEmpty() || !counter.isNullOrEmpty()) {
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
                if (!counter.isNullOrEmpty()) {
                    Text(
                        text = counter,
                        style = WiomTheme.type.bodyMd,
                        color = if (enabled) WiomTheme.color.text.subtle else WiomTheme.color.text.disabled,
                    )
                }
            }
        }
    }
}

// region Previews ------------------------------------------------------------------------------

@Preview(name = "Input · Default (empty)", showBackground = true)
@Composable
private fun PreviewInputDefault() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomInput(
            value = "",
            onValueChange = {},
            title = "Name",
            placeholder = "Enter your name",
            helper = "As on Aadhaar",
        )
    }
}

@Preview(name = "Input · Filled", showBackground = true)
@Composable
private fun PreviewInputFilled() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomInput(
            value = "Saurav Gupta",
            onValueChange = {},
            title = "Name",
            helper = "As on Aadhaar",
        )
    }
}

@Preview(name = "Input · Error", showBackground = true)
@Composable
private fun PreviewInputError() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomInput(
            value = "98",
            onValueChange = {},
            title = "Mobile number",
            leadingIcon = Icons.Rounded.Phone,
            helper = "Enter a 10-digit mobile number",
            status = WiomInputStatus.Error,
        )
    }
}

@Preview(name = "Input · Success", showBackground = true)
@Composable
private fun PreviewInputSuccess() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomInput(
            value = "9876543210",
            onValueChange = {},
            title = "Mobile number",
            leadingIcon = Icons.Rounded.Phone,
            helper = "Mobile verified",
            status = WiomInputStatus.Success,
        )
    }
}

@Preview(name = "Input · Warning", showBackground = true)
@Composable
private fun PreviewInputWarning() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomInput(
            value = "abhishek.garg@example",
            onValueChange = {},
            title = "Email",
            helper = "Looks unusual — double-check the domain",
            status = WiomInputStatus.Warning,
        )
    }
}

@Preview(name = "Input · Disabled", showBackground = true)
@Composable
private fun PreviewInputDisabled() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomInput(
            value = "Locked field",
            onValueChange = {},
            title = "Customer ID",
            enabled = false,
        )
    }
}

@Preview(name = "Input · ReadOnly (dropdown trigger)", showBackground = true)
@Composable
private fun PreviewInputReadOnlyDropdown() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomInput(
            value = "English",
            onValueChange = {},
            title = "Language",
            trailingIcon = Icons.Rounded.KeyboardArrowDown,
            readOnly = true,
            onClick = {},
        )
    }
}

@Preview(name = "Input · Phone (+91 via icon, India-only)", showBackground = true)
@Composable
private fun PreviewInputPhone() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomInput(
            value = "9876543210",
            onValueChange = {},
            title = "Mobile number",
            leadingIcon = Icons.Rounded.Phone,
            helper = "We'll send you an OTP",
            keyboardOptions = KeyboardOptions(
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone,
            ),
        )
    }
}

@Preview(name = "Input · Search", showBackground = true)
@Composable
private fun PreviewInputSearch() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomInput(
            value = "Wiom Pro",
            onValueChange = {},
            leadingIcon = Icons.Rounded.Search,
            trailingIcon = Icons.Rounded.Close,
            onTrailingIconClick = {},
            placeholder = "Search plans, routers…",
        )
    }
}

@Preview(name = "Input · Currency (₹ / .00)", showBackground = true)
@Composable
private fun PreviewInputCurrency() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomInput(
            value = "599",
            onValueChange = {},
            title = "Recharge amount",
            prefix = "₹",
            suffix = ".00",
            helper = "Includes 18% GST",
            keyboardOptions = KeyboardOptions(
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number,
            ),
        )
    }
}

@Preview(name = "Input · Password (visibility toggle)", showBackground = true)
@Composable
private fun PreviewInputPassword() {
    com.wiom.designsystem.theme.WiomTheme {
        var revealed by remember { mutableStateOf(false) }
        WiomInput(
            value = "super-secret",
            onValueChange = {},
            title = "Password",
            leadingIcon = Icons.Rounded.Lock,
            trailingIcon = if (revealed) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
            onTrailingIconClick = { revealed = !revealed },
            visualTransformation = if (revealed) VisualTransformation.None else PasswordVisualTransformation(),
        )
    }
}

@Preview(name = "Input · Counter", showBackground = true)
@Composable
private fun PreviewInputCounter() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomInput(
            value = "Hello",
            onValueChange = {},
            title = "Nickname",
            counter = "5 / 20",
        )
    }
}

@Preview(name = "Textarea · Default (empty)", showBackground = true)
@Composable
private fun PreviewTextareaDefault() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomTextarea(
            value = "",
            onValueChange = {},
            title = "Installation address",
            placeholder = "Door no, building, landmark, city…",
            helper = "Used only for installation scheduling",
        )
    }
}

@Preview(name = "Textarea · Filled with counter", showBackground = true)
@Composable
private fun PreviewTextareaFilled() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomTextarea(
            value = "House 12, Green Park, near metro station, New Delhi 110016",
            onValueChange = {},
            title = "Installation address",
            counter = "60 / 200",
        )
    }
}

@Preview(name = "Textarea · Error", showBackground = true)
@Composable
private fun PreviewTextareaError() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomTextarea(
            value = "No landmark",
            onValueChange = {},
            title = "Installation address",
            helper = "Please add a landmark so our engineer can find you",
            status = WiomInputStatus.Error,
        )
    }
}

@Preview(name = "Textarea · Disabled", showBackground = true)
@Composable
private fun PreviewTextareaDisabled() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomTextarea(
            value = "Locked content",
            onValueChange = {},
            title = "Previous address (locked)",
            enabled = false,
        )
    }
}

// endregion
