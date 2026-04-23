package com.wiom.designsystem.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.psiUtil.containingClass

/**
 * Flags raw design-primitive literals that should come from `WiomTheme.*`.
 *
 *  - `Color(0xFF...)` → use `WiomTheme.color.bg.*` / `.text.*` / `.stroke.*` / `.icon.*`
 *  - `<number>.sp`    → use `WiomTheme.type.*` TextStyle
 *  - `<number>.dp`    → use `WiomTheme.spacing.*`, `.radius.*`, `.stroke.*`, `.iconSize.*`
 *
 * Scope: expressions inside a function annotated `@Composable`, or property
 * initializers in a Composable class / object. Foundation / design-system
 * module files are skipped by path (they're where the tokens live).
 */
class WiomTokenRule(config: Config = Config.empty) : Rule(config) {

    override val issue = Issue(
        id = "WiomTokenRule",
        severity = Severity.CodeSmell,
        description = "Raw Color(...), .sp, or .dp literal inside a @Composable. " +
            "Use WiomTheme.color.* / type.* / spacing.* / radius.* / stroke.* / iconSize.* instead. " +
            "Bypass with @file:Suppress(\"WiomNotUsed\") + // non-wiom: <reason> + an ADR link in the PR.",
        debt = io.gitlab.arturbosch.detekt.api.Debt.FIVE_MINS,
    )

    override fun visitKtFile(file: KtFile) {
        // Don't lint the design-system module itself — it's where tokens are defined.
        val path = file.virtualFilePath
        if ("designsystem/src/main" in path || "designsystem-rules" in path) return
        super.visitKtFile(file)
    }

    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)
        val callee = expression.calleeExpression?.text ?: return
        if (callee != "Color") return
        if (!expression.isInComposable()) return
        val arg = expression.valueArguments.firstOrNull()?.getArgumentExpression()?.text ?: return
        // Color(0xFF...) or Color(red, green, blue, …) with literal ints — flag either form.
        if (arg.startsWith("0x") || arg.matches(Regex("^-?\\d+$"))) {
            report(
                CodeSmell(
                    issue,
                    Entity.from(expression),
                    "Raw Color() literal. Use a token from WiomTheme.color.{bg,text,stroke,icon}.",
                )
            )
        }
    }

    override fun visitDotQualifiedExpression(expression: KtDotQualifiedExpression) {
        super.visitDotQualifiedExpression(expression)
        val selector = expression.selectorExpression?.text ?: return
        if (selector != "sp" && selector != "dp") return
        val receiver = expression.receiverExpression.text
        // <literal>.sp or <literal>.dp
        if (receiver.matches(Regex("^-?\\d+(\\.\\d+)?$"))) {
            if (!expression.isInComposable()) return
            val tokenHint = when (selector) {
                "sp" -> "WiomTheme.type.* (size comes from a TextStyle — never a raw .sp)"
                else -> "WiomTheme.spacing.* / .radius.* / .stroke.* / .iconSize.*"
            }
            report(
                CodeSmell(
                    issue,
                    Entity.from(expression),
                    "Raw ${receiver}.${selector}. Use $tokenHint.",
                )
            )
        }
    }

    private fun com.intellij.psi.PsiElement.isInComposable(): Boolean {
        var parent: com.intellij.psi.PsiElement? = this.parent
        while (parent != null) {
            if (parent is KtNamedFunction) {
                val hasComposable = parent.annotationEntries.any {
                    it.shortName?.asString() == "Composable"
                }
                if (hasComposable) return true
            }
            if (parent is KtFile) return false
            parent = parent.parent
        }
        return false
    }
}
