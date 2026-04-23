package com.wiom.designsystem.foundation.typography

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.wiom.designsystem.R

/**
 * Wiom typography — 13 tokens on Noto Sans.
 *
 * Default reading text = `body.lg` (16sp Regular). Default interactive = `label.lg` (16sp SemiBold).
 * Page/dialog/sheet headers default to `heading.lg` (24 Bold). Chat bubble body is an
 * intentional exception at `body.md` (14). Never use sub-14sp for Hindi body text.
 */
@Immutable
data class WiomTypography(
    val displayLg: TextStyle,
    val headingXl: TextStyle,
    val headingLg: TextStyle,
    val headingMd: TextStyle,
    val titleLg: TextStyle,
    val titleSm: TextStyle,
    val bodyLg: TextStyle,     // ★ default reading text
    val bodyMd: TextStyle,     // card desc / chat bubble / helper / inline error
    val bodySm: TextStyle,     // caption / chat reply preview / timestamp
    val labelLg: TextStyle,    // ★ default interactive (CTA / input label / radio-checkbox-switch / toast action)
    val labelMd: TextStyle,    // chip / filter / tab / badge / "Edit" / "See all"
    val labelSm: TextStyle,    // chat reply sender / bottom nav
    val metaXs: TextStyle,     // ⚠ chat-bubble timestamp / badge count ONLY
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
    displayLg = TextStyle(fontFamily = fontFamily, fontSize = 48.sp, lineHeight = 64.sp, fontWeight = FontWeight.Bold),
    headingXl = TextStyle(fontFamily = fontFamily, fontSize = 32.sp, lineHeight = 44.sp, fontWeight = FontWeight.Bold),
    headingLg = TextStyle(fontFamily = fontFamily, fontSize = 24.sp, lineHeight = 32.sp, fontWeight = FontWeight.Bold),
    headingMd = TextStyle(fontFamily = fontFamily, fontSize = 20.sp, lineHeight = 28.sp, fontWeight = FontWeight.Bold),
    titleLg  = TextStyle(fontFamily = fontFamily, fontSize = 20.sp, lineHeight = 28.sp, fontWeight = FontWeight.Medium),
    titleSm  = TextStyle(fontFamily = fontFamily, fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Bold),
    bodyLg   = TextStyle(fontFamily = fontFamily, fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Normal),
    bodyMd   = TextStyle(fontFamily = fontFamily, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Normal),
    bodySm   = TextStyle(fontFamily = fontFamily, fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Normal),
    labelLg  = TextStyle(fontFamily = fontFamily, fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.SemiBold),
    labelMd  = TextStyle(fontFamily = fontFamily, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.SemiBold),
    labelSm  = TextStyle(fontFamily = fontFamily, fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.SemiBold),
    metaXs   = TextStyle(fontFamily = fontFamily, fontSize = 10.sp, lineHeight = 16.sp, fontWeight = FontWeight.Normal),
)

val LocalWiomTypography = staticCompositionLocalOf { wiomTypography() }
