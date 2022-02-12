package com.iammonk.spansimple.spans

import android.text.style.AlignmentSpan
import android.text.Layout

class AlignOppositeSpan : AlignmentSpan {
    override fun getAlignment(): Layout.Alignment {
        return Layout.Alignment.ALIGN_OPPOSITE
    }
}