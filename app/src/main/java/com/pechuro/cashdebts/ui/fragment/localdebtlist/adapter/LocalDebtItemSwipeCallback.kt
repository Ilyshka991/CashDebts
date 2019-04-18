package com.pechuro.cashdebts.ui.fragment.localdebtlist.adapter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.ui.base.BaseItemTouchCallback
import com.pechuro.cashdebts.ui.utils.px
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class LocalDebtItemSwipeCallback @Inject constructor() :
    BaseItemTouchCallback<LocalDebtItemSwipeCallback.SwipeAction>(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
    private val _actionEmitter = PublishSubject.create<SwipeAction>()

    override val actionEmitter: Observable<SwipeAction>
        get() = _actionEmitter

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        when (direction) {
            ItemTouchHelper.RIGHT -> _actionEmitter.onNext(SwipeAction.Delete(viewHolder.adapterPosition))
            ItemTouchHelper.LEFT -> _actionEmitter.onNext(SwipeAction.Edit(viewHolder.adapterPosition))
        }
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        var drawingDx = dX

        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 10.px

        val icon: Drawable
        val backgroundColor: ColorDrawable
        when {
            dX >= 0 -> {
                icon = recyclerView.context.resources.getDrawable(R.drawable.ic_delete_white)
                backgroundColor = ColorDrawable(Color.RED)
            }
            dX < 0 -> {
                icon = recyclerView.context.resources.getDrawable(R.drawable.ic_edit_white)
                backgroundColor = ColorDrawable(Color.BLUE)
                drawingDx /= 4
            }
            else -> throw IllegalArgumentException()
        }

        val iconTop = itemView.top + ((itemView.height - icon.intrinsicHeight) / 2)
        val iconBottom = iconTop + icon.intrinsicHeight
        val iconPadding = 16.px

        when {
            dX > 0 -> {
                val iconLeft = itemView.left + iconPadding
                val iconRight = iconLeft + icon.intrinsicWidth
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                backgroundColor.setBounds(
                    itemView.left, itemView.top,
                    itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom
                )
            }
            dX < 0 -> {
                val iconLeft = itemView.right - iconPadding - icon.intrinsicWidth
                val iconRight = itemView.right - iconPadding
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                backgroundColor.setBounds(
                    itemView.right + dX.toInt() - backgroundCornerOffset,
                    itemView.top, itemView.right, itemView.bottom
                )
            }
            else -> backgroundColor.setBounds(0, 0, 0, 0)
        }

        backgroundColor.draw(canvas)
        icon.draw(canvas)

        super.onChildDraw(canvas, recyclerView, viewHolder, drawingDx, dY, actionState, isCurrentlyActive)
    }

    sealed class SwipeAction : BaseItemTouchCallback.TouchActions() {
        class Delete(val position: Int) : SwipeAction()
        class Edit(val position: Int) : SwipeAction()
    }
}