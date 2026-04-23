package com.wiom.designsystem.component.toast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.wiom.designsystem.foundation.icon.WiomIcon
import com.wiom.designsystem.theme.WiomTheme
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Toast status family.
 *
 * - [Neutral] : dark inverse surface. Used for the classic "fire-and-forget"
 *   feedback where no status is implied. Equivalent of the Material dark snackbar.
 * - [Positive] : success surface. Use sparingly per app constitution —
 *   only when the user genuinely needs confirmation (e.g. wallet withdrawal sent).
 * - [Critical] : error surface. Use for server-rejected / failed actions.
 * - [Warning] : non-blocking caution (offline, degraded). One-token family;
 *   body + heading share [WiomColors.Text.onWarning] (#372902 olive).
 * - [Info] : neutral state change, purely informational.
 */
enum class WiomToastStatus {
    Neutral,
    Positive,
    Critical,
    Warning,
    Info,
}

/**
 * Optional action slot for a toast (Undo / Retry / View).
 *
 * Keep labels to a single word when possible — toast width is tight.
 */
@Immutable
data class WiomToastAction(
    val label: String,
    val onClick: () -> Unit,
)

/**
 * Wiom Toast.
 *
 * A transient floating surface that appears at the bottom of the screen and
 * auto-dismisses. Shadow `shadow.lg`, radius `radius.medium`, `space.lg` padding.
 *
 * Most callers should NOT use this composable directly — prefer
 * [WiomToastHost] + [rememberWiomToastState] inside a `Scaffold`. This
 * composable is exposed for static placement in previews and non-host surfaces.
 *
 * @param status One of [WiomToastStatus].
 * @param message Body text. Single line preferred, 2 tolerated. Longer copy truncates.
 * @param modifier Modifier; caller typically passes bottom padding + 8dp side insets.
 * @param action Optional [WiomToastAction]. Labels use `type.labelLg` in the tone's action color.
 * @param onClose Optional close handler — when non-null, a close X appears at the trailing edge
 *   (persistent toast pattern).
 */
@Composable
fun WiomToast(
    status: WiomToastStatus,
    message: String,
    modifier: Modifier = Modifier,
    action: WiomToastAction? = null,
    onClose: (() -> Unit)? = null,
) {
    val visuals = toastVisuals(status)
    val shape = RoundedCornerShape(WiomTheme.radius.medium)

    Row(
        modifier = modifier
            .shadow(elevation = WiomTheme.shadow.lg, shape = shape, clip = false)
            .clip(shape)
            .background(visuals.fill, shape)
            .padding(WiomTheme.spacing.lg),
        horizontalArrangement = Arrangement.spacedBy(WiomTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        WiomIcon(
            imageVector = visuals.icon,
            contentDescription = null,
            size = WiomTheme.iconSize.md,
            tint = visuals.iconTint,
        )
        Text(
            text = message,
            style = WiomTheme.type.bodyMd,
            color = visuals.bodyColor,
            maxLines = 2,
            modifier = Modifier.weight(1f, fill = true),
        )
        if (action != null) {
            // `clickable` outside the padding so the padding is the click target, not a dead margin.
            Text(
                text = action.label,
                style = WiomTheme.type.labelLg,
                color = visuals.actionColor,
                maxLines = 1,
                modifier = Modifier
                    .clip(RoundedCornerShape(WiomTheme.radius.small))
                    .clickable(role = Role.Button, onClick = action.onClick)
                    .padding(horizontal = WiomTheme.spacing.sm, vertical = WiomTheme.spacing.xs),
            )
        }
        if (onClose != null) {
            WiomIcon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "Dismiss",
                size = WiomTheme.iconSize.md,
                tint = visuals.iconTint,
                modifier = Modifier
                    .clip(RoundedCornerShape(WiomTheme.radius.small))
                    .clickable(role = Role.Button, onClick = onClose),
            )
        }
    }
}

// ---- Host / State -----------------------------------------------------------

/**
 * Data describing a toast about to show.
 *
 * Pass to [WiomToastState.showToast] from your screen. `duration` is in milliseconds;
 * pass `null` for a persistent toast the user must dismiss via the close X.
 *
 * The recommended defaults follow the skill's behavior table:
 * - Informational : `DURATION_SHORT` (2s)
 * - Actionable    : `DURATION_LONG` (8s)
 * - Persistent    : `duration = null`, require `onClose` + `showClose = true`
 */
@Immutable
data class WiomToastMessage(
    val status: WiomToastStatus,
    val message: String,
    val action: WiomToastAction? = null,
    val durationMillis: Long? = DURATION_SHORT,
    val showClose: Boolean = false,
) {
    companion object {
        const val DURATION_SHORT: Long = 2_000L
        const val DURATION_LONG: Long = 8_000L
    }
}

/**
 * State holder for a [WiomToastHost].
 *
 * Obtain via [rememberWiomToastState] and call [showToast] from any coroutine
 * scope. Max 1 toast visible at a time — new toasts queue, the current one
 * plays out its duration (or dismiss) before the next shows.
 */
@Stable
class WiomToastState {
    private val _current = MutableStateFlow<WiomToastMessage?>(null)

    /** Observe the currently visible toast (null when nothing is showing). */
    val current: StateFlow<WiomToastMessage?> = _current.asStateFlow()

    private val queue = Channel<WiomToastMessage>(capacity = Channel.UNLIMITED)

    /** Enqueue a toast. Returns immediately; the host coroutine drives visibility. */
    fun showToast(message: WiomToastMessage) {
        queue.trySend(message)
    }

    /** Dismiss the currently visible toast early (e.g. from an action callback). */
    fun dismiss() {
        _current.value = null
    }

    internal suspend fun runHost() {
        for (message in queue) {
            _current.value = message
            if (message.durationMillis != null) {
                delay(message.durationMillis)
                // The user may have dismissed this toast via action / close during the delay;
                // that path nulls _current. Only clear here when the same message is still
                // showing.
                if (_current.value === message) {
                    _current.value = null
                }
            } else {
                // Persistent — wait until the user closes it.
                while (_current.value === message) {
                    delay(100L)
                }
            }
        }
    }
}

/** Create and remember a [WiomToastState] at the composition root. */
@Composable
fun rememberWiomToastState(): WiomToastState = remember { WiomToastState() }

/**
 * Host surface for [WiomToastState]. Place inside `Scaffold(snackbarHost = { … })`
 * or anywhere at the bottom of your screen.
 *
 * Handles the queue (max 1 visible), auto-dismiss timing, and enter/exit animation
 * (300ms slide + fade, matching skill §12).
 *
 * @param state Returned from [rememberWiomToastState].
 * @param modifier Modifier. Skill calls for `padding(horizontal = 8dp, vertical = 8dp)`
 *   on the host to get the "floating" 8dp inset on each side.
 */
@Composable
fun WiomToastHost(
    state: WiomToastState,
    modifier: Modifier = Modifier,
) {
    val current by state.current.collectAsState()

    LaunchedEffect(state) {
        state.runHost()
    }

    AnimatedVisibility(
        visible = current != null,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = 300),
        ) + fadeIn(animationSpec = tween(durationMillis = 300)),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = 300),
        ) + fadeOut(animationSpec = tween(durationMillis = 300)),
        modifier = modifier,
    ) {
        val message = current ?: return@AnimatedVisibility
        WiomToast(
            status = message.status,
            message = message.message,
            action = message.action?.let { action ->
                WiomToastAction(
                    label = action.label,
                    onClick = {
                        action.onClick()
                        state.dismiss()
                    },
                )
            },
            onClose = if (message.showClose) {
                { state.dismiss() }
            } else null,
        )
    }
}

