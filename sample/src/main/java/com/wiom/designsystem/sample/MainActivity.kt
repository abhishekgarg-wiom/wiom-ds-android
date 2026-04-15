package com.wiom.designsystem.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.wiom.designsystem.component.badge.WiomBadgeColor
import com.wiom.designsystem.component.badge.WiomBadgeCount
import com.wiom.designsystem.component.badge.WiomBadgeDot
import com.wiom.designsystem.component.badge.WiomBadgeLabel
import com.wiom.designsystem.component.badge.WiomBadgeSize
import com.wiom.designsystem.component.badge.WiomBadgeStyle
import com.wiom.designsystem.component.bottomsheet.WiomBottomSheet
import com.wiom.designsystem.component.bottomsheet.WiomBottomSheetHeader
import com.wiom.designsystem.component.bottomsheet.WiomBottomSheetListItem
import com.wiom.designsystem.component.checkbox.WiomCheckbox
import com.wiom.designsystem.component.dropdown.WiomDropdown
import com.wiom.designsystem.component.dropdown.WiomDropdownOption
import com.wiom.designsystem.component.input.WiomInput
import com.wiom.designsystem.component.listitem.WiomListItem
import com.wiom.designsystem.component.navigationbar.WiomNavItem
import com.wiom.designsystem.component.navigationbar.WiomNavigationBar
import com.wiom.designsystem.component.pagination.WiomPaginationBars
import com.wiom.designsystem.component.pagination.WiomPaginationCounter
import com.wiom.designsystem.component.pagination.WiomPaginationDotStyle
import com.wiom.designsystem.component.pagination.WiomPaginationDots
import com.wiom.designsystem.component.radio.WiomRadio
import com.wiom.designsystem.component.switch.WiomSwitch
import com.wiom.designsystem.component.tabsfilters.WiomChip
import com.wiom.designsystem.component.tabsfilters.WiomChipRow
import com.wiom.designsystem.component.tabsfilters.WiomPillTabs
import com.wiom.designsystem.component.tabsfilters.WiomUnderlineFilter
import com.wiom.designsystem.component.topbar.WiomTopBar
import com.wiom.designsystem.component.topbar.WiomTopBarIconAction
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.foundation.icon.WiomIcons
import com.wiom.designsystem.theme.WiomTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WiomTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = WiomTheme.colors.surface.base) {
                    SampleScreen()
                }
            }
        }
    }
}

@Composable
private fun SampleScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        WiomTopBar(
            title = "Wiom DS Showcase",
            leading = null,
            actions = {
                WiomTopBarIconAction(
                    icon = { WiomIcon(WiomIcons.search, "Search", tint = WiomTheme.colors.text.secondary) },
                    onClick = {},
                )
            },
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(WiomTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.xl),
        ) {
            BadgesSection()
            CheckboxSection()
            RadioSection()
            SwitchSection()
            InputSection()
            ListItemSection()
            DropdownSection()
            TabsFiltersSection()
            PaginationSection()
            BottomSheetSection()
        }
        NavBarFooter()
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text = text, style = WiomTheme.type.headingMd, color = WiomTheme.colors.text.primary)
}

@Composable
private fun BadgesSection() {
    Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
        SectionTitle("Badges")
        Row(horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
            WiomBadgeDot(color = WiomBadgeColor.Brand)
            WiomBadgeDot(color = WiomBadgeColor.Negative)
            WiomBadgeCount(count = 5)
            WiomBadgeCount(count = 25, color = WiomBadgeColor.Brand)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
            WiomBadgeLabel("Confirmed", color = WiomBadgeColor.Positive, style = WiomBadgeStyle.Filled)
            WiomBadgeLabel("Pending", color = WiomBadgeColor.Warning)
            WiomBadgeLabel("असफल", color = WiomBadgeColor.Negative, style = WiomBadgeStyle.Filled)
            WiomBadgeLabel("पक्का", size = WiomBadgeSize.Small, color = WiomBadgeColor.Positive)
        }
    }
}

@Composable
private fun CheckboxSection() {
    var c1 by remember { mutableStateOf(false) }
    var c2 by remember { mutableStateOf(true) }
    Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md)) {
        SectionTitle("Checkbox")
        WiomCheckbox(checked = c1, onCheckedChange = { c1 = it }, label = "I agree to the Terms & Conditions", helper = "You can review the full terms before agreeing")
        WiomCheckbox(checked = c2, onCheckedChange = { c2 = it }, label = "Send me offers and updates via SMS")
    }
}

@Composable
private fun RadioSection() {
    var payment by remember { mutableStateOf("upi") }
    Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md)) {
        SectionTitle("Radio")
        WiomRadio(selected = payment == "upi", onClick = { payment = "upi" }, label = "Pay via UPI", helper = "Instant transfer from your bank")
        WiomRadio(selected = payment == "card", onClick = { payment = "card" }, label = "Debit / Credit Card")
        WiomRadio(selected = payment == "cod", onClick = { payment = "cod" }, label = "Cash on delivery")
    }
}

