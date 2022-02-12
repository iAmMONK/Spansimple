package com.iammonk.spansimple.style

import kotlin.math.max

class StyleValue {
    enum class Unit { PX, EM, PERCENTAGE }

    var intValue: Int = 0
    var floatValue: Float = 0f
    var unit: Unit
        private set

    constructor(intValue: Int) {
        unit = Unit.PX
        this.intValue = intValue
    }

    constructor(floatValue: Float, unit: Unit) {
        this.floatValue = floatValue
        this.unit = unit
    }

    override fun toString(): String {
        return "" + max(intValue.toFloat(), floatValue) + unit
    }

    companion object {
        fun parse(value: String): StyleValue? {
            if (value == "0") {
                return StyleValue(0f, Unit.EM)
            }
            val substring = value.substring(0, value.length - 2)
            if (value.endsWith("px")) {
                return try {
                    val intValue = substring.toInt()
                    StyleValue(intValue)
                } catch (nfe: NumberFormatException) {
                    null
                }
            }
            if (value.endsWith("%")) {
                return try {
                    val percentage = value.substring(0, value.length - 1).toInt()
                    val floatValue = percentage / 100f
                    StyleValue(floatValue, Unit.PERCENTAGE)
                } catch (nfe: NumberFormatException) {
                    null
                }
            }
            return if (value.endsWith("em")) {
                try {
                    val number = substring.toFloat()
                    StyleValue(number, Unit.EM)
                } catch (nfe: NumberFormatException) {
                    null
                }
            } else null
        }
    }
}