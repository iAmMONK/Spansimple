package com.iammonk.spansimple.spans

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.LineBackgroundSpan
import com.iammonk.spansimple.HtmlSpanner
import com.iammonk.spansimple.style.Style
import com.iammonk.spansimple.style.StyleValue

class BorderSpan(
    private val style: Style,
    private val start: Int,
    private val end: Int,
    private val usecolour: Boolean
) : LineBackgroundSpan {
    override fun drawBackground(
        c: Canvas, p: Paint,
        left: Int, right: Int,
        top: Int, baseline: Int, bottom: Int,
        text: CharSequence, start: Int, end: Int,
        lnum: Int
    ) {
        var mLeft = left
        var mRight = right
        var baseMargin = 0
        if (style.marginLeft != null) {
            val styleValue = style.marginLeft
            if (styleValue.unit === StyleValue.Unit.PX) {
                if (styleValue.intValue > 0) {
                    baseMargin = styleValue.intValue
                }
            } else if (styleValue.floatValue > 0f) {
                baseMargin = (styleValue.floatValue * HtmlSpanner.HORIZONTAL_EM_WIDTH).toInt()
            }

            //Leave a little bit of room
            baseMargin--
        }
        if (baseMargin > 0) {
            mLeft += baseMargin
        }
        val originalColor = p.color
        val originalStrokeWidth = p.strokeWidth
        if (usecolour && style.backgroundColor != null) {
            p.color = style.backgroundColor
            p.style = Paint.Style.FILL
            c.drawRect(mLeft.toFloat(), top.toFloat(), mRight.toFloat(), bottom.toFloat(), p)
        }
        if (usecolour && style.borderColor != null) {
            p.color = style.borderColor
        }
        val strokeWidth: Int = if (style.borderWidth?.unit == StyleValue.Unit.PX) {
            style.borderWidth.intValue
        } else {
            1
        }
        p.strokeWidth = strokeWidth.toFloat()
        mRight -= strokeWidth
        p.style = Paint.Style.STROKE
        if (start <= this.start) {
            c.drawLine(mLeft.toFloat(), top.toFloat(), mRight.toFloat(), top.toFloat(), p)
        }
        if (end >= this.end) {
            c.drawLine(mLeft.toFloat(), bottom.toFloat(), mRight.toFloat(), bottom.toFloat(), p)
        }
        c.drawLine(mLeft.toFloat(), top.toFloat(), mLeft.toFloat(), bottom.toFloat(), p)
        c.drawLine(mRight.toFloat(), top.toFloat(), mRight.toFloat(), bottom.toFloat(), p)
        p.color = originalColor
        p.strokeWidth = originalStrokeWidth
    }
}