# Adopting Wiom Design System in your Android app

One-pager. Takes ~15 minutes to set up, then every new feature is Wiom by default.

---

## Principle

**New code uses Wiom. Old code stays.** You don't have to migrate existing screens. To bypass Wiom on a new feature, open an ADR.

---

## Who this is for

- **Partner app** — actively being rewritten from Flutter to Kotlin. Every new Compose screen must use Wiom from day one. Highest-priority adopter.
- **Customer app** — live in Kotlin. Apply Wiom to new features. Existing screens are untouched.

---

## Steps (~15 min)

### 1. Add the library

In `settings.gradle.kts`:
```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

In `app/build.gradle.kts`:
```kotlin
dependencies {
    implementation("com.github.abhishekgarg-wiom.wiom-ds-android:designsystem:v1.0.0")
}
```

Sync. First sync builds the library on JitPack (~2 min).

### 2. Wrap your app

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WiomTheme {
                AppNavHost()
            }
        }
    }
}
```

### 3. Enable the Detekt rules

In `app/build.gradle.kts` (or the module where you lint Compose code):
```kotlin
dependencies {
    detektPlugins("com.github.abhishekgarg-wiom.wiom-ds-android:designsystem-rules:v1.0.0")
}
```

The rules:
- **`WiomTokenRule`** (error) — blocks raw `Color(...)`, `.sp`, `.dp` in Composables
- **`WiomIconImportRule`** (error) — blocks non-Rounded Material icons
- **`WiomMaterialComponentRule`** (warning) — suggests the Wiom equivalent when you use a Material3 primitive

### 4. Drop in the PR template

Copy `https://github.com/abhishekgarg-wiom/wiom-ds-android/blob/main/adoption-kit/pr-template.md` into your repo at `.github/pull_request_template.md`.

### 5. Drop in the CLAUDE.md

Copy `https://github.com/abhishekgarg-wiom/wiom-ds-android/blob/main/adoption-kit/consumer-CLAUDE.md` into your consumer-app repo at `CLAUDE.md`. Future Claude Code sessions in that repo will honor the Wiom-by-default rule.

### 6. Drop in the ADR template

Copy `https://github.com/abhishekgarg-wiom/wiom-ds-android/blob/main/adoption-kit/adr-template.md` into your repo at `docs/adr/TEMPLATE.md`. Devs copy it to `docs/adr/<nnnn>-not-using-wiom-for-<x>.md` when they need to opt out.

### Done

Open a small PR touching one file, commit, push. CI should run and the rules should light up on any raw hex / sp / dp / Material icons you didn't clean. That's proof the wiring works.

---

## What counts as "new"

- A **new screen**, new Composable, new feature module → Wiom by default, full rules apply.
- **Bug fix** in an old screen → don't migrate in the same PR. Ship the fix. The rule kicks in only for new code.
- **Refactor** that touches a lot of existing code → treat it like a mini migration; either migrate the UI to Wiom or open an ADR explaining why not.

---

## The opt-out path (use sparingly)

When Wiom genuinely doesn't fit:

1. Open an ADR using `docs/adr/TEMPLATE.md` → fill in why, link issue on Wiom repo
2. Add to the file you can't make Wiom-clean:
   ```kotlin
   @file:Suppress("WiomNotUsed")
   // non-wiom: Material TimePickerDialog — no Wiom equivalent (ADR 0042, revisit 2026-09-01)
   ```
3. Link the ADR in the PR description

Review should treat the ADR as the main discussion, not the code diff.

---

## When to upgrade the library

When a new Wiom version ships:
- Read the CHANGELOG for breaking changes.
- Bump the version in your `build.gradle.kts`.
- Sync. Fix anything the Kotlin compiler flags.
- Exercise your migrated screens; flag regressions as issues on the Wiom repo.

v1.0.0 uses the **element-first token API** (`WiomTheme.color.bg.*` / `.text.*` / `.stroke.*` / `.icon.*`). Older versions (v0.3.0 and below) used role-first (`WiomTheme.colors.brand.primary`). If you're upgrading from v0.3.x, expect to update every token call site.

---

## Where to ask

- Bug / visual mismatch → https://github.com/abhishekgarg-wiom/wiom-ds-android/issues/new/choose
- General question → https://github.com/abhishekgarg-wiom/wiom-ds-android/discussions
- Need a new component / variant → Issue with the ✨ template, then ADR to unblock yourself
