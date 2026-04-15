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
import com.wiom.designsystem.foundation.icon.WiomIcons
import com.wiom.designsystem.theme.WiomTheme

enum class WiomStepState { Completed, Active, Upcoming, Error }

/**
 * Step indicator atom — 32dp circle with state-driven fill, stroke, and inner glyph.
 *
 * Use through [WiomStepperHorizontal] or [WiomStepperVertical] in real screens;
 * this atom is exposed for documentation or non-standard surfaces.
 */
@Composable
fun WiomStepIndicator(
    state: WiomStepState,
    number: Int,
    modifier: Modifier = Modifier,
) {
    val fill: Color = when (state) {
        WiomStepState.Completed -> WiomTheme.colors.brand.primary
        WiomStepState.Error -> WiomTheme.colors.negative.primary
        else -> WiomTheme.colors.surface.base
    }
    val borderColor: Color = when (state) {
        WiomStepState.Active -> WiomTheme.colors.brand.primary
        WiomStepState.Upcoming -> WiomTheme.colors.border.default
        else -> Color.Transparent
    }
    val borderWidth = when (state) {
        WiomStepState.Active -> WiomTheme.stroke.medium
        WiomStepState.Upcoming -> WiomTheme.stroke.small
        else -> 0.dp
    }
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(fill)
            .border(width = borderWidth, color = borderColor, shape = CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        when (state) {
            WiomStepState.Completed -> WiomIcon(
                id = WiomIcons.check,
                contentDescription = null,
                size = 20.dp,
                tint = WiomTheme.colors.text.onColor,
            )
            WiomStepState.Error -> WiomIcon(
                id = WiomIcons.priorityHigh,
                contentDescription = null,
                size = 20.dp,
                tint = WiomTheme.colors.text.onColor,
            )
            else -> Text(
                text = number.toString(),
                style = WiomTheme.type.labelMd,
                color = if (state == WiomStepState.Active)
                    WiomTheme.colors.brand.primary
                else
                    WiomTheme.colors.text.secondary,
            )
        }
    }
}

data class WiomHorizontalStep(val label: String)

/**
 * Horizontal stepper — 2–6 steps with 1–2 word labels. For wizards (recharge, payment, KYC).
 *
 * The connector between steps fills brand-primary once the previous step is Completed;
 * otherwise uses border.default.
 */
@Composable
fun WiomStepperHorizontal(
    steps: List<WiomHorizontalStep>,
    currentStep: Int,
    modifier: Modifier = Modifier,
) {
    require(steps.size in 2..6) { "WiomStepperHorizontal supports 2–6 steps; got ${steps.size}" }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        steps.forEachIndexed { idx, step ->
            val stepNumber = idx + 1
            val state = stateFor(stepNumber = stepNumber, currentStep = currentStep)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
            ) {
                WiomStepIndicator(state = state, number = stepNumber)
                Text(
                    text = step.label,
                    style = WiomTheme.type.labelSm,
                    color = if (stepNumber <= currentStep) WiomTheme.colors.text.primary else WiomTheme.colors.text.secondary,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                )
            }
            if (idx < steps.lastIndex) {
                val filled = stepNumber < currentStep
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 15.dp)
                        .height(WiomTheme.stroke.medium)
                        .clip(RoundedCornerShape(WiomTheme.radius.full))
                        .background(
                            if (filled) WiomTheme.colors.brand.primary
                            else WiomTheme.colors.border.default
                        )
                )
            }
        }
    }
}

/**
 * One row of a vertical stepper — rail (indicator + connector) + content (title, optional
 * description, optional action slot). Used inside [WiomStepperVertical] but also exposed
 * for custom layouts.
 *
 * The action slot accepts any composable — typically [com.wiom.designsystem.component.button.WiomButton],
 * a tertiary button, or a [com.wiom.designsystem.component.input.WiomInput] for inline entry.
 */
data class WiomVerticalStep(
    val title: String,
    val description: String? = null,
    val action: (@Composable () -> Unit)? = null,
)

@Composable
fun WiomStepperVertical(
    steps: List<WiomVerticalStep>,
    currentStep: Int,
    modifier: Modifier = Modifier,
    stateOverrides: Map<Int, WiomStepState> = emptyMap(),
) {
    require(steps.size in 2..6) { "WiomStepperVertical supports 2–6 steps; got ${steps.size}" }
    Column(modifier = modifier.fillMaxWidth()) {
        steps.forEachIndexed { idx, step ->
            val stepNumber = idx + 1
            val state = stateOverrides[stepNumber] ?: stateFor(stepNumber, currentStep)
            val isLast = idx == steps.lastIndex
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier
                        .width(32.dp)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs),
                ) {
                    WiomStepIndicator(state = state, number = stepNumber)
                    if (!isLast) {
                        Box(
                            modifier = Modifier
                                .width(WiomTheme.stroke.medium)
                                .weight(1f)
                                .clip(RoundedCornerShape(WiomTheme.radius.full))
                                .background(
                                    if (stepNumber < currentStep) WiomTheme.colors.brand.primary
                                    else WiomTheme.colors.border.default
                                )
                        )
                    }
                }
                Spacer(modifier = Modifier.width(WiomTheme.spacing.md))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = if (isLast) 0.dp else WiomTheme.spacing.xl),
                    verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs),
                ) {
                    Text(
                        text = step.title,
                        style = WiomTheme.type.labelMd,
                        color = if (state == WiomStepState.Upcoming)
                            WiomTheme.colors.text.secondary
                        else
                            WiomTheme.colors.text.primary,
                    )
                    step.description?.let {
                        Text(text = it, style = WiomTheme.type.bodySm, color = WiomTheme.colors.text.secondary)
                    }
                    step.action?.let {
                        Spacer(modifier = Modifier.height(WiomTheme.spacing.sm))
                        it()
                    }
                }
            }
        }
    }
}

private fun stateFor(stepNumber: Int, currentStep: Int): WiomStepState = when {
    stepNumber < currentStep -> WiomStepState.Completed
    stepNumber == currentStep -> WiomStepState.Active
    else -> WiomStepState.Upcoming
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun StepperHorizontalPreview() = WiomTheme {
    Column(
        modifier = Modifier
            .background(WiomTheme.colors.surface.base)
            .padding(WiomTheme.spacing.lg),
    ) {
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

@Preview(showBackground = true, widthDp = 360, heightDp = 500)
@Composable
private fun StepperVerticalPreview() = WiomTheme {
    Column(
        modifier = Modifier
            .background(WiomTheme.colors.surface.base)
            .padding(WiomTheme.spacing.lg),
    ) {
        WiomStepperVertical(
            steps = listOf(
                WiomVerticalStep(title = "Personal info", description = "Name, DOB, email verified"),
                WiomVerticalStep(title = "Address", description = "Installation address confirmed"),
                WiomVerticalStep(
                    title = "Aadhaar",
                    description = "Enter the OTP sent to your registered mobile",
                ),
                WiomVerticalStep(title = "Selfie", description = "Clear photo in good lighting"),
            ),
            currentStep = 3,
        )
    }
}
