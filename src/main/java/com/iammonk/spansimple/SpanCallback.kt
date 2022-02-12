package com.iammonk.spansimple

import android.text.SpannableStringBuilder

interface SpanCallback {
    fun applySpan(spanner: HtmlSpanner, builder: SpannableStringBuilder)
}