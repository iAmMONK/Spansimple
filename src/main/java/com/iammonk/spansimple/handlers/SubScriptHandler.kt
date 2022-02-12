package com.iammonk.spansimple.handlers

import android.text.SpannableStringBuilder
import android.text.style.SubscriptSpan
import com.iammonk.spansimple.SpanStack
import com.iammonk.spansimple.TagNodeHandler
import org.htmlcleaner.TagNode

class SubScriptHandler : TagNodeHandler() {
    override fun handleTagNode(
        node: TagNode, builder: SpannableStringBuilder,
        start: Int, end: Int, spanStack: SpanStack
    ) {
        spanStack.pushSpan(SubscriptSpan(), start, end)
    }
}