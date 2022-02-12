package com.iammonk.spansimple

import android.text.Spannable
import android.text.SpannableStringBuilder
import com.iammonk.spansimple.css.CompiledRule
import com.iammonk.spansimple.style.Style
import org.htmlcleaner.TagNode
import java.util.*

class SpanStack {
    private val spanItemStack = Stack<SpanCallback>()
    private val rules: MutableSet<CompiledRule> = HashSet()
    private val lookupCache: MutableMap<TagNode, List<CompiledRule>> = HashMap()

    fun registerCompiledRule(rule: CompiledRule) {
        rules.add(rule)
    }

    fun getStyle(node: TagNode, baseStyle: Style): Style {
        if (!lookupCache.containsKey(node)) {
            val matchingRules: MutableList<CompiledRule> = ArrayList()
            for (rule in rules) {
                if (rule.matches(node)) {
                    matchingRules.add(rule)
                }
            }
            lookupCache[node] = matchingRules
        }
        var result = baseStyle
        lookupCache[node]?.forEach {
            result = it.applyStyle(result)
        }
        return result
    }

    fun pushSpan(span: Any?, start: Int, end: Int) {
        if (end > start) {
            val callback = object : SpanCallback {
                override fun applySpan(spanner: HtmlSpanner, builder: SpannableStringBuilder) {
                    builder.setSpan(
                        span, start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
            spanItemStack.push(callback)
        }
    }

    fun pushSpan(callback: SpanCallback) {
        spanItemStack.push(callback)
    }

    fun applySpans(spanner: HtmlSpanner, builder: SpannableStringBuilder) {
        while (!spanItemStack.isEmpty()) {
            spanItemStack.pop().applySpan(spanner, builder)
        }
    }
}