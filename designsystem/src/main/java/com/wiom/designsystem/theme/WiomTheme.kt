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
 * Root theme for Wiom apps. Wrap your app once in `WiomTheme { ... }` and use
 * [WiomTheme.colors], [WiomTheme.type], [WiomTheme.spacing], [WiomTheme.radius],
 * [WiomTheme.stroke], [WiomTheme.shadow], [WiomTheme.icon] anywhere underneath.
 *
 * Also bridges Material3's [MaterialTheme] so Material components (Button, TextField, etc.)
 * pick up approximate Wiom colors and typography.
 */
@Composable
fun WiomTheme(
    colors: WiomColors = lightWiomColors(),
    typography: WiomTypography = wiomTypography(),
    spacing: WiomSpacing = WiomSpacing(),
    radius: WiomRadius = WiomRadius(),
    stroke: WiomStroke = WiomStroke(),
    shadow: WiomShadow = WiomShadow(),
    icon: WiomIconSize = WiomIconSize(),
    content: @Composable () -> Unit,
) {
    val materialColorScheme = lightColorScheme(
        primary = colors.brand.primary,
        onPrimary = colors.text.onColor,
        secondary = colors.brand.secondary,
        onSecondary = colors.text.inverse,
        error = colors.negative.primary,
        onError = colors.text.onColor,
        background = colors.surface.base,
        onBackground = colors.text.primary,
        surface = colors.surface.base,
        onSurface = colors.text.primary,
        surfaceVariant = colors.surface.subtle,
        onSurfaceVariant = colors.text.secondary,
        outline = colors.border.default,
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
        LocalWiomIconSize provides icon,
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = materialTypography,
            content = content,
        )
    }
}

/** Access Wiom tokens anywhere under [WiomTheme]. */
object WiomTheme {
    val colors: WiomColors
        @Composable @ReadOnlyComposable
        get() = LocalWiomColors.current

    val type: WiomTypography
        @Composable @ReadOnlyComposable
        get() = LocalWiomTypography.current

    val spacing: WiomSpacing
        @Composable @ReadOnlyComposable
        get() = LocalWiomSpacing.current

    val radius: WiomRadius
        @Composable @ReadOnlyComposable
        get() = LocalWiomRadius.current

    val stroke: WiomStroke
        @Composable @ReadOnlyComposable
        get() = LocalWiomStroke.current

    val shadow: WiomShadow
        @Composable @ReadOnlyComposable
        get() = LocalWiomShadow.current

    val icon: WiomIconSize
        @Composable @ReadOnlyComposable
        get() = LocalWiomIconSize.current
}
