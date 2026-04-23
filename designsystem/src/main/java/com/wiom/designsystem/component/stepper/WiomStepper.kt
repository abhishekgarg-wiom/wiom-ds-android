package com.wiom.designsystem.component.stepper

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.PriorityHigh
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.theme.WiomTheme

/**
 * Wiom Stepper — progress through a known, finite sequence of 2–6 steps.
 *
 *  - [WiomStepperHorizontal] — compact row of indicators + short labels (wizards).
 *  - [WiomStepperVertical] — column of indicators with per-step title + description + optional
 *    action slot (status flows, KYC, installation tracking).
 *  - [WiomStepIndicator] — the shared 32dp circle atom with 4 states.
 *
 * If steps are unknown or open-ended, use `WiomPaginationBars` or a progress bar instead.
 */

/** The 4 visual states of a step indicator. */
enum class WiomStepState { Completed, Active, Upcoming, Error }

/** A single step in a horizontal stepper. */
data class WiomHorizontalStep(
    val label: String,
)

/** A single step in a vertical stepper. */
data class WiomVerticalStep(
    val title: String,
    val description: String? = null,
    val action: (@Composable () -> Unit)? = null,
)

/**
 * Derive the visual state for a step index given the current-step index.
 *
 * - step < current → [WiomStepState.Completed]
 * - step == current → [WiomStepState.Active]
 * - step > current → [WiomStepState.Upcoming]
 *
 * Callers can override any entry to [WiomStepState.Error] via the `stateOverrides` map on
 * [WiomStepperHorizontal] / [WiomStepperVertical].
 */
internal fun deriveState(step: Int, current: Int): WiomStepState = when {
    step < current -> WiomStepState.Completed
    step == current -> WiomStepState.Active
    else -> WiomStepState.Upcoming
}

/**
 * Shared 32dp circle atom. Use [WiomStepperHorizontal] / [WiomStepperVertical] in real screens;
 * this atom is intended for documentation / custom layouts only.
 *
 *  - **Completed** — `bg.brand` fill + white [Icons.Rounded.Check] (20dp).
 *  - **Active** — `bg.default` fill + 2dp `stroke.brandFocus` ring + brand-colored number.
 *  - **Upcoming** — `bg.default` fill + 1dp `stroke.subtle` ring + `text.subtle` number.
 *  - **Error** — `bg.critical` fill + white [Icons.Rounded.PriorityHigh] (20dp).
 */
@Composable
fun WiomStepIndicator(
    state: WiomStepState,
    number: Int,
    modifier: Modifier = Modifier,
) {
    val fill = when (state) {
        WiomStepState.Completed -> WiomTheme.color.bg.brand
        WiomStepState.Error -> WiomTheme.color.bg.critical
        WiomStepState.Active, WiomStepState.Upcoming -> WiomTheme.color.bg.default
    }
    val borderWidth = when (state) {
        WiomStepState.Active -> WiomTheme.stroke.medium
        WiomStepState.Upcoming -> WiomTheme.stroke.small
        else -> 0.dp
    }
    val borderColor = when (state) {
        WiomStepState.Active -> WiomTheme.color.stroke.brandFocus
        WiomStepState.Upcoming -> WiomTheme.color.stroke.subtle
        else -> Color.Transparent
    }
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(fill)
            .then(
                if (borderWidth > 0.dp) Modifier.border(borderWidth, borderColor, CircleShape)
                else Modifier
            ),
        contentAlignment = Alignment.Center,
    ) {
        when (state) {
            WiomStepState.Completed -> WiomIcon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                size = WiomTheme.iconSize.sm,
                tint = WiomTheme.color.icon.inverse,
            )
            WiomStepState.Error -> WiomIcon(
                imageVector = Icons.Rounded.PriorityHigh,
                contentDescription = null,
                size = WiomTheme.iconSize.sm,
                tint = WiomTheme.color.icon.inverse,
            )
            WiomStepState.Active -> Text(
                text = number.toString(),
                style = WiomTheme.type.labelMd,
                color = WiomTheme.color.text.brand,
            )
            WiomStepState.Upcoming -> Text(
                text = number.toString(),
                style = WiomTheme.type.labelMd,
                color = WiomTheme.color.text.subtle,
            )
        }
    }
}

