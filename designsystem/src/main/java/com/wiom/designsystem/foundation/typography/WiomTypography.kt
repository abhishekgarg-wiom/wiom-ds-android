package com.wiom.designsystem.foundation.typography

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Wiom typography scale — 13 semantic tokens.
 *
 * Font: Noto Sans (target — v0.1 ships with FontFamily.Default;
 * wire Google Fonts + Noto Sans in v0.2).
 */
@Immutable
data class WiomTypography(
    val displayLg: TextStyle,
    val headingXl: TextStyle,
    val headingLg: TextStyle,
    val headingMd: TextStyle,
    val titleLg: TextStyle,
    val titleSm: TextStyle,
    val bodyLg: TextStyle,
    val bodyMd: TextStyle,
    val bodySm: TextStyle,
    val labelLg: TextStyle,
    val labelMd: TextStyle,
    val labelSm: TextStyle,
    val metaXs: TextStyle,
)

fun wiomTypography(
    fontFamily: FontFamily = FontFamily.Default,
): WiomTypography = WiomTypography(
    displayLg = TextStyle(
        fontFamily = fontFamily,
        fontSize = 48.sp,
        lineHeight = 64.sp,
        fontWeight = FontWeight.Bold,
    ),
    headingXl = TextStyle(
        fontFamily = fontFamily,
        fontSize = 32.sp,
        lineHeight = 44.sp,
        fontWeight = FontWeight.Bold,
    ),
    headingLg = TextStyle(
        fontFamily = fontFamily,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.Bold,
    ),
    headingMd = TextStyle(
        fontFamily = fontFamily,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Bold,
    ),
    titleLg = TextStyle(
        fontFamily = fontFamily,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Medium,
    ),
    titleSm = TextStyle(
        fontFamily = fontFamily,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Bold,
    ),
    bodyLg = TextStyle(
        fontFamily = fontFamily,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal,
    ),
    bodyMd = TextStyle(
        fontFamily = fontFamily,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Normal,
    ),
    bodySm = TextStyle(
        fontFamily = fontFamily,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Normal,
    ),
    labelLg = TextStyle(
        fontFamily = fontFamily,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    labelMd = TextStyle(
        fontFamily = fontFamily,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    labelSm = TextStyle(
        fontFamily = fontFamily,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    metaXs = TextStyle(
        fontFamily = fontFamily,
        fontSize = 10.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Normal,
    ),
)

val LocalWiomTypography = staticCompositionLocalOf { wiomTypography() }
