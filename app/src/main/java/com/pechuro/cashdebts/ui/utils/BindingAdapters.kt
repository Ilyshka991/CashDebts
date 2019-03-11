package com.pechuro.cashdebts.ui.utils

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputLayout
import com.pechuro.cashdebts.R

@BindingAdapter("visibility")
fun setVisibility(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("error")
fun setError(inputLayout: TextInputLayout, errorId: Int?) {
    if (errorId == null) {
        inputLayout.error = null
    } else {
        inputLayout.error = inputLayout.context.getString(errorId)
    }
}

@BindingAdapter("image_avatar")
fun loadImage(view: ImageView, imageUrl: Uri?) {
    Glide.with(view)
        .load(imageUrl)
        .apply(RequestOptions().placeholder(R.drawable.avatar))
        .apply(RequestOptions().error(R.drawable.avatar))
        .apply(RequestOptions().circleCrop())
        .into(view)
}