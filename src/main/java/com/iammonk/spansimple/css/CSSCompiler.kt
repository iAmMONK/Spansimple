package com.iammonk.spansimple.css

import android.graphics.Color
import com.iammonk.spansimple.style.Style
import com.iammonk.spansimple.style.Style.*
import com.iammonk.spansimple.style.StyleValue
import org.htmlcleaner.TagNode

/**
 * Compiler for CSS Rules.
 *
 *
 * The compiler takes the raw parsed form (a Rule) of a CSS rule
 * and transforms it into an executable CompiledRule where all
 * the parsing of values has already been done.
 */
object CSSCompiler {

    @JvmStatic
    fun compile(rule: Rule): CompiledRule {
        val matchers: MutableList<List<TagNodeMatcher>> = ArrayList()
        val styleUpdaters: MutableList<StyleUpdater> = ArrayList()
        for (selector in rule.selectors) {
            val selMatchers = createMatchersFromSelector(selector)
            matchers.add(selMatchers)
        }
        var blank = Style()

        rule.propertyValues.forEach { (key, value) ->
            val updater = getStyleUpdater(key, value)

            if (updater != null) {
                styleUpdaters.add(updater)
                blank = updater(blank)
            }
        }

        val asText = rule.toString()
        return CompiledRule(matchers, styleUpdaters, asText)
    }

    private fun parseCSSColor(color: String): Int {

        var colorString = color

        //Check for CSS short-hand notation: #0fc -> #00ffcc
        if (colorString.length == 4 && colorString.startsWith("#")) {
            val builder = StringBuilder("#")
            colorString.toList()
                .drop(1)
                .forEach {
                    builder.append(it)
                    builder.append(it)
                }
            colorString = builder.toString()
        }
        return Color.parseColor(colorString)
    }

    private fun createMatchersFromSelector(selector: Selector): List<TagNodeMatcher> {
        val matchers: MutableList<TagNodeMatcher> = ArrayList()
        val selectorString = selector.toString()
        val parts = selectorString.split("\\s").toTypedArray()

        //Create a reversed matcher list
        for (i in parts.indices.reversed()) {
            matchers.add(createMatcherFromPart(parts[i]))
        }
        return matchers
    }

    private fun createMatcherFromPart(selectorPart: String): TagNodeMatcher {

        //Match by class
        if (selectorPart.contains('.')) {
            return ClassMatcher(selectorPart)
        }
        return if (selectorPart.startsWith("#")) {
            IdMatcher(selectorPart)
        } else TagNameMatcher(selectorPart)
    }

    @JvmStatic
    fun getStyleUpdater(key: String, value: String): StyleUpdater? {
        try {
            when (key) {
                "color" -> {
                    val color = parseCSSColor(value)
                    return { style: Style -> style.setColor(color) }
                }
                "background-color" -> {
                    val color = parseCSSColor(value)
                    return { style: Style -> style.setBackgroundColor(color) }
                }
                "align", "text-align" -> {
                    val alignment = TextAlignment.valueOf(value.uppercase())
                    return { style: Style -> style.setTextAlignment(alignment) }
                }
                "font-weight" -> {
                    val weight = FontWeight.valueOf(value.uppercase())
                    return { style: Style -> style.setFontWeight(weight) }
                }
                "font-style" -> {
                    val fontStyle = FontStyle.valueOf(value.uppercase())
                    return { style: Style -> style.setFontStyle(fontStyle) }
                }
                "margin-bottom" -> {
                    val styleValue = StyleValue.parse(value)
                    if (styleValue != null) {
                        return { style: Style -> style.setMarginBottom(styleValue) }
                    } else return null
                }
                "margin-top" -> {
                    val styleValue = StyleValue.parse(value)
                    if (styleValue != null) {
                        return { style: Style -> style.setMarginTop(styleValue) }
                    } else return null
                }
                "margin-left" -> {
                    val styleValue = StyleValue.parse(value)
                    if (styleValue != null) {
                        return { style: Style -> style.setMarginLeft(styleValue) }
                    } else return null
                }
                "margin-right" -> {
                    val styleValue = StyleValue.parse(value)
                    if (styleValue != null) {
                        return { style: Style -> style.setMarginRight(styleValue) }
                    } else return null
                }
                "margin" -> return parseMargin(value)
                "text-indent" -> {
                    val styleValue = StyleValue.parse(value)
                    if (styleValue != null) {
                        return { style: Style -> style.setTextIndent(styleValue) }
                    } else return null
                }
                "display" -> {
                    val displayStyle = DisplayStyle.valueOf(value.uppercase())
                    return { style: Style -> style.setDisplayStyle(displayStyle) }
                }
                "border-style" -> {
                    val borderStyle = BorderStyle.valueOf(value.uppercase())
                    return { style: Style -> style.setBorderStyle(borderStyle) }
                }
                "border-color" -> {
                    val borderColor = parseCSSColor(value)
                    return { style: Style -> style.setBorderColor(borderColor) }
                }
                "border-width" -> {
                    val borderWidth = StyleValue.parse(value)
                    return if (borderWidth != null) {
                        { style: Style -> style.setBorderWidth(borderWidth) }
                    } else null
                }
                "border" -> {
                    parseBorder(value)
                }
            }
        } catch (any: Exception) {
            return null
        }

        return null
    }