/**
 * Horizontal stepper for 2–6 step wizards with 1–2 word labels.
 *
 * Layout: each step is an indicator + label column (`space.sm` gap between). Between every two
 * indicators is a 2dp connector that fills the remaining horizontal space. The connector is
 * filled `bg.brand` when the step it comes out of is already Completed, otherwise `stroke.subtle`.
 *
 * @param steps 2–6 steps. Labels should be 1–2 words.
 * @param currentStep 1-based active step. Steps before it are Completed, after it are Upcoming.
 * @param stateOverrides optional per-step override — useful to mark a step as `Error`.
 *   Keys are 1-based step indices.
 */
@Composable
fun WiomStepperHorizontal(
    steps: List<WiomHorizontalStep>,
    currentStep: Int,
    modifier: Modifier = Modifier,
    stateOverrides: Map<Int, WiomStepState> = emptyMap(),
) {
    if (steps.isEmpty()) return
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        steps.forEachIndexed { idx, step ->
            val stepNumber = idx + 1
            val state = stateOverrides[stepNumber] ?: deriveState(stepNumber, currentStep)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
            ) {
                WiomStepIndicator(state = state, number = stepNumber)
                Text(
                    text = step.label,
                    style = WiomTheme.type.labelSm,
                    color = when (state) {
                        WiomStepState.Upcoming -> WiomTheme.color.text.subtle
                        else -> WiomTheme.color.text.default
                    },
                    textAlign = TextAlign.Center,
                )
            }
            if (idx < steps.lastIndex) {
                // Connector: fills horizontal space between this indicator and the next, vertically
                // centered on the 32dp indicator circle. Brand-filled when the previous step is
                // Completed (not Error, not Active).
                val filled = state == WiomStepState.Completed
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .clip(RoundedCornerShape(WiomTheme.radius.full))
                            .background(
                                if (filled) WiomTheme.color.bg.brand
                                else WiomTheme.color.stroke.subtle
                            )
                    )
                }
            }
        }
    }
}

/**
 * Vertical stepper for status flows — each step has a title, optional description, and an
 * optional action slot that can host any composable (a `WiomButton`, a `WiomInput`, an OTP
 * field, a tertiary CTA, anything).
 *
 * Layout per step:
 *  - 32dp rail column on the left holds the indicator and a 2dp connector that stretches to
 *    fill vertical space up to the next step.
 *  - Content column on the right holds title + description + optional action.
 *  - Rail → content gap is `space.md`; content bottom padding is `space.xl` between steps.
 *
 * @param steps 2–6 steps.
 * @param currentStep 1-based active step.
 * @param stateOverrides optional per-step overrides (typically for Error).
 */
