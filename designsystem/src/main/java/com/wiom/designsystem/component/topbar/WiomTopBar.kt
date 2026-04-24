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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
 * animation in v1.
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
    leading: @Composable () -> Unit = {
        WiomTopBarIconAction(icon = Icons.AutoMirrored.Rounded.ArrowBack, onClick = {})
    },
    actions: @Composable RowScope.() -> Unit = {},
) {
    val shadowDp = if (state == WiomTopBarState.Scrolled) WiomTheme.shadow.sm else WiomTheme.shadow.none
    val container = modifier
        .fillMaxWidth()
        .shadow(shadowDp)
        .background(WiomTheme.color.bg.default)

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
        )
        WiomTopBarSize.Medium -> MediumTopBar(
            modifier = container,
            title = title,
            leading = leading,
            actions = actions,
        )
        WiomTopBarSize.Large -> LargeTopBar(
            modifier = container,
            title = title,
            leading = leading,
            actions = actions,
        )
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
) {
    Row(
        modifier = modifier
            .padding(horizontal = WiomTheme.spacing.xs, vertical = WiomTheme.spacing.sm),
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
                            color = WiomTheme.color.text.default,
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
                        color = WiomTheme.color.text.default,
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
    // Intrinsic height from icon (24dp) + vertical padding (sm = 8dp) = 40dp — above the
    // 40dp search-pill minimum; grows with user font scale.
    Row(
        modifier = modifier
            .background(
                color = WiomTheme.color.bg.subtle,
                shape = RoundedCornerShape(WiomTheme.radius.full),
            )
            .padding(horizontal = WiomTheme.spacing.md, vertical = WiomTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        WiomIcon(
            imageVector = Icons.Rounded.Search,
            contentDescription = null,
            tint = WiomTheme.color.icon.nonAction,
        )
        Text(
            modifier = Modifier.padding(start = WiomTheme.spacing.sm),
            text = query.ifEmpty { placeholder },
            style = WiomTheme.type.bodyLg,
            color = if (query.isEmpty()) WiomTheme.color.text.disabled else WiomTheme.color.text.default,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        // Note: BasicTextField wiring is left to the consumer — pass a modifier or plug into
        // WiomInput once that component is ready. This matches the v1 surface: display only.
        @Suppress("UNUSED_EXPRESSION") onQueryChange
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
) {
    Column(modifier = modifier) {
        ActionRow(leading = leading, actions = actions)
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = WiomTheme.spacing.lg,
                    end = WiomTheme.spacing.lg,
                    top = WiomTheme.spacing.sm,
                    bottom = WiomTheme.spacing.sm,
                ),
            text = title,
            style = WiomTheme.type.headingLg,
            color = WiomTheme.color.text.default,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
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
) {
    Column(modifier = modifier) {
        ActionRow(leading = leading, actions = actions)
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = WiomTheme.spacing.lg,
                    end = WiomTheme.spacing.lg,
                    top = WiomTheme.spacing.md,
                    bottom = WiomTheme.spacing.xxl,
                ),
            text = title,
            style = WiomTheme.type.headingXl,
            color = WiomTheme.color.text.default,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ActionRow(
    leading: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
