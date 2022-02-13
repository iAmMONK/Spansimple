package com.iammonk.spansimple.handlers

import android.text.SpannableStringBuilder
import com.iammonk.spansimple.SpanStack
import com.iammonk.spansimple.TagNodeHandler
import org.htmlcleaner.TagNode

class NewLineHandler(wrappedHandler: TagNodeHandler) : WrappingHandler(wrappedHandler) {
    override fun handleTagNode(
        node: TagNode, builder: SpannableStringBuilder,
        start: Int, end: Int, spanStack: SpanStack
    ) {
        super.handleTagNode(node, builder, start, end, spanStack)
        appendNewLine(builder)
    }
}