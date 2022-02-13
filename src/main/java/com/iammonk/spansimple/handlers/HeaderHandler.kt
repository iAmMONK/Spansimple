package com.iammonk.spansimple.handlers

import com.iammonk.spansimple.style.Style
import com.iammonk.spansimple.style.StyleValue

class HeaderHandler private constructor(size: Float, margin: Float) : StyledTextHandler() {

    companion object {
        val H1 = HeaderHandler(1.5f, 0.5f)
        val H2 = HeaderHandler(1.4f, 0.6f)
        val H3 = HeaderHandler(1.3f, 0.7f)
        val H4 = HeaderHandler(1.2f, 0.8f)
        val H5 = HeaderHandler(1.1f, 0.9f)
        val H6 = HeaderHandler(1f, 1f)
    }

    private val size: StyleValue = StyleValue(size, StyleValue.Unit.EM)
    private val margin: StyleValue = StyleValue(margin, StyleValue.Unit.EM)

    override val style: Style
        get() = super.style.setFontSize(size)
            .setFontWeight(Style.FontWeight.BOLD)
            .setDisplayStyle(Style.DisplayStyle.BLOCK)
            .setMarginBottom(margin)
            .setMarginTop(margin)
}