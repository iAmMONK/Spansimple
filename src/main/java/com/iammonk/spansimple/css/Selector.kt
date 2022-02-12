package com.iammonk.spansimple.css

class Selector(private val name: String) {

    override fun toString(): String = name

    override fun equals(`object`: Any?): Boolean {
        if (`object` is Selector) {
            return `object`.name.equals(name, ignoreCase = true)
        }
        return false
    }

    override fun hashCode(): Int = toString().hashCode()
}