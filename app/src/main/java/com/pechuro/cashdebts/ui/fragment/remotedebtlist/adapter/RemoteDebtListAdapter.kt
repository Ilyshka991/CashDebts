package com.pechuro.cashdebts.ui.fragment.remotedebtlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.model.DiffResult
import com.pechuro.cashdebts.ui.base.BaseViewHolder
import com.pechuro.cashdebts.ui.fragment.remotedebtlist.data.RemoteDebt
import kotlinx.android.synthetic.main.item_remote_debt.view.*
import java.text.SimpleDateFormat
import javax.inject.Inject

class RemoteDebtListAdapter @Inject constructor(private val dateFormatter: SimpleDateFormat) :
    RecyclerView.Adapter<BaseViewHolder<RemoteDebt>>() {
    private var debtList = emptyList<RemoteDebt>()

    private val onClickListener = View.OnClickListener {
        val itemInfo = it.tag as? ItemInfo ?: return@OnClickListener
        with(itemInfo) {
            data.isExpanded = !data.isExpanded
            notifyItemChanged(viewHolder.adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<RemoteDebt> = when (viewType) {
        VIEW_TYPE_COMMON -> {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_remote_debt, parent, false)
            ViewHolder(view)
        }
        VIEW_TYPE_EMPTY -> {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_debt_empty, parent, false)
            EmptyViewHolder(view)
        }
        else -> throw IllegalArgumentException()
    }

    override fun getItemCount() = debtList.size

    override fun getItemViewType(position: Int) = when {
        debtList[position].isEmpty() -> VIEW_TYPE_EMPTY
        else -> VIEW_TYPE_COMMON
    }

    override fun onBindViewHolder(holder: BaseViewHolder<RemoteDebt>, position: Int) = holder.onBind(debtList[position])

    fun update(result: DiffResult<RemoteDebt>) {
        when {
            debtList.isEmpty() -> {
                debtList = result.dataList
                notifyDataSetChanged()
            }
            debtList == result.dataList -> return
            else -> {
                debtList = result.dataList
                result.diffResult?.dispatchUpdatesTo(this) ?: notifyDataSetChanged()
            }
        }
    }

    private inner class ViewHolder(private val view: View) : BaseViewHolder<RemoteDebt>(view) {

        override fun onBind(data: RemoteDebt) {
            view.apply {
                if ((itemView.tag as? ItemInfo)?.data === data) {
                    if (data.description.isNotEmpty()) text_description.isVisible = data.isExpanded
                    return
                }
                text_debtor.text = data.user.phoneNumber
                text_value.text = "aa ${data.value}"

                itemView.tag = ItemInfo(data, this@ViewHolder)
                setOnClickListener(onClickListener)
            }
        }
    }

    private class EmptyViewHolder(view: View) : BaseViewHolder<RemoteDebt>(view) {
        override fun onBind(data: RemoteDebt) {}
    }

    private class ItemInfo(
        val data: RemoteDebt,
        val viewHolder: RemoteDebtListAdapter.ViewHolder
    )

    companion object ViewTypes {
        private const val VIEW_TYPE_COMMON = 1
        private const val VIEW_TYPE_EMPTY = 2
    }
}