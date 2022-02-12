package com.iammonk.spansimple.css

import com.iammonk.spansimple.css.CSSCompiler.TagNodeMatcher
import com.iammonk.spansimple.style.Style
import org.htmlcleaner.TagNode

/**
 * A Compiled CSS Rule.
 *
 * A CompiledRule consists of a numbers of matchers which can match TagNodes,
 * and StyleUpdaters which can update a Style object if the rule matches.
 */
class CompiledRule internal constructor(
    private val matchers: List<List<TagNodeMatcher>>,
    private val styleUpdaters: List<StyleUpdater>,
    private val asText: String
) {

    fun applyStyle(style: Style): Style {
        var result = style
        styleUpdaters.forEach { result = it(result) }
        return result
    }

    fun matches(tagNode: TagNode): Boolean = matchers.any { matchesChain(it, tagNode) }

    private fun matchesChain(matchers: List<TagNodeMatcher>, tagNode: TagNode): Boolean {
        var nodeToMatch = tagNode

        for (matcher in matchers) {
            if (!matcher.matches(nodeToMatch)) {
                return false
            }
            nodeToMatch = nodeToMatch.parent
        }
        return true
    }


    override fun toString() = asText
}