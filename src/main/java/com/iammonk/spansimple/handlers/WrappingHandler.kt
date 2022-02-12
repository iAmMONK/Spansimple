package com.iammonk.spansimple.handlers

import android.text.SpannableStringBuilder
import com.iammonk.spansimple.HtmlSpanner
import com.iammonk.spansimple.SpanStack
import com.iammonk.spansimple.TagNodeHandler
import org.htmlcleaner.TagNode


open class WrappingHandler(private val wrappedHandler: TagNodeHandler) : TagNodeHandler() {
    override fun handleTagNode(
        node: TagNode,
        builder: SpannableStringBuilder,
        start: Int,
        end: Int,
        spanStack: SpanStack
    ) {
        wrappedHandler.handleTagNode(node, builder, start, end, spanStack)
    }

    override var spanner: HtmlSpanner?
        get() = super.spanner
        set(spanner) {
            super.spanner = spanner
            wrappedHandler.spanner = spanner
        }
}