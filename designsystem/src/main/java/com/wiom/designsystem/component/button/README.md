# WiomButton

The approved CTA foundation for the Wiom Android apps. One `WiomButton` composable
covers five types; [`WiomAcknowledge`](#wiomacknowledge) is a 1st-person consent
row paired with a Primary CTA for irreversible actions.

Source of truth: `.skills-cache/wiom-cta.md`. Token source of truth:
`foundation/color/WiomColors.kt`.

---

## Types

| Type          | Use for                                                    | Fill                 | Label / Icon                 |
| ------------- | ---------------------------------------------------------- | -------------------- | ---------------------------- |
| `Primary`     | The one main action per screen                             | `bg.brand`           | `text.onBrand` / `icon.inverse` |
| `Secondary`   | Alternate prominent action (outlined, brand border)        | transparent          | `text.brand` / `icon.brand`  |
| `Tertiary`    | Lightweight dismiss / soft action (text-only)              | transparent          | `text.brand` / `icon.brand`  |
| `Destructive` | Irreversible destruction (delete, cancel plan, remove)     | `bg.critical`        | `text.onCritical` / `icon.inverse` |
| `PreBooking`  | Customer-app pre-booking flows only                        | `bg.brandAccent`     | `text.onBrandAccent` / `icon.action` |

## States

| State     | Behavior                                                           |
| --------- | ------------------------------------------------------------------ |
| Default   | Rest fill. Pressed fill is derived automatically (`InteractionSource`). |
| Disabled  | `bg.disabled`, `text.disabled`, `icon.disabled`. Non-interactive. |
| Loading   | Content fades out; a spinner in the matching tint centers in the button. |

Pressed is NOT a parameter — it is driven by `InteractionSource.collectIsPressedAsState()`.
Hover is not a Wiom state (touch-first). Focus is surfaced externally via the
`stroke.brandFocus` / `stroke.criticalFocus` ring, not as a fill variant.

## API

```kotlin
@Composable
fun WiomButton(
    text: String,
    type: WiomButtonType,
    modifier: Modifier = Modifier,
    state: WiomButtonState = WiomButtonState.Default,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onClick: () -> Unit = {},
)

@Composable
fun WiomAcknowledge(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
)
```

## Wiom use cases

- **Sticky-bottom CTA**: `Primary` pinned above the nav bar for the reason-for-being
  action (Recharge, Submit, Pay).
- **Side-by-side pair**: `Secondary + Primary` (cancel/confirm) inside a bottom sheet.
- **Stacked pair**: `Primary + Tertiary` (pay now / maybe later) on a soft-dismiss sheet.
- **Destructive pair**: `Secondary + Destructive` — never pair Primary + Destructive.
- **Pre-booking**: Customer-app pre-booking flows only, never for generic Buy / Pay.
- **Acknowledge + Primary**: 1st-person verification before irreversible actions
  ("मैंने सभी जानकारी सही दी है"). Paired Primary stays `Disabled` until `checked`.

## Rules

1. Max 1 Primary and 1 Destructive per screen.
2. Never Destructive for Skip / Cancel / Close / No thanks — those are Secondary/Tertiary.
3. Right = preferred in side-by-side. Top = preferred in stacked.
4. Tertiary never side-by-side with Primary.
5. CTA copy reflects the destination ("Recharge", not "Continue").
6. Never wrap / truncate / shrink CTA text — rewrite shorter.
7. No `Modifier.height(...)`. Width via `.weight(1f)` (Row) or `.fillMaxWidth()` (Column).
8. Icons from `Icons.Rounded.*` only — never `Icons.Default/Filled/Outlined/Sharp`.
9. Acknowledge copy is 1st-person ("मैंने…" / "I've…"), never pre-checked.

## Tokens

- Radius: `radius.large` (16 dp)
- Padding: `spacing.lg` (16 dp) horizontal, `spacing.md` (12 dp) vertical
- Label: `type.labelLg` (16 sp SemiBold)
- Icon size: `iconSize.sm` (20 dp)
- Icon ↔ label gap: `spacing.sm` (8 dp)
- Shadow: none (flat; sticky bars use `stroke.small + stroke.subtle` border-top instead)
