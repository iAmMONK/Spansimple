package com.iammonk.spansimple.style

import com.iammonk.spansimple.FontFamily

class Style {
    enum class TextAlignment {
        LEFT, CENTER, RIGHT
    }

    enum class FontWeight {
        NORMAL, BOLD
    }

    enum class FontStyle {
        NORMAL, ITALIC
    }

    enum class DisplayStyle {
        BLOCK, INLINE
    }

    enum class BorderStyle {
        SOLID, DASHED, DOTTED, DOUBLE
    }

    companion object {
        /**
         * Temporary constant for the width of 1 horizontal em
         * Used for calculating margins.
         */
        const val HORIZONTAL_EM_WIDTH = 10
    }


    val fontFamily: FontFamily?
    val textAlignment: TextAlignment?
    val fontSize: StyleValue?
    val fontWeight: FontWeight?
    val fontStyle: FontStyle?
    val color: Int?
    val backgroundColor: Int?
    val borderColor: Int?
    val displayStyle: DisplayStyle?
    val borderStyle: BorderStyle?
    val borderWidth: StyleValue?
    val textIndent: StyleValue?
    val marginTop: StyleValue?
    val marginBottom: StyleValue?
    val marginLeft: StyleValue?
    val marginRight: StyleValue?

    constructor() {
        fontFamily = null
        textAlignment = null
        fontSize = null
        fontWeight = null
        fontStyle = null
        color = null
        backgroundColor = null
        displayStyle = null
        marginBottom = null
        textIndent = null
        marginTop = null
        marginLeft = null
        marginRight = null
        borderColor = null
        borderStyle = null
        borderWidth = null
    }

    constructor(
        family: FontFamily?, textAlignment: TextAlignment?, fontSize: StyleValue?,
        fontWeight: FontWeight?, fontStyle: FontStyle?, color: Int?,
        backgroundColor: Int?, displayStyle: DisplayStyle?, marginTop: StyleValue?,
        marginBottom: StyleValue?, marginLeft: StyleValue?, marginRight: StyleValue?,
        textIndent: StyleValue?, borderColor: Int?, borderStyle: BorderStyle?,
        borderWidth: StyleValue?
    ) {
        fontFamily = family
        this.textAlignment = textAlignment
        this.fontSize = fontSize
        this.fontWeight = fontWeight
        this.fontStyle = fontStyle
        this.color = color
        this.backgroundColor = backgroundColor
        this.displayStyle = displayStyle
        this.marginBottom = marginBottom
        this.textIndent = textIndent
        this.marginTop = marginTop
        this.marginLeft = marginLeft
        this.marginRight = marginRight
        this.borderColor = borderColor
        this.borderWidth = borderWidth
        this.borderStyle = borderStyle
    }

    fun setFontFamily(fontFamily: FontFamily?): Style {
        return Style(
            fontFamily, textAlignment, fontSize,
            fontWeight, fontStyle, color, backgroundColor, displayStyle,
            marginTop, marginBottom, marginLeft, marginRight, textIndent,
            borderColor, borderStyle, borderWidth
        )
    }

    fun setTextAlignment(alignment: TextAlignment?): Style {
        return Style(
            fontFamily, alignment, fontSize, fontWeight,
            fontStyle, color, backgroundColor, displayStyle,
            marginTop, marginBottom, marginLeft, marginRight, textIndent,
            borderColor, borderStyle, borderWidth
        )
    }

    fun setFontSize(fontSize: StyleValue?): Style {
        return Style(
            fontFamily, textAlignment, fontSize, fontWeight,
            fontStyle, color, backgroundColor, displayStyle,
            marginTop, marginBottom, marginLeft, marginRight, textIndent,
            borderColor, borderStyle, borderWidth
        )
    }

    fun setFontWeight(fontWeight: FontWeight?): Style {
        return Style(
            fontFamily, textAlignment, fontSize, fontWeight,
            fontStyle, color, backgroundColor, displayStyle,
            marginTop, marginBottom, marginLeft, marginRight, textIndent,
            borderColor, borderStyle, borderWidth
        )
    }

    fun setFontStyle(fontStyle: FontStyle?): Style {
        return Style(
            fontFamily, textAlignment, fontSize,
            fontWeight, fontStyle, color, backgroundColor,
            displayStyle, marginTop, marginBottom, marginLeft, marginRight,
            textIndent, borderColor, borderStyle, borderWidth
        )
    }

    fun setColor(color: Int?): Style {
        return Style(
            fontFamily, textAlignment, fontSize,
            fontWeight, fontStyle, color, backgroundColor,
            displayStyle, marginTop, marginBottom, marginLeft, marginRight,
            textIndent, borderColor, borderStyle, borderWidth
        )
    }

