package com.wiom.designsystem.component.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.theme.WiomTheme

/**
 * Size of a [WiomTopBar]. See skill `wiom-top-bar`.
 *
 * - [Small] — 64dp. Default for 90% of screens. Supports Default, Centered, Scrolled, Search states.
 * - [Medium] — 112dp. Content-heavy screens where the title deserves more weight. Default + Scrolled.
 * - [Large] — 152dp. Hero / landing surfaces only. One per flow. Default + Scrolled.
 */
enum class WiomTopBarSize { Small, Medium, Large }

/**
 * State of a [WiomTopBar]. Not every size supports every state — see skill matrix.
 *
 * - [Default] — standard header.
 * - [Centered] — modal / bottom-sheet headers. Small only. One trailing action max.
 * - [Scrolled] — programmatic state applied when scroll offset > 0. Adds `shadow.sm`.
 * - [Search] — search pill replaces title. Small only.
 */
enum class WiomTopBarState { Default, Centered, Scrolled, Search }

/**
 * Wiom TopBar — the header at the top of every screen.
 *
 * Pick a [size] (visual weight) and a [state] (structural intent), then compose with
 * [leading] (defaults to `ArrowBack`) and [actions] (a `RowScope` for trailing icons / text CTAs).
 *
 * For Medium / Large, the [title] sits on a second row below the action row — no collapse
 * animation.
 *
 * Use [WiomTopBarIconAction] for icon actions in the trailing row and [WiomTopBarTextAction]
 * for text CTAs like "Save" / "Done".
 *
 * @param title screen title — keep ≤ 24 chars on 360dp screens.
 * @param size visual weight — Small / Medium / Large.
 * @param state state — Default / Centered (Small only) / Scrolled / Search (Small only).
 * @param subtitle optional second line (Small only — recommended).
 * @param searchQuery current query text when [state] is [WiomTopBarState.Search].
 * @param searchPlaceholder placeholder inside the search pill.
 * @param onSearchQueryChange query change callback when state is Search.
 * @param leading leading slot — defaults to an ArrowBack icon action. Pass `{}` to hide.
 * @param actions trailing row — compose with [WiomTopBarIconAction] / [WiomTopBarTextAction].
 */
@Composable
fun WiomTopBar(
    title: String,
    modifier: Modifier = Modifier,
    size: WiomTopBarSize = WiomTopBarSize.Small,
    state: WiomTopBarState = WiomTopBarState.Default,
    subtitle: String? = null,
    searchQuery: String = "",
    searchPlaceholder: String = "Search",
    onSearchQueryChange: (String) -> Unit = {},
    isDarkVariant: Boolean = false,
    leading: @Composable () -> Unit = {
        WiomTopBarIconAction(icon = Icons.AutoMirrored.Rounded.ArrowBack, onClick = {})
    },
    actions: @Composable RowScope.() -> Unit = {},
) {
    // Skill §6: dark variant uses `bg.brand.bold` + `text.inverse` + `icon.inverse`. Dark variant
    // doesn't support `Show Subtitle = true` or `state = Search` — there's no dimmed-inverse text
    // token and no translucent-inverse pill fill that contrasts with `bg.brand.bold`. Guard hard.
    if (isDarkVariant) {
        require(subtitle.isNullOrEmpty()) {
            "Dark-variant top bar doesn't support a subtitle (no dimmed-inverse text token)"
        }
        require(state != WiomTopBarState.Search) {
            "Dark-variant top bar doesn't support the Search state (no inverse-translucent pill fill)"
        }
    }
    val shadowDp = if (state == WiomTopBarState.Scrolled) WiomTheme.shadow.sm else WiomTheme.shadow.none
    val containerColor =
        if (isDarkVariant) WiomTheme.color.bg.brandBold else WiomTheme.color.bg.default
    // Bar consumes the status-bar inset so the surface reads as continuous with the OS strip.
    val container = modifier
        .fillMaxWidth()
        .shadow(shadowDp)
        .background(containerColor)
        .statusBarsPadding()

    val titleColor =
        if (isDarkVariant) WiomTheme.color.text.inverse else WiomTheme.color.text.default
    when (size) {
        WiomTopBarSize.Small -> SmallTopBar(
            modifier = container,
            title = title,
            state = state,
            subtitle = subtitle,
            searchQuery = searchQuery,
            searchPlaceholder = searchPlaceholder,
            onSearchQueryChange = onSearchQueryChange,
            leading = leading,
            actions = actions,
            titleColor = titleColor,
        )
        WiomTopBarSize.Medium -> MediumTopBar(
            modifier = container,
            title = title,
            leading = leading,
            actions = actions,
            titleColor = titleColor,
        )
        WiomTopBarSize.Large -> LargeTopBar(
            modifier = container,
            title = title,
            leading = leading,
            actions = actions,
            titleColor = titleColor,
        )
    }
}

