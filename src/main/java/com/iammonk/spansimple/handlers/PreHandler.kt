package com.iammonk.spansimple.handlers

import android.text.SpannableStringBuilder
import com.iammonk.spansimple.SpanStack
import com.iammonk.spansimple.TagNodeHandler
import com.iammonk.spansimple.TextUtil.replaceHtmlEntities
import com.iammonk.spansimple.spans.FontFamilySpan
import org.htmlcleaner.ContentNode
import org.htmlcleaner.TagNode

class PreHandler : TagNodeHandler() {
    private fun getPlainText(buffer: StringBuffer, node: Any) {
        if (node is ContentNode) {
            val text = replaceHtmlEntities(node.content, true)
            buffer.append(text)
        } else if (node is TagNode) {
            for (child in node.allChildren) {
                getPlainText(buffer, child)
            }
        }
    }

    override fun handleTagNode(
        node: TagNode, builder: SpannableStringBuilder,
        start: Int, end: Int, spanStack: SpanStack
    ) {
        val buffer = StringBuffer()
        getPlainText(buffer, node)
        builder.append(buffer.toString())
        val monoSpace = settings.fontResolver.monoSpaceFont
        spanStack.pushSpan(FontFamilySpan(monoSpace), start, builder.length)
        appendNewLine(builder)
        appendNewLine(builder)
    }

    override val rendersContent: Boolean get() = true
}