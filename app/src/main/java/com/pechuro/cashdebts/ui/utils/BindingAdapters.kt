package com.pechuro.cashdebts.ui.utils

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibility")
fun setVisibility(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}