package com.pechuro.cashdebts.ui.fragment.localdebtlist.adapter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.R
import javax.inject.Inject


class ItemSwipeToDeleteCallback @Inject constructor() :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        when (direction) {
            ItemTouchHelper.RIGHT -> {

            }
            ItemTouchHelper.LEFT -> {

            }
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
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20

        val icon: Drawable
        val backgroundColor: ColorDrawable
        when {
            dX >= 0 -> {
                icon = recyclerView.context.resources.getDrawable(R.drawable.ic_delete)
                backgroundColor = ColorDrawable(Color.RED)
            }
            dX < 0 -> {
                icon = recyclerView.context.resources.getDrawable(R.drawable.ic_done)
                backgroundColor = ColorDrawable(Color.BLUE)
            }
            else -> throw IllegalArgumentException()
        }

        val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
        val iconTop = itemView.top + iconMargin
        val iconBottom = iconTop + icon.intrinsicHeight

        when {
            dX > 0 -> {
                val iconLeft = itemView.left + iconMargin
                val iconRight = iconLeft + icon.intrinsicWidth
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                backgroundColor.setBounds(
                    itemView.left, itemView.top,
                    itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom
                )
            }
            dX < 0 -> {
                val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                val iconRight = itemView.right - iconMargin
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
    }
}