package com.iammonk.spansimple.handlers.attributes

import android.text.SpannableStringBuilder
import com.iammonk.spansimple.SpanStack
import com.iammonk.spansimple.css.CSSCompiler.getStyleUpdater
import com.iammonk.spansimple.handlers.StyledTextHandler
import com.iammonk.spansimple.style.Style
import org.htmlcleaner.TagNode

class StyleAttributeHandler(wrapHandler: StyledTextHandler) : WrappingStyleHandler(wrapHandler) {
    override fun handleTagNode(
        node: TagNode, builder: SpannableStringBuilder, start: Int, end: Int, useStyle: Style,
        stack: SpanStack
    ) {
        val styleAttr = node.getAttributeByName("style")
        if (settings.isAllowStyling && styleAttr != null) {
            super.handleTagNode(
                node, builder, start, end,
                parseStyleFromAttribute(useStyle, styleAttr),
                stack
            )
        } else {
            super.handleTagNode(node, builder, start, end, useStyle, stack)
        }
    }

    private fun parseStyleFromAttribute(baseStyle: Style, attribute: String): Style {
        var style = baseStyle
        val pairs = attribute.split(";").toTypedArray()
        for (pair in pairs) {
            val keyVal = pair.split(":").toTypedArray()
            if (keyVal.size != 2) {
                return baseStyle
            }
            val key = keyVal[0].lowercase().trim { it <= ' ' }
            val value = keyVal[1].lowercase().trim { it <= ' ' }
            val updater = getStyleUpdater(key, value)
            if (updater != null) {
                style = updater(style)
            }
        }
        return style
    }
}