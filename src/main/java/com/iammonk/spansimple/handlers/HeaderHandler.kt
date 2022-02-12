package com.iammonk.spansimple.handlers

import com.iammonk.spansimple.style.Style
import com.iammonk.spansimple.style.StyleValue

class HeaderHandler(size: Float, margin: Float) : StyledTextHandler() {
    private val size: StyleValue = StyleValue(size, StyleValue.Unit.EM)
    private val margin: StyleValue = StyleValue(margin, StyleValue.Unit.EM)

    override val style: Style
        get() = super.style.setFontSize(size)
            .setFontWeight(Style.FontWeight.BOLD)
            .setDisplayStyle(Style.DisplayStyle.BLOCK)
            .setMarginBottom(margin)
            .setMarginTop(margin)
}