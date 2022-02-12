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
package com.iammonk.spansimple

import android.text.SpannableStringBuilder
import org.htmlcleaner.TagNode


abstract class TagNodeHandler {
    /**
     * Returns a reference to the HtmlSpanner.
     *
     * @return the HtmlSpanner;
     */
    /**
     * Called by HtmlSpanner when this TagNodeHandler is registered.
     */
    open var spanner: HtmlSpanner? = null

    /**
     * Called before the children of this node are handled, allowing for text to
     * be inserted before the childrens' text.
     *
     * Default implementation is a no-op.
     */
    open fun beforeChildren(
        node: TagNode,
        builder: SpannableStringBuilder,
        spanStack: SpanStack
    ) {

    }

    /**
     * If this TagNodeHandler takes care of rendering the content.
     *
     * If true, the parser will not add the content itself.
     */
    open val rendersContent: Boolean get() = false

    /**
     * Handle the given node and add spans if needed.
     *
     * @param node
     * the node to handle
     * @param builder
     * the current stringbuilder
     * @param start
     * start position of inner text of this node
     * @param end
     * end position of inner text of this node.
     *
     * @param spanStack stack to push new spans on
     */
    abstract fun handleTagNode(
        node: TagNode,
        builder: SpannableStringBuilder, start: Int, end: Int, spanStack: SpanStack
    )

    /**
     * Utility method to append newlines while making sure that there are never
     * more than 2 consecutive newlines in the text (if whitespace stripping was
     * enabled).
     * @return true if a newline was added
     */
    protected fun appendNewLine(builder: SpannableStringBuilder): Boolean {
        val len = builder.length
        if (spanner!!.isStripExtraWhiteSpace) {
            // Should never have more than 2 \n characters in a row.
            if (len > 2 && builder[len - 1] == '\n' && builder[len - 2] == '\n') {
                return false
            }
        }
        builder.append("\n")
        return true
    }
}