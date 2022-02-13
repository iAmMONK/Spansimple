package com.iammonk.spansimple

import android.text.Spannable
import android.text.SpannableStringBuilder
import com.iammonk.spansimple.TextUtil.replaceHtmlEntities
import com.iammonk.spansimple.css.CSSCompiler
import com.iammonk.spansimple.css.CSSParser
import com.iammonk.spansimple.handlers.*
import com.iammonk.spansimple.handlers.attributes.AlignmentAttributeHandler
import com.iammonk.spansimple.handlers.attributes.BorderAttributeHandler
import com.iammonk.spansimple.handlers.attributes.StyleAttributeHandler
import com.iammonk.spansimple.style.Style
import com.iammonk.spansimple.style.StyleValue
import org.htmlcleaner.ContentNode
import org.htmlcleaner.HtmlCleaner
import org.htmlcleaner.TagNode

class HtmlSpanner {
    private val handlers: MutableMap<String, TagNodeHandler> = mutableMapOf()
    private val settings: SpanningSettings = SpanningSettings()
    private val htmlCleaner: HtmlCleaner = createHtmlCleaner()

    /**
     * Registers a new custom TagNodeHandler.
     *
     * If a TagNodeHandler was already registered for the specified tagName it
     * will be overwritten.
     */
    private fun registerHandler(tagName: String, handler: TagNodeHandler) {
        handlers[tagName] = handler
        handler.settings = settings
    }

    /**
     * Removes the handler for the given tag.
     *
     * @param tagName the tag to remove handlers for.
     */
    fun unregisterHandler(tagName: String) {
        handlers.remove(tagName)
    }

    /**
     * Parses the text in the given String.
     * @return a Spanned version of the text.
     */
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
        stack.applySpans(settings, result)
        return result
    }

    /**
     * Gets the currently registered handler for this tag.
     *
     * Used so it can be wrapped.
     * @return the registed TagNodeHandler, or null if none is registered.
     */
    fun getHandlerFor(tagName: String): TagNodeHandler? {
        return handlers[tagName]
    }

    /**
     * Creates spanned text from a TagNode.
     */
    fun fromTagNode(node: TagNode): Spannable {
        val result = SpannableStringBuilder()
        val stack = SpanStack()
        applySpan(result, node, stack)
        stack.applySpans(settings, result)
        return result
    }

    private fun handleContent(
        builder: SpannableStringBuilder, node: Any
    ) {
        val contentNode = node as ContentNode
        var text = replaceHtmlEntities(
            contentNode.content, false
        )
        if (settings.isStripExtraWhiteSpace) {
            //Replace unicode non-breaking space with normal space.
            text = text.replace('\u00A0', ' ')
        }
        if (text.trim { it <= ' ' }.isNotEmpty()) {
            builder.append(text)
        }
    }

    private fun applySpan(
        builder: SpannableStringBuilder, node: TagNode, stack: SpanStack
    ) {
        var handler = handlers[node.name]
        if (handler == null) {
            handler = StyledTextHandler()
            handler.settings = settings
        }
        val lengthBefore = builder.length
        handler.beforeChildren(node, builder, stack)
        if (!handler.rendersContent) {
            for (childNode in node.allChildren) {
                if (childNode is ContentNode) {
                    handleContent(builder, childNode)
                } else if (childNode is TagNode) {
                    applySpan(builder, childNode, stack)
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
        val monSpaceHandler: TagNodeHandler = wrap(MonoSpaceHandler())
        val brHandler: TagNodeHandler = NewLineHandler(wrap(StyledTextHandler()))
        val paragraphStyle = Style()
            .setDisplayStyle(Style.DisplayStyle.BLOCK)
            .setMarginBottom(
                StyleValue(1.0f, StyleValue.Unit.EM)
            )
        val pHandler: TagNodeHandler =
            BorderAttributeHandler(wrap(StyledTextHandler(paragraphStyle)))
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
        val spanHandler: TagNodeHandler = BorderAttributeHandler(wrap(StyledTextHandler(spanStyle)))

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
        registerHandler("h1", wrap(HeaderHandler(1.5f, 0.5f)))
        registerHandler("h2", wrap(HeaderHandler(1.4f, 0.6f)))
        registerHandler("h3", wrap(HeaderHandler(1.3f, 0.7f)))
        registerHandler("h4", wrap(HeaderHandler(1.2f, 0.8f)))
        registerHandler("h5", wrap(HeaderHandler(1.1f, 0.9f)))
        registerHandler("h6", wrap(HeaderHandler(1f, 1f)))
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

    companion object {
        /**
         * Temporary constant for the width of 1 horizontal em
         * Used for calculating margins.
         */
        const val HORIZONTAL_EM_WIDTH = 10
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

        private fun wrap(handler: StyledTextHandler): StyledTextHandler {
            return StyleAttributeHandler(AlignmentAttributeHandler(handler))
        }
    }
    /**
     * Creates a new HtmlSpanner using the given HtmlCleaner instance.
     *
     * This allows for a custom-configured HtmlCleaner.
     *
     * Creates a new HtmlSpanner using a default HtmlCleaner instance.
     */
    init {
        registerBuiltInHandlers()
    }
}