    fun setBackgroundColor(bgColor: Int?): Style {
        return Style(
            fontFamily, textAlignment, fontSize,
            fontWeight, fontStyle, color, bgColor,
            displayStyle, marginTop, marginBottom, marginLeft, marginRight,
            textIndent, borderColor, borderStyle, borderWidth
        )
    }

    fun setDisplayStyle(displayStyle: DisplayStyle?): Style {
        return Style(
            fontFamily, textAlignment, fontSize,
            fontWeight, fontStyle, color, backgroundColor,
            displayStyle, marginTop, marginBottom, marginLeft, marginRight,
            textIndent, borderColor, borderStyle, borderWidth
        )
    }

    fun setMarginBottom(marginBottom: StyleValue?): Style {
        return Style(
            fontFamily, textAlignment, fontSize,
            fontWeight, fontStyle, color, backgroundColor,
            displayStyle, marginTop, marginBottom, marginLeft,
            marginRight, textIndent, borderColor, borderStyle, borderWidth
        )
    }

    fun setMarginTop(marginTop: StyleValue?): Style {
        return Style(
            fontFamily, textAlignment, fontSize,
            fontWeight, fontStyle, color, backgroundColor,
            displayStyle, marginTop, marginBottom, marginLeft,
            marginRight, textIndent, borderColor, borderStyle, borderWidth
        )
    }

    fun setMarginLeft(marginLeft: StyleValue?): Style {
        return Style(
            fontFamily, textAlignment, fontSize,
            fontWeight, fontStyle, color, backgroundColor,
            displayStyle, marginTop, marginBottom, marginLeft,
            marginRight, textIndent, borderColor, borderStyle, borderWidth
        )
    }

    fun setMarginRight(marginRight: StyleValue?): Style {
        return Style(
            fontFamily, textAlignment, fontSize,
            fontWeight, fontStyle, color, backgroundColor,
            displayStyle, marginTop, marginBottom, marginLeft,
            marginRight, textIndent, borderColor, borderStyle, borderWidth
        )
    }

    fun setTextIndent(textIndent: StyleValue?): Style {
        return Style(
            fontFamily, textAlignment, fontSize,
            fontWeight, fontStyle, color, backgroundColor,
            displayStyle, marginTop, marginBottom, marginLeft, marginRight,
            textIndent, borderColor, borderStyle, borderWidth
        )
    }

    fun setBorderStyle(borderStyle: BorderStyle?): Style {
        return Style(
            fontFamily, textAlignment, fontSize,
            fontWeight, fontStyle, color, backgroundColor,
            displayStyle, marginTop, marginBottom, marginLeft, marginRight,
            textIndent, borderColor, borderStyle, borderWidth
        )
    }

    fun setBorderColor(borderColor: Int?): Style {
        return Style(
            fontFamily, textAlignment, fontSize,
            fontWeight, fontStyle, color, backgroundColor,
            displayStyle, marginTop, marginBottom, marginLeft, marginRight,
            textIndent, borderColor, borderStyle, borderWidth
        )
    }

    fun setBorderWidth(borderWidth: StyleValue?): Style {
        return Style(
            fontFamily, textAlignment, fontSize,
            fontWeight, fontStyle, color, backgroundColor,
            displayStyle, marginTop, marginBottom, marginLeft, marginRight,
            textIndent, borderColor, borderStyle, borderWidth
        )
    }

    override fun toString(): String {
        val result = StringBuilder("{\n")
        if (fontFamily != null) {
            result.append(
                """  font-family: ${fontFamily.name}
"""
            )
        }
        if (textAlignment != null) {
            result.append("  text-alignment: $textAlignment\n")
        }
        if (fontSize != null) {
            result.append("  font-size: $fontSize\n")
        }
        if (fontWeight != null) {
            result.append("  font-weight: $fontWeight\n")
        }
        if (fontStyle != null) {
            result.append("  font-style: $fontStyle\n")
        }
        if (color != null) {
            result.append("  color: $color\n")
        }
        if (backgroundColor != null) {
            result.append("  background-color: $backgroundColor\n")
        }
        if (displayStyle != null) {
            result.append("  display: $displayStyle\n")
        }
        if (marginTop != null) {
            result.append("  margin-top: $marginTop\n")
        }
        if (marginBottom != null) {
            result.append("  margin-bottom: $marginBottom\n")
        }
        if (marginLeft != null) {
            result.append("  margin-left: $marginLeft\n")
        }
        if (marginRight != null) {
            result.append("  margin-right: $marginRight\n")
        }
        if (textIndent != null) {
            result.append("  text-indent: $textIndent\n")
        }
        if (borderStyle != null) {
            result.append("  border-style: $borderStyle\n")
        }
        if (borderColor != null) {
            result.append("  border-color: $borderColor\n")
        }
        if (borderWidth != null) {
            result.append("  border-style: $borderWidth\n")
        }
        result.append("}\n")
        return result.toString()
    }
}