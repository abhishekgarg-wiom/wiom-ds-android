package com.wiom.designsystem.component.selectioncontrol

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.theme.WiomTheme

/**
 * Checkbox selection state.
 *
 * `Indeterminate` is valid only on a parent checkbox of a multi-select tree
 * where some children are selected.
 */
enum class WiomCheckboxSelection { No, Indeterminate, Selected }

/** Checkbox / Radio states. Switch has its own state enum since switches never error. */
enum class WiomIndicatorState { Default, Disabled, Error }

/** Switch states. Pressed is typically driven by `InteractionSource`, not by callers. */
enum class WiomSwitchState { Default, Disabled }

// -----------------------------------------------------------------------------
// Checkbox
// -----------------------------------------------------------------------------

/**
 * Wiom checkbox indicator — 24×24 dp, `radius.tiny`.
 *
 * No label, no helper, no row chrome — those live on `WiomListItem` (or the
 * inline form parent like [WiomAcknowledge][com.wiom.designsystem.component.button.WiomAcknowledge]).
 *
 * Selection states: `No` · `Indeterminate` (dash glyph) · `Selected` (check glyph).
 * States: `Default` / `Disabled` / `Error`. On `Error + Selected/Indeterminate`
 * the fill swaps from `bg.brand` to `bg.critical`.
 *
 * Size is fixed at 24dp — do NOT pass width/height in [modifier]. Material's
 * Check/Remove glyphs have intrinsic ~2dp internal padding, so a 24dp icon
 * centred in a 24dp Box renders at the right visual density (~20dp visible).
 */
@Composable
fun WiomCheckbox(
    selection: WiomCheckboxSelection,
    state: WiomIndicatorState,
    onToggle: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val enabled = state != WiomIndicatorState.Disabled && onToggle != null
    val shape = RoundedCornerShape(WiomTheme.radius.tiny)

    val tap = if (enabled) {
        Modifier.toggleable(
            value = selection == WiomCheckboxSelection.Selected,
            onValueChange = { onToggle?.invoke() },
            role = Role.Checkbox,
        )
    } else Modifier

    Box(
        modifier = modifier
            .size(24.dp)
            .then(tap)
            .then(checkboxSurface(selection, state, shape)),
        contentAlignment = Alignment.Center,
    ) {
        when (selection) {
            WiomCheckboxSelection.Selected -> WiomIcon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = glyphTint(state),
                size = WiomTheme.iconSize.md, // 24dp glyph in 24dp box — Material adds ~2dp intrinsic pad
            )
            WiomCheckboxSelection.Indeterminate -> WiomIcon(
                imageVector = Icons.Rounded.Remove,
                contentDescription = null,
                tint = glyphTint(state),
                size = WiomTheme.iconSize.md,
            )
            WiomCheckboxSelection.No -> Unit
        }
    }
}

@Composable
private fun checkboxSurface(
    selection: WiomCheckboxSelection,
    state: WiomIndicatorState,
    shape: RoundedCornerShape,
): Modifier {
    val c = WiomTheme.color
    val filled = selection != WiomCheckboxSelection.No
    val fill: Color = when {
        !filled -> Color.Transparent
        state == WiomIndicatorState.Disabled -> c.bg.disabled
        state == WiomIndicatorState.Error -> c.bg.critical
        else -> c.bg.brand
    }
    return if (filled) {
        // Filled state = no border (fill is the boundary)
        Modifier.clip(shape).background(color = fill, shape = shape)
    } else {
        val stroke = when (state) {
            WiomIndicatorState.Default -> c.stroke.strong
            WiomIndicatorState.Disabled -> c.stroke.subtle
            WiomIndicatorState.Error -> c.stroke.criticalFocus
        }
        Modifier
            .clip(shape)
            .border(width = WiomTheme.stroke.medium, color = stroke, shape = shape)
    }
}

// -----------------------------------------------------------------------------
// Radio
// -----------------------------------------------------------------------------

/**
 * Wiom radio indicator — 24×24 dp, circular.
 *
 * Always used in a group of 2+. Standalone radio is an anti-pattern. Inner dot
 * (10×10) visible only when [selected].
 *
 * On `Error + selected` the fill swaps from `bg.brand` to `bg.critical`.
 */
@Composable
fun WiomRadio(
    selected: Boolean,
    state: WiomIndicatorState,
    onSelect: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val enabled = state != WiomIndicatorState.Disabled && onSelect != null
    val c = WiomTheme.color
    val shape = CircleShape

    val fill: Color = when {
        !selected -> Color.Transparent
        state == WiomIndicatorState.Disabled -> c.bg.disabled
        state == WiomIndicatorState.Error -> c.bg.critical
        else -> c.bg.brand
    }

    val surface = if (selected) {
        Modifier.clip(shape).background(color = fill, shape = shape)
    } else {
        val stroke = when (state) {
            WiomIndicatorState.Default -> c.stroke.strong
            WiomIndicatorState.Disabled -> c.stroke.subtle
            WiomIndicatorState.Error -> c.stroke.criticalFocus
        }
        Modifier
            .clip(shape)
            .border(width = WiomTheme.stroke.medium, color = stroke, shape = shape)
    }

    val tap = if (enabled) {
        Modifier.selectable(
            selected = selected,
            onClick = { onSelect?.invoke() },
            role = Role.RadioButton,
        )
    } else Modifier

    Box(
        modifier = modifier
            .size(24.dp)
            .then(tap)
            .then(surface),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(shape)
                    .background(color = glyphTint(state), shape = shape),
            )
        }
    }
}

// -----------------------------------------------------------------------------
// Switch
// -----------------------------------------------------------------------------

