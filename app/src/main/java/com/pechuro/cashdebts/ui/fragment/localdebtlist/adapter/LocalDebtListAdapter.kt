package com.pechuro.cashdebts.ui.fragment.localdebtlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.databinding.ItemLocalDebtBinding
import com.pechuro.cashdebts.ui.base.base.BaseViewHolder
import com.pechuro.cashdebts.ui.fragment.localdebtlist.data.LocalDebt

class LocalDebtListAdapter : RecyclerView.Adapter<BaseViewHolder<LocalDebt>>() {
    private val debts = mutableListOf<LocalDebt>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<LocalDebt> {
        val binding = ItemLocalDebtBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = debts.size

    override fun onBindViewHolder(holder: BaseViewHolder<LocalDebt>, position: Int) = holder.onBind(debts[position])


    class ViewHolder(private val binding: ItemLocalDebtBinding) : BaseViewHolder<LocalDebt>(binding.root) {
        override fun onBind(data: LocalDebt) {
            binding.debt = data
            binding.executePendingBindings()
        }
    }
}