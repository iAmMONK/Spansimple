# Android HTML rendering library

[![](https://jitpack.io/v/iAmMONK/Spansimple.svg)](https://jitpack.io/#iAmMONK/Spansimple)

Inspired by:

* HTMLSpanner https://github.com/NightWhistler/HtmlSpanner.

With code from:

* CSSParser https://github.com/corgrath/osbcp-css-parser

# CSS support

Spansimple also supports the most common subset of CSS: both style tags and style attributes
are parsed by default, and the style of all built-in tags can be updated.

# Supported Tags

* i
* em
* cite
* dfn
* b
* strong
* blockquote
* ul
* ol
* tt
* code
* style
* br
* p
* div
* h1
* h2
* h3
* h4
* h5
* h6
* pre
* big
* small
* sub
* sup
* center
* li
* a
* img
* font
* span

# Supported Style Attributes

* ~~font-family~~ currently removed but let me know if you need it
* text-alignment
* ~~font-size~~ currently removed but let me know if you need it
* font-weight
* font-style
* color
* background-color
* display
* margin-top
* margin-bottom
* margin-left
* margin-right
* text-indent
* border-style
* border-color
* border-style

# Usage

1. In root ``build.gradle`` add ``allprojects { maven { url 'https://jitpack.io' } } }``

2. In app module ``build.gradle`` add following dependency

``implementation 'com.github.iAmMONK:Spansimple:0.1.3'``

3. In its simplest form, just call  
   ``HtmlSpanner()(html)`` Yes it's not a typo  
   or  
   ``HtmlSpanner().fromHtml(html)``

   There are also some settings available:  
   ``isAllowStyling`` - specify if CSS styling should be used. Default = ``true``  
   ``isStripExtraWhiteSpace`` - specify if extra spaces should be stripped from the input. Default = ``false``  
   ``isUseColoursFromStyle`` - specify if colors from CSS colours should be used. Default = ``true``  
   
   To modify these settings, use the constructor with settings. Example:
   ```
   HtmlSpanner {
      isAllowStyling = false
      isStripExtraWhiteSpace = true
      isUseColoursFromStyle = false
   }(html)
   ```
   Note: You don't need to specify all 3, only those that you want to change.

