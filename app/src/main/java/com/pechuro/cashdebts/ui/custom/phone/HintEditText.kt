package com.pechuro.cashdebts.ui.custom.phone

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
    private val paint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        textSize = this@HintEditText.textSize
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        calculateCharSizes()
        calculateTextOffset()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (hintText != null && length() < hintText!!.length) {
            val top = measuredHeight / 1.55
            var offsetX = textOffset
            for (i in length() until hintText!!.length) {
                offsetX += if (hintText!![i] == ' ') {
                    spaceSize
                } else {
                    canvas?.drawText(hintText!![i].toString(), offsetX, top.toFloat(), paint)
                    numberSize
                }
            }
        }
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (hintText != null && length() < hintText!!.length && hintText!![length()] == ' ') {
            if (lengthAfter > lengthBefore) {
                this.text.append(' ')
            } else {
                this.text.delete(length() - 1, length())
            }
        }
        calculateTextOffset()
    }

    private fun calculateCharSizes() {
        spaceSize = getPaint().measureText(" ")
        numberSize = getPaint().measureText("1")
        invalidate()
    }

    private fun calculateTextOffset() {
        textOffset = paddingStart.toFloat()
        if (length() > 0) textOffset += getPaint().measureText(text, 0, length())
    }
}