@Composable
private fun SwitchSection() {
    var s1 by remember { mutableStateOf(true) }
    var s2 by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md)) {
        SectionTitle("Switch")
        WiomSwitch(checked = s1, onCheckedChange = { s1 = it }, label = "Auto-renew plan", helper = "Your plan will renew on the 15th of each month")
        WiomSwitch(checked = s2, onCheckedChange = { s2 = it }, label = "Share usage data")
    }
}

@Composable
private fun InputSection() {
    var phone by remember { mutableStateOf("9876543210") }
    var amount by remember { mutableStateOf("500") }
    Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.lg)) {
        SectionTitle("Input")
        WiomInput(
            value = phone,
            onValueChange = { phone = it },
            title = "Mobile number",
            leadingIcon = { WiomIcon(WiomIcons.phone, null, size = WiomTheme.icon.sm, tint = WiomTheme.colors.text.secondary) },
            helper = "We'll send an OTP",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        )
        WiomInput(
            value = amount,
            onValueChange = { amount = it },
            title = "Recharge amount",
            prefix = "₹",
            suffix = ".00",
            helper = "Includes 18% GST",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
    }
}

@Composable
private fun ListItemSection() {
    Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
        SectionTitle("List items")
        WiomListItem(primary = "Account", leadingIcon = { WiomIcon(WiomIcons.phone, null, size = WiomTheme.icon.md, tint = WiomTheme.colors.text.secondary) }, onClick = {})
        WiomListItem(primary = "Language", trailingMeta = "English", onClick = {})
        WiomListItem(primary = "Build", secondary = "v0.1.0 — Up to date", trailingIcon = null)
    }
}

@Composable
private fun DropdownSection() {
    val langs = listOf(
        WiomDropdownOption("en", "English"),
        WiomDropdownOption("hi", "हिन्दी"),
        WiomDropdownOption("te", "తెలుగు"),
    )
    var lang by remember { mutableStateOf<String?>("hi") }
    Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
        SectionTitle("Dropdown")
        WiomDropdown(value = lang, options = langs, onValueChange = { lang = it }, label = "Language")
    }
}

@Composable
private fun TabsFiltersSection() {
    var pillIdx by remember { mutableStateOf(0) }
    var filterIdx by remember { mutableStateOf(1) }
    var installSel by remember { mutableStateOf(true) }
    var repairSel by remember { mutableStateOf(true) }
    var rechargeSel by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md)) {
        SectionTitle("Tabs · Filters · Chips")
        WiomPillTabs(tabs = listOf("मेरे टिकट", "टीम के टिकट"), selectedIndex = pillIdx, onTabSelect = { pillIdx = it })
        WiomUnderlineFilter(filters = listOf("सभी", "पेंडिंग", "बंद"), selectedIndex = filterIdx, onFilterSelect = { filterIdx = it })
        WiomChipRow {
            WiomChip(label = "इंस्टॉलेशन", selected = installSel, onClick = { installSel = !installSel })
            WiomChip(label = "रिपेयर", selected = repairSel, onClick = { repairSel = !repairSel })
            WiomChip(label = "रिचार्ज", selected = rechargeSel, onClick = { rechargeSel = !rechargeSel })
        }
    }
}

@Composable
private fun PaginationSection() {
    Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.lg)) {
        SectionTitle("Pagination")
        WiomPaginationDots(total = 4, current = 2, style = WiomPaginationDotStyle.Expanded)
        WiomPaginationBars(total = 5, current = 3, counterLabel = "Step 3 of 5")
        WiomPaginationCounter(current = 3, total = 10, onPrev = {}, onNext = {})
    }
}

@Composable
private fun BottomSheetSection() {
    var show by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm)) {
        SectionTitle("Bottom sheet")
        WiomListItem(primary = "Show options sheet", onClick = { show = true })
        if (show) {
            WiomBottomSheet(onDismissRequest = { show = false }) {
                WiomBottomSheetHeader(title = "Policy Options")
                WiomBottomSheetListItem(label = "View Policy Details", description = "See coverage, plan, and expiry", icon = WiomIcons.phone, onClick = {})
                WiomBottomSheetListItem(label = "Make a Payment", icon = WiomIcons.checkCircle, onClick = {})
                WiomBottomSheetListItem(label = "Contact Support", icon = WiomIcons.search, onClick = {})
            }
        }
    }
}

@Composable
private fun NavBarFooter() {
    val items = listOf(
        WiomNavItem("Home", WiomIcons.search),
        WiomNavItem("Plans", WiomIcons.expandMore),
        WiomNavItem("Bills", WiomIcons.phone, hasBadge = true),
        WiomNavItem("Profile", WiomIcons.menu),
    )
    var nav by remember { mutableStateOf(0) }
    WiomNavigationBar(items = items, selectedIndex = nav, onSelect = { nav = it })
}
