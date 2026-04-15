package com.wiom.designsystem.component.dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wiom.designsystem.component.listitem.WiomListItem
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.foundation.icon.WiomIcons
import com.wiom.designsystem.theme.WiomTheme

data class WiomDropdownOption<T>(val value: T, val label: String)

/**
 * Wiom dropdown — single-select picker.
 *
 * Field shares visual language with [com.wiom.designsystem.component.input.WiomInput]:
 * label above, bordered 12dp-radius container, helper below. Expanded state shows
 * a menu of [WiomListItem] rows below the field with brand-color active border.
 *
 * For multi-select, see [WiomMultiDropdown] (coming). For action menus, use the
 * same primitives — pass a callback per row instead of a `value` model.
 */
@Composable
fun <T> WiomDropdown(
    value: T?,
    options: List<WiomDropdownOption<T>>,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "Select an option",
    helper: String? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = options.firstOrNull { it.value == value }?.label

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
    ) {
        label?.let {
            Text(
                text = it,
                style = WiomTheme.type.labelMd,
                color = if (enabled) WiomTheme.colors.text.primary else WiomTheme.colors.text.disabled,
            )
        }

        Box {
            DropdownField(
                valueText = selectedLabel ?: placeholder,
                placeholderShown = selectedLabel == null,
                enabled = enabled,
                isError = isError,
                expanded = expanded,
                leadingIcon = leadingIcon,
                onClick = { if (enabled) expanded = !expanded },
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(WiomTheme.colors.surface.base),
            ) {
                options.forEach { opt ->
                    WiomListItem(
                        primary = opt.label,
                        trailingIcon = null,
                        selected = opt.value == value,
                        onClick = {
                            onValueChange(opt.value)
                            expanded = false
                        },
                    )
                }
            }
        }

        helper?.let {
            Text(
                text = it,
                style = WiomTheme.type.bodySm,
                color = when {
                    !enabled -> WiomTheme.colors.text.disabled
                    isError -> WiomTheme.colors.negative.primary
                    else -> WiomTheme.colors.text.secondary
                },
            )
        }
    }
}

@Composable
private fun DropdownField(
    valueText: String,
    placeholderShown: Boolean,
    enabled: Boolean,
    isError: Boolean,
    expanded: Boolean,
    leadingIcon: (@Composable () -> Unit)?,
    onClick: () -> Unit,
) {
    val fill: Color = when {
        !enabled -> WiomTheme.colors.surface.subtle
        else -> WiomTheme.colors.surface.base
    }
    val (strokeWidth, borderColor) = when {
        isError -> WiomTheme.stroke.medium to WiomTheme.colors.negative.primary
        expanded -> WiomTheme.stroke.medium to WiomTheme.colors.brand.primary
        !enabled -> WiomTheme.stroke.small to WiomTheme.colors.border.default.copy(alpha = 0.4f)
        else -> WiomTheme.stroke.small to WiomTheme.colors.border.default
    }
    val shape = RoundedCornerShape(WiomTheme.radius.medium)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(fill)
            .border(width = strokeWidth, color = borderColor, shape = shape)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = WiomTheme.spacing.lg, vertical = WiomTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
    ) {
        leadingIcon?.invoke()
        Text(
            text = valueText,
            style = WiomTheme.type.bodyLg,
            color = when {
                !enabled -> WiomTheme.colors.text.disabled
                placeholderShown -> WiomTheme.colors.text.disabled
                else -> WiomTheme.colors.text.primary
            },
            modifier = Modifier.weight(1f),
            maxLines = 1,
        )
        val chevronTint = when {
            !enabled -> WiomTheme.colors.text.disabled
            isError -> WiomTheme.colors.negative.primary
            expanded -> WiomTheme.colors.brand.primary
            else -> WiomTheme.colors.text.secondary
        }
        Box(
            modifier = Modifier.graphicsLayer { rotationZ = if (expanded) 180f else 0f },
        ) {
            WiomIcon(
                id = WiomIcons.expandMore,
                contentDescription = null,
                size = 20.dp,
                tint = chevronTint,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun DropdownPreview() = WiomTheme {
    val options = listOf(
        WiomDropdownOption("en", "English"),
        WiomDropdownOption("hi", "हिन्दी"),
        WiomDropdownOption("te", "తెలుగు"),
    )
    var selected by remember { mutableStateOf<String?>("hi") }
    Column(
        modifier = Modifier
            .background(WiomTheme.colors.surface.base)
            .padding(WiomTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.lg),
    ) {
        WiomDropdown(
            value = selected,
            options = options,
            onValueChange = { selected = it },
            label = "Language",
            helper = "Used across the app",
        )
    }
}
