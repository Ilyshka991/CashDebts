package com.pechuro.cashdebts.ui.fragment.localdebtlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.data.data.model.DebtRole
import com.pechuro.cashdebts.model.DiffResult
import com.pechuro.cashdebts.ui.base.BaseViewHolder
import com.pechuro.cashdebts.ui.fragment.localdebtlist.data.LocalDebt
import kotlinx.android.synthetic.main.item_local_debt.view.*
import java.text.SimpleDateFormat
import javax.inject.Inject

class LocalDebtListAdapter @Inject constructor(private val dateFormatter: SimpleDateFormat) :
    RecyclerView.Adapter<BaseViewHolder<LocalDebt>>() {
    private var debtList = emptyList<LocalDebt>()

    private val onClickListener = View.OnClickListener {
        val itemInfo = it.tag as? ItemInfo ?: return@OnClickListener
        with(itemInfo) {
            data.isExpanded = !data.isExpanded
            notifyItemChanged(viewHolder.adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<LocalDebt> = when (viewType) {
        VIEW_TYPE_COMMON -> {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_local_debt, parent, false)
            ViewHolder(view)
        }
        VIEW_TYPE_EMPTY -> {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_local_debt_empty, parent, false)
            EmptyViewHolder(view)
        }
        else -> throw IllegalArgumentException()
    }

    override fun getItemCount() = debtList.size

    override fun getItemViewType(position: Int) = when {
        debtList[position].isEmpty() -> VIEW_TYPE_EMPTY
        else -> VIEW_TYPE_COMMON
    }

    override fun onBindViewHolder(holder: BaseViewHolder<LocalDebt>, position: Int) = holder.onBind(debtList[position])

    fun update(result: DiffResult<LocalDebt>) {
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

    fun getItemByPosition(position: Int) = debtList[position]

    private inner class ViewHolder(private val view: View) : BaseViewHolder<LocalDebt>(view) {

        override fun onBind(data: LocalDebt) {
            view.apply {
                if ((itemView.tag as? ItemInfo)?.data === data) {
                    if (data.description.isNotEmpty()) text_description.isVisible = data.isExpanded
                    text_date.isVisible = data.isExpanded
                    return
                }

                text_debtor.text = data.personName

                val textValueStringRes = when (data.role) {
                    DebtRole.CREDITOR -> R.string.item_local_debt_msg_creditor
                    DebtRole.DEBTOR -> R.string.item_local_debt_msg_debtor
                    else -> throw IllegalArgumentException()
                }
                val textValue = view.context.getString(textValueStringRes, data.value)
                text_value.text = textValue

                text_description.apply {
                    isVisible = data.isExpanded
                    if (data.description.isEmpty()) isVisible = false else text = data.description
                }

                text_date.apply {
                    text = dateFormatter.format(data.date)
                    isVisible = data.isExpanded
                }

                text_date.text = dateFormatter.format(data.date)

                itemView.tag = ItemInfo(data, this@ViewHolder)
                setOnClickListener(onClickListener)
            }
        }
    }

    private class EmptyViewHolder(view: View) : BaseViewHolder<LocalDebt>(view) {
        override fun onBind(data: LocalDebt) {}
    }

    private class ItemInfo(
        val data: LocalDebt,
        val viewHolder: LocalDebtListAdapter.ViewHolder
    )

    companion object ViewTypes {
        private const val VIEW_TYPE_COMMON = 1
        private const val VIEW_TYPE_EMPTY = 2
    }
}

