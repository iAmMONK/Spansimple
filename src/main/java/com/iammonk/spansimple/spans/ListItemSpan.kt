package com.iammonk.spansimple.spans

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.Spanned
import android.text.style.LeadingMarginSpan
import com.iammonk.spansimple.HtmlSpanner

class ListItemSpan : LeadingMarginSpan {
    private val mNumber: Int

    constructor() {
        mNumber = -1
    }

    constructor(number: Int) {
        mNumber = number
    }

    override fun getLeadingMargin(first: Boolean): Int {
        return if (mNumber != -1) {
            2 * NUMBER_RADIUS + STANDARD_GAP_WIDTH
        } else {
            2 * BULLET_RADIUS + STANDARD_GAP_WIDTH
        }
    }

    override fun drawLeadingMargin(
        c: Canvas, p: Paint, x: Int, dir: Int, top: Int,
        baseline: Int, bottom: Int, text: CharSequence, start: Int, end: Int,
        first: Boolean, l: Layout
    ) {
        if ((text as Spanned).getSpanStart(this) == start) {
            val style = p.style
            p.style = Paint.Style.FILL
            if (mNumber != -1) {
                c.drawText("$mNumber.", (x + dir).toFloat(), baseline.toFloat(), p)
            } else {
                c.drawText("\u2022", (x + dir).toFloat(), baseline.toFloat(), p)
            }
            p.style = style
        }
    }

    companion object {
        private const val BULLET_RADIUS = 3
        private const val NUMBER_RADIUS = 5

        //Gap should be about 1em
        const val STANDARD_GAP_WIDTH = HtmlSpanner.HORIZONTAL_EM_WIDTH
    }
}