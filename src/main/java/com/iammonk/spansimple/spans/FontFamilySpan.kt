package com.iammonk.spansimple.spans

import android.graphics.Paint
import android.text.TextPaint
import android.text.style.TypefaceSpan
import com.iammonk.spansimple.FontFamily

class FontFamilySpan(val fontFamily: FontFamily) : TypefaceSpan(fontFamily.name) {
    var isBold = false
    var isItalic = false
    override fun updateDrawState(ds: TextPaint) {
        applyCustomTypeFace(ds, fontFamily)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeFace(paint, fontFamily)
    }

    private fun applyCustomTypeFace(paint: Paint, tf: FontFamily) {
        paint.isAntiAlias = true
        paint.typeface = tf.defaultTypeface
        if (isBold) {
            if (tf.isFakeBold) {
                paint.isFakeBoldText = true
            } else {
                paint.typeface = tf.boldTypeface
            }
        }
        if (isItalic) {
            if (tf.isFakeItalic) {
                paint.textSkewX = -0.25f
            } else {
                paint.typeface = tf.italicTypeface
            }
        }
        if (isBold && isItalic && tf.boldItalicTypeface != null) {
            paint.typeface = tf.boldItalicTypeface
        }
    }

    override fun toString(): String {
        val builder = StringBuilder("{\n")
        builder.append(
            """  font-family: ${fontFamily.name}
"""
        )
        builder.append("  bold: $isBold\n")
        builder.append("  italic: $isItalic\n")
        builder.append("}")
        return builder.toString()
    }
}