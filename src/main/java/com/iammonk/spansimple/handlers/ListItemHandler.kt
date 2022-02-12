package com.iammonk.spansimple.handlers

import android.text.SpannableStringBuilder
import com.iammonk.spansimple.SpanStack
import com.iammonk.spansimple.TagNodeHandler
import com.iammonk.spansimple.spans.ListItemSpan
import org.htmlcleaner.TagNode

class ListItemHandler : TagNodeHandler() {
    private fun getMyIndex(node: TagNode): Int {
        if (node.parent == null) {
            return -1
        }
        var i = 1
        for (child in node.parent.allChildren) {
            if (child === node) {
                return i
            }
            if (child is TagNode) {
                if ("li" == child.name) {
                    i++
                }
            }
        }
        return -1
    }

    private fun getParentName(node: TagNode): String? {
        return if (node.parent == null) {
            null
        } else node.parent.name
    }

    override fun handleTagNode(
        node: TagNode, builder: SpannableStringBuilder,
        start: Int, end: Int, spanStack: SpanStack
    ) {
        if (builder.isNotEmpty() && builder[builder.length - 1] != '\n'
        ) {
            builder.append("\n")
        }
        if ("ol" == getParentName(node)) {
            val bSpan = ListItemSpan(getMyIndex(node))
            spanStack.pushSpan(bSpan, start, end)
        } else if ("ul" == getParentName(node)) {
            // Unicode bullet character.
            val bSpan = ListItemSpan()
            spanStack.pushSpan(bSpan, start, end)
        }
    }
}