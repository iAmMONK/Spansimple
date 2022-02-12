package com.iammonk.spansimple.handlers.attributes

import android.text.SpannableStringBuilder
import com.iammonk.spansimple.SpanStack
import com.iammonk.spansimple.handlers.StyledTextHandler
import com.iammonk.spansimple.style.Style
import org.htmlcleaner.TagNode


class AlignmentAttributeHandler(wrapHandler: StyledTextHandler) :
    WrappingStyleHandler(wrapHandler) {
    override fun handleTagNode(
        node: TagNode, builder: SpannableStringBuilder,
        start: Int, end: Int, useStyle: Style, stack: SpanStack
    ) {
        var mStyle = useStyle
        val align = node.getAttributeByName("align")
        when {
            "right".equals(align, ignoreCase = true) -> {
                mStyle = mStyle.setTextAlignment(Style.TextAlignment.RIGHT)
            }
            "center".equals(align, ignoreCase = true) -> {
                mStyle = mStyle.setTextAlignment(Style.TextAlignment.CENTER)
            }
            "left".equals(align, ignoreCase = true) -> {
                mStyle = mStyle.setTextAlignment(Style.TextAlignment.LEFT)
            }
        }
        super.handleTagNode(node, builder, start, end, mStyle, stack)
    }
}