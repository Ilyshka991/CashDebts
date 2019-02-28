package com.pechuro.cashdebts.ui.activity.main.debtlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.data.model.Debt
import com.pechuro.cashdebts.databinding.ItemDebtBinding
import com.pechuro.cashdebts.ui.base.BaseViewHolder

class DebtListAdapter : RecyclerView.Adapter<BaseViewHolder<Debt>>() {
    private val debts = mutableListOf<Debt>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Debt> {
        val binding = ItemDebtBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = debts.size

    override fun onBindViewHolder(holder: BaseViewHolder<Debt>, position: Int) = holder.onBind(debts[position])

    fun addData(data: Debt) {
        if (!debts.contains(data)) {
            debts += data
            notifyItemInserted(debts.lastIndex)
        }
    }

    fun removeData(data: Debt) {
        val index = debts.indexOf(data)
        debts.removeAt(index)
        notifyItemRemoved(index)
    }

    fun modifyData(data: Debt) {
        val index = debts.indexOfFirst { it.id == data.id }
        debts.removeAt(index)
        debts.add(index, data)
        notifyItemChanged(index)
    }

    class ViewHolder(private val binding: ItemDebtBinding) : BaseViewHolder<Debt>(binding.root) {
        override fun onBind(data: Debt) {
            binding.debt = data
            binding.executePendingBindings()
        }
    }
}