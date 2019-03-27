package com.pechuro.cashdebts.ui.utils

import android.view.View

fun View.isVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}