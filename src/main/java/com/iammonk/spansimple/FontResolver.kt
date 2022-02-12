package com.iammonk.spansimple

interface FontResolver {
    val defaultFont: FontFamily?
    val sansSerifFont: FontFamily?
    val serifFont: FontFamily?
    val monoSpaceFont: FontFamily?
    fun getFont(name: String?): FontFamily?
}