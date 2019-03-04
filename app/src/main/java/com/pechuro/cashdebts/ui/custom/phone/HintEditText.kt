package com.pechuro.cashdebts.ui.custom.phone

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.EditText

class HintEditText : EditText {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var hintText: String? = null

    private var textOffset = 0F
    private var spaceSize = 0F
    private var numberSize = 0F
    private val paint = Paint()
    private val rect = Rect()

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        calculateCharSizes()
        calculateTextOffset()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (hintText != null && length() < hintText!!.length) {
            val top = measuredHeight / 2
            var offsetX = textOffset
            for (a in length() until hintText!!.length) {
                offsetX += if (hintText!![a] == ' ') {
                    spaceSize
                } else {
                    rect.set(
                        offsetX.toInt() + 6.px,
                        top,
                        (offsetX + numberSize).toInt() + 3.px,
                        top + 2.px
                    )
                    canvas?.drawRect(rect, paint)
                    numberSize
                }
            }
        }
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        calculateTextOffset()
    }

    private fun calculateCharSizes() {
        spaceSize = getPaint().measureText(" ")
        numberSize = getPaint().measureText("1")
        invalidate()
    }

    private fun calculateTextOffset() {
        textOffset = if (length() > 0) getPaint().measureText(text, 0, length()) else 0f
    }

    private val Int.px: Int
        get() = (this * resources.displayMetrics.density).toInt()
}

