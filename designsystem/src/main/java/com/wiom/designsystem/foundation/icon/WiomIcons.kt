package com.wiom.designsystem.foundation.icon

import androidx.annotation.DrawableRes
import com.wiom.designsystem.R

/**
 * Wiom icon set — Material Symbols Rounded (filled, weight 400).
 *
 * RULES:
 *  - Consumers MUST use WiomIcons.<name> only. Never import Icons.Default / Icons.Filled / Icons.Outlined.
 *  - To add a new icon: download the Material Symbols Rounded SVG (fill=1, wght=400) from
 *    https://fonts.google.com/icons?icon.style=Rounded, convert to Android Vector Drawable,
 *    place in designsystem/src/main/res/drawable/ as `wiom_ic_<name>.xml`, then add a property here.
 *  - If an icon is visually unique to a product surface (not a standard glyph), skip this set —
 *    ship it as a product-specific drawable in the consuming app.
 */
object WiomIcons {
    @DrawableRes val search: Int = R.drawable.wiom_ic_search
    @DrawableRes val cancel: Int = R.drawable.wiom_ic_cancel
    @DrawableRes val close: Int = R.drawable.wiom_ic_close
    @DrawableRes val check: Int = R.drawable.wiom_ic_check
    @DrawableRes val phone: Int = R.drawable.wiom_ic_phone
    @DrawableRes val visibility: Int = R.drawable.wiom_ic_visibility
    @DrawableRes val visibilityOff: Int = R.drawable.wiom_ic_visibility_off
    @DrawableRes val checkCircle: Int = R.drawable.wiom_ic_check_circle
    @DrawableRes val error: Int = R.drawable.wiom_ic_error
    @DrawableRes val warning: Int = R.drawable.wiom_ic_warning
    @DrawableRes val refresh: Int = R.drawable.wiom_ic_refresh
    @DrawableRes val expandMore: Int = R.drawable.wiom_ic_expand_more
    @DrawableRes val arrowBack: Int = R.drawable.wiom_ic_arrow_back
    @DrawableRes val menu: Int = R.drawable.wiom_ic_menu
    @DrawableRes val moreVert: Int = R.drawable.wiom_ic_more_vert
}
