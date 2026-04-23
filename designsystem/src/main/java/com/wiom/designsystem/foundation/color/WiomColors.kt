package com.wiom.designsystem.foundation.color

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Wiom colors — element-first semantic tokens.
 *
 * Four namespaces mapped 1:1 to Compose slots:
 *  - [Bg]      → Modifier.background(...), Surface(color=...), Box(background=...)
 *  - [Text]    → Text(color=...)
 *  - [Stroke]  → Modifier.border(...), BorderStroke(...), Divider
 *  - [Icon]    → Icon(tint=...)
 *
 * Never use raw hex in UI code. Legacy role-first tokens (`color.brand.primary`,
 * `color.warning.primary`) are GONE — element-first is the only API.
 */
@Immutable
data class WiomColors(
    val bg: Bg,
    val text: Text,
    val stroke: Stroke,
    val icon: Icon,
    val overlay: Overlay,
) {
    @Immutable
    data class Bg(
        val default: Color,           // #FAF9FC — page / card / input / sheet
        val subtle: Color,            // #F1EDF7 — hover tint, disabled fill, section cards
        val muted: Color,             // #E8E4F0 — grouped sections, alt rows
        val inverse: Color,           // #161021 — dark hero / preview / footer
        val selected: Color,          // #FFCCED (Brand_200) — active chip / current row / selected tab
        val disabled: Color,          // #D7D3E0 — disabled component fill
        val brand: Color,             // #D9008D (Brand_600) — Primary CTA, brand emphasis
        val brandHover: Color,        // #C00080 — Primary hover
        val brandPressed: Color,      // #A30070 — Primary pressed
        val brandSubtle: Color,       // #FFE5F6 — brand-tinted cards, secondary pressed
        val brandBold: Color,         // #443152 (Brand_900) — premium / partner header
        val brandAccent: Color,       // #FF66C9 (Brand_400) — Pre-booking CTA
        val brandAccentPressed: Color,// #EA5DB8
        val critical: Color,          // #D92130 — Destructive CTA
        val criticalHover: Color,     // #BF1D2A
        val criticalPressed: Color,   // #A31824
        val criticalSubtle: Color,    // #FFE5E7 — error banner
        val positive: Color,          // #008043
        val positiveSubtle: Color,    // #E1FAED — success banner
        val warning: Color,           // #FFDA40 (Warning_300 gold) — warning fill (yellow, never orange)
        val warningSubtle: Color,     // #FFF2BF — warning banner
        val info: Color,              // #6D17CE
        val infoSubtle: Color,        // #F1E5FF
    )

    @Immutable
    data class Text(
        val default: Color,           // #161021 — primary reading text
        val subtle: Color,            // #5C5570 — description, metadata, helper
        val disabled: Color,          // #A7A1B2 — disabled text + input placeholder (one token, two uses)
        val inverse: Color,           // #FAF9FC — on dark surfaces
        val brand: Color,             // #D9008D — Secondary/Tertiary CTA text, inline link
        val brandHover: Color,        // #C00080
        val onBrand: Color,           // #FFFFFF — on bg.brand
        val onBrandAccent: Color,     // #161021 — on bg.brandAccent (bright pink needs DARK label for AA)
        val critical: Color,          // #D92130 — error heading, inline error
        val onCritical: Color,        // #FFFFFF — on bg.critical
        val positive: Color,          // #008043 — success heading
        val onPositive: Color,        // #FFFFFF — on bg.positive
        val onWarning: Color,         // #372902 — THE warning text token. Valid on bg.warning, bg.warningSubtle, AND bg.default (no separate text.warning exists)
        val info: Color,              // #6D17CE — info heading, info link
        val onInfo: Color,            // #FFFFFF — on bg.info
        val selected: Color,          // #D9008D — text/icon on bg.selected
    )

    @Immutable
    data class Stroke(
        val subtle: Color,            // #D7D3E0 — card outline, input rest border
        val default: Color,           // #E8E4F0 — inner divider, lighter separator
        val strong: Color,            // #473F55 — small-control rest border (checkbox/radio/switch unchecked)
        val selected: Color,          // #D9008D — selected card/chip border (= stroke.brand.focus)
        val brandFocus: Color,        // #D9008D — keyboard focus ring · Secondary CTA rest border · active input
        val criticalFocus: Color,     // #D92130 — invalid input · destructive focus
        val positiveFocus: Color,     // #008043
        val warningFocus: Color,      // #E2B203 (Warning_300 gold) — warning-state input border
        val infoFocus: Color,         // #6D17CE
    )

    @Immutable
    data class Icon(
        val action: Color,            // #352D42 (Neutral_900) — tappable chrome icons (back, close, menu, filter)
        val nonAction: Color,         // #898296 (Neutral_500) — decorative / non-tappable icons, passes AA (3.48:1 on bg.default)
        val inverse: Color,           // #FFFFFF — on colored CTA fills (Primary / Destructive)
        val brand: Color,             // #D9008D — Secondary/Tertiary CTA icons
        val critical: Color,          // #D92130
        val positive: Color,          // #008043
        val warning: Color,           // #E2B203 — ONLY on bg.warning/bg.warningSubtle surfaces (invisible on white)
        val info: Color,              // #6D17CE
        val disabled: Color,          // #A7A1B2 — same hex as text.disabled; stays visible on bg.disabled (#D7D3E0)
    )

    @Immutable
    data class Overlay(
        val scrim: Color,             // rgba(22,16,33,0.50) — modal backdrop / bottom-sheet scrim (THE ONLY overlay token)
    )
}

