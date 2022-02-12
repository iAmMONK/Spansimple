package com.iammonk.spansimple.handlers

import android.text.SpannableStringBuilder
import android.text.style.SuperscriptSpan
import com.iammonk.spansimple.SpanStack
import com.iammonk.spansimple.TagNodeHandler
import org.htmlcleaner.TagNode

class SuperScriptHandler : TagNodeHandler() {
    override fun handleTagNode(
        node: TagNode, builder: SpannableStringBuilder,
        start: Int, end: Int, spanStack: SpanStack
    ) {
        spanStack.pushSpan(SuperscriptSpan(), start, end)
    }
}