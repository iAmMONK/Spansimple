/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 */
package com.iammonk.spansimple.css

import kotlin.jvm.JvmOverloads
import java.util.ArrayList
import java.lang.StringBuilder

class Rule @JvmOverloads constructor(private val _selectors: MutableList<Selector> = ArrayList()) {
    private val _propertyValues: MutableMap<String, String> = mutableMapOf()

    val selectors: List<Selector> get() = _selectors
    val propertyValues: Map<String, String> get() = _propertyValues

    fun addPropertyValue(propertyValue: Pair<String, String>) {
        _propertyValues[propertyValue.first] = propertyValue.second
    }

    fun addSelectors(selectors: List<Selector>) {
        _selectors.addAll(selectors)
    }

    fun removePropertyValue(propertyValue: Pair<String, String>) {
        _propertyValues.remove(propertyValue.first)
    }

    fun addSelector(selector: Selector) {
        _selectors.add(selector)
    }

    fun removeSelector(selector: Selector) {
        _selectors.remove(selector)
    }

    override fun toString(): String {
        val out = StringBuilder()
        out.append(
            """${selectors.joinToString { it.toString() }} {
"""
        )
        _propertyValues.forEach { (key, value) ->
            out.append("\t$key;$value;\n")
        }

        out.append("}\n")
        return out.toString()
    }
}