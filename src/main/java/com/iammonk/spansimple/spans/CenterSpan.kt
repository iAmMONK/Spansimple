package com.iammonk.spansimple.spans

import android.text.Layout
import android.text.style.AlignmentSpan

class CenterSpan : AlignmentSpan {
    override fun getAlignment(): Layout.Alignment {
        return Layout.Alignment.ALIGN_CENTER
    }
}