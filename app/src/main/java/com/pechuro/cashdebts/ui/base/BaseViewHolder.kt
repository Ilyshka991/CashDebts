package com.pechuro.cashdebts.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    protected abstract fun onBind(data: BaseViewHolderData)
}

abstract class BaseViewHolderData