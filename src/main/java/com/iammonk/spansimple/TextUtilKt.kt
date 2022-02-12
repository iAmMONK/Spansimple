package com.iammonk.spansimple

import java.util.regex.Matcher
import java.util.regex.Pattern

object TextUtil {
    private val SPECIAL_CHAR_WHITESPACE = Pattern
        .compile("(&[a-z]*;|&#x?([a-f]|[A-F]|[0-9])*;|[\\s\n]+)")
    private val SPECIAL_CHAR_NO_WHITESPACE = Pattern
        .compile("(&[a-z]*;|&#x?([a-f]|[A-F]|[0-9])*;)")
    private val REPLACEMENTS = mapOf(
        "&nbsp;" to "\u00A0",
        "&amp;" to "&",
        "&quot;" to "\"",
        "&cent;" to "¢",
        "&lt;" to "<",
        "&gt;" to ">",
        "&sect;" to "§",
        "&ldquo;" to "“",
        "&rdquo;" to "”",
        "&lsquo;" to "‘",
        "&rsquo;" to "’",
        "&ndash;" to "\u2013",
        "&mdash;" to "\u2014",
        "&horbar;" to "\u2015",
        "&apos;" to "'",
        "&iexcl;" to "¡",
        "&pound;" to "£",
        "&curren;" to "¤",
        "&yen;" to "¥",
        "&brvbar;" to "¦",
        "&uml;" to "¨",
        "&copy;" to "©",
        "&ordf;" to "ª",
        "&laquo;" to "«",
        "&not;" to "¬",
        "&reg;" to "®",
        "&macr;" to "¯",
        "&deg;" to "°",
        "&plusmn;" to "±",
        "&sup2;" to "²",
        "&sup3;" to "³",
        "&acute;" to "´",
        "&micro;" to "µ",
        "&para;" to "¶",
        "&middot;" to "·",
        "&cedil;" to "¸",
        "&sup1;" to "¹",
        "&ordm;" to "º",
        "&raquo;" to "»",
        "&frac14;" to "¼",
        "&frac12;" to "½",
        "&frac34;" to "¾",
        "&iquest;" to "¿",
        "&times;" to "×",
        "&divide;" to "÷",
        "&Agrave;" to "À",
        "&Aacute;" to "Á",
        "&Acirc;" to "Â",
        "&Atilde;" to "Ã",
        "&Auml;" to "Ä",
        "&Aring;" to "Å",
        "&AElig;" to "Æ",
        "&Ccedil;" to "Ç",
        "&Egrave;" to "È",
        "&Eacute;" to "É",
        "&Ecirc;" to "Ê",
        "&Euml;" to "Ë",
        "&Igrave;" to "Ì",
        "&Iacute;" to "Í",
        "&Icirc;" to "Î",
        "&Iuml;" to "Ï",
        "&ETH;" to "Ð",
        "&Ntilde;" to "Ñ",
        "&Ograve;" to "Ò",
        "&Oacute;" to "Ó",
        "&Ocirc;" to "Ô",
        "&Otilde;" to "Õ",
        "&Ouml;" to "Ö",
        "&Oslash;" to "Ø",
        "&Ugrave;" to "Ù",
        "&Uacute;" to "Ú",
        "&Ucirc;" to "Û",
        "&Uuml;" to "Ü",
        "&Yacute;" to "Ý",
        "&THORN;" to "Þ",
        "&szlig;" to "ß",
        "&agrave;" to "à",
        "&aacute;" to "á",
        "&acirc;" to "â",
        "&atilde;" to "ã",
        "&auml;" to "ä",
        "&aring;" to "å",
        "&aelig;" to "æ",
        "&ccedil;" to "ç",
        "&egrave;" to "è",
        "&eacute;" to "é",
        "&ecirc;" to "ê",
        "&euml;" to "ë",
        "&igrave;" to "ì",
        "&iacute;" to "í",
        "&icirc;" to "î",
        "&iuml;" to "ï",
        "&eth;" to "ð",
        "&ntilde;" to "ñ",
        "&ograve;" to "ò",
        "&oacute;" to "ó",
        "&ocirc;" to "ô",
        "&otilde;" to "õ",
        "&ouml;" to "ö",
        "&oslash;" to "ø",
        "&ugrave;" to "ù",
        "&uacute;" to "ú",
        "&ucirc;" to "û",
        "&uuml;" to "ü",
        "&yacute;" to "ý",
        "&thorn;" to "þ",
        "&yuml;" to "ÿ"
    )

    /**
     * Replaces all HTML entities ( &lt;, &amp; ), with their Unicode
     * characters.
     */
    @JvmStatic
    fun replaceHtmlEntities(text: String, preserveFormatting: Boolean): String {
        val result = StringBuffer()
        val replacements: MutableMap<String, String> = HashMap(REPLACEMENTS)
        val matcher: Matcher = if (preserveFormatting) {
            SPECIAL_CHAR_NO_WHITESPACE.matcher(text)
        } else {
            replacements[""] = " "
            replacements["\n"] = " "
            SPECIAL_CHAR_WHITESPACE.matcher(text)
        }

        while (matcher.find()) {
            try {
                matcher.appendReplacement(result, getReplacement(matcher, replacements))
            } catch (i: ArrayIndexOutOfBoundsException) {
                //Ignore, seems to be a matcher bug
            }
        }
        matcher.appendTail(result)
        return result.toString()
    }

    private fun getReplacement(
        matcher: Matcher,
        replacements: Map<String, String>
    ): String {
        val match = matcher.group(0).trim { it <= ' ' }

        return replacements[match] ?: if (match.startsWith("&#")) {
                try {
                    //Check if it's hex or normal
                    if (match.startsWith("&#x")) {
                        Integer.decode("0x" + match.substring(3, match.length - 1)).toString()
                    } else {
                        match.substring(2, match.length - 1)
                    }
                } catch (nfe: NumberFormatException) {
                    ""
                }
            } else ""
    }
}