/**
 * Pair the OS status bar with the top bar. Call once per screen, after `enableEdgeToEdge`.
 *
 * Light variant → status-bar background `bg.default`, status-bar content (time / signal /
 * battery) **dark**. Dark variant → status-bar background `bg.brand.bold`, content **light**.
 *
 * Skipped silently when not hosted in an Activity (tests, previews, custom ContextWrapper).
 *
 * @param isDarkVariant `true` matches `WiomTopBar(..., isDarkVariant = true)`.
 */
@Composable
fun WiomTopBarStatusBar(isDarkVariant: Boolean = false) {
    val view = androidx.compose.ui.platform.LocalView.current
    val containerColor =
        if (isDarkVariant) WiomTheme.color.bg.brandBold else WiomTheme.color.bg.default
    androidx.compose.runtime.SideEffect {
        val window = (view.context as? android.app.Activity)?.window ?: return@SideEffect
        @Suppress("DEPRECATION")
        window.statusBarColor = containerColor.toArgb()
        androidx.core.view.WindowCompat
            .getInsetsController(window, view)
            .isAppearanceLightStatusBars = !isDarkVariant
    }
}

// -------------------------------------------------------------------------------------------------
// Helpers
// -------------------------------------------------------------------------------------------------

/**
 * Icon action for the trailing row / leading slot of [WiomTopBar].
 *
 * Renders a 48dp touch target via `space.md` padding around a 24dp glyph, tinted `icon.action`.
 */
@Composable
fun WiomTopBarIconAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(WiomTheme.spacing.md),
        contentAlignment = Alignment.Center,
    ) {
        WiomIcon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = WiomTheme.color.icon.action,
        )
    }
}

/**
 * Text action for the trailing row of [WiomTopBar] — e.g. "Save", "Done", "Cancel".
 *
 * Uses `type.labelMd` · `text.brand`. Never destructive — use a full button inside the screen
 * for irreversible actions.
 */
@Composable
fun WiomTopBarTextAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .defaultMinSize(minHeight = WiomTheme.spacing.huge)
            .clickable(onClick = onClick)
            .padding(horizontal = WiomTheme.spacing.md),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = WiomTheme.type.labelMd,
            color = WiomTheme.color.text.brand,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

// -------------------------------------------------------------------------------------------------
// Small
// -------------------------------------------------------------------------------------------------

