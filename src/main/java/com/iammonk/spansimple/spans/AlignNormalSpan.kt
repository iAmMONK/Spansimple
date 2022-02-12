package com.iammonk.spansimple.spans

import android.text.Layout
import android.text.style.AlignmentSpan

class AlignNormalSpan : AlignmentSpan {
    override fun getAlignment(): Layout.Alignment {
        return Layout.Alignment.ALIGN_NORMAL
    }
}