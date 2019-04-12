package com.pechuro.cashdebts.ui.base

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable

abstract class BaseItemTouchCallback<T : BaseItemTouchCallback.TouchActions>(
    dragDirs: Int,
    swipeDirs: Int
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    abstract val actionEmitter: Observable<T>

    abstract class TouchActions
}

class ItemTouchHelper<T : BaseItemTouchCallback.TouchActions>(callback: BaseItemTouchCallback<T>) :
    ItemTouchHelper(callback) {
    val actionEmitter = callback.actionEmitter
}

abstract class BaseViewHolder<T : Any>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(data: T)
}