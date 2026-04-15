# Wiom Design System — Android

Jetpack Compose design system for Wiom's Android apps. Provides tokens (colors, typography, spacing, radius, stroke, shadow, icons) and pre-built components so developers don't maintain UI themselves.

---

## Integration

### 1. Add JitPack to your root `settings.gradle.kts`

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }   // <-- add this
    }
}
```

### 2. Add the dependency in your `app/build.gradle.kts`

```kotlin
dependencies {
    implementation("com.github.abhishekgarg-wiom.wiom-ds-android:designsystem:v0.0.1")
}
```

> JitPack multi-module coordinate format: `com.github.<user>.<repo>:<module>:<tag>`. The `:designsystem` suffix is the library module — `:sample` is the demo app and is not published.

### 3. Wrap your app in `WiomTheme`

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WiomTheme {
                // your app
            }
        }
    }
}
```

That's it. All Wiom tokens are now available anywhere under `WiomTheme { }`.

---

## Usage

### Tokens (use from anywhere)

```kotlin
@Composable
fun Example() {
    Column(
        modifier = Modifier
            .background(WiomTheme.colors.surface.base)
            .padding(WiomTheme.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(WiomTheme.spacing.sm),
    ) {
        Text(
            "Heading",
            style = WiomTheme.type.headingLg,
            color = WiomTheme.colors.text.primary,
        )
        Text(
            "Body text",
            style = WiomTheme.type.bodyMd,
            color = WiomTheme.colors.text.secondary,
        )
    }
}
```

### Icons

```kotlin
WiomIcon(
    id = WiomIcons.search,
    contentDescription = "Search",
    tint = WiomTheme.colors.text.secondary,
)
```

---

## What's in v0.0.1

- Foundations: 43 color tokens, 13 typography tokens, 13 spacing tokens, 7 radius tokens, 2 stroke tokens, 5 shadow tokens, 4 icon size tokens
- `WiomTheme` composable + `WiomTheme.*` token accessor
- `WiomIcon` + `WiomIcons` facade with 10 Material Symbols Rounded drawables
- Material3 bridge — Material components pick up approximate Wiom colors

### Coming next

- `WiomInput` — input, textarea, OTP, search, mobile, password (see `wiom-input-fields` skill)
- `WiomButton` — primary, secondary, destructive, ghost
- `WiomTopBar` — Small/Medium/Large × 4 states
- More icons as components need them

---

## Repo layout

- `designsystem/` — the library module (published)
- `sample/` — demo app showing every token and component in use
- `CLAUDE.md` — rules for Claude Code when editing this repo
- `.github/workflows/` — CI
- `jitpack.yml` — JitPack build config

---

## Contributing

All changes go through pull request. Claude Code follows the rules in `CLAUDE.md`. Human contributors should read it too — it's short.

1. Branch from `main`
2. Edit/add components under `designsystem/src/main/java/com/wiom/designsystem/`
3. Add preview composables and sample app usage
4. Update `CHANGELOG.md`
5. Open PR — CI runs lint and build
6. Merge → tag new version → JitPack picks it up

---

## Design system rules (quick)

**All five rules are hard rules.** Violations break downstream apps.

1. **Token-only.** No raw hex, sp, dp outside `foundation/`.
2. **Intrinsic sizing.** Never set fixed height/width on text-bearing components.
3. **Icons through facade.** `WiomIcons.<name>` only — not `Icons.Default.*`.
4. **Document use cases.** Every component README lists concrete Wiom examples.
5. **Versioning.** SemVer. Breaking = major. New feature = minor. Fix = patch.

Full rules + rationale in [CLAUDE.md](./CLAUDE.md).

---

## Context

Wiom serves Indian households. Design assumptions:

- **India-only userbase** — no country code prefixes in phone fields
- **Currency:** INR only (`₹`)
- **Languages:** Hindi + English (budget 1.3x text expansion)
- **Devices:** Budget Android, min SDK 24
- **Tone:** trust-heavy (payments/recharges), low cognitive load, outdoor-readable

---

## License

Apache License 2.0. See [LICENSE](./LICENSE).
