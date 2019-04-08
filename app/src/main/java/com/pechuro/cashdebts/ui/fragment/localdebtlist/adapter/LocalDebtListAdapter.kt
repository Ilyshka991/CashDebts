package com.pechuro.cashdebts.ui.fragment.localdebtlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.R
import com.pechuro.cashdebts.model.DiffResult
import com.pechuro.cashdebts.ui.base.BaseViewHolder
import com.pechuro.cashdebts.ui.fragment.localdebtlist.data.LocalDebt
import kotlinx.android.synthetic.main.item_local_debt.view.*

class LocalDebtListAdapter : RecyclerView.Adapter<BaseViewHolder<LocalDebt>>() {
    private val debts = mutableListOf<LocalDebt>()

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

    override fun getItemCount() = debts.size

    override fun getItemViewType(position: Int) = when {
        debts[position].isEmpty() -> VIEW_TYPE_EMPTY
        else -> VIEW_TYPE_COMMON
    }

    override fun onBindViewHolder(holder: BaseViewHolder<LocalDebt>, position: Int) =
        holder.onBind(debts[position])

    fun update(result: DiffResult<LocalDebt>) {
        debts.clear()
        debts += result.dataList
        result.diffResult?.dispatchUpdatesTo(this) ?: notifyDataSetChanged()
    }

    class ViewHolder(private val view: View) : BaseViewHolder<LocalDebt>(view) {
        override fun onBind(data: LocalDebt) {
            view.apply {
                text_debtor.text = data.personName
                text_value.text = data.value.toString()
                text_description.text = data.description ?: ""
            }
        }
    }

    class EmptyViewHolder(view: View) : BaseViewHolder<LocalDebt>(view) {
        override fun onBind(data: LocalDebt) {}
    }

    companion object ViewTypes {
        private const val VIEW_TYPE_COMMON = 1
        private const val VIEW_TYPE_EMPTY = 2
    }
}