fun lightWiomColors(): WiomColors = WiomColors(
    bg = WiomColors.Bg(
        default = Color(0xFFFAF9FC),
        subtle = Color(0xFFF1EDF7),
        muted = Color(0xFFE8E4F0),
        inverse = Color(0xFF161021),
        selected = Color(0xFFFFCCED),
        disabled = Color(0xFFD7D3E0),
        brand = Color(0xFFD9008D),
        brandHover = Color(0xFFC00080),
        brandPressed = Color(0xFFA30070),
        brandSubtle = Color(0xFFFFE5F6),
        brandBold = Color(0xFF443152),
        brandAccent = Color(0xFFFF66C9),
        brandAccentPressed = Color(0xFFEA5DB8),
        critical = Color(0xFFD92130),
        criticalHover = Color(0xFFBF1D2A),
        criticalPressed = Color(0xFFA31824),
        criticalSubtle = Color(0xFFFFE5E7),
        positive = Color(0xFF008043),
        positiveSubtle = Color(0xFFE1FAED),
        warning = Color(0xFFFFDA40),
        warningSubtle = Color(0xFFFFF2BF),
        info = Color(0xFF6D17CE),
        infoSubtle = Color(0xFFF1E5FF),
    ),
    text = WiomColors.Text(
        default = Color(0xFF161021),
        subtle = Color(0xFF5C5570),
        disabled = Color(0xFFA7A1B2),
        inverse = Color(0xFFFAF9FC),
        brand = Color(0xFFD9008D),
        brandHover = Color(0xFFC00080),
        onBrand = Color(0xFFFFFFFF),
        onBrandAccent = Color(0xFF161021),
        critical = Color(0xFFD92130),
        onCritical = Color(0xFFFFFFFF),
        positive = Color(0xFF008043),
        onPositive = Color(0xFFFFFFFF),
        onWarning = Color(0xFF372902),
        info = Color(0xFF6D17CE),
        onInfo = Color(0xFFFFFFFF),
        selected = Color(0xFFD9008D),
    ),
    stroke = WiomColors.Stroke(
        subtle = Color(0xFFD7D3E0),
        default = Color(0xFFE8E4F0),
        strong = Color(0xFF473F55),
        selected = Color(0xFFD9008D),
        brandFocus = Color(0xFFD9008D),
        criticalFocus = Color(0xFFD92130),
        positiveFocus = Color(0xFF008043),
        warningFocus = Color(0xFFE2B203),
        infoFocus = Color(0xFF6D17CE),
    ),
    icon = WiomColors.Icon(
        action = Color(0xFF352D42),
        nonAction = Color(0xFF898296),
        inverse = Color(0xFFFFFFFF),
        brand = Color(0xFFD9008D),
        critical = Color(0xFFD92130),
        positive = Color(0xFF008043),
        warning = Color(0xFFE2B203),
        info = Color(0xFF6D17CE),
        disabled = Color(0xFFA7A1B2),
    ),
    overlay = WiomColors.Overlay(
        scrim = Color(0x80161021),
    ),
)

val LocalWiomColors = staticCompositionLocalOf { lightWiomColors() }
