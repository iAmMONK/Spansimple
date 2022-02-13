package com.iammonk.spansimple.handlers

import android.text.SpannableStringBuilder
import com.iammonk.spansimple.SpanStack
import com.iammonk.spansimple.TagNodeHandler
import com.iammonk.spansimple.spans.VerticalMarginSpan
import com.iammonk.spansimple.style.Style
import com.iammonk.spansimple.style.StyleCallback
import com.iammonk.spansimple.style.StyleValue
import org.htmlcleaner.TagNode

open class StyledTextHandler @JvmOverloads constructor(
    private val _style: Style = Style()
) : TagNodeHandler() {

    open val style get() = _style

    override fun beforeChildren(
        node: TagNode,
        builder: SpannableStringBuilder,
        spanStack: SpanStack
    ) {
        val useStyle = spanStack.getStyle(node, _style)
        if (builder.isNotEmpty() && useStyle.displayStyle === Style.DisplayStyle.BLOCK) {
            if (builder[builder.length - 1] != '\n') {
                builder.append('\n')
            }
        }

        //If we have a top margin, we insert an extra newline. We'll manipulate the line height
        //of this newline to create the margin.
        if (useStyle.marginTop != null) {
            val styleValue = useStyle.marginTop
            if (styleValue.unit === StyleValue.Unit.PX) {
                if (styleValue.intValue > 0) {
                    if (appendNewLine(builder)) {
                        spanStack.pushSpan(
                            VerticalMarginSpan(styleValue.intValue.toFloat()),
                            builder.length - 1, builder.length
                        )
                    }
                }
            } else {
                if (styleValue.floatValue > 0f) {
                    if (appendNewLine(builder)) {
                        spanStack.pushSpan(
                            VerticalMarginSpan(styleValue.floatValue),
                            builder.length - 1, builder.length
                        )
                    }
                }
            }
        }
    }

    override fun handleTagNode(
        node: TagNode, builder: SpannableStringBuilder,
        start: Int, end: Int, spanStack: SpanStack
    ) {
        val styleFromCSS = spanStack.getStyle(node, _style)
        handleTagNode(node, builder, start, end, styleFromCSS, spanStack)
    }

    open fun handleTagNode(
        node: TagNode,
        builder: SpannableStringBuilder,
        start: Int,
        end: Int,
        useStyle: Style,
        stack: SpanStack
    ) {
        if (useStyle.displayStyle === Style.DisplayStyle.BLOCK) {
            appendNewLine(builder)

            //If we have a bottom margin, we insert an extra newline. We'll manipulate the line height
            //of this newline to create the margin.
            if (useStyle.marginBottom != null) {
                val styleValue = useStyle.marginBottom
                if (styleValue.unit === StyleValue.Unit.PX) {
                    if (styleValue.intValue > 0) {
                        appendNewLine(builder)
                        stack.pushSpan(
                            VerticalMarginSpan(styleValue.intValue.toFloat()),
                            builder.length - 1, builder.length
                        )
                    }
                } else {
                    if (styleValue.floatValue > 0f) {
                        appendNewLine(builder)
                        stack.pushSpan(
                            VerticalMarginSpan(styleValue.floatValue),
                            builder.length - 1, builder.length
                        )
                    }
                }
            }
        }
        if (builder.length > start) {
            stack.pushSpan(
                StyleCallback(
                    defaultFont = settings.fontResolver.defaultFont,
                    useStyle = useStyle,
                    start = start,
                    end = builder.length
                )
            )
        }
    }
}