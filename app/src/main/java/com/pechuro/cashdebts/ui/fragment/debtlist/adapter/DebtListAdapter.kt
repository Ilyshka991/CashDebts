package com.pechuro.cashdebts.ui.fragment.debtlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pechuro.cashdebts.databinding.ItemDebtBinding
import com.pechuro.cashdebts.ui.base.BaseViewHolder

class DebtListAdapter : RecyclerView.Adapter<BaseViewHolder>() {
    private val debts = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = ItemDebtBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = debts.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.onBind(debts[position])

    fun setData(data: List<String>) {
        debts.clear()
        debts += data
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemDebtBinding) : BaseViewHolder(binding.root) {
        override fun onBind(data: String) {
            binding.text.text = data
        }
    }
}