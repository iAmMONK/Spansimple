package com.iammonk.spansimple.handlers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import com.iammonk.spansimple.SpanStack
import com.iammonk.spansimple.TagNodeHandler
import org.htmlcleaner.TagNode
import java.io.IOException
import java.net.URL

class ImageHandler : TagNodeHandler() {
    override fun handleTagNode(
        node: TagNode, builder: SpannableStringBuilder,
        start: Int, end: Int, spanStack: SpanStack
    ) {
        val src = node.getAttributeByName("src")
        builder.append("\uFFFC")
        val bitmap = loadBitmap(src)
        if (bitmap != null) {

            val drawable: Drawable = BitmapDrawable(bitmap)
            drawable.setBounds(
                0, 0, bitmap.width - 1,
                bitmap.height - 1
            )
            spanStack.pushSpan(ImageSpan(drawable), start, builder.length)
        }
    }

    /**
     * Loads a Bitmap from the given url.
     *
     * @param url
     * @return a Bitmap, or null if it could not be loaded.
     */
    private fun loadBitmap(url: String?): Bitmap? {
        return try {
            BitmapFactory.decodeStream(URL(url).openStream())
        } catch (io: IOException) {
            null
        }
    }
}