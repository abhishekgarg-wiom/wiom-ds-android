package com.wiom.designsystem.foundation.color

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.wiom.designsystem.foundation.color.WiomColorPrimitives as P

/**
 * Wiom colors — element-first semantic tokens.
 *
 * Four namespaces mapped 1:1 to Compose slots:
 *   - [Bg]      → Modifier.background(...), Surface(color=...), Box(background=...)
 *   - [Text]    → Text(color=...)
 *   - [Stroke]  → Modifier.border(...), BorderStroke(...), Divider
 *   - [Icon]    → Icon(tint=...)
 *
 * Plus [Overlay] (the single `scrim` for modal backdrops).
 *
 * Never use raw hex in feature code. All values resolve from [WiomColorPrimitives]
 * (internal) — feature code accesses semantic tokens only.
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
        // Neutrals
        val default: Color,           // page / card / input / sheet
        val subtle: Color,            // hover tint, section cards, shimmer
        val muted: Color,             // grouped sections, skeleton fills
        val disabled: Color,          // disabled component fill
        val inverse: Color,           // dark hero / preview / footer
        val selected: Color,          // active chip / current row / selected tab
        // Brand (non-action surfaces)
        val brandSubtle: Color,       // soft brand surface · Secondary/Tertiary CTA pressed fill
        val brandBold: Color,         // premium / partner header (non-action)
        // Status (non-action surfaces)
        val positiveSubtle: Color,    // success banner / card bg
        val positive: Color,          // solid success fill (badge / chip)
        val criticalSubtle: Color,    // error banner / card bg
        val warningSubtle: Color,     // warning banner / card bg — pairs with text.onWarning (8.76:1 AA)
        val warning: Color,           // solid warning fill (badge / chip) — pairs with text.onWarning (7.16:1 AA). Yellow gold, never orange.
        val infoSubtle: Color,        // info banner / card bg
        val info: Color,              // solid info fill (badge)
        // Brand action surfaces
        val brand: Color,             // Primary CTA fill
        val brandHover: Color,        // Primary hover
        val brandPressed: Color,      // Primary pressed
        val brandAccent: Color,       // Pre-booking CTA fill (customer app only)
        val brandAccentPressed: Color,
        // Critical action surfaces (destructive only)
        val critical: Color,          // Destructive CTA fill
        val criticalHover: Color,
        val criticalPressed: Color,
    )

    @Immutable
    data class Text(
        val default: Color,           // primary reading text on light surfaces (NOT warning surfaces)
        val subtle: Color,            // descriptions, metadata, helper, placeholder copy via text.disabled
        val disabled: Color,          // disabled text + input placeholder (one token, two sanctioned uses)
        val inverse: Color,           // text on dark surfaces (bg.inverse, bg.brand.bold)
        val selected: Color,          // selected tab label, active chip, current nav
        val brand: Color,             // Secondary/Tertiary CTA text, inline link
        val brandHover: Color,        // hover state for brand-tinted text
        val onBrand: Color,           // text on bg.brand (Primary CTA label) — #FFFFFF, distinct from icon.inverse
        val onBrandAccent: Color,     // text on bg.brand.accent — DARK because bright pink fails AA with white
        val critical: Color,          // error heading, inline error, required asterisk
        val onCritical: Color,        // text on bg.critical (Destructive CTA label)
        val positive: Color,          // success heading
        val onPositive: Color,        // text on bg.positive
        val onWarning: Color,         // THE warning text token. Valid on bg.warning (7.16:1), bg.warningSubtle (8.76:1), AND bg.default (14.18:1). No separate text.warning exists — warning is a one-token family.
        val info: Color,              // info heading, info link
        val onInfo: Color,            // text on bg.info
    )

    @Immutable
    data class Stroke(
        // Neutrals
        val subtle: Color,            // card outlines, input rest border
        val default: Color,           // inner-card divider, lighter separator
        val strong: Color,            // small-control rest border (checkbox/radio/switch unchecked)
        val selected: Color,          // selected card/chip border (= stroke.brand.focus)
        // Brand
        val brand: Color,             // soft decorative brand border (Brand_300)
        val brandFocus: Color,        // saturated — Secondary CTA rest border, keyboard focus ring
        // Status outlines (soft) + focus rings (saturated)
        val positive: Color,          // success card outline (soft)
        val positiveFocus: Color,     // focus ring on positive elements
        val critical: Color,          // error outline (soft)
        val criticalFocus: Color,     // invalid input · destructive focus
        val warning: Color,           // warning outline (saturated gold) — inside bg.warningSubtle only. No separate stroke.warningFocus — warning has no focus state.
        val info: Color,              // info card outline (soft)
        val infoFocus: Color,         // focus ring on info elements
    )

    @Immutable
    data class Icon(
        val action: Color,            // tappable chrome (back, close, menu, filter)
        val nonAction: Color,         // decorative / non-tappable. Neutral_500 — passes graphics AA on bg.default (3.48:1)
        val inverse: Color,           // on dark surfaces · inside Primary/Destructive CTAs. #FAF9FC, NOT #FFFFFF — solid icons are visually heavier than glyphs at the same hex, so icons step one lightness lighter than their text equivalents.
        val brand: Color,             // Secondary/Tertiary CTA icons, selected nav, focused chevron
        val critical: Color,
        val positive: Color,
        val warning: Color,           // ONLY on bg.warning/bg.warningSubtle, or inside RatingBar/StarIcon. Fails graphics AA on bg.default (1.6:1).
        val info: Color,
        val disabled: Color,          // same hex as text.disabled (Neutral_400). Distinct from icon.nonAction (Neutral_500, darker for AA on light surfaces).
    )

    @Immutable
    data class Overlay(
        val scrim: Color,             // rgba(22,16,33,0.50) — modal backdrop / bottom-sheet scrim. THE only overlay token.
    )
}

fun lightWiomColors(): WiomColors = WiomColors(
    bg = WiomColors.Bg(
        default = P.Neutral_50,
        subtle = P.Neutral_100,
        muted = P.Neutral_200,
        disabled = P.Neutral_300,
        inverse = P.Neutral_950,
        selected = P.Brand_200,
        brandSubtle = P.Brand_100,
        brandBold = P.Brand_900,
        positiveSubtle = P.Positive_100,
        positive = P.Positive_600,
        criticalSubtle = P.Critical_100,
        warningSubtle = P.Warning_100,    // #FFE9A1
        warning = P.Warning_300,          // #E2B203 gold (never orange)
        infoSubtle = P.Info_100,
        info = P.Info_600,
        brand = P.Brand_600,
        brandHover = P.Brand_700,
        brandPressed = P.Brand_800,
        brandAccent = P.Brand_400,
        brandAccentPressed = P.Brand_500,
        critical = P.Critical_600,
        criticalHover = P.Critical_700,
        criticalPressed = P.Critical_800,
    ),
    text = WiomColors.Text(
        default = P.Neutral_950,
        subtle = P.Neutral_700,
        disabled = P.Neutral_400,
        inverse = P.Neutral_50,
        selected = P.Brand_600,
        brand = P.Brand_600,
        brandHover = P.Brand_700,
        onBrand = P.Gray_50,              // #FFFFFF — distinct from icon.inverse (#FAF9FC)
        onBrandAccent = P.Neutral_950,    // dark label on bright Pre-booking pink (white fails AA)
        critical = P.Critical_600,
        onCritical = P.Gray_50,
        positive = P.Positive_600,
        onPositive = P.Gray_50,
        onWarning = P.Warning_900,        // #372902 dark olive — pairs with bg.warning, bg.warningSubtle, and bg.default
        info = P.Info_600,
        onInfo = P.Gray_50,
    ),
    stroke = WiomColors.Stroke(
        subtle = P.Neutral_300,
        default = P.Neutral_200,
        strong = P.Neutral_800,
        selected = P.Brand_600,
        brand = P.Brand_300,              // soft outline
        brandFocus = P.Brand_600,         // saturated — Secondary CTA / focus ring
        positive = P.Positive_300,
        positiveFocus = P.Positive_600,
        critical = P.Critical_300,
        criticalFocus = P.Critical_600,
        warning = P.Warning_300,
        info = P.Info_300,
        infoFocus = P.Info_600,
    ),
    icon = WiomColors.Icon(
        action = P.Neutral_900,
        nonAction = P.Neutral_500,
        inverse = P.Neutral_50,           // #FAF9FC (one step lighter than #FFFFFF — calibrated for icons, see foundations §icon)
        brand = P.Brand_600,
        critical = P.Critical_600,
        positive = P.Positive_600,
        warning = P.Warning_300,
        info = P.Info_600,
        disabled = P.Neutral_400,
    ),
    overlay = WiomColors.Overlay(
        scrim = Color(0x80161021),        // rgba(22,16,33,0.50) — alpha 128 of Neutral_950
    ),
)

val LocalWiomColors = staticCompositionLocalOf { lightWiomColors() }
