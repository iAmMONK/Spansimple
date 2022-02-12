package com.iammonk.spansimple.css

internal enum class State {
    INSIDE_SELECTOR,
    INSIDE_COMMENT,
    INSIDE_PROPERTY_NAME,
    INSIDE_VALUE,
    INSIDE_VALUE_ROUND_BRACKET
}