    /**
     * Parses a border definition.
     *
     *
     * Border definitions are a complete mess, since the order is not set.
     */
    private fun parseBorder(borderDefinition: String): StyleUpdater {
        val parts = borderDefinition.split("\\s").toTypedArray()
        var borderWidth: StyleValue? = null
        var borderColor: Int? = null
        var borderStyle: BorderStyle? = null
        for (part in parts) {
            if (borderWidth == null) {
                borderWidth = StyleValue.parse(part)
                if (borderWidth != null) {
                    continue
                }
            }
            if (borderColor == null) {
                try {
                    borderColor = parseCSSColor(part)
                    continue
                } catch (ia: IllegalArgumentException) {
                    //try next one
                }
            }
            if (borderStyle == null) {
                try {
                    borderStyle = BorderStyle.valueOf(part.uppercase())
                } catch (ia: IllegalArgumentException) {
                    //next loop iteration
                }
            }
        }
        val finalBorderWidth = borderWidth
        val finalBorderColor = borderColor
        val finalBorderStyle = borderStyle
        return { style: Style ->
            var newStyle = style

            if (finalBorderColor != null) {
                newStyle = newStyle.setBorderColor(finalBorderColor)
            }
            if (finalBorderWidth != null) {
                newStyle = newStyle.setBorderWidth(finalBorderWidth)
            }
            if (finalBorderStyle != null) {
                newStyle = newStyle.setBorderStyle(finalBorderStyle)
            }
            newStyle
        }
    }

    private fun parseMargin(marginValue: String): StyleUpdater {
        val parts = marginValue.split("\\s").toTypedArray()
        var bottomMarginString = ""
        var topMarginString = ""
        var leftMarginString = ""
        var rightMarginString = ""

        //See http://www.w3schools.com/css/css_margin.asp
        when (parts.size) {
            1 -> {
                bottomMarginString = parts[0]
                topMarginString = parts[0]
                leftMarginString = parts[0]
                rightMarginString = parts[0]
            }
            2 -> {
                topMarginString = parts[0]
                bottomMarginString = parts[0]
                leftMarginString = parts[1]
                rightMarginString = parts[1]
            }
            3 -> {
                topMarginString = parts[0]
                leftMarginString = parts[1]
                rightMarginString = parts[1]
                bottomMarginString = parts[2]
            }
            4 -> {
                topMarginString = parts[0]
                rightMarginString = parts[1]
                bottomMarginString = parts[2]
                leftMarginString = parts[3]
            }
        }
        val marginBottom = StyleValue.parse(bottomMarginString)
        val marginTop = StyleValue.parse(topMarginString)
        val marginLeft = StyleValue.parse(leftMarginString)
        val marginRight = StyleValue.parse(rightMarginString)
        return { style: Style ->
            var resultStyle = style
            if (marginBottom != null) {
                resultStyle = resultStyle.setMarginBottom(marginBottom)
            }
            if (marginTop != null) {
                resultStyle = resultStyle.setMarginTop(marginTop)
            }
            if (marginLeft != null) {
                resultStyle = resultStyle.setMarginLeft(marginLeft)
            }
            if (marginRight != null) {
                resultStyle = resultStyle.setMarginRight(marginRight)
            }
            resultStyle
        }
    }

    interface TagNodeMatcher {
        fun matches(tagNode: TagNode?): Boolean
    }

    private class ClassMatcher(selector: String) : TagNodeMatcher {
        private var tagName: String? = null
        private var className: String? = null

        override fun matches(tagNode: TagNode?): Boolean {
            if (tagNode == null) {
                return false
            }

            //If a tag name is given it should match
            if (tagName?.isNotEmpty() == true && tagName != tagNode.name) return false
            val classAttribute = tagNode.getAttributeByName("class")
            return classAttribute != null && classAttribute == className
        }

        init {
            val elements = selector.split("\\.").toTypedArray()
            if (elements.size == 2) {
                tagName = elements[0]
                className = elements[1]
            }
        }
    }

    private class TagNameMatcher(selector: String) : TagNodeMatcher {
        private val tagName: String = selector.trim { it <= ' ' }

        override fun matches(tagNode: TagNode?): Boolean {
            return tagNode != null && tagName.equals(tagNode.name, ignoreCase = true)
        }

    }

    private class IdMatcher(selector: String) : TagNodeMatcher {
        private val id: String = selector.substring(1)

        override fun matches(tagNode: TagNode?): Boolean {
            if (tagNode == null) {
                return false
            }
            val idAttribute = tagNode.getAttributeByName("id")
            return idAttribute != null && idAttribute == id
        }

    }
}