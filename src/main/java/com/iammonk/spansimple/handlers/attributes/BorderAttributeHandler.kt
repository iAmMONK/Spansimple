package com.iammonk.spansimple.handlers.attributes

import android.text.SpannableStringBuilder
import com.iammonk.spansimple.SpanStack
import com.iammonk.spansimple.handlers.StyledTextHandler
import com.iammonk.spansimple.spans.BorderSpan
import com.iammonk.spansimple.style.Style
import org.htmlcleaner.TagNode

class BorderAttributeHandler(handler: StyledTextHandler) : WrappingStyleHandler(handler) {
    override fun handleTagNode(
        node: TagNode, builder: SpannableStringBuilder, start: Int, end: Int,
        useStyle: Style, stack: SpanStack
    ) {
        if (node.getAttributeByName("border") != null) {
            stack.pushSpan(
                BorderSpan(useStyle, start, end, settings.isUseColoursFromStyle),
                start,
                end
            )
        }
        super.handleTagNode(node, builder, start, end, useStyle, stack)
    }
}