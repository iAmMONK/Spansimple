/*
 * Copyright (C) 2011 Alex Kuiper <http://www.nightwhistler.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iammonk.spansimple.handlers

import android.text.SpannableStringBuilder
import com.iammonk.spansimple.SpanStack
import com.iammonk.spansimple.TagNodeHandler
import org.htmlcleaner.TagNode

class NewLineHandler(private val numberOfNewLines: Int, wrappedHandler: TagNodeHandler) : WrappingHandler(wrappedHandler) {
    override fun handleTagNode(
        node: TagNode, builder: SpannableStringBuilder,
        start: Int, end: Int, spanStack: SpanStack
    ) {
        super.handleTagNode(node, builder, start, end, spanStack)
        for (i in 0 until numberOfNewLines) {
            appendNewLine(builder)
        }
    }
}