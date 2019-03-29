package com.pechuro.cashdebts.ui.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.google.android.material.textfield.TextInputLayout
import com.pechuro.cashdebts.R

fun ImageView.loadAvatar(url: String?) {
    val crossFadeFactory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

    Glide.with(this)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade(crossFadeFactory))
        .placeholder(R.drawable.avatar)
        .error(R.drawable.avatar)
        .circleCrop()
        .into(this)
}

fun TextInputLayout.setError(errorId: Int) {
    if (errorId == -1) {
        error = null
    } else {
        error = context.getString(errorId)
    }
}