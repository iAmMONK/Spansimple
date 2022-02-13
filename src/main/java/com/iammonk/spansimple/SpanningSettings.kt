package com.iammonk.spansimple

class SpanningSettings(
    /**
     * Indicates whether the text style may be updated.
     *
     * If this is set to false, all CSS is ignored
     * and the basic built-in style is used.
     *
     * Switch to specify is CSS style should be used.
     *
     * Switch to determine if CSS is used
     **/
    var isAllowStyling: Boolean = true,
    /**
     * Returns if whitespace is being stripped.
     *
     *
     * Switch to specify whether excess whitespace should be stripped from the
     * input.
     **/
    var isStripExtraWhiteSpace: Boolean = false,
    /**
     * Switch to specify if the colours from CSS
     * should override user-specified colours.
     *
     * If CSS colours are used
     **/
    var isUseColoursFromStyle: Boolean = true,
    val fontResolver: SystemFontResolver = SystemFontResolver()
)