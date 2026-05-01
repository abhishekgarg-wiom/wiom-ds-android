package com.wiom.designsystem.foundation.color

import androidx.compose.ui.graphics.Color

/**
 * Wiom color primitives — 67 raw values across 7 families.
 *
 * **Internal — never import directly in feature code.** Feature code uses semantic
 * tokens via [WiomColors] (`WiomTheme.color.bg.*`, `text.*`, `stroke.*`, `icon.*`).
 *
 * This file is for design-system maintainers, dark-mode work, and theme extensions only.
 *
 * Step numbers reflect measured OKLCH lightness, matching Tailwind / Radix convention:
 * Neutral and Gray cover the full 50–950 range (11 steps each); Brand, Positive, Critical,
 * Warning, and Info use 100–900 (9 steps each).
 *
 * Anchors:
 *   - Brand_600 = #D9008D (non-negotiable)
 *   - Info_600  = #6D17CE
 *
 * Source: `wiom-design-foundations/references/color/primitives.md`.
 */
internal object WiomColorPrimitives {

    // ── Brand — 9 steps ────────────────────────────────────────────────────────
    val Brand_100 = Color(0xFFFFE5F6)
    val Brand_200 = Color(0xFFFFCCED)
    val Brand_300 = Color(0xFFFFB2E4)
    val Brand_400 = Color(0xFFFF66C9)
    val Brand_500 = Color(0xFFEA5DB8)
    val Brand_600 = Color(0xFFD9008D)
    val Brand_700 = Color(0xFFC00080)
    val Brand_800 = Color(0xFFA30070)
    val Brand_900 = Color(0xFF443152)

    // ── Neutral — 11 steps (warm mauve undertone) ──────────────────────────────
    val Neutral_50  = Color(0xFFFAF9FC)
    val Neutral_100 = Color(0xFFF1EDF7)
    val Neutral_200 = Color(0xFFE8E4F0)
    val Neutral_300 = Color(0xFFD7D3E0)
    val Neutral_400 = Color(0xFFA7A1B2)
    val Neutral_500 = Color(0xFF898296)
    val Neutral_600 = Color(0xFF736C82)
    // Upstream spec inconsistency at SHA ed110b6:
    //   - color/primitives.md says Neutral_700 = #665E75
    //   - color/core-tokens.md says text.subtle = #5C5570 → Neutral_700
    // We use the core-tokens.md value (#5C5570) — it's the canonical semantic-token
    // hex and matches all prior shipped versions. Tracked as upstream PR.
    val Neutral_700 = Color(0xFF5C5570)
    val Neutral_800 = Color(0xFF473F55)
    val Neutral_900 = Color(0xFF352D42)
    val Neutral_950 = Color(0xFF161021)

    // ── Gray — 11 steps (pure grayscale, no brand warmth — chat domain) ────────
    val Gray_50  = Color(0xFFFFFFFF)
    val Gray_100 = Color(0xFFF5F6F7)
    val Gray_200 = Color(0xFFEBEDF0)
    val Gray_300 = Color(0xFFB6B9BF)
    val Gray_400 = Color(0xFF9FA1A7)
    val Gray_500 = Color(0xFF84868A)
    val Gray_600 = Color(0xFF6E7074)
    val Gray_700 = Color(0xFF595B5E)
    val Gray_800 = Color(0xFF424446)
    val Gray_900 = Color(0xFF2A2B2D)
    val Gray_950 = Color(0xFF121314)

    // ── Positive — 9 steps (success green) ─────────────────────────────────────
    val Positive_100 = Color(0xFFE1FAED)
    val Positive_200 = Color(0xFFC9F0DD)
    val Positive_300 = Color(0xFFA5E5C6)
    val Positive_400 = Color(0xFF66B588)
    val Positive_500 = Color(0xFF409B66)
    val Positive_600 = Color(0xFF008043)
    val Positive_700 = Color(0xFF0B6D3A)
    val Positive_800 = Color(0xFF06522A)
    val Positive_900 = Color(0xFF033519)

    // ── Critical — 9 steps (error/destructive red) ─────────────────────────────
    val Critical_100 = Color(0xFFFFE5E7)
    val Critical_200 = Color(0xFFFFCCD0)
    val Critical_300 = Color(0xFFFFB3B9)
    val Critical_400 = Color(0xFFF07778)
    val Critical_500 = Color(0xFFE2484B)
    val Critical_600 = Color(0xFFD92130)
    val Critical_700 = Color(0xFFBF1D2A)
    val Critical_800 = Color(0xFFA31824)
    val Critical_900 = Color(0xFF5C0005)

    // ── Warning — 9 steps (yellow throughout, never orange) ────────────────────
    val Warning_100 = Color(0xFFFFE9A1)
    val Warning_200 = Color(0xFFFED461)
    val Warning_300 = Color(0xFFE2B203)
    val Warning_400 = Color(0xFFC59C17)
    val Warning_500 = Color(0xFFA48111)
    val Warning_600 = Color(0xFF896C0D)
    val Warning_700 = Color(0xFF705708)
    val Warning_800 = Color(0xFF544105)
    val Warning_900 = Color(0xFF372902)

    // ── Info — 9 steps (purple) ────────────────────────────────────────────────
    val Info_100 = Color(0xFFF1E5FF)
    val Info_200 = Color(0xFFE4CCFF)
    val Info_300 = Color(0xFFD6B2FF)
    val Info_400 = Color(0xFFAF81FF)
    val Info_500 = Color(0xFF965AFD)
    val Info_600 = Color(0xFF6D17CE)
    val Info_700 = Color(0xFF5600A9)
    val Info_800 = Color(0xFF3C007C)
    val Info_900 = Color(0xFF23004E)
}
