package com.iammonk.spansimple.handlers

import com.iammonk.spansimple.style.Style

class MonoSpaceHandler : StyledTextHandler() {
    override val style: Style get() =
        Style().setFontFamily(settings.fontResolver.monoSpaceFont)
}