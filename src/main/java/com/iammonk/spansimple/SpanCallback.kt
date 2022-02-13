package com.iammonk.spansimple

import android.text.SpannableStringBuilder

interface SpanCallback {
    fun applySpan(settings: SpanningSettings, builder: SpannableStringBuilder)
}