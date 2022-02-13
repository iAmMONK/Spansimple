package com.iammonk.spansimple.handlers

import android.text.SpannableStringBuilder
import com.iammonk.spansimple.SpanStack
import com.iammonk.spansimple.css.CSSCompiler.getStyleUpdater
import com.iammonk.spansimple.css.StyleUpdater
import com.iammonk.spansimple.style.Style
import org.htmlcleaner.TagNode

/**
 * Handler for font-tags
 */
class FontHandler : StyledTextHandler(Style()) {
    override fun handleTagNode(
        node: TagNode, builder: SpannableStringBuilder,
        start: Int, end: Int, useStyle: Style, stack: SpanStack
    ) {
        var mStyle = useStyle
        if (settings.isAllowStyling) {
            val face = node.getAttributeByName("face")
            val size = node.getAttributeByName("size")
            val color = node.getAttributeByName("color")
            val family = settings.fontResolver.getFont(face)
            mStyle = mStyle.setFontFamily(family)
            if (size != null) {
                val updater: StyleUpdater? = getStyleUpdater("font-size", size)
                if (updater != null) {
                    mStyle = updater(mStyle)
                }
            }
            if (color != null && settings.isUseColoursFromStyle) {
                val updater = getStyleUpdater("color", color)
                if (updater != null) {
                    mStyle = updater(mStyle)
                }
            }
        }
        super.handleTagNode(node, builder, start, end, mStyle, stack)
    }
}