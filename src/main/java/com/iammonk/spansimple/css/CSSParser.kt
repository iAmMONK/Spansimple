package com.iammonk.spansimple.css

class CSSParser private constructor() {
    private val selectorNames: MutableList<String> = mutableListOf()
    private val values: MutableMap<String, String> = mutableMapOf()
    private var selectorName = ""
    private var propertyName = ""
    private var valueName = ""
    private var state: State = State.INSIDE_SELECTOR
    private var previousChar: Char? = null
    private var beforeCommentMode: State = State.INSIDE_SELECTOR

    /**
     * Main parse logic.
     *
     * @param rules The list of rules.
     * @param c     The current currency.
     * @param nextC The next currency (or null).
     * @throws Exception If any errors occurs.
     */
    @Throws(Exception::class)
    private fun parse(rules: MutableList<Rule>, c: Char, nextC: Char?) {

        // Special case if we find a comment
        if (Chars.SLASH == c && Chars.STAR == nextC) {

            // It's possible to find a comment in a comment
            if (state != State.INSIDE_COMMENT) {
                beforeCommentMode = state
            }
            state = State.INSIDE_COMMENT
        }
        when (state) {
            State.INSIDE_SELECTOR -> {
                parseSelector(c)
            }
            State.INSIDE_COMMENT -> {
                parseComment(c)
            }
            State.INSIDE_PROPERTY_NAME -> {
                parsePropertyName(rules, c)
            }
            State.INSIDE_VALUE -> {
                parseValue(c)
            }
            State.INSIDE_VALUE_ROUND_BRACKET -> {
                parseValueInsideRoundBrackets(c)
            }
        }

        // Save the previous character
        previousChar = c
    }

    /**
     * Parse a value.
     *
     * @param char The current character.
     * @throws IncorrectFormatException If any errors occur.
     */
    @Throws(IncorrectFormatException::class)
    private fun parseValue(char: Char) {

        // Special case if the value is a data uri, the value can contain a ;
        //		boolean valueHasDataURI = valueName.toLowerCase().indexOf("data:") != -1;
        when (char) {
            Chars.SEMI_COLON -> {
                values[propertyName.trim { it <= ' ' }] = valueName.trim { it <= ' ' }

                propertyName = ""
                valueName = ""
                state = State.INSIDE_PROPERTY_NAME
            }
            Chars.ROUND_BRACKET_BEG -> {
                valueName += Chars.ROUND_BRACKET_BEG
                state = State.INSIDE_VALUE_ROUND_BRACKET
            }
            Chars.COLON -> {
                throw IncorrectFormatException(
                    IncorrectFormatException.ErrorCode.FOUND_COLON_WHILE_READING_VALUE,
                    "The value '" + valueName.trim { it <= ' ' } + "' for property '" + propertyName.trim { it <= ' ' } + "' in the selector '" + selectorName.trim { it <= ' ' } + "' had a ':' character.")
            }
            Chars.BRACKET_END -> {
                throw IncorrectFormatException(
                    IncorrectFormatException.ErrorCode.FOUND_END_BRACKET_BEFORE_SEMICOLON,
                    "The value '" + valueName.trim { it <= ' ' } + "' for property '" + propertyName.trim { it <= ' ' } + "' in the selector '" + selectorName.trim { it <= ' ' } + "' should end with an ';', not with '}'.")
            }
            else -> {
                valueName += char
            }
        }
    }

    /**
     * Parse value inside a round bracket (
     *
     * @param char The current character.
     * @throws IncorrectFormatException If any error occurs.
     */
    @Throws(IncorrectFormatException::class)
    private fun parseValueInsideRoundBrackets(char: Char) {
        if (Chars.ROUND_BRACKET_END == char) {
            valueName += Chars.ROUND_BRACKET_END
            state = State.INSIDE_VALUE
        } else {
            valueName += char
        }
    }

    /**
     * Parse property name.
     *
     * @param rules The list of rules.
     * @param char     The current character.
     * @throws IncorrectFormatException If any error occurs
     */
    @Throws(IncorrectFormatException::class)
    private fun parsePropertyName(rules: MutableList<Rule>, char: Char) {
        when (char) {
            Chars.COLON -> {
                state = State.INSIDE_VALUE
            }
            Chars.SEMI_COLON -> {
                throw IncorrectFormatException(
                    IncorrectFormatException.ErrorCode.FOUND_SEMICOLON_WHEN_READING_PROPERTY_NAME,
                    "Unexpected character '" + char + "' for property '" + propertyName.trim { it <= ' ' } + "' in the selector '" + selectorName.trim { it <= ' ' } + "' should end with an ';', not with '}'.")
            }
            Chars.BRACKET_END -> {
                val rule = Rule()

                /*
                     * Huge logic to create a new rule
                     */
                rule.addSelectors(selectorNames.map { selector -> Selector(selector.trim { it <= ' ' }) })

                selectorNames.clear()
                val selector = Selector(selectorName.trim { it <= ' ' })
                selectorName = ""
                rule.addSelector(selector)

                // Add the property values
                values.forEach { (key, value) ->
                    rule.addPropertyValue(key to value)
                }
                values.clear()

                if (rule.propertyValues.isNotEmpty()) {
                    rules.add(rule)
                }
                state = State.INSIDE_SELECTOR
            }
            else -> {
                propertyName += char
            }
        }
    }

    /**
     * Parse a selector.
     *
     * @param char The current character.
     */
    private fun parseComment(char: Char) {
        if (Chars.STAR == previousChar && Chars.SLASH == char) {
            state = beforeCommentMode
        }
    }

    /**
     * Parse a selector.
     *
     * @param char The current character.
     * @throws IncorrectFormatException If an error occurs.
     */
    @Throws(IncorrectFormatException::class)
    private fun parseSelector(char: Char) {
        when(char) {
            Chars.BRACKET_BEG -> {
                state = State.INSIDE_PROPERTY_NAME
            }
            Chars.COMMA -> {
                if (selectorName.trim { it <= ' ' }.isEmpty()) {
                    throw IncorrectFormatException(
                        IncorrectFormatException.ErrorCode.FOUND_COLON_WHEN_READING_SELECTOR_NAME,
                        "Found an ',' in a selector name without any actual name before it."
                    )
                }
                selectorNames.add(selectorName.trim { it <= ' ' })
                selectorName = ""
            }
            else -> {
                selectorName += char
            }
        }
    }

    companion object {
        /**
         * Reads CSS as a String and returns back a list of Rules.
         *
         * @param css A String representation of CSS.
         * @return A list of Rules
         * @throws Exception If any errors occur.
         */
        @Throws(Exception::class)
        fun parse(css: String): List<Rule> {
            val parser = CSSParser()
            val rules: MutableList<Rule> = mutableListOf()

            if (css.trim { it <= ' ' }.isEmpty()) return rules

            for (i in css.indices) {
                val c = css[i]
                if (i < css.length - 1) {
                    val nextC = css[i + 1]
                    parser.parse(rules, c, nextC)
                } else {
                    parser.parse(rules, c, null)
                }
            }
            return rules
        }
    }
}