@Composable
fun WiomStepperVertical(
    steps: List<WiomVerticalStep>,
    currentStep: Int,
    modifier: Modifier = Modifier,
    stateOverrides: Map<Int, WiomStepState> = emptyMap(),
) {
    if (steps.isEmpty()) return
    Column(modifier = modifier.fillMaxWidth()) {
        steps.forEachIndexed { idx, step ->
            val stepNumber = idx + 1
            val state = stateOverrides[stepNumber] ?: deriveState(stepNumber, currentStep)
            val isLast = idx == steps.lastIndex
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.Top,
            ) {
                // Rail column — 32dp wide. Contains indicator and (if not last) a fill-height connector.
                Column(
                    modifier = Modifier
                        .width(32.dp)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs),
                ) {
                    WiomStepIndicator(state = state, number = stepNumber)
                    if (!isLast) {
                        val filled = state == WiomStepState.Completed
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .weight(1f)
                                .clip(RoundedCornerShape(WiomTheme.radius.full))
                                .background(
                                    if (filled) WiomTheme.color.bg.brand
                                    else WiomTheme.color.stroke.subtle
                                )
                        )
                    }
                }
                Spacer(Modifier.width(WiomTheme.spacing.md))
                // Content column — title, description, optional action.
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = if (isLast) 0.dp else WiomTheme.spacing.xl),
                ) {
                    Text(
                        text = step.title,
                        style = WiomTheme.type.labelMd,
                        color = when (state) {
                            WiomStepState.Upcoming -> WiomTheme.color.text.subtle
                            else -> WiomTheme.color.text.default
                        },
                    )
                    if (step.description != null) {
                        Spacer(Modifier.height(WiomTheme.spacing.xxs))
                        Text(
                            text = step.description,
                            style = WiomTheme.type.bodyMd,
                            color = WiomTheme.color.text.subtle,
                        )
                    }
                    if (step.action != null) {
                        Spacer(Modifier.height(WiomTheme.spacing.md))
                        step.action.invoke()
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewStepIndicatorCompleted() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomStepIndicator(state = WiomStepState.Completed, number = 1)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewStepIndicatorActive() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomStepIndicator(state = WiomStepState.Active, number = 2)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewStepIndicatorUpcoming() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomStepIndicator(state = WiomStepState.Upcoming, number = 3)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF9FC)
@Composable
private fun PreviewStepIndicatorError() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomStepIndicator(state = WiomStepState.Error, number = 2)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 360)
@Composable
private fun PreviewStepperHorizontal() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomStepperHorizontal(
            steps = listOf(
                WiomHorizontalStep("Plan"),
                WiomHorizontalStep("Method"),
                WiomHorizontalStep("Confirm"),
                WiomHorizontalStep("Pay"),
                WiomHorizontalStep("Done"),
            ),
            currentStep = 3,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 360)
@Composable
private fun PreviewStepperHorizontalWithError() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomStepperHorizontal(
            steps = listOf(
                WiomHorizontalStep("Plan"),
                WiomHorizontalStep("Method"),
                WiomHorizontalStep("Pay"),
                WiomHorizontalStep("Done"),
            ),
            currentStep = 3,
            stateOverrides = mapOf(2 to WiomStepState.Error),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 360)
@Composable
private fun PreviewStepperVertical() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomStepperVertical(
            steps = listOf(
                WiomVerticalStep(
                    title = "Order placed",
                    description = "We've received your request.",
                ),
                WiomVerticalStep(
                    title = "Approved",
                    description = "Plan approved; preparing dispatch.",
                ),
                WiomVerticalStep(
                    title = "Installed",
                    description = "Technician assigned for tomorrow 10 AM.",
                ),
                WiomVerticalStep(
                    title = "Activated",
                    description = "Your connection will go live after install.",
                ),
            ),
            currentStep = 3,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 360)
@Composable
private fun PreviewStepperVerticalWithAction() {
    com.wiom.designsystem.theme.WiomTheme {
        WiomStepperVertical(
            steps = listOf(
                WiomVerticalStep(
                    title = "Personal info",
                    description = "Name, DOB, phone verified.",
                ),
                WiomVerticalStep(
                    title = "Address",
                    description = "Service pincode and street confirmed.",
                ),
                WiomVerticalStep(
                    title = "Aadhaar",
                    description = "Enter the OTP sent to your registered number.",
                    action = {
                        Text(
                            text = "[ OTP field slot ]",
                            style = WiomTheme.type.labelMd,
                            color = WiomTheme.color.text.brand,
                        )
                    },
                ),
                WiomVerticalStep(
                    title = "Selfie",
                    description = "Take a clear photo in good light.",
                ),
            ),
            currentStep = 3,
        )
    }
}
