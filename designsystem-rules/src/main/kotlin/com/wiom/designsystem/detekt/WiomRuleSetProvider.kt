package com.wiom.designsystem.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

/**
 * Wiom Design System Detekt rules.
 *
 * In a consuming app's `build.gradle.kts`:
 * ```
 * detektPlugins("com.github.abhishekgarg-wiom.wiom-ds-android:designsystem-rules:v1.0.0")
 * ```
 *
 * Rules:
 *  - [WiomTokenRule] (error) — raw `Color(0xFF…)`, `.sp`, `.dp` literals in `@Composable` bodies.
 *  - [WiomIconImportRule] (error) — imports of `androidx.compose.material.icons.Icons.Default/Filled/Outlined/Sharp/TwoTone`.
 *  - [WiomMaterialComponentRule] (warning) — Material3 components that have a Wiom equivalent.
 *
 * **Opt-out** per file with `@file:Suppress("WiomNotUsed")` + an inline
 * `// non-wiom: <short reason>` comment. PR review must justify via an ADR.
 */
class WiomRuleSetProvider : RuleSetProvider {
    override val ruleSetId: String = "wiom"

    override fun instance(config: Config): RuleSet = RuleSet(
        ruleSetId,
        listOf(
            WiomTokenRule(config),
            WiomIconImportRule(config),
            WiomMaterialComponentRule(config),
        ),
    )
}