/**
 * Wiom switch — 52×32 dp track, 24 dp thumb, 4 dp inset.
 *
 * Instant-apply binary setting — notifications, auto-pay, privacy toggles, dark
 * mode. Never for form fields that require a submit (use [WiomCheckbox]).
 *
 * No Error state (switches are instant-apply; there's no form validation layer).
 * Pressed is derived from `InteractionSource` automatically.
 */
@Composable
fun WiomSwitch(
    checked: Boolean,
    state: WiomSwitchState = WiomSwitchState.Default,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val enabled = state != WiomSwitchState.Disabled && onCheckedChange != null
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val c = WiomTheme.color
    val trackShape = RoundedCornerShape(WiomTheme.radius.full)

    // Track fill
    val trackFill: Color = when (state) {
        WiomSwitchState.Disabled -> c.bg.disabled
        WiomSwitchState.Default -> when {
            checked && isPressed -> c.bg.brandPressed
            checked -> c.bg.brand
            !checked && isPressed -> c.bg.subtle
            else -> c.bg.muted
        }
    }

    // Track border — only unfilled (off) states have it
    val trackBorder: Color? = when {
        checked -> null // filled = no border
        state == WiomSwitchState.Disabled -> c.stroke.subtle
        else -> c.stroke.strong
    }

    // Thumb position: 4dp inset left when off, 52 - 4 - 24 = 24dp from left when on
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 24.dp else 4.dp,
        label = "WiomSwitchThumbOffset",
    )

    val tap = if (enabled) {
        Modifier.toggleable(
            value = checked,
            onValueChange = { onCheckedChange?.invoke(it) },
            interactionSource = interactionSource,
            indication = null,
            role = Role.Switch,
        )
    } else Modifier

    var track = modifier
        .size(width = 52.dp, height = 32.dp)
        .then(tap)
        .clip(trackShape)
        .background(color = trackFill, shape = trackShape)

    if (trackBorder != null) {
        track = track.border(width = WiomTheme.stroke.medium, color = trackBorder, shape = trackShape)
    }

    Box(modifier = track) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset, y = 4.dp)
                .size(24.dp)
                .clip(CircleShape)
                // Thumb is physically white on every track tone — foundations skill §"thumb".
                .background(color = Color.White, shape = CircleShape),
        )
    }
}

// -----------------------------------------------------------------------------
// Shared helpers
// -----------------------------------------------------------------------------

@Composable
private fun glyphTint(state: WiomIndicatorState): Color = when (state) {
    WiomIndicatorState.Disabled -> WiomTheme.color.icon.disabled
    else -> WiomTheme.color.icon.inverse
}

// -----------------------------------------------------------------------------
// Previews
// -----------------------------------------------------------------------------

@Preview(name = "Checkbox — all variants", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewCheckboxGrid() {
    WiomTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.lg),
            modifier = Modifier.padding(WiomTheme.spacing.lg),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
                WiomCheckbox(WiomCheckboxSelection.No, WiomIndicatorState.Default, onToggle = { })
                WiomCheckbox(WiomCheckboxSelection.Indeterminate, WiomIndicatorState.Default, onToggle = { })
                WiomCheckbox(WiomCheckboxSelection.Selected, WiomIndicatorState.Default, onToggle = { })
            }
            Spacer(Modifier.size(WiomTheme.spacing.sm))
            Row(horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
                WiomCheckbox(WiomCheckboxSelection.No, WiomIndicatorState.Disabled, onToggle = null)
                WiomCheckbox(WiomCheckboxSelection.Indeterminate, WiomIndicatorState.Disabled, onToggle = null)
                WiomCheckbox(WiomCheckboxSelection.Selected, WiomIndicatorState.Disabled, onToggle = null)
            }
            Spacer(Modifier.size(WiomTheme.spacing.sm))
            Row(horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
                WiomCheckbox(WiomCheckboxSelection.No, WiomIndicatorState.Error, onToggle = { })
                WiomCheckbox(WiomCheckboxSelection.Indeterminate, WiomIndicatorState.Error, onToggle = { })
                WiomCheckbox(WiomCheckboxSelection.Selected, WiomIndicatorState.Error, onToggle = { })
            }
        }
    }
}

@Preview(name = "Radio — all variants", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewRadioGrid() {
    WiomTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.lg),
            modifier = Modifier.padding(WiomTheme.spacing.lg),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
                WiomRadio(selected = false, state = WiomIndicatorState.Default, onSelect = { })
                WiomRadio(selected = true, state = WiomIndicatorState.Default, onSelect = { })
            }
            Spacer(Modifier.size(WiomTheme.spacing.sm))
            Row(horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
                WiomRadio(selected = false, state = WiomIndicatorState.Disabled, onSelect = null)
                WiomRadio(selected = true, state = WiomIndicatorState.Disabled, onSelect = null)
            }
            Spacer(Modifier.size(WiomTheme.spacing.sm))
            Row(horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
                WiomRadio(selected = false, state = WiomIndicatorState.Error, onSelect = { })
                WiomRadio(selected = true, state = WiomIndicatorState.Error, onSelect = { })
            }
        }
    }
}

@Preview(name = "Switch — all variants", showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewSwitchGrid() {
    WiomTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.lg),
            modifier = Modifier.padding(WiomTheme.spacing.lg),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
                WiomSwitch(checked = false, onCheckedChange = { })
                WiomSwitch(checked = true, onCheckedChange = { })
            }
            Spacer(Modifier.size(WiomTheme.spacing.sm))
            Row(horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
                WiomSwitch(checked = false, state = WiomSwitchState.Disabled, onCheckedChange = null)
                WiomSwitch(checked = true, state = WiomSwitchState.Disabled, onCheckedChange = null)
            }
        }
    }
}
