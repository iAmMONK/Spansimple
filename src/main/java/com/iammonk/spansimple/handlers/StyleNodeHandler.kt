package com.iammonk.spansimple.handlers

import android.text.SpannableStringBuilder
import com.iammonk.spansimple.SpanStack
import com.iammonk.spansimple.TagNodeHandler
import com.iammonk.spansimple.css.CSSCompiler.compile
import com.iammonk.spansimple.css.CSSParser
import org.htmlcleaner.ContentNode
import org.htmlcleaner.TagNode

class StyleNodeHandler : TagNodeHandler() {
    override fun handleTagNode(
        node: TagNode,
        builder: SpannableStringBuilder,
        start: Int,
        end: Int,
        spanStack: SpanStack
    ) {
        if (spanner!!.isAllowStyling) {
            if (node.allChildren.size == 1) {
                val childNode: Any = node.allChildren[0]
                if (childNode is ContentNode) {
                    parseCSSFromText(
                        childNode.content,
                        spanStack
                    )
                }
            }
        }
    }

    private fun parseCSSFromText(text: String, spanStack: SpanStack) {
        try {
            CSSParser.parse(text).forEach { rule ->
                spanStack.registerCompiledRule(compile(rule))
            }
        } catch (e: Exception) {

        }
    }

    override val rendersContent: Boolean get() = true
}