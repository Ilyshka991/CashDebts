package com.pechuro.cashdebts.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import androidx.core.view.isVisible


class ExpandableLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    var isExpanded: Boolean = false
        set(value) {
            field = value
            toggle(value)
        }

    private fun toggle(isExpanded: Boolean) {
        if (isExpanded) expandView(this) else hideView(this)
    }

    private fun expandView(view: LinearLayout) {
        view.measure(
            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT
        )
        val targetHeight = view.measuredHeight
        view.apply {
            layoutParams.height = 1
            isVisible = true
        }
        object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, transformation: Transformation) {
                val height = (targetHeight * interpolatedTime).toInt()
                if (interpolatedTime == 0F) return
                view.layoutParams.height = height
                view.requestLayout()
            }

            override fun willChangeBounds() = true
        }.apply {
            duration = 300
        }.also {
            view.startAnimation(it)
        }
    }

    private fun hideView(view: LinearLayout) {
        val initialHeight = view.measuredHeight
        object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, transformation: Transformation) {
                if (interpolatedTime == 1F) {
                    view.isVisible = false
                } else {
                    view.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    view.requestLayout()
                }
            }

            override fun willChangeBounds() = true
        }.apply {
            duration = 200
        }.also {
            view.startAnimation(it)
        }
    }
}