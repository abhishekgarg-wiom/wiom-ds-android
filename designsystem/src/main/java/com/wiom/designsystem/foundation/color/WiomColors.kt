package com.wiom.designsystem.foundation.color

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class WiomColors(
    val brand: Brand,
    val text: Text,
    val surface: Surface,
    val border: Border,
    val positive: Positive,
    val negative: Negative,
    val warning: Warning,
    val info: Info,
    val overlay: Overlay,
) {
    @Immutable
    data class Brand(
        val primary: Color,
        val primaryHover: Color,
        val primaryPressed: Color,
        val primarySoft: Color,
        val primarySubtle: Color,
        val primaryTint: Color,
        val secondary: Color,
    )

    @Immutable
    data class Text(
        val primary: Color,
        val secondary: Color,
        val disabled: Color,
        val inverse: Color,
        val link: Color,
        val linkHover: Color,
        val onColor: Color,
    )

    @Immutable
    data class Surface(
        val base: Color,
        val subtle: Color,
        val muted: Color,
        val strong: Color,
        val inverse: Color,
        val selected: Color,
    )

    @Immutable
    data class Border(
        val default: Color,
        val subtle: Color,
        val strong: Color,
        val brand: Color,
        val selected: Color,
    )

    @Immutable
    data class Positive(
        val primary: Color,
        val soft: Color,
        val subtle: Color,
        val tint: Color,
    )

    @Immutable
    data class Negative(
        val primary: Color,
        val primaryHover: Color,
        val primaryPressed: Color,
        val soft: Color,
        val subtle: Color,
        val tint: Color,
    )

    @Immutable
    data class Warning(
        val primary: Color,
        val soft: Color,
        val subtle: Color,
    )

    @Immutable
    data class Info(
        val primary: Color,
        val soft: Color,
        val subtle: Color,
        val tint: Color,
    )

    @Immutable
    data class Overlay(
        val default: Color,
    )
}

fun lightWiomColors(): WiomColors = WiomColors(
    brand = WiomColors.Brand(
        primary = Color(0xFFD9008D),
        primaryHover = Color(0xFFC00080),
        primaryPressed = Color(0xFFA30070),
        primarySoft = Color(0xFFFFB2E4),
        primarySubtle = Color(0xFFFFCCED),
        primaryTint = Color(0xFFFFE5F6),
        secondary = Color(0xFF4A1535),
    ),
    text = WiomColors.Text(
        primary = Color(0xFF161021),
        secondary = Color(0xFF5C5570),
        disabled = Color(0xFFA7A1B2),
        inverse = Color(0xFFFAF9FC),
        link = Color(0xFFD9008D),
        linkHover = Color(0xFFC00080),
        onColor = Color(0xFFFFFFFF),
    ),
    surface = WiomColors.Surface(
        base = Color(0xFFFAF9FC),
        subtle = Color(0xFFF1EDF7),
        muted = Color(0xFFE8E4F0),
        strong = Color(0xFFD7D3E0),
        inverse = Color(0xFF161021),
        selected = Color(0xFFFFE5F6),
    ),
    border = WiomColors.Border(
        default = Color(0xFFD7D3E0),
        subtle = Color(0xFFE8E4F0),
        strong = Color(0xFF665E75),
        brand = Color(0xFFFFB2E4),
        selected = Color(0xFFD9008D),
    ),
    positive = WiomColors.Positive(
        primary = Color(0xFF008043),
        soft = Color(0xFFA5E5C6),
        subtle = Color(0xFFC9F0DD),
        tint = Color(0xFFE1FAED),
    ),
    negative = WiomColors.Negative(
        primary = Color(0xFFD92130),
        primaryHover = Color(0xFFBF1D2A),
        primaryPressed = Color(0xFFA31824),
        soft = Color(0xFFFFB3B9),
        subtle = Color(0xFFFFCCD0),
        tint = Color(0xFFFFE5E7),
    ),
    warning = WiomColors.Warning(
        primary = Color(0xFFB85C00),
        soft = Color(0xFFFFDA40),
        subtle = Color(0xFFFFF2BF),
    ),
    info = WiomColors.Info(
        primary = Color(0xFF6D17CE),
        soft = Color(0xFFD6B2FF),
        subtle = Color(0xFFE4CCFF),
        tint = Color(0xFFF1E5FF),
    ),
    overlay = WiomColors.Overlay(
        default = Color(0x80161021),
    ),
)

val LocalWiomColors = staticCompositionLocalOf { lightWiomColors() }
