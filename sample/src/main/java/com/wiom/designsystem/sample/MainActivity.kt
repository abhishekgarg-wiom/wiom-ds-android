package com.wiom.designsystem.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.foundation.icon.WiomIcons
import com.wiom.designsystem.theme.WiomTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WiomTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = WiomTheme.colors.surface.base,
                ) {
                    SampleScreen()
                }
            }
        }
    }
}

@Composable
private fun SampleScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(WiomTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xl),
    ) {
        SectionTitle("Wiom Design System — v0.1.0")

        TypographySection()
        BrandColorsSection()
        StatusColorsSection()
        IconsSection()
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = WiomTheme.type.headingLg,
        color = WiomTheme.colors.text.primary,
    )
}

@Composable
private fun SubTitle(text: String) {
    Text(
        text = text,
        style = WiomTheme.type.titleSm,
        color = WiomTheme.colors.text.primary,
        modifier = Modifier.padding(bottom = WiomTheme.spacing.sm),
    )
}

@Composable
private fun TypographySection() {
    Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
        SubTitle("Typography")
        Text("Heading Lg — 24/Bold", style = WiomTheme.type.headingLg, color = WiomTheme.colors.text.primary)
        Text("Title Sm — 16/Bold", style = WiomTheme.type.titleSm, color = WiomTheme.colors.text.primary)
        Text("Body Md — 14/Regular (default)", style = WiomTheme.type.bodyMd, color = WiomTheme.colors.text.primary)
        Text("Body Sm — 12/Regular (helper)", style = WiomTheme.type.bodySm, color = WiomTheme.colors.text.secondary)
        Text("Label Lg — 16/SemiBold (button)", style = WiomTheme.type.labelLg, color = WiomTheme.colors.text.primary)
    }
}

@Composable
private fun BrandColorsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
        SubTitle("Brand colors")
        Row(horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
            ColorSwatch(WiomTheme.colors.brand.primary, "primary")
            ColorSwatch(WiomTheme.colors.brand.primaryHover, "hover")
            ColorSwatch(WiomTheme.colors.brand.primaryPressed, "pressed")
            ColorSwatch(WiomTheme.colors.brand.primarySoft, "soft")
            ColorSwatch(WiomTheme.colors.brand.primarySubtle, "subtle")
            ColorSwatch(WiomTheme.colors.brand.primaryTint, "tint")
        }
    }
}

@Composable
private fun StatusColorsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
        SubTitle("Status colors")
        Row(horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
            ColorSwatch(WiomTheme.colors.positive.primary, "success")
            ColorSwatch(WiomTheme.colors.negative.primary, "error")
            ColorSwatch(WiomTheme.colors.warning.primary, "warning")
            ColorSwatch(WiomTheme.colors.info.primary, "info")
        }
    }
}

@Composable
private fun IconsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
        SubTitle("Icons (Material Symbols Rounded)")
        Row(
            horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.lg),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WiomIcon(WiomIcons.search, contentDescription = "search", tint = WiomTheme.colors.text.primary)
            WiomIcon(WiomIcons.phone, contentDescription = "phone", tint = WiomTheme.colors.text.primary)
            WiomIcon(WiomIcons.checkCircle, contentDescription = "success", tint = WiomTheme.colors.positive.primary)
            WiomIcon(WiomIcons.error, contentDescription = "error", tint = WiomTheme.colors.negative.primary)
            WiomIcon(WiomIcons.warning, contentDescription = "warning", tint = WiomTheme.colors.warning.primary)
            WiomIcon(WiomIcons.visibility, contentDescription = "visibility", tint = WiomTheme.colors.text.secondary)
        }
    }
}

@Composable
private fun ColorSwatch(color: Color, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(WiomTheme.spacing.huge)
                .background(color, RoundedCornerShape(WiomTheme.radius.small))
                .border(
                    width = WiomTheme.stroke.small,
                    color = WiomTheme.colors.border.default,
                    shape = RoundedCornerShape(WiomTheme.radius.small),
                ),
        )
        Text(
            label,
            style = WiomTheme.type.bodySm,
            color = WiomTheme.colors.text.secondary,
            modifier = Modifier.padding(top = WiomTheme.spacing.xs),
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun SampleScreenPreview() {
    WiomTheme {
        Surface(color = WiomTheme.colors.surface.base) {
            SampleScreen()
        }
    }
}
