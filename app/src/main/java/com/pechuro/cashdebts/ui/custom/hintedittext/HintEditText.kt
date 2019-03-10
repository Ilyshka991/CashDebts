package com.pechuro.cashdebts.ui.custom.hintedittext

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.EditText
import androidx.core.content.res.use
import com.pechuro.cashdebts.R

class HintEditText : EditText {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    var hintText: String? = null

    private var isAlignCenter = false
    private var isAlreadyAligned = false

    private var textOffset = 0F
    private var spaceSize = 0F
    private var charSize = 0F

    private val paint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        textSize = this@HintEditText.textSize
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        calculateCharSizes()
        calculateTextOffset()
        if (!isAlreadyAligned) {
            if (isAlignCenter) alignCenter()
            isAlreadyAligned = true
        }
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
                    charSize
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

    fun getEnteredText() = text.replace("[ ]".toRegex(), "")

    private fun init(attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.HintEditText).use {
            hintText = it.getString(R.styleable.HintEditText_hint)
            isAlignCenter = it.getBoolean(R.styleable.HintEditText_is_align_center, false)
        }
    }

    private fun alignCenter() {
        if (hintText.isNullOrEmpty()) {
            textAlignment = TEXT_ALIGNMENT_CENTER
            return
        }
        val numberHintSpaces = hintText!!.count { it == ' ' }
        val numberHintChars = hintText!!.length - numberHintSpaces
        textOffset = (measuredWidth - numberHintSpaces * spaceSize - numberHintChars * charSize) / 2
        setPadding(textOffset.toInt(), paddingTop, paddingRight, paddingBottom)
    }

    private fun calculateCharSizes() {
        spaceSize = getPaint().measureText(" ")
        charSize = getPaint().measureText("1")
    }

    private fun calculateTextOffset() {
        textOffset = paddingStart.toFloat()
        if (length() > 0) textOffset += getPaint().measureText(text, 0, length())
    }
}

