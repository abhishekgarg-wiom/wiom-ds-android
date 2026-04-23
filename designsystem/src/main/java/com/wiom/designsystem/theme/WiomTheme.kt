package com.wiom.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.wiom.designsystem.foundation.color.LocalWiomColors
import com.wiom.designsystem.foundation.color.WiomColors
import com.wiom.designsystem.foundation.color.lightWiomColors
import com.wiom.designsystem.foundation.icon.LocalWiomIconSize
import com.wiom.designsystem.foundation.icon.WiomIconSize
import com.wiom.designsystem.foundation.radius.LocalWiomRadius
import com.wiom.designsystem.foundation.radius.WiomRadius
import com.wiom.designsystem.foundation.shadow.LocalWiomShadow
import com.wiom.designsystem.foundation.shadow.WiomShadow
import com.wiom.designsystem.foundation.spacing.LocalWiomSpacing
import com.wiom.designsystem.foundation.spacing.WiomSpacing
import com.wiom.designsystem.foundation.stroke.LocalWiomStroke
import com.wiom.designsystem.foundation.stroke.WiomStroke
import com.wiom.designsystem.foundation.typography.LocalWiomTypography
import com.wiom.designsystem.foundation.typography.WiomTypography
import com.wiom.designsystem.foundation.typography.wiomTypography

/**
 * Root theme. Wrap the app once in `WiomTheme { }` and access tokens via
 * [WiomTheme.color] / [WiomTheme.type] / [WiomTheme.spacing] / [WiomTheme.radius] /
 * [WiomTheme.stroke] / [WiomTheme.shadow] / [WiomTheme.iconSize].
 */
@Composable
fun WiomTheme(
    colors: WiomColors = lightWiomColors(),
    typography: WiomTypography = wiomTypography(),
    spacing: WiomSpacing = WiomSpacing(),
    radius: WiomRadius = WiomRadius(),
    stroke: WiomStroke = WiomStroke(),
    shadow: WiomShadow = WiomShadow(),
    iconSize: WiomIconSize = WiomIconSize(),
    content: @Composable () -> Unit,
) {
    val materialColorScheme = lightColorScheme(
        primary = colors.bg.brand,
        onPrimary = colors.text.onBrand,
        secondary = colors.bg.brandBold,
        onSecondary = colors.text.inverse,
        error = colors.bg.critical,
        onError = colors.text.onCritical,
        background = colors.bg.default,
        onBackground = colors.text.default,
        surface = colors.bg.default,
        onSurface = colors.text.default,
        surfaceVariant = colors.bg.subtle,
        onSurfaceVariant = colors.text.subtle,
        outline = colors.stroke.subtle,
    )
    val materialTypography = Typography(
        displayLarge = typography.displayLg,
        headlineLarge = typography.headingXl,
        headlineMedium = typography.headingLg,
        headlineSmall = typography.headingMd,
        titleLarge = typography.titleLg,
        titleMedium = typography.titleSm,
        titleSmall = typography.labelMd,
        bodyLarge = typography.bodyLg,
        bodyMedium = typography.bodyMd,
        bodySmall = typography.bodySm,
        labelLarge = typography.labelLg,
        labelMedium = typography.labelMd,
        labelSmall = typography.labelSm,
    )
    CompositionLocalProvider(
        LocalWiomColors provides colors,
        LocalWiomTypography provides typography,
        LocalWiomSpacing provides spacing,
        LocalWiomRadius provides radius,
        LocalWiomStroke provides stroke,
        LocalWiomShadow provides shadow,
        LocalWiomIconSize provides iconSize,
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = materialTypography,
            content = content,
        )
    }
}

object WiomTheme {
    val color: WiomColors @Composable @ReadOnlyComposable get() = LocalWiomColors.current
    val type: WiomTypography @Composable @ReadOnlyComposable get() = LocalWiomTypography.current
    val spacing: WiomSpacing @Composable @ReadOnlyComposable get() = LocalWiomSpacing.current
    val radius: WiomRadius @Composable @ReadOnlyComposable get() = LocalWiomRadius.current
    val stroke: WiomStroke @Composable @ReadOnlyComposable get() = LocalWiomStroke.current
    val shadow: WiomShadow @Composable @ReadOnlyComposable get() = LocalWiomShadow.current
    val iconSize: WiomIconSize @Composable @ReadOnlyComposable get() = LocalWiomIconSize.current
}