@Composable
private fun SmallTopBar(
    modifier: Modifier,
    title: String,
    state: WiomTopBarState,
    subtitle: String?,
    searchQuery: String,
    searchPlaceholder: String,
    onSearchQueryChange: (String) -> Unit,
    leading: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    titleColor: androidx.compose.ui.graphics.Color = WiomTheme.color.text.default,
) {
    val outerStart = WiomTheme.spacing.xs
    val outerEnd = if (state == WiomTopBarState.Search) WiomTheme.spacing.lg else WiomTheme.spacing.xs
    Row(
        modifier = modifier
            .height(64.dp)
            .padding(start = outerStart, end = outerEnd, top = WiomTheme.spacing.sm, bottom = WiomTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Leading
        leading()

        when (state) {
            WiomTopBarState.Search -> {
                // Pill replaces title. No trailing icons. Optional trailing text action (Cancel).
                SearchPill(
                    query = searchQuery,
                    placeholder = searchPlaceholder,
                    onQueryChange = onSearchQueryChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = WiomTheme.spacing.sm),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    content = actions,
                )
            }
            WiomTopBarState.Centered -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = WiomTheme.spacing.md),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = title,
                            style = WiomTheme.type.titleLg,
                            color = titleColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                        )
                        if (!subtitle.isNullOrEmpty()) {
                            Text(
                                modifier = Modifier.padding(top = WiomTheme.spacing.xs),
                                text = subtitle,
                                style = WiomTheme.type.bodySm,
                                color = WiomTheme.color.text.subtle,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    content = actions,
                )
            }
            else -> {
                // Default / Scrolled — start-aligned title column
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = WiomTheme.spacing.md),
                ) {
                    Text(
                        text = title,
                        style = WiomTheme.type.titleLg,
                        color = titleColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (!subtitle.isNullOrEmpty()) {
                        Text(
                            modifier = Modifier.padding(top = WiomTheme.spacing.xs),
                            text = subtitle,
                            style = WiomTheme.type.bodySm,
                            color = WiomTheme.color.text.subtle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    content = actions,
                )
            }
        }
    }
}

