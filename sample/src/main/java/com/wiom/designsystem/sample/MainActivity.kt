package com.wiom.designsystem.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.theme.WiomTheme

/**
 * v1.0.0 sample app — **foundations health-check**.
 *
 * This screen shows the theme is wiring tokens + typography + icons correctly.
 * Per-component visual review lives in each component's `@Preview` blocks
 * (open any `Wiom*.kt` file and use Android Studio's design view).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WiomTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = WiomTheme.color.bg.default) {
                    Showcase()
                }
            }
        }
    }
}

@Composable
private fun Showcase() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(WiomTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xl),
    ) {
        Header()
        ColorsSwatch()
        TypographySpecimen()
        IconsShowcase()
    }
}

@Composable
private fun Header() {
    Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs)) {
        Text("Wiom DS — v1.0.0", style = WiomTheme.type.headingLg, color = WiomTheme.color.text.default)
        Text(
            "Element-first tokens · Material 3 Icons Rounded · Noto Sans · 16 components.",
            style = WiomTheme.type.bodyMd,
            color = WiomTheme.color.text.subtle,
        )
    }
}

@Composable
private fun ColorsSwatch() {
    Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
        Text("Colors", style = WiomTheme.type.titleSm, color = WiomTheme.color.text.default)
        Row(horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
            Swatch(WiomTheme.color.bg.brand, "brand")
            Swatch(WiomTheme.color.bg.critical, "critical")
            Swatch(WiomTheme.color.bg.positive, "positive")
            Swatch(WiomTheme.color.bg.warning, "warning")
            Swatch(WiomTheme.color.bg.info, "info")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
            Swatch(WiomTheme.color.bg.brandSubtle, "brandSubtle")
            Swatch(WiomTheme.color.bg.criticalSubtle, "criticalSubtle")
            Swatch(WiomTheme.color.bg.positiveSubtle, "positiveSubtle")
            Swatch(WiomTheme.color.bg.warningSubtle, "warningSubtle")
            Swatch(WiomTheme.color.bg.infoSubtle, "infoSubtle")
        }
    }
}

@Composable
private fun Swatch(color: Color, label: String) {
    Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs)) {
        Box(
            modifier = Modifier
                .size(WiomTheme.spacing.huge)
                .background(color, RoundedCornerShape(WiomTheme.radius.small))
        )
        Text(label, style = WiomTheme.type.bodySm, color = WiomTheme.color.text.subtle)
    }
}

@Composable
private fun TypographySpecimen() {
    Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xs)) {
        Text("Typography (Noto Sans via Google Fonts)", style = WiomTheme.type.titleSm, color = WiomTheme.color.text.default)
        Text("headingLg 24 Bold", style = WiomTheme.type.headingLg, color = WiomTheme.color.text.default)
        Text("titleLg 20 Medium", style = WiomTheme.type.titleLg, color = WiomTheme.color.text.default)
        Text("bodyLg 16 Regular (default)", style = WiomTheme.type.bodyLg, color = WiomTheme.color.text.default)
        Text("bodyMd 14 Regular", style = WiomTheme.type.bodyMd, color = WiomTheme.color.text.subtle)
        Text("labelLg 16 SemiBold (interactive)", style = WiomTheme.type.labelLg, color = WiomTheme.color.text.default)
        Text("labelMd 14 SemiBold", style = WiomTheme.type.labelMd, color = WiomTheme.color.text.default)
    }
}

@Composable
private fun IconsShowcase() {
    Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
        Text("Icons — Icons.Rounded.* via WiomIcon", style = WiomTheme.type.titleSm, color = WiomTheme.color.text.default)
        Row(horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.lg)) {
            WiomIcon(Icons.Rounded.Search, contentDescription = "Search", tint = WiomTheme.color.icon.action)
            WiomIcon(Icons.Rounded.Phone, contentDescription = "Phone", tint = WiomTheme.color.icon.brand)
            WiomIcon(Icons.Rounded.Settings, contentDescription = "Settings", tint = WiomTheme.color.icon.nonAction)
        }
    }
}
