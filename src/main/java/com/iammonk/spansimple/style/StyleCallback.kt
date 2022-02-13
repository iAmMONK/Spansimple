package com.iammonk.spansimple.style

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.*
import com.iammonk.spansimple.FontFamily
import com.iammonk.spansimple.HtmlSpanner
import com.iammonk.spansimple.SpanCallback
import com.iammonk.spansimple.SpanningSettings
import com.iammonk.spansimple.spans.*
import com.iammonk.spansimple.style.Style.TextAlignment
import kotlin.math.min

class StyleCallback(
    private val defaultFont: FontFamily,
    private val useStyle: Style,
    private val start: Int,
    private val end: Int
) : SpanCallback {
    override fun applySpan(settings: SpanningSettings, builder: SpannableStringBuilder) {
        if (useStyle.fontFamily != null || useStyle.fontStyle != null || useStyle.fontWeight != null) {
            val originalSpan = getFontFamilySpan(builder, start, end)
            val newSpan: FontFamilySpan = when {
                useStyle.fontFamily == null && originalSpan == null ->
                    FontFamilySpan(defaultFont)
                useStyle.fontFamily != null ->
                    FontFamilySpan(useStyle.fontFamily)
                else ->
                    FontFamilySpan(originalSpan!!.fontFamily)
            }

            if (useStyle.fontWeight != null) {
                newSpan.isBold = useStyle.fontWeight === Style.FontWeight.BOLD
            } else if (originalSpan != null) {
                newSpan.isBold = originalSpan.isBold
            }
            if (useStyle.fontStyle != null) {
                newSpan.isItalic = useStyle.fontStyle === Style.FontStyle.ITALIC
            } else if (originalSpan != null) {
                newSpan.isItalic = originalSpan.isItalic
            }
            builder.setSpan(newSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        //If there's no border, we use a BackgroundColorSpan to draw colour behind the text
        if (settings.isUseColoursFromStyle && useStyle.backgroundColor != null && useStyle.borderStyle == null) {
            builder.setSpan(
                BackgroundColorSpan(useStyle.backgroundColor),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        //If there is a border, the BorderSpan will also draw the background colour if needed.
        if (useStyle.borderStyle != null) {
            builder.setSpan(
                BorderSpan(useStyle, start, end, settings.isUseColoursFromStyle), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        if (useStyle.fontSize != null) {
            val styleValue = useStyle.fontSize
            if (styleValue.unit == StyleValue.Unit.PX) {
                if (styleValue.intValue > 0) {
                    builder.setSpan(
                        AbsoluteSizeSpan(styleValue.intValue), start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            } else {
                if (styleValue.floatValue > 0f) {
                    builder.setSpan(
                        RelativeSizeSpan(styleValue.floatValue), start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
        }
        if (settings.isUseColoursFromStyle && useStyle.color != null) {
            builder.setSpan(
                ForegroundColorSpan(useStyle.color), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        if (useStyle.textAlignment != null) {
            val alignSpan: AlignmentSpan = when (useStyle.textAlignment) {
                TextAlignment.LEFT -> AlignNormalSpan()
                TextAlignment.CENTER -> CenterSpan()
                TextAlignment.RIGHT -> AlignOppositeSpan()
            }
            builder.setSpan(
                alignSpan, start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        if (useStyle.textIndent != null) {
            val styleValue = useStyle.textIndent
            var marginStart = start
            while (marginStart < end && builder[marginStart] == '\n') {
                marginStart++
            }
            val marginEnd = min(end, marginStart + 1)
            if (styleValue.unit == StyleValue.Unit.PX) {
                if (styleValue.intValue > 0) {
                    builder.setSpan(
                        LeadingMarginSpan.Standard(styleValue.intValue, 0), marginStart, marginEnd,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            } else {
                if (styleValue.floatValue > 0f) {
                    builder.setSpan(
                        LeadingMarginSpan.Standard(
                            (HtmlSpanner.HORIZONTAL_EM_WIDTH * styleValue.floatValue).toInt(),
                            0
                        ), marginStart, marginEnd,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
        }

        /* We ignore negative horizontal margins, since that would cause the text to be rendered off-screen */if (useStyle.marginLeft != null) {
            val styleValue = useStyle.marginLeft
            if (styleValue.unit == StyleValue.Unit.PX) {
                if (styleValue.intValue > 0) {
                    builder.setSpan(
                        LeadingMarginSpan.Standard(styleValue.intValue), start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            } else if (styleValue.floatValue > 0f) {
                builder.setSpan(
                    LeadingMarginSpan.Standard(
                        (HtmlSpanner.HORIZONTAL_EM_WIDTH * styleValue.floatValue).toInt()
                    ), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }

    /**
     * Returns the current FontFamilySpan in use on the given subsection of the builder.
     *
     * If no FontFamily has been set yet, spanner.getDefaultFont() is returned.
     *
     * @param builder the text to check
     * @param start start of the section
     * @param end end of the section
     * @return a FontFamily object
     */
    private fun getFontFamilySpan(
        builder: SpannableStringBuilder,
        start: Int,
        end: Int
    ): FontFamilySpan? {
        val spans = builder.getSpans(start, end, FontFamilySpan::class.java)
        return if (spans?.isNotEmpty() == true) {
            spans[spans.size - 1]
        } else null
    }
}