// ---- internals --------------------------------------------------------------

@Immutable
private data class ToastVisuals(
    val fill: Color,
    val bodyColor: Color,
    val iconTint: Color,
    val actionColor: Color,
    val icon: ImageVector,
)

@Composable
private fun toastVisuals(status: WiomToastStatus): ToastVisuals {
    val c = WiomTheme.color
    return when (status) {
        WiomToastStatus.Neutral -> ToastVisuals(
            fill = c.bg.inverse,
            bodyColor = c.text.inverse,
            iconTint = c.icon.inverse,
            actionColor = c.text.inverse,
            icon = Icons.Rounded.Info,
        )
        WiomToastStatus.Positive -> ToastVisuals(
            fill = c.bg.positiveSubtle,
            bodyColor = c.text.default,
            iconTint = c.icon.positive,
            actionColor = c.text.brand,
            icon = Icons.Rounded.CheckCircle,
        )
        WiomToastStatus.Critical -> ToastVisuals(
            fill = c.bg.criticalSubtle,
            // Body on Critical banners uses text.default per CLAUDE.md § 6.
            bodyColor = c.text.default,
            iconTint = c.icon.critical,
            actionColor = c.text.brand,
            icon = Icons.Rounded.Error,
        )
        WiomToastStatus.Warning -> ToastVisuals(
            fill = c.bg.warningSubtle,
            // Warning is one-token — body and action share text.onWarning.
            bodyColor = c.text.onWarning,
            iconTint = c.icon.warning,
            actionColor = c.text.onWarning,
            icon = Icons.Rounded.Warning,
        )
        WiomToastStatus.Info -> ToastVisuals(
            fill = c.bg.infoSubtle,
            bodyColor = c.text.default,
            iconTint = c.icon.info,
            actionColor = c.text.brand,
            icon = Icons.Rounded.Info,
        )
    }
}

