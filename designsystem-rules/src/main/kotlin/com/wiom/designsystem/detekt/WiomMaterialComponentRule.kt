package com.wiom.designsystem.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtCallExpression

/**
 * Warns on Material3 components that have a Wiom equivalent.
 * Severity is warning (not error) to leave space for the one-off cases
 * where a Material primitive is the right answer.
 */
class WiomMaterialComponentRule(config: Config = Config.empty) : Rule(config) {

    override val issue = Issue(
        id = "WiomMaterialComponentRule",
        severity = Severity.Warning,
        description = "Material3 component used where a Wiom equivalent exists. Prefer the Wiom API.",
        debt = io.gitlab.arturbosch.detekt.api.Debt.FIVE_MINS,
    )

    private val suggestions = mapOf(
        "Button" to "WiomButton",
        "OutlinedButton" to "WiomButton(type = Secondary)",
        "TextButton" to "WiomButton(type = Tertiary)",
        "FilledTonalButton" to "WiomButton",
        "OutlinedTextField" to "WiomInput",
        "TextField" to "WiomInput",
        "Switch" to "WiomSwitch",
        "Checkbox" to "WiomCheckbox",
        "TriStateCheckbox" to "WiomCheckbox (indeterminate state)",
        "RadioButton" to "WiomRadio",
        "TopAppBar" to "WiomTopBar",
        "CenterAlignedTopAppBar" to "WiomTopBar(centered = true)",
        "MediumTopAppBar" to "WiomTopBar(size = Medium)",
        "LargeTopAppBar" to "WiomTopBar(size = Large)",
        "NavigationBar" to "WiomNavigationBar",
        "NavigationBarItem" to "WiomNavItem in WiomNavigationBar",
        "ModalBottomSheet" to "WiomBottomSheet",
        "AlertDialog" to "WiomAlertDialog",
        "BasicAlertDialog" to "WiomAlertDialog",
        "LinearProgressIndicator" to "WiomProgressIndicator (linear)",
        "CircularProgressIndicator" to "WiomProgressIndicator (circular) or WiomLoader",
        "Snackbar" to "WiomToast",
        "TabRow" to "WiomPillTabs or WiomUnderlineFilter",
        "ScrollableTabRow" to "WiomUnderlineFilter(scrollable = true)",
        "Tab" to "a WiomPillTabs / WiomUnderlineFilter item",
        "FilterChip" to "WiomChip",
        "AssistChip" to "WiomChip",
        "InputChip" to "WiomChip",
    )

    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)
        val callee = expression.calleeExpression?.text ?: return
        val replacement = suggestions[callee] ?: return
        report(
            CodeSmell(
                issue,
                Entity.from(expression),
                "Material3 '$callee' — prefer '$replacement'. If you genuinely need Material here, " +
                    "add // non-wiom: <reason> + link an ADR in your PR.",
            )
        )
    }
}
