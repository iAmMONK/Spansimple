package com.iammonk.spansimple

import android.text.Spannable
import android.text.SpannableStringBuilder
import com.iammonk.spansimple.TextUtil.replaceHtmlEntities
import com.iammonk.spansimple.css.CSSCompiler
import com.iammonk.spansimple.css.CSSParser
import com.iammonk.spansimple.handlers.*
import com.iammonk.spansimple.handlers.attributes.BorderAttributeHandler
import com.iammonk.spansimple.style.Style
import com.iammonk.spansimple.style.StyleValue
import org.htmlcleaner.ContentNode
import org.htmlcleaner.HtmlCleaner
import org.htmlcleaner.TagNode

class HtmlSpanner(
    settings: SpanningSettings.() -> Unit
) {
    private val _settings: SpanningSettings = SpanningSettings().apply(settings)
    private val handlers: MutableMap<String, TagNodeHandler> = mutableMapOf()
    private val htmlCleaner: HtmlCleaner = createHtmlCleaner()

    init {
        registerBuiltInHandlers()
    }

    private fun registerHandler(tagName: String, handler: TagNodeHandler) {
        handlers[tagName] = handler
        handler.settings = _settings
    }

    fun unregisterHandler(tagName: String) {
        handlers.remove(tagName)
    }

    fun fromHtml(html: String): Spannable {
        return fromTagNode(htmlCleaner.clean(html), null)
    }

    fun fromHtml(html: String, css: String): Spannable {
        return fromTagNode(htmlCleaner.clean(html), css)
    }

    private fun fromTagNode(
        node: TagNode,
        css: String?
    ): Spannable {
        val result = SpannableStringBuilder()
        val stack = SpanStack()

        css?.run {
            CSSParser.parse(css).forEach { rule ->
                stack.registerCompiledRule(CSSCompiler.compile(rule))
            }
        }

        applySpan(result, node, stack)
        stack.applySpans(_settings, result)
        return result
    }

    private fun handleContent(
        builder: SpannableStringBuilder,
        node: ContentNode
    ) {
        var text = replaceHtmlEntities(node.content, false)
        if (_settings.isStripExtraWhiteSpace) {
            //Replace unicode non-breaking space with normal space.
            text = text.replace('\u00A0', ' ')
        }
        if (text.trim { it <= ' ' }.isNotEmpty()) {
            builder.append(text)
        }
    }

    private fun applySpan(
        builder: SpannableStringBuilder,
        node: TagNode,
        stack: SpanStack
    ) {
        var handler = handlers[node.name]
        if (handler == null) {
            handler = StyledTextHandler()
            handler.settings = _settings
        }
        val lengthBefore = builder.length
        handler.beforeChildren(node, builder, stack)
        if (!handler.rendersContent) {
            node.allChildren.forEach { child ->
                when (child) {
                    is ContentNode -> handleContent(builder, child)
                    is TagNode -> applySpan(builder, child, stack)
                }
            }
        }
        val lengthAfter = builder.length
        handler.handleTagNode(node, builder, lengthBefore, lengthAfter, stack)
    }

    private fun registerBuiltInHandlers() {
        val italicHandler: TagNodeHandler =
            StyledTextHandler(Style().setFontStyle(Style.FontStyle.ITALIC))
        val boldHandler: TagNodeHandler =
            StyledTextHandler(Style().setFontWeight(Style.FontWeight.BOLD))
        val marginHandler: TagNodeHandler =
            StyledTextHandler(Style().setMarginLeft(StyleValue(2.0f, StyleValue.Unit.EM)))
        val monSpaceHandler: TagNodeHandler = MonoSpaceHandler().wrapped
        val brHandler: TagNodeHandler = NewLineHandler(StyledTextHandler().wrapped)
        val paragraphStyle = Style()
            .setDisplayStyle(Style.DisplayStyle.BLOCK)
            .setMarginBottom(
                StyleValue(1.0f, StyleValue.Unit.EM)
            )
        val pHandler: TagNodeHandler =
            BorderAttributeHandler(StyledTextHandler(paragraphStyle).wrapped)
        val preHandler: TagNodeHandler = PreHandler()
        val bigHandler: TagNodeHandler =
            StyledTextHandler(Style().setFontSize(StyleValue(1.25f, StyleValue.Unit.EM)))
        val smallHandler: TagNodeHandler =
            StyledTextHandler(Style().setFontSize(StyleValue(0.8f, StyleValue.Unit.EM)))
        val subHandler: TagNodeHandler = SubScriptHandler()
        val superHandler: TagNodeHandler = SuperScriptHandler()
        val centerHandler: TagNodeHandler =
            StyledTextHandler(Style().setTextAlignment(Style.TextAlignment.CENTER))
        val spanStyle = Style().setDisplayStyle(Style.DisplayStyle.INLINE)
        val spanHandler: TagNodeHandler =
            BorderAttributeHandler(StyledTextHandler(spanStyle).wrapped)

        registerHandler("i", italicHandler)
        registerHandler("em", italicHandler)
        registerHandler("cite", italicHandler)
        registerHandler("dfn", italicHandler)
        registerHandler("b", boldHandler)
        registerHandler("strong", boldHandler)
        registerHandler("blockquote", marginHandler)
        registerHandler("ul", marginHandler)
        registerHandler("ol", marginHandler)
        registerHandler("tt", monSpaceHandler)
        registerHandler("code", monSpaceHandler)
        registerHandler("style", StyleNodeHandler())
        registerHandler("br", brHandler)
        registerHandler("p", pHandler)
        registerHandler("div", pHandler)
        registerHandler("h1", HeaderHandler.H1.wrapped)
        registerHandler("h2", HeaderHandler.H2.wrapped)
        registerHandler("h3", HeaderHandler.H3.wrapped)
        registerHandler("h4", HeaderHandler.H4.wrapped)
        registerHandler("h5", HeaderHandler.H5.wrapped)
        registerHandler("h6", HeaderHandler.H6.wrapped)
        registerHandler("pre", preHandler)
        registerHandler("big", bigHandler)
        registerHandler("small", smallHandler)
        registerHandler("sub", subHandler)
        registerHandler("sup", superHandler)
        registerHandler("center", centerHandler)
        registerHandler("li", ListItemHandler())
        registerHandler("a", LinkHandler())
        registerHandler("img", ImageHandler())
        registerHandler("font", FontHandler())
        registerHandler("span", spanHandler)
    }

    private fun createHtmlCleaner(): HtmlCleaner {
        val result = HtmlCleaner()
        val cleanerProperties = result.properties
        cleanerProperties.isAdvancedXmlEscape = true
        cleanerProperties.isOmitXmlDeclaration = true
        cleanerProperties.isOmitDoctypeDeclaration = false
        cleanerProperties.isTranslateSpecialEntities = true
        cleanerProperties.isTransResCharsToNCR = true
        cleanerProperties.isRecognizeUnicodeChars = true
        cleanerProperties.isIgnoreQuestAndExclam = true
        cleanerProperties.isUseEmptyElementTags = false
        cleanerProperties.pruneTags = "script,title"
        return result
    }
}
