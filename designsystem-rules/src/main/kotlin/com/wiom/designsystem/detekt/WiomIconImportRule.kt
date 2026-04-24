package com.wiom.designsystem.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtImportDirective

/**
 * Flags imports of non-Rounded Material Icons. The Wiom design system uses
 * Material 3 Rounded only: `Icons.Rounded.*` from `androidx.compose.material:material-icons-extended`.
 *
 * `Icons.AutoMirrored.Rounded.*` is also accepted — same Rounded shape, adds
 * RTL auto-mirroring for directional icons (ArrowBack, ArrowForward, etc.).
 *
 * Banned import roots:
 *  - `androidx.compose.material.icons.Icons.Default.*`
 *  - `androidx.compose.material.icons.Icons.Filled.*`
 *  - `androidx.compose.material.icons.Icons.Outlined.*`
 *  - `androidx.compose.material.icons.Icons.Sharp.*`
 *  - `androidx.compose.material.icons.Icons.TwoTone.*`
 */
class WiomIconImportRule(config: Config = Config.empty) : Rule(config) {

    override val issue = Issue(
        id = "WiomIconImportRule",
        severity = Severity.CodeSmell,
        description = "Non-Rounded Material Icon imported. Wiom uses Icons.Rounded.* only. " +
            "Bypass with @file:Suppress(\"WiomNotUsed\") + // non-wiom: <reason> + ADR.",
        debt = io.gitlab.arturbosch.detekt.api.Debt.FIVE_MINS,
    )

    private val bannedFamilies = setOf("Default", "Filled", "Outlined", "Sharp", "TwoTone")

    override fun visitImportDirective(importDirective: KtImportDirective) {
        super.visitImportDirective(importDirective)
        val path = importDirective.importedFqName?.asString() ?: return
        if (!path.startsWith("androidx.compose.material.icons.Icons.")) return
        val family = path.removePrefix("androidx.compose.material.icons.Icons.").substringBefore('.')
        if (family in bannedFamilies) {
            report(
                CodeSmell(
                    issue,
                    Entity.from(importDirective),
                    "Import '$path' uses Icons.$family — replace with Icons.Rounded.${path.substringAfterLast('.')}.",
                )
            )
        }
    }
}