@Composable
private fun SearchPill(
    query: String,
    placeholder: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    // space-16 H × space-8 V padding, gap space-12 between leading icon and field, 20 dp icon
    // (smaller than the 24 dp action-row icons per skill §1).
    Row(
        modifier = modifier
            .background(
                color = WiomTheme.color.bg.subtle,
                shape = RoundedCornerShape(WiomTheme.radius.full),
            )
            .padding(horizontal = WiomTheme.spacing.lg, vertical = WiomTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md),
    ) {
        WiomIcon(
            imageVector = Icons.Rounded.Search,
            contentDescription = null,
            size = WiomTheme.iconSize.sm,
            tint = WiomTheme.color.icon.nonAction,
        )
        Box(modifier = Modifier.weight(1f)) {
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = WiomTheme.type.bodyLg.copy(color = WiomTheme.color.text.default),
                cursorBrush = SolidColor(WiomTheme.color.text.default),
            )
            if (query.isEmpty()) {
                Text(
                    text = placeholder,
                    style = WiomTheme.type.bodyLg,
                    color = WiomTheme.color.text.disabled,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Medium — 112dp total. Action row + title row below.
// Skill: space.sm top + 32lh (headingLg) + space.sm bottom on the title area.
// -------------------------------------------------------------------------------------------------

@Composable
private fun MediumTopBar(
    modifier: Modifier,
    title: String,
    leading: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    titleColor: androidx.compose.ui.graphics.Color = WiomTheme.color.text.default,
) {
    Column(modifier = modifier.height(112.dp)) {
        ActionRow(leading = leading, actions = actions)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = WiomTheme.spacing.lg, vertical = WiomTheme.spacing.sm),
            contentAlignment = Alignment.BottomStart,
        ) {
            Text(
                text = title,
                style = WiomTheme.type.headingLg,
                color = titleColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Large — 152dp total. Action row + hero title.
// Skill: space.md top + 44lh (headingXl) + space.xxl bottom on the title area.
// -------------------------------------------------------------------------------------------------

@Composable
private fun LargeTopBar(
    modifier: Modifier,
    title: String,
    leading: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    titleColor: androidx.compose.ui.graphics.Color = WiomTheme.color.text.default,
) {
    Column(modifier = modifier.height(152.dp)) {
        ActionRow(leading = leading, actions = actions)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(
                    start = WiomTheme.spacing.lg,
                    end = WiomTheme.spacing.lg,
                    top = WiomTheme.spacing.md,
                    bottom = WiomTheme.spacing.xxl,
                ),
            contentAlignment = Alignment.BottomStart,
        ) {
            Text(
                text = title,
                style = WiomTheme.type.headingXl,
                color = titleColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun ActionRow(
    leading: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
) {
    // Action row is 48 dp (Medium / Large) per skill §2.
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = WiomTheme.spacing.xs, vertical = WiomTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leading()
        Box(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            content = actions,
        )
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@Preview(name = "Small · Default", showBackground = true, widthDp = 360)
@Composable
private fun PreviewSmallDefault() {
    WiomTheme {
        WiomTopBar(
            title = "Profile",
            size = WiomTopBarSize.Small,
            state = WiomTopBarState.Default,
            actions = {
                WiomTopBarIconAction(icon = Icons.Rounded.Search, onClick = {})
                WiomTopBarIconAction(icon = Icons.Rounded.Notifications, onClick = {})
                WiomTopBarIconAction(icon = Icons.Rounded.MoreVert, onClick = {})
            },
        )
    }
}

@Preview(name = "Small · Default · Subtitle", showBackground = true, widthDp = 360)
@Composable
private fun PreviewSmallDefaultSubtitle() {
    WiomTheme {
        WiomTopBar(
            title = "Notifications",
            subtitle = "Last updated just now",
            size = WiomTopBarSize.Small,
            state = WiomTopBarState.Default,
        )
    }
}

@Preview(name = "Small · Centered", showBackground = true, widthDp = 360)
@Composable
private fun PreviewSmallCentered() {
    WiomTheme {
        WiomTopBar(
            title = "Edit profile",
            size = WiomTopBarSize.Small,
            state = WiomTopBarState.Centered,
            leading = {
                WiomTopBarIconAction(icon = Icons.Rounded.Close, onClick = {})
            },
            actions = {
                WiomTopBarTextAction(text = "Save", onClick = {})
            },
        )
    }
}

@Preview(name = "Small · Scrolled", showBackground = true, widthDp = 360)
@Composable
private fun PreviewSmallScrolled() {
    WiomTheme {
        WiomTopBar(
            title = "Settings",
            size = WiomTopBarSize.Small,
            state = WiomTopBarState.Scrolled,
            actions = {
                WiomTopBarIconAction(icon = Icons.Rounded.MoreVert, onClick = {})
            },
        )
    }
}

@Preview(name = "Small · Search", showBackground = true, widthDp = 360)
@Composable
private fun PreviewSmallSearch() {
    WiomTheme {
        WiomTopBar(
            title = "",
            size = WiomTopBarSize.Small,
            state = WiomTopBarState.Search,
            searchPlaceholder = "Search plans, invoices, tickets",
            actions = {
                WiomTopBarTextAction(text = "Cancel", onClick = {})
            },
        )
    }
}

@Preview(name = "Medium · Default", showBackground = true, widthDp = 360)
@Composable
private fun PreviewMediumDefault() {
    WiomTheme {
        WiomTopBar(
            title = "Payment history",
            size = WiomTopBarSize.Medium,
            state = WiomTopBarState.Default,
            actions = {
                WiomTopBarIconAction(icon = Icons.Rounded.Search, onClick = {})
                WiomTopBarIconAction(icon = Icons.Rounded.MoreVert, onClick = {})
            },
        )
    }
}

@Preview(name = "Medium · Scrolled", showBackground = true, widthDp = 360)
@Composable
private fun PreviewMediumScrolled() {
    WiomTheme {
        WiomTopBar(
            title = "Payment history",
            size = WiomTopBarSize.Medium,
            state = WiomTopBarState.Scrolled,
        )
    }
}

@Preview(name = "Large · Default", showBackground = true, widthDp = 360)
@Composable
private fun PreviewLargeDefault() {
    WiomTheme {
        WiomTopBar(
            title = "Good morning,\nAbhishek",
            size = WiomTopBarSize.Large,
            state = WiomTopBarState.Default,
            actions = {
                WiomTopBarIconAction(icon = Icons.Rounded.Notifications, onClick = {})
                WiomTopBarIconAction(icon = Icons.Rounded.MoreVert, onClick = {})
            },
        )
    }
}

@Preview(name = "Large · Scrolled", showBackground = true, widthDp = 360)
@Composable
private fun PreviewLargeScrolled() {
    WiomTheme {
        WiomTopBar(
            title = "Dashboard",
            size = WiomTopBarSize.Large,
            state = WiomTopBarState.Scrolled,
        )
    }
}
