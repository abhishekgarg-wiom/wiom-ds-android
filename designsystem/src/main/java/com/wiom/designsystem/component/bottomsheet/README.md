# WiomBottomSheet

Material3 `ModalBottomSheet` wrapped with Wiom tokens. The bottom-anchored surface for supplementary content, option menus, payment confirmations, success/error feedback, pickers, and short forms.

Use a dialog — not a bottom sheet — for blocking single-thought alerts.

---

## Anatomy

```
Scrim (overlay.scrim)
└─ Sheet Surface
   • bg.default · top corners radius.xlarge · bottom corners radius.none
   ├─ Drag handle  (32 × 4 dp pill · stroke.strong · radius.full · space.sm vertical padding)
   ├─ Header       (WiomBottomSheetHeader · divider · stroke.small · stroke.subtle)
   ├─ Content      (WiomBottomSheetListItem rows, WiomBottomSheetIllustration, WiomInput, …)
   └─ Actions      (WiomBottomSheetActions — divider-above, 1–3 WiomButton instances)
```

The sheet applies `navigationBarsPadding()` so the bottom action bar never collides with the gesture-nav pill.

---

## Size variants

The container takes a `WiomBottomSheetSize` enum. List variants set a min-height heuristic; hug-height variants let content drive the height.

| Size | Height behavior | Typical content |
|---|---|---|
| `Compact` | `heightIn(min = 240.dp)` | Header + 2 list rows |
| `Half` | `heightIn(min = 400.dp)` | Header + 3–4 list rows, picker |
| `Expanded` | `heightIn(min = 600.dp)` | Header + 5+ rows, filter sheet |
| `Full` | fill (via `skipPartiallyExpanded`) | Long scrollable content |
| `Illustration` | hug | Centered illustration + text |
| `IllustrationCta` | hug | Illustration + text + action bar |
| `IllustrationLeft` | hug | Compact feedback with image left, text right (composed inline) |
| `Share` | hug | Header + horizontal row / list of share targets |
| `Form` | hug | Header + 1–3 `WiomInput` fields + action bar |

Choosing between Size and the content helpers is independent — the enum is a sheet-height hint, the helpers control the anatomy you put inside.

---

## API

```kotlin
@Composable
fun WiomBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    size: WiomBottomSheetSize = WiomBottomSheetSize.Compact,
    showHandle: Boolean = true,
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = size == WiomBottomSheetSize.Full,
    ),
    content: @Composable ColumnScope.() -> Unit,
)

enum class WiomBottomSheetSize {
    Compact, Half, Expanded, Full,
    Illustration, IllustrationCta, IllustrationLeft, Share, Form
}
```

### Helpers

- `WiomBottomSheetHeader(title, subtitle = null)` — title `type.headingLg` left-aligned, optional subtitle `type.bodyMd` · `text.subtle`. Padding `space.xl` L, `space.lg` R, `space.xs` T, `space.md` B. Divider below.
- `WiomBottomSheetListItem(label, onClick, description = null, icon = null)` — built inline with `space.xl` horizontal padding. Icon lives in a 40dp `bg.brandSubtle` circle; label is `type.labelLg`; trailing `Icons.Rounded.ChevronRight` is 20dp `icon.disabled`.
- `WiomBottomSheetIllustration(icon, heading, subtext)` — 120dp `bg.brandSubtle` circle with 48dp `icon.brand` glyph centered. Heading + subtext centered.
- `WiomBottomSheetActions { content: @Composable RowScope.() -> Unit }` — top divider, `space.xl` H × `space.lg` V padding, `space.md` gap. Place 1–3 `WiomButton` calls inside.

---

## Usage

### Payment confirmation (Illustration + actions)

```kotlin
if (showSheet) {
    WiomBottomSheet(
        onDismissRequest = { showSheet = false },
        size = WiomBottomSheetSize.IllustrationCta,
    ) {
        WiomBottomSheetIllustration(
            icon = Icons.Rounded.Payment,
            heading = "Pay Rs 299?",
            subtext = "Monthly recharge for Plan A. Amount will be debited from your UPI account.",
        )
        WiomBottomSheetActions {
            WiomButton(
                text = "Cancel",
                onClick = { showSheet = false },
                modifier = Modifier.weight(1f),
                type = WiomButtonType.Secondary,
            )
            WiomButton(
                text = "Pay now",
                onClick = { /* … */ },
                modifier = Modifier.weight(1f),
                type = WiomButtonType.Primary,
            )
        }
    }
}
```

### Option menu (Half + list rows, no actions)

```kotlin
WiomBottomSheet(
    onDismissRequest = onDismiss,
    size = WiomBottomSheetSize.Half,
) {
    WiomBottomSheetHeader(title = "Policy options")
    WiomBottomSheetListItem("View policy details", onClick = { /* … */ }, icon = Icons.Rounded.Description)
    WiomBottomSheetListItem("Make a payment",     onClick = { /* … */ }, icon = Icons.Rounded.Payment)
    WiomBottomSheetListItem("Contact support",    onClick = { /* … */ }, icon = Icons.Rounded.SupportAgent)
    WiomBottomSheetListItem("Download documents", onClick = { /* … */ }, icon = Icons.Rounded.Download)
}
```

---

## Rules

- Top corners are always `radius.xlarge`; bottom corners are always `radius.none` — the sheet anchors to the screen edge.
- Drag handle is the dismiss affordance. Don't add a close button.
- No top border on the action bar — `space.lg` top padding + divider above the row provides the separation. (The divider is intentional for list + actions; illustration variants can omit actions entirely if no commit is needed.)
- `space.xl` (24dp) horizontal padding everywhere — header left, list row, illustration block, action bar. Don't fall back to 16dp.
- Rows are built inline, not via `WiomListItem` — that component's 16dp inset would break the sheet's 24dp alignment.
- Max 3 actions in `WiomBottomSheetActions`. For three side-by-side actions, none may be a Primary CTA — follow the `wiom-cta` button-group rule.
- Don't nest a bottom sheet inside another bottom sheet.
- Don't use a bottom sheet for a blocking single-thought alert — use `WiomDialog` instead.

---

## Tokens used

| Slot | Token |
|---|---|
| Sheet fill | `bg.default` |
| Scrim | `overlay.scrim` |
| Top corner radius | `radius.xlarge` |
| Handle fill | `stroke.strong` |
| Handle radius | `radius.full` |
| Header title | `type.headingLg` · `text.default` |
| Header subtitle | `type.bodyMd` · `text.subtle` |
| Divider | `stroke.small` · `stroke.subtle` |
| List item label | `type.labelLg` · `text.default` |
| List item description | `type.bodyMd` · `text.subtle` |
| List item icon circle | `bg.brandSubtle` · 40dp · `radius.full` |
| List item icon | `icon.brand` · 20dp |
| Chevron | `icon.disabled` · 20dp |
| Illustration circle | `bg.brandSubtle` · 120dp · `radius.full` |
| Illustration icon | `icon.brand` · 48dp |
| Illustration heading | `type.headingLg` · `text.default` |
| Illustration subtext | `type.bodyLg` · `text.subtle` |
