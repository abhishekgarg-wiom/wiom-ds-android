package com.wiom.designsystem.foundation.typography

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.wiom.designsystem.R

/**
 * Wiom typography — 13 tokens on Noto Sans.
 *
 * Default reading text = `bodyLg` (16sp Regular). Default interactive = `labelLg` (16sp SemiBold).
 * Page/dialog/sheet headers default to `headingLg` (24 Bold). Chat bubble body is an
 * intentional exception at `bodyMd` (14). Never use sub-14sp for Hindi body text.
 *
 * Tracking (letter-spacing) per spec:
 *   - Display / Heading / Title — negative tracking (-0.01 to -0.02em) for tight large-type rhythm
 *   - Body — 0
 *   - Label — +0.01em (slight openness for SemiBold interactive text)
 *   - Meta — +0.02em (10sp restricted to chat ts / badge counts)
 */
@Immutable
data class WiomTypography(
    val displayLg: TextStyle,
    val headingXl: TextStyle,
    val headingLg: TextStyle,
    val headingMd: TextStyle,
    val titleLg: TextStyle,
    val titleSm: TextStyle,
    val bodyLg: TextStyle,     // default reading text
    val bodyMd: TextStyle,     // card desc / chat bubble / helper / inline error
    val bodySm: TextStyle,     // caption / chat reply preview / timestamp
    val labelLg: TextStyle,    // default interactive (CTA / input label / radio-checkbox-switch / toast action)
    val labelMd: TextStyle,    // chip / filter / tab / badge / "Edit" / "See all"
    val labelSm: TextStyle,    // chat reply sender / bottom nav
    val metaXs: TextStyle,     // chat-bubble timestamp / badge count ONLY (10sp restricted)
)

private val notoSansProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs,
)

private val notoSans = GoogleFont("Noto Sans")

private val notoSansFamily = FontFamily(
    Font(googleFont = notoSans, fontProvider = notoSansProvider, weight = FontWeight.Normal),
    Font(googleFont = notoSans, fontProvider = notoSansProvider, weight = FontWeight.Medium),
    Font(googleFont = notoSans, fontProvider = notoSansProvider, weight = FontWeight.SemiBold),
    Font(googleFont = notoSans, fontProvider = notoSansProvider, weight = FontWeight.Bold),
)

fun wiomTypography(
    fontFamily: FontFamily = notoSansFamily,
): WiomTypography = WiomTypography(
    displayLg = TextStyle(fontFamily = fontFamily, fontSize = 48.sp, lineHeight = 64.sp, fontWeight = FontWeight.Bold,    letterSpacing = (-0.02).em),
    headingXl = TextStyle(fontFamily = fontFamily, fontSize = 32.sp, lineHeight = 44.sp, fontWeight = FontWeight.Bold,    letterSpacing = (-0.01).em),
    headingLg = TextStyle(fontFamily = fontFamily, fontSize = 24.sp, lineHeight = 32.sp, fontWeight = FontWeight.Bold,    letterSpacing = (-0.01).em),
    headingMd = TextStyle(fontFamily = fontFamily, fontSize = 20.sp, lineHeight = 28.sp, fontWeight = FontWeight.Bold,    letterSpacing = (-0.01).em),
    titleLg   = TextStyle(fontFamily = fontFamily, fontSize = 20.sp, lineHeight = 28.sp, fontWeight = FontWeight.Medium,  letterSpacing = (-0.01).em),
    titleSm   = TextStyle(fontFamily = fontFamily, fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Bold,    letterSpacing = 0.em),
    bodyLg    = TextStyle(fontFamily = fontFamily, fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Normal,  letterSpacing = 0.em),
    bodyMd    = TextStyle(fontFamily = fontFamily, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Normal,  letterSpacing = 0.em),
    bodySm    = TextStyle(fontFamily = fontFamily, fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Normal,  letterSpacing = 0.em),
    labelLg   = TextStyle(fontFamily = fontFamily, fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.01.em),
    labelMd   = TextStyle(fontFamily = fontFamily, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.01.em),
    labelSm   = TextStyle(fontFamily = fontFamily, fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.01.em),
    metaXs    = TextStyle(fontFamily = fontFamily, fontSize = 10.sp, lineHeight = 16.sp, fontWeight = FontWeight.Normal,  letterSpacing = 0.02.em),
)

val LocalWiomTypography = staticCompositionLocalOf { wiomTypography() }
