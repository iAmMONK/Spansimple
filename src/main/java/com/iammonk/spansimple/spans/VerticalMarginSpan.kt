package com.iammonk.spansimple.spans

import android.graphics.Paint.FontMetricsInt
import android.text.style.LineHeightSpan
import kotlin.math.abs

class VerticalMarginSpan @JvmOverloads constructor(
    private val margin: Float? = null,
    private val absolute: Int? = null
) : LineHeightSpan {

    override fun chooseHeight(
        text: CharSequence, start: Int, end: Int, spanstartv: Int, v: Int,
        fm: FontMetricsInt
    ) {
        var height = abs(fm.descent - fm.ascent)
        if (margin != null) {
            height = (height * margin).toInt()
        } else if (absolute != null) {
            height = absolute
        }
        fm.descent = fm.ascent + height
    }
}