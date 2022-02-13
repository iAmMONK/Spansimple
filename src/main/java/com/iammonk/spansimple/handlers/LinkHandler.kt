package com.iammonk.spansimple.handlers

import android.text.SpannableStringBuilder
import android.text.style.URLSpan
import com.iammonk.spansimple.SpanStack
import com.iammonk.spansimple.TagNodeHandler
import org.htmlcleaner.TagNode

class LinkHandler : TagNodeHandler() {
    override fun handleTagNode(
        node: TagNode, builder: SpannableStringBuilder,
        start: Int, end: Int, spanStack: SpanStack
    ) {
        val href = node.getAttributeByName("href")
        spanStack.pushSpan(URLSpan(href), start, end)
    }
}