// ---- Previews ---------------------------------------------------------------

@Preview(name = "Toast — Neutral", showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 360)
@Composable
private fun PreviewToastNeutral() {
    WiomTheme {
        WiomToast(
            status = WiomToastStatus.Neutral,
            message = "Draft saved",
            modifier = Modifier.padding(WiomTheme.spacing.sm),
        )
    }
}

@Preview(name = "Toast — Positive", showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 360)
@Composable
private fun PreviewToastPositive() {
    WiomTheme {
        WiomToast(
            status = WiomToastStatus.Positive,
            message = "Nikaasi safal rahi",
            modifier = Modifier.padding(WiomTheme.spacing.sm),
        )
    }
}

@Preview(name = "Toast — Critical", showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 360)
@Composable
private fun PreviewToastCritical() {
    WiomTheme {
        WiomToast(
            status = WiomToastStatus.Critical,
            message = "Server ne request reject kiya",
            modifier = Modifier.padding(WiomTheme.spacing.sm),
        )
    }
}

@Preview(name = "Toast — Warning", showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 360)
@Composable
private fun PreviewToastWarning() {
    WiomTheme {
        WiomToast(
            status = WiomToastStatus.Warning,
            message = "Offline — purana data dikh raha hai",
            modifier = Modifier.padding(WiomTheme.spacing.sm),
        )
    }
}

@Preview(name = "Toast — Info", showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 360)
@Composable
private fun PreviewToastInfo() {
    WiomTheme {
        WiomToast(
            status = WiomToastStatus.Info,
            message = "Status update ho gayi",
            modifier = Modifier.padding(WiomTheme.spacing.sm),
        )
    }
}

@Preview(name = "Toast — Actionable (Critical + Retry)", showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 360)
@Composable
private fun PreviewToastActionable() {
    WiomTheme {
        WiomToast(
            status = WiomToastStatus.Critical,
            message = "Call failed",
            action = WiomToastAction(label = "Retry", onClick = { }),
            modifier = Modifier.padding(WiomTheme.spacing.sm),
        )
    }
}

@Preview(name = "Toast — Undo (Neutral)", showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 360)
@Composable
private fun PreviewToastUndo() {
    WiomTheme {
        WiomToast(
            status = WiomToastStatus.Neutral,
            message = "Ticket delete kiya",
            action = WiomToastAction(label = "Undo", onClick = { }),
            modifier = Modifier.padding(WiomTheme.spacing.sm),
        )
    }
}

@Preview(name = "Toast — Persistent Warning with close", showBackground = true, backgroundColor = 0xFFFAF9FC, widthDp = 360)
@Composable
private fun PreviewToastPersistent() {
    WiomTheme {
        WiomToast(
            status = WiomToastStatus.Warning,
            message = "Offline — purana data dikh raha hai",
            onClose = { },
            modifier = Modifier.padding(WiomTheme.spacing.sm),
        )
    }
}
