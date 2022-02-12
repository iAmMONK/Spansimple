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

/**
 * Represents a property and its value of a CSS rule.
 *
 * @author [Christoffer Pettersson](mailto:christoffer@christoffer.me)
 */
class PropertyValue
/**
 * Creates a new PropertyValue based on a property and its value.
 *
 * @param property The CSS property (such as 'width' or 'color').
 * @param value    The value of the property (such as '100px' or 'red').
 */(
    /**
     * Returns the property.
     *
     * @return The property.
     */
    val property: String,
    /**
     * Returns the value.
     *
     * @return The value.
     */
    val value: String
) {
    override fun toString(): String {
        return "$property: $value"
    }

    override fun equals(`object`: Any?): Boolean {
        if (`object` is PropertyValue) {
            val target = `object`
            return target.property.equals(property, ignoreCase = true) && target.value.equals(
                value,
                ignoreCase = true
            )
        }
        return false
    }

    override fun hashCode(): Int {
        return toString().hashCode()
    }
}