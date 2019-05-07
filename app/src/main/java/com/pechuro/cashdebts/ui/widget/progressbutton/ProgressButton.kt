package com.pechuro.cashdebts.ui.widget.progressbutton

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.core.content.res.use
import com.pechuro.cashdebts.R

class ProgressButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val button: Button
    private val progress: ProgressBar

    init {
        inflate(context, R.layout.layout_progress_button, this)
        button = findViewById(R.id.button)
        progress = findViewById(R.id.progress)
        attrs?.let { obtainAttrs(it) }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        button.setOnClickListener(l)
    }

    fun setProgress(isShow: Boolean) {
        progress.visibility = if (isShow) VISIBLE else INVISIBLE
        button.visibility = if (isShow) INVISIBLE else VISIBLE
    }

    override fun setEnabled(enabled: Boolean) {
        button.isEnabled = enabled
    }

    @SuppressLint("Recycle")
    private fun obtainAttrs(attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressButton)
        ta.use {
            button.apply {
                text = it.getString(R.styleable.ProgressButton_android_text)
            }
        }
    }
}