package com.iammonk.spansimple.handlers.attributes

import android.text.SpannableStringBuilder
import com.iammonk.spansimple.SpanStack
import com.iammonk.spansimple.handlers.StyledTextHandler
import com.iammonk.spansimple.style.Style
import org.htmlcleaner.TagNode

open class WrappingStyleHandler(private val wrappedHandler: StyledTextHandler) :
    StyledTextHandler(Style()) {
    override val style: Style get() = wrappedHandler.style

    override fun beforeChildren(
        node: TagNode,
        builder: SpannableStringBuilder,
        spanStack: SpanStack
    ) {
        wrappedHandler.beforeChildren(node, builder, spanStack)
    }

    override fun handleTagNode(
        node: TagNode, builder: SpannableStringBuilder, start: Int, end: Int, useStyle: Style,
        stack: SpanStack
    ) {
        wrappedHandler.handleTagNode(node, builder, start, end, useStyle, stack)
    }
}