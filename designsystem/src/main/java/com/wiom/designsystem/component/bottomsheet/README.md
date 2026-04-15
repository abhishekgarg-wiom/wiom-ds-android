# WiomBottomSheet

Modal bottom sheet for supplementary content and contextual actions. Surface anchored to screen bottom with scrim overlay.

Wraps Material3's `ModalBottomSheet` with Wiom tokens. For v0.1 we ship **Modal** only (with scrim); Standard (persistent, no scrim) comes later.

## When to use

- **List-based:** option menu, quick settings, picker (Language, Sort)
- **Illustration:** confirmation, success, error feedback with visual context
- Contextual actions without leaving the screen

## When NOT to use

- Critical alert requiring immediate attention → Dialog
- Full-page flow → new screen
- Simple toast/notification → Snackbar
- Tooltip → inline hint

## API

```kotlin
var showSheet by remember { mutableStateOf(false) }

if (showSheet) {
    WiomBottomSheet(onDismissRequest = { showSheet = false }) {
        WiomBottomSheetHeader(title = "Policy Options")
        WiomBottomSheetListItem(
            label = "View Policy Details",
            description = "See coverage, plan, and expiry",
            icon = WiomIcons.phone,
            onClick = { /* ... */ },
        )
        WiomBottomSheetListItem(
            label = "Make a Payment",
            icon = WiomIcons.checkCircle,
            onClick = { /* ... */ },
        )
        WiomBottomSheetActions {
            // Pass WiomCta buttons here when available.
            // In v0.1, use your own button primitives.
        }
    }
}
```

### Illustration layout (confirmation / success / error)

```kotlin
WiomBottomSheet(onDismissRequest = { showSheet = false }) {
    WiomBottomSheetIllustration(
        icon = WiomIcons.checkCircle,
        heading = "Payment Successful",
        subtext = "₹299 has been charged. Your plan is now active until 15 May.",
    )
    WiomBottomSheetActions {
        // Single primary CTA ("Done") — coming with WiomCta.
    }
}
```

## Rules

1. **Always include a drag handle.** Primary dismiss affordance — this is baked in.
2. **No close button.** Drag handle + scrim tap are enough.
3. **No border + shadow together.** Shadow alone provides depth (Material3 handles this).
4. **Top corners `radius.xlarge` only.** Bottom corners are `0dp` — the sheet anchors to the screen edge.
5. **Title is left-aligned** in list layout. Centered only in illustration layout.
6. **Divider after header** (full width) — dividers between list items are inset inside cards.
7. **Never nest a bottom sheet inside another.**
8. **Max ~7 list items** before scroll becomes necessary.

## Composition

- `WiomBottomSheet { }` — the container. Handles dismiss, scrim, shape.
- `WiomBottomSheetHeader(title, subtitle)` — left-aligned title + divider.
- `WiomBottomSheetListItem(label, description, icon, onClick)` — brand-tint circle icon + label + description + chevron. Uses `WiomListItem` under the hood.
- `WiomBottomSheetIllustration(icon, heading, subtext)` — 120dp brand-tint circle with 48dp icon, centered heading + subtext.
- `WiomBottomSheetActions { }` — row of buttons at the bottom, divider above.

## Tokens

- Container: `surface.base` · `radius.xlarge` top, `radius.none` bottom
- Scrim: `overlay.default` (rgba 22,16,33,0.50)
- Drag handle: 32×4dp · `border.strong` · `radius.full`
- Header title: `type.headingMd` · `text.primary`
- Header subtitle: `type.bodySm` · `text.secondary`
- Header padding: `space.xl` left, `space.lg` right, `space.xs` top, `space.md` bottom
- Divider: `stroke.small` · `border.subtle`
- List item icon circle: 40dp · `brand.primaryTint`
- List item icon: 20dp · `brand.primary`
- Illustration circle: 120dp · `brand.primaryTint` · radius full
- Illustration icon: 48dp (`icon.lg`) · `brand.primary`
- Illustration heading: `type.headingMd` · centered
- Illustration subtext: `type.bodyMd` · `text.secondary` · centered
- Action bar padding: `space.xl` H, `space.lg` V · gap: `space.md`
