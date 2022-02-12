package com.iammonk.spansimple

import android.graphics.Typeface

class FontFamily(val name: String, var defaultTypeface: Typeface) {
    var boldTypeface: Typeface? = null
    var italicTypeface: Typeface? = null
    var boldItalicTypeface: Typeface? = null
    val isFakeBold: Boolean
        get() = boldTypeface == null
    val isFakeItalic: Boolean
        get() = italicTypeface == null

    override fun toString() = name
}