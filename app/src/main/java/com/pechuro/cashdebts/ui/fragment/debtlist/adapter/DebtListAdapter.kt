package com.pechuro.cashdebts.ui.fragment.debtlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.data.model.Debt
import com.pechuro.cashdebts.databinding.ItemDebtBinding
import com.pechuro.cashdebts.ui.base.BaseViewHolder

class DebtListAdapter(private val diffCallback: DebtDiffCallback) : RecyclerView.Adapter<BaseViewHolder<Debt>>() {
    private val debts = mutableListOf<Debt>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Debt> {
        val binding = ItemDebtBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = debts.size

    override fun onBindViewHolder(holder: BaseViewHolder<Debt>, position: Int) = holder.onBind(debts[position])

    fun setData(data: List<Debt>) {
        diffCallback.setData(debts, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        debts.clear()
        debts += data
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(private val binding: ItemDebtBinding) : BaseViewHolder<Debt>(binding.root) {
        override fun onBind(data: Debt) {
            binding.debt = data
            binding.executePendingBindings()
        }
    }
}