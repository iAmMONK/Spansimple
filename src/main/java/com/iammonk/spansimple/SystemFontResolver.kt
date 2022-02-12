package com.iammonk.spansimple

import android.graphics.Typeface

class SystemFontResolver : FontResolver {


    override var defaultFont: FontFamily = FontFamily("default", Typeface.DEFAULT)
    override var serifFont: FontFamily = FontFamily("serif", Typeface.SERIF)
    override var sansSerifFont: FontFamily = FontFamily("sans-serif", Typeface.SANS_SERIF)
    override val monoSpaceFont: FontFamily = FontFamily("monospace", Typeface.MONOSPACE)

    override fun getFont(name: String?): FontFamily {
        if (name != null && name.isNotEmpty()) {
            val parts = name.split(",(\\s)*").toTypedArray()
            parts.forEach { part ->
                var fontName = part
                if (fontName.startsWith("\"") && fontName.endsWith("\"")) {
                    fontName = fontName.substring(1, fontName.length - 1)
                }
                if (fontName.startsWith("\'") && fontName.endsWith("\'")) {
                    fontName = fontName.substring(1, fontName.length - 1)
                }

                resolveFont(fontName)?.let { return it }
            }
        }
        return defaultFont
    }

    private fun resolveFont(name: String): FontFamily? {
        return when {
            name.equals("serif", ignoreCase = true) -> serifFont
            name.equals("sans-serif", ignoreCase = true) -> sansSerifFont
            name.equals("monospace", ignoreCase = true) -> monoSpaceFont
            else -> null
        